The ReLogo API docs are generated in the following steps:
1. Look at the buildGroovyDocs.xml file and follow the instructions for running via command line.
2. Run the repast.simphony.relogo.util.ReLogoReferenceCreator class.

Further details on merging with Repast Simphony API here:
Creating the ReLogoPrimitives.html and the ReLogo Groovydocs: (as of Feb 6, 2012)

The buildGroovyDocs.xml ant build file is used to create the Groovydoc API document.

This is merged this with the main Repast Simphony API by:
0. Use RepastJavaAPI.zip from r.s.docs repository.
1. including the groovy.ico and inherit.gif files to the base directory.
2. replacing the existing repast/simphony/relogo directory with the groovydoc generated directory.
3. executed the command:
	find . -name '*.DS_Store' -type f -delete 
to remove all the .DS_Store files
4. zip up and replace old version: zip -r RepastJavaAPI api


ReLogoPrimitives.html document generation (files in r.s.relogo.runtime):
- there is an Excel file, separated into sheets for turtle, patch, link, observer and utility primitives.
- ReLogoReferenceCreator.groovy is used to parse the Excel file and to create the primitives html file.
- the primitives html file is then placed at the top level of the Repast Simphony API documentation

The ReLogoReferenceCreator class has a number of tests within the reference document creation:
- check to see that the link exists in the ReLogo API
- check to see which methods from the T,P,L,O,Us classes are not included in the Primitives document

The ReLogoReferenceCreator class creates the Primitives document in the docs/ReLogo API directory.
