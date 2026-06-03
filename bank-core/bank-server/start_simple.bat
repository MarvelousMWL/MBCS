@echo off
cd /d e:\Agent\MBCS\bank-core\bank-server

set CLASSPATH=E:\Agent\MBCS\bank-core\bank-server\target\classes
set CLASSPATH=%CLASSPATH%;E:\Agent\MBCS\bank-core\bank-liability\target\classes
set CLASSPATH=%CLASSPATH%;E:\Agent\MBCS\bank-core\bank-customer\target\classes
set CLASSPATH=%CLASSPATH%;E:\Agent\MBCS\bank-core\bank-teller\target\classes
set CLASSPATH=%CLASSPATH%;E:\Agent\MBCS\bank-core\bank-common\target\classes
set CLASSPATH=%CLASSPATH%;E:\Agent\MBCS\bank-core\bank-server\libs\*

echo Classpath: %CLASSPATH%
echo Starting Bank Server...
java com.bank.server.BankServerApplication
pause
