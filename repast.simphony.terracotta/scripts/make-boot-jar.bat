@echo off

rem
rem  All content copyright (c) 2003-2006 Terracotta, Inc.,
rem  except as may otherwise be noted in a separate copyright notice.
rem  All rights reserved.
rem

setlocal
set TC_INSTALL_DIR=%~d0%~p0..
for %%i in ("%TC_INSTALL_DIR%") do set TC_INSTALL_DIR=C:\Program Files\Terracotta\terracotta-2.5.0

if not exist "%JAVA_HOME%" set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_03
FOR %%i IN ("%JAVA_HOME%") DO SET JAVA_HOME=%%~fsi

set CLASSPATH=%TC_INSTALL_DIR%\lib\tc.jar
set JAVA_OPTS=-Dtc.install-root=%TC_INSTALL_DIR% %JAVA_OPTS%
%JAVA_HOME%\bin\java %JAVA_OPTS% -cp %CLASSPATH% com.tc.object.tools.BootJarTool make %*
endlocal
