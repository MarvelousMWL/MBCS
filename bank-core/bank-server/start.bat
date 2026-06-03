@echo off
setlocal enabledelayedexpansion

REM 读取classpath文件
set /p CP=<"E:\Agent\MyProject\MBCS\bank-core\bank-server\cp.txt"

REM 设置classpath
set CLASSPATH=%CP%

REM 启动应用
java com.bank.server.BankServerApplication > startup.log 2>&1
