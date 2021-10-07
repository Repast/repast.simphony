#!/bin/bash
# Repast Simphony Model Starter

PWD="${0%/*}"
cd $PWD

# Note the Repast Simphony Directories.
REPAST_SIMPHONY_ROOT=$PWD/repast.simphony/repast.simphony.runtime_$REPAST_VERSION
REPAST_SIMPHONY_LIB=$REPAST_SIMPHONY_ROOT/lib

# Define the Core Repast Simphony Directories and JARs
CP=$CP:$REPAST_SIMPHONY_ROOT/bin
CP=$CP:$REPAST_SIMPHONY_LIB/saf.core.runtime.jar
CP=$CP:$REPAST_SIMPHONY_LIB/commons-logging-1.1.2.jar
CP=$CP:$REPAST_SIMPHONY_LIB/javassist-3.17.1-GA.jar
CP=$CP:$REPAST_SIMPHONY_LIB/jpf.jar
CP=$CP:$REPAST_SIMPHONY_LIB/jpf-boot.jar
CP=$CP:$REPAST_SIMPHONY_LIB/log4j-1.2.16.jar
CP=$CP:$REPAST_SIMPHONY_LIB/xpp3_min-1.1.4c.jar
CP=$CP:$REPAST_SIMPHONY_LIB/xstream-1.4.7.jar
CP=$CP:$REPAST_SIMPHONY_LIB/xmlpull-1.1.3.1.jar
CP=$CP:$REPAST_SIMPHONY_LIB/commons-cli-1.3.1.jar
CP=$CP:$PWD/groovylib/$Groovy_Jar
CP=$CP:lib/*

# Change to the project directory
cd %PROJECT_NAME%

# Start the Model
java -XX:+IgnoreUnrecognizedVMOptions --add-modules=ALL-SYSTEM --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED -cp $CP repast.simphony.runtime.RepastMain  ./%SCENARIO_DIRECTORY%
