@ECHO OFF
TITLE %PROJECT_NAME%

REM Repast Simphony model run script for Windows systems 
REM 
REM Please note that the paths given below use a Linux-like notation. 

REM Note the Repast Simphony Directories.
set REPAST_SIMPHONY_ROOT=../repast.simphony/repast.simphony.runtime_$REPAST_VERSION/

REM Define the Core Repast Simphony Directories and JARs
SET CP=%CP%;%REPAST_SIMPHONY_ROOT%bin
SET CP=%CP%;%REPAST_SIMPHONY_ROOT%lib/*

REM Groovy jar
SET CP=%CP%;../groovylib/$Groovy_Jar

REM User model lib jars
SET CP=%CP%;lib/*

REM Change to the project directory
CD "%PROJECT_NAME%"

REM Start the Model
START javaw -XX:+IgnoreUnrecognizedVMOptions --add-modules=ALL-SYSTEM --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED -cp "%CP%" repast.simphony.runtime.RepastMain "./%SCENARIO_DIRECTORY%"
