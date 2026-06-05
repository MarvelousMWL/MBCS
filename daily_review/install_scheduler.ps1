# ============================================================
# MBCS 每日复盘 - Windows Task Scheduler 安装脚本
# 注册每日 8:00 自动执行的任务
# ============================================================

$ErrorActionPreference = "Stop"
$root = "E:\Agent\MyProject\MBCS"
$scriptPath = "$root\daily_review\daily_review.ps1"
$taskName = "MBCS-DailyReview"
$logDir = "$root\daily_review\logs"

if (-not (Test-Path $scriptPath)) {
    Write-Host "\u2717 脚本不存在: $scriptPath" -ForegroundColor Red
    exit 1
}

if (-not (Test-Path $logDir)) { New-Item $logDir -ItemType Directory -Force | Out-Null }

$existing = Get-ScheduledTask -TaskName $taskName -ErrorAction SilentlyContinue
if ($existing) {
    Write-Host "  \u26a0 任务 [$taskName] 已存在，正在更新..." -ForegroundColor Yellow
    Unregister-ScheduledTask -TaskName $taskName -Confirm:$false
}

$action = New-ScheduledTaskAction -Execute "powershell.exe" -Argument "-ExecutionPolicy Bypass -File \"`"$scriptPath`"\" -WindowStyle Hidden"
$trigger = New-ScheduledTaskTrigger -Daily -At "08:00"
$settings = New-ScheduledTaskSettingsSet -AllowStartIfOnBatteries -DontStopIfGoingOnBatteries -StartWhenAvailable -MultipleInstances IgnoreNew
$principal = New-ScheduledTaskPrincipal -UserId "SYSTEM" -LogonType ServiceAccount -RunLevel Highest

try {
    Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Settings $settings -Principal $principal -Description "MBCS项目每日复盘：启动服务→页面巡检→缺陷修复→输出日报" -Force
    Write-Host "  \u2713 计划任务 [$taskName] 注册成功！" -ForegroundColor Green
    Write-Host "  \u23f0 执行时间: 每日 08:00" -ForegroundColor Cyan
    Write-Host "  \ud83d\udccd 脚本路径: $scriptPath" -ForegroundColor Cyan
} catch {
    Write-Host "  \u2717 计划任务注册失败: $_" -ForegroundColor Red
    Write-Host "  \u26a0 尝试使用当前用户注册..." -ForegroundColor Yellow
    try {
        $principal2 = New-ScheduledTaskPrincipal -UserId "INTERACTIVE" -LogonType Interactive -RunLevel Highest
        Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Settings $settings -Principal $principal2 -Description "MBCS每日复盘" -Force
        Write-Host "  \u2713 计划任务 [$taskName] 注册成功（当前用户）！" -ForegroundColor Green
    } catch {
        Write-Host "  \u2717 仍然失败: $_" -ForegroundColor Red
        Write-Host ""
        Write-Host "  \u26a0 请以管理员身份运行 PowerShell，然后手动执行：" -ForegroundColor Yellow
        Write-Host "    powershell -ExecutionPolicy Bypass -File \"`"$PSCommandPath`"\"" -ForegroundColor White
        exit 1
    }
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  MBCS 每日复盘任务安装完成" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "相关命令：" -ForegroundColor White
Write-Host "  - 立即运行: Start-ScheduledTask -TaskName '$taskName'" -ForegroundColor Gray
Write-Host "  - 查看状态: Get-ScheduledTask -TaskName '$taskName' | fl" -ForegroundColor Gray
Write-Host "  - 手动执行: $scriptPath" -ForegroundColor Gray
Write-Host "  - 删除任务: Unregister-ScheduledTask -TaskName '$taskName' -Confirm:$false" -ForegroundColor Gray
