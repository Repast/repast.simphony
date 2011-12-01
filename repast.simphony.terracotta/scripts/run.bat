@echo off

rem
rem  All content copyright (c) 2003-2006 Terracotta, Inc.,
rem  except as may otherwise be noted in a separate copyright notice.
rem  All rights reserved.
rem

rem
rem samples\pojos\TerracottaTest
rem
rem Environment variables required by dso-env helper script:
rem  JAVA_HOME: root of Java Development Kit installation
rem  TC_INSTALL_DIR: root of Terracotta installation
rem
rem Arguments to dso-env helper script:
rem  -q: do not print value of TC_JAVA_OPTS
rem  tc-config.xml: path to DSO config file
rem
rem Environment variable set by dso-env helper script:
rem  TC_JAVA_OPTS: Java options needed to activate DSO
rem

setlocal
set TC_INSTALL_DIR=%~d0%~p0..\..\..
for %%i in ("%TC_INSTALL_DIR%") do set TC_INSTALL_DIR=%%~fsi

cd %~d0%~p0

if not exist "%JAVA_HOME%" set JAVA_HOME=%TC_INSTALL_DIR%\jre
for %%i in ("%JAVA_HOME%") do set JAVA_HOME=%%~fsi

set TC_CONFIG_PATH=tc-config.xml
call %TC_INSTALL_DIR%\bin\dso-env.bat -q "%TC_CONFIG%"
set JAVA_OPTS=%TC_JAVA_OPTS% %JAVA_OPTS%
start "Some Project" %JAVA_HOME%\bin\java -Dcom.tc.l1.connect.versionMatchCheck.enabled=false %JAVA_OPTS% -cp classes repast.simphony.runtime %* 

endlocal
