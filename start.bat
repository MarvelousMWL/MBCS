@echo off
chcp 65001 >nul
cd /d E:\Agent\MyProject\MBCS
powershell -ExecutionPolicy Bypass -File start.ps1
pause
