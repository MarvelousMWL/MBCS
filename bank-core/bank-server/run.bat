@echo off
cd /d E:\Agent\MyProject\MBCS\bank-core\bank-server
echo Starting at %DATE% %TIME% > run_log.txt
mvn spring-boot:run -s E:\Agent\MyProject\MBCS\bank-maven\settings.xml >> run_log.txt 2>&1
echo Exited at %DATE% %TIME% >> run_log.txt
