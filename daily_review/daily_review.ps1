# ============================================================
# MBCS 每日复盘自动化
# ============================================================
param([switch]$SkipStart)
$ErrorActionPreference = "Continue"
$root = "E:\Agent\MyProject\MBCS"
$date = Get-Date -Format "yyyy-MM-dd"
$reportFile = "$root\daily_review\日报_$date.md"
$logDir = "$root\daily_review\logs"
$runLog = "$logDir\run_$date.log"
$resultFile = "$logDir\result_$date.json"
if (-not (Test-Path $logDir)) { New-Item $logDir -ItemType Directory -Force | Out-Null }
function Log { param($msg) $t = Get-Date -Format "HH:mm:ss"; "$t $msg" | Out-File $runLog -Append -Encoding UTF8; Write-Host "$t $msg" }
Log "=== MBCS Daily Review ==="

function Stop-Residual {
  Log "[1/5] Cleaning..."
  @(8080,3000) | ForEach-Object { try { $p = Get-NetTCPConnection -LocalPort $_ -ErrorAction Stop; Stop-Process -Id $p.OwningProcess -Force; Start-Sleep 1 } catch {} }
  Get-Process java -ErrorAction SilentlyContinue | Where-Object CommandLine -match "bank|maven|spring" | Stop-Process -Force
  Get-Process node -ErrorAction SilentlyContinue | Where-Object { $cmd = (Get-CimInstance Win32_Process -Filter "ProcessId = $(  Get-Process node -ErrorAction SilentlyContinue | Where-Object CommandLine -match "vite|run_all" | Stop-Process -Force.Id)").CommandLine; $cmd -match "vite|run_all|serve-prod" } | Stop-Process -Force
  Start-Sleep 2; Log "  OK"
}

function Start-Svcs {
  Log "[2/5] Starting..."
  $errs = @()
  Log "  Backend (mvn spring-boot:run)..."
  $mvnArgs = @("spring-boot:run","-Dspring-boot.run.jvmArguments=""-Xms512m -Xmx1024m""","-s","$root\bank-maven\settings.xml")
  Start-Process -WindowStyle Hidden -FilePath "mvn.cmd" -ArgumentList $mvnArgs -WorkingDirectory "$root\bank-core\bank-server" -RedirectStandardOutput "$logDir\server_$date.log" -RedirectStandardError "$logDir\server_$date.log.err"
  Start-Sleep 12
  Log "  Frontend (serve-prod)..."
  $vite = "$root\bank-web\serve-prod.mjs"
  if (Test-Path $vite) { Start-Process -WindowStyle Hidden -FilePath "node" -ArgumentList $vite -WorkingDirectory "$root\bank-web" -RedirectStandardOutput "$logDir\front_$date.log" -RedirectStandardError "$logDir\front_$date.log.err" }
  else { Start-Process -WindowStyle Hidden -FilePath "node" -ArgumentList "$root\bank-web\serve-prod.mjs" -WorkingDirectory "$root\bank-web" -RedirectStandardOutput "$logDir\front_$date.log" -RedirectStandardError "$logDir\front_$date.log.err" }
  Start-Sleep 3
  Log "  Waiting..."
  $bk=$false; $ft=$false
  for ($i=0; $i -lt 25; $i++) {
    if (-not $bk) { try { (Invoke-WebRequest "http://localhost:8080/api/liability/product" -UseBasicParsing -TimeoutSec 2 -ErrorAction Stop).StatusCode -eq 200; $bk=$true; Log "  Backend OK" } catch {} }
    if (-not $ft) { try { (Invoke-WebRequest "http://localhost:3000" -UseBasicParsing -TimeoutSec 2 -ErrorAction Stop).StatusCode -eq 200; $ft=$true; Log "  Frontend OK" } catch {} }
    if ($bk -and $ft) { break }; Start-Sleep 3
  }
  if (-not $bk) { $errs += "backend timeout" }
  if (-not $ft) { $errs += "frontend timeout" }
    # 等待数据库连接池完全就绪（热机）
  if ($bk) {
    Log "  Warming up DB connection pool..."
    for ($i=0; $i -lt 10; $i++) {
      try {
        $r = Invoke-WebRequest "http://localhost:8080/api/teller/institution" -UseBasicParsing -TimeoutSec 3 -ErrorAction Stop
        if ($r.StatusCode -eq 200) { Log "  DB pool ready"; break }
      } catch {}
      Start-Sleep 2
    }
  }
  return $errs
}

# === MAIN ===
Stop-Residual
$e = @()
if (-not $SkipStart) { $e = Start-Svcs }

Log "Running node tests + report..."
$nodeScript = "$root\daily_review\run_all.js"
$p = Start-Process -WindowStyle Hidden -FilePath "node" -ArgumentList $nodeScript -RedirectStandardOutput $resultFile -RedirectStandardError "$resultFile.err" -PassThru
$p | Wait-Process -Timeout 60 -ErrorAction SilentlyContinue
Start-Sleep 1

if (Test-Path $resultFile) {
  $output = Get-Content $resultFile -Raw -Encoding UTF8
  if ($output -match "报告已生成") { Log "  Report saved: $reportFile" }
  Write-Host $output
} else { Log "  No output from node script" }

Log "=== DONE ==="
# ============================================================
# 通知 Codex：把完整日报内容写入通知文件
# ============================================================
$notificationFile = "$root\daily_review\_notification.json"

# 从结果输出中提取关键指标
$apiOk = @()
$apiFail = @()
if ($output) {
  $lines = $output -split "`n"
  foreach ($line in $lines) {
    if ($line -match "OK (\w+)") { $apiOk += $Matches[1] }
    if ($line -match "FAIL (\w+)") { $apiFail += $Matches[1] }
  }
}
$hasLogin = if ($output -match "Login OK") { $true } else { $false }
$browserOkCount = 0
$browserFailCount = 1
if ($output -match "(\d+) 通过") { $browserOkCount = [int]$Matches[1] }
if ($output -match "(\d+) 失败") { $browserFailCount = [int]$Matches[1] }
$issues = 0
if ($output -match "Issues:\s*(\d+)") { $issues = [int]$Matches[1] }
$hasReport = (Test-Path $reportFile)
$hasError = ($e.Count -gt 0)

# 读取完整日报内容
$reportContent = ""
if ($hasReport) {
  $reportContent = Get-Content $reportFile -Raw -Encoding UTF8
}

$notification = @{
  date       = $date
  time       = Get-Date -Format "HH:mm:ss"
  status     = if ($hasError) { "FAILED" } else { "OK" }
  apiOk      = $apiOk
  apiFail    = $apiFail
  loginOk    = $hasLogin
  browserOk  = $browserOkCount
  browserFail = $browserFailCount
  issues     = $issues
  hasReport  = $hasReport
  errors     = if ($e.Count -gt 0) { $e -join "; " } else { "" }
  reportFull = if ($hasReport) { $reportContent } else { "" }
}
$notificationJson = $notification | ConvertTo-Json -Compress -Depth 10
[System.IO.File]::WriteAllText($notificationFile, $notificationJson, [System.Text.UTF8Encoding]::new($false))
Log "  Notification written: $notificationFile"








