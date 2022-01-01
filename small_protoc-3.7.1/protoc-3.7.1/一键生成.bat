@echo off

set DIR=%~dp0
set INPUTDIR=proto
set JAVADIR=java
echo "%DIR%"

cd /d %DIR%%INPUTDIR%

setlocal enabledelayedexpansion

if exist %DIR%%JAVADIR% rmdir /s /q %DIR%%JAVADIR%
@echo "mkdir java"
mkdir %DIR%%JAVADIR%

for /r %%i in (*.proto) do (
	echo "%DIR%%JAVADIR%"
	%DIR%protoc --proto_path=%DIR%%INPUTDIR% --java_out=%DIR%%JAVADIR% %%i
)

echo "finished"
cd /d %DIR%
pause