# ============================================================
# MBCS Daily Review - Windows Task Scheduler Install
# Registers daily 8:00 automatic execution task
# ============================================================

$ErrorActionPreference = "Stop"
$root = "E:\Agent\MyProject\MBCS"
$scriptPath = "$root\daily_review\daily_review.ps1"
$taskName = "MBCS-DailyReview"
$logDir = "$root\daily_review\logs"

if (-not (Test-Path $scriptPath)) {
    Write-Host "Script not found: $scriptPath" -ForegroundColor Red
    exit 1
}

if (-not (Test-Path $logDir)) { New-Item $logDir -ItemType Directory -Force | Out-Null }

$existing = Get-ScheduledTask -TaskName $taskName -ErrorAction SilentlyContinue
if ($existing) {
    Write-Host "Task [$taskName] already exists, updating..." -ForegroundColor Yellow
    Unregister-ScheduledTask -TaskName $taskName -Confirm:$false
}

$action = New-ScheduledTaskAction -Execute "powershell.exe" -Argument "-ExecutionPolicy Bypass -File `"$scriptPath`" -WindowStyle Hidden"
$trigger = New-ScheduledTaskTrigger -Daily -At "08:00"
$settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries -StartWhenAvailable -MultipleInstances IgnoreNew
$taskExists = $false

# Try SYSTEM principal first
try {
    $principal = New-ScheduledTaskPrincipal -UserId "SYSTEM" -LogonType ServiceAccount -RunLevel Highest
    Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Settings $settings -Principal $principal -Description "MBCS Daily Review" -Force
    Write-Host "Registered as SYSTEM." -ForegroundColor Green
    $taskExists = $true
} catch {
    Write-Host "SYSTEM registration failed, trying current user..." -ForegroundColor Yellow
}

if (-not $taskExists) {
    try {
        $principal = New-ScheduledTaskPrincipal -UserId "INTERACTIVE" -LogonType Interactive -RunLevel Highest
        Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Settings $settings -Principal $principal -Description "MBCS Daily Review" -Force
        Write-Host "Registered as current user." -ForegroundColor Green
        $taskExists = $true
    } catch {
        Write-Host "Interactive registration also failed." -ForegroundColor Red
    }
}

if (-not $taskExists) {
    Write-Host "Falling back to schtasks.exe..." -ForegroundColor Yellow
    schtasks /Create /SC DAILY /TN $taskName /TR "powershell.exe -ExecutionPolicy Bypass -File `"$scriptPath`"" /ST 08:00 /F
    if ($LASTEXITCODE -eq 0) {
        Write-Host "schtasks registration succeeded." -ForegroundColor Green
        $taskExists = $true
    }
}

if ($taskExists) {
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  MBCS Daily Review task registered" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "Related commands:" -ForegroundColor White
    Write-Host "  - Run now: Start-ScheduledTask -TaskName '$taskName'" -ForegroundColor Gray
    Write-Host "  - Check: schtasks /Query /TN '$taskName' /FO LIST /V" -ForegroundColor Gray
    Write-Host "  - Manual: $scriptPath" -ForegroundColor Gray
    Write-Host "  - Delete: Unregister-ScheduledTask -TaskName '$taskName' -Confirm:`$false" -ForegroundColor Gray
} else {
    Write-Host "Failed to register task. Run as Administrator and try again." -ForegroundColor Red
    exit 1
}
