:: Run the Repast stand along batch runner
::
:: The Java bin path may be passed in otherwise it is assumed to be on the path.

:: Set the java bin to the first arg
set "javacommand=%1" 

:: If no java command arg set then assume java is on the path
if "%~1" == "" set "javacommand=java"

%javacommand% -jar batch_runner.jar