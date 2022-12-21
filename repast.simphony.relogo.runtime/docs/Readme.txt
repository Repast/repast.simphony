The ReLogo API docs are generated in the following steps:

1. Look at the buildGroovyDocs.xml file and follow the instructions for running 
   via command line or Eclipse launch config.

	- The buildGroovyDocs.xml ant build file is used to create the Groovydoc API document.
	- This will create the docs/RelogoAPI folder with groovy doc html 

2. Update the release version number in repast.simphony.relogo.util.ReLogoReferenceCreator 
	   and then run it.
	   
	   ***** NOTE! Requires Java 11 and doesnt work with Java 17+.  Set the Java execution
	         environment in the launch.

	- ReLogoPrimitives.html document generation (files in r.s.relogo.runtime):
		- there is an Excel file, separated into sheets for turtle, patch, link, 
		  observer and utility primitives.
		- ReLogoReferenceCreator.groovy is used to parse the Excel file and to 
		  create the primitives html file.
		- the primitives html file is then placed at the top level of the Repast 
		  Simphony API documentation

	- The ReLogoReferenceCreator class has a number of tests within the reference 
	  document creation:
		- check to see that the link exists in the ReLogo API
		- check to see which methods from the T,P,L,O,Us classes are not included 
		  in the Primitives document


3. Merged groovy docs with the main Repast Simphony API by

	- Copy the RelogoPrimitives.html to the RepastSimphonyAPI base directory.
	- Copy the groovy.ico and inherit.gif files to the RepastSimphonyAPI base directory.
	- Replace the existing repast/simphony/relogo directory with the groovydoc 
	  generated directory - ignore other generated files. 
	
	- Optionally executed the command to remove all the .DS_Store files:
		find . -name '*.DS_Store' -type f -delete 

	- zip up and replace old version: zip -r RepastSimphonyAPI api