The ReLogo API docs are generated in the following steps:

1. Use the existing Eclipse launch config to run buildGroovyDocs.xml.  The reference
	to specific Groovy JDK local libs is important.

	- The buildGroovyDocs.xml ant build file is used to create the Groovydoc API document.
	- This will create the docs/RelogoAPI folder with groovy doc html
	- This will copy the relevant parts of groovydoc into the Repast javadocs folder
		automatically. 

2. Update the release version number in repast.simphony.relogo.util.ReLogoReferenceCreator 
	   and then run it.
	   
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

3. Copy the RelogoPrimitives.html to the RepastSimphonyAPI base directory.
