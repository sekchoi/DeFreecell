@rem echo off

set DIR=%~dp0
set DIR=%DIR:~0,-1%
cd /d %DIR%

set JAR=DeFreecell.jar

set JAVA=javaw.exe
rem set JAVA=d:\wbin\jdk17\bin\javaw.exe

rem start %JAVA% -cp .\%JAR% quixote.ai.defreecell.DeFreecell
start %JAVA% -jar %JAR%
