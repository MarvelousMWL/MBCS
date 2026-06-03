@echo off
cd /d "E:\Agent\MyProject\MBCS\bank-core\bank-server"
mvn spring-boot:run -s "E:\Agent\MyProject\MBCS\bank-maven\settings.xml" > startup.log 2>&1
