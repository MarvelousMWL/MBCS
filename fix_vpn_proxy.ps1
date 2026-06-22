# Fix VPN Proxy - 修复 VPN 导致的 Git 代理问题
# 用法: powershell -File fix_vpn_proxy.ps1

Write-Host "=== Git Proxy Fix ===" -ForegroundColor Cyan
Write-Host ""

# 1. 检查当前配置
$httpProxy = git config --global http.proxy 2>$null
$httpsProxy = git config --global https.proxy 2>$null
Write-Host "Current config:" -ForegroundColor Yellow
Write-Host "  http.proxy  = $httpProxy"
Write-Host "  https.proxy = $httpsProxy"
Write-Host ""

# 2. 修复: http.proxy 必须为空（本地 Gitea 走 HTTP）
if ($httpProxy) {
    git config --global --unset http.proxy
    Write-Host "[FIXED] Removed http.proxy (was: $httpProxy)" -ForegroundColor Green
} else {
    Write-Host "[OK] http.proxy already unset" -ForegroundColor Gray
}

# 3. 确保 https.proxy 正确（GitHub 需要代理）
$expectedHttps = "http://127.0.0.1:7890"
if ($httpsProxy -ne $expectedHttps) {
    git config --global https.proxy $expectedHttps
    Write-Host "[FIXED] Set https.proxy = $expectedHttps" -ForegroundColor Green
} else {
    Write-Host "[OK] https.proxy already correct" -ForegroundColor Gray
}

# 4. 验证
Write-Host ""
Write-Host "=== Verify ===" -ForegroundColor Cyan
$test = git ls-remote http://localhost:3001/mbcsadmin/MBCS.git HEAD 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "[PASS] Gitea (local) reachable" -ForegroundColor Green
} else {
    Write-Host "[FAIL] Gitea unreachable: $test" -ForegroundColor Red
}

Write-Host ""
Write-Host "Done. If VPN reconnects, run this script again." -ForegroundColor Cyan
