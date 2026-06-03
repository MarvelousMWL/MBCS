# MBCS 一键启动 - start.ps1
param([switch]$Build)
$root = "E:\Agent\MyProject\MBCS"

function Wait-For {
    param($url, $name, $timeout = 40)
    for($i=0; $i -lt $timeout; $i+=3) {
        try { $r = Invoke-WebRequest $url -UseBasicParsing -TimeoutSec 2 -ErrorAction Stop; if($r.StatusCode -eq 200){ Write-Host "OK $name"; return $true } }catch{}
        Start-Sleep 3
    }
    Write-Host "FAIL $name"; return $false
}

function Start-Svc {
    param($name, $jar, $port)
    $p = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
    if($p){ Stop-Process -Id $p.OwningProcess -Force -ErrorAction SilentlyContinue; Start-Sleep 2 }
    if($Build){
        Write-Host "Building $name..."
        Push-Location "$root\bank-core"
        mvn clean package -Dmaven.test.skip=true -pl $name -am 2>&1 | Out-Null
        mvn package org.springframework.boot:spring-boot-maven-plugin:repackage -Dmaven.test.skip=true -pl $name 2>&1 | Out-Null
        Pop-Location
    }
    if(!(Test-Path $jar)){ Write-Host "JAR not found: $jar"; return $false }
    Start-Process -WindowStyle Hidden java "-jar $jar"
    Start-Sleep 8
    return (Wait-For "http://localhost:$port/api/liability/product" $name) -or (Wait-For "http://localhost:$port/api/customer" $name)
}

Write-Host "=== MBCS Startup ==="
$ok = $true
if(!(Start-Svc "bank-liability" "$root\bank-core\bank-liability\target\bank-liability-1.0.0-SNAPSHOT.jar" 8080)){ $ok = $false }
if(!(Start-Svc "bank-customer" "$root\bank-core\bank-customer\target\bank-customer-1.0.0-SNAPSHOT.jar" 8081)){ $ok = $false }

$np = Get-Process -Name "node" -ErrorAction SilentlyContinue | Where-Object { $_.CommandLine -match "vite" }
if($np){ $np | Stop-Process -Force; Start-Sleep 2 }
Start-Process -WindowStyle Hidden node "E:\Agent\MyProject\MBCS\bank-web\node_modules\.bin\vite" -WorkingDirectory "E:\Agent\MyProject\MBCS\bank-web"
Start-Sleep 3
Wait-For "http://localhost:3000" "frontend"

if($ok){ Write-Host "All services OK -> http://localhost:3000" }
else{ Write-Host "Some services failed" }
