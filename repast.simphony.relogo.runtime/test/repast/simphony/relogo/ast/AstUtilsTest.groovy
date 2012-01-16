package repast.simphony.relogo.ast;
import org.apache.tools.ant.util.FileUtils;
import org.codehaus.groovy.ast.expr.ArgumentListExpression 
import org.codehaus.groovy.ast.expr.ConstantExpression 
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode 
import org.codehaus.groovy.ast.MethodNode 
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.VariableScope 
import org.codehaus.groovy.ast.stmt.BlockStatement 
import org.codehaus.groovy.ast.stmt.ExpressionStatement 
import org.codehaus.groovy.control.CompilePhase;
import org.objectweb.asm.Opcodes;
import junit.framework.Assert;

import org.codehaus.groovy.ast.builder.*

import repast.simphony.relogo.ast.ReLogoGlobalASTTransformation.DefDisplayReturner;

import groovy.util.GroovyTestCase;
import groovy.util.slurpersupport.GPathResult;

class AstUtilsTest extends GroovyTestCase {
	
	ReLogoGlobalASTTransformation trans = new ReLogoGlobalASTTransformation();
	public void setUp() {
		trans = new ReLogoGlobalASTTransformation();
	}
	
	public void testGetCandidateDisplayFiles(){
		String directoryString = "testdata/testdata.rs"
		List candidateFiles = trans.getCandidateDisplayFiles(directoryString)
		assertTrue(!candidateFiles.isEmpty())
		assertTrue(candidateFiles.size() == 2)
		Set expectedNames = ["repast.simphony.action.display_5.xml","repast.simphony.action.display_relogoDefault.xml"]
		assertEquals(expectedNames, candidateFiles.collect{it.name} as Set)
	}
	
	public void testFindDefaultReLogoDisplayFile(){
		//testdata.rs contains three files and one default display file = "repast.simphony.action.display_5.xml"
		String projectPath = "testdata"
		String projectName = "testdata"
		DefDisplayReturner result = trans.findDefaultReLogoDisplayFile(projectPath, projectName)
		assertEquals("repast.simphony.action.display_5.xml", result.displayFile.name)
		assertTrue(result.root instanceof GPathResult)
		//testdata2.rs contains one file and no default display file
		projectName = "testdata2"
		result = trans.findDefaultReLogoDisplayFile(projectPath, projectName)
		assertNull(result)
		//testdata3.rs contains no files
		projectName = "testdata3"
		result = trans.findDefaultReLogoDisplayFile(projectPath, projectName)
		assertNull(result)
	}
	
	public void testModifyDisplayFile(){
		// testdata4.rs is empty to begin with (see tearDown)
		// testdata5.rs has the initial file and the expected file which are used but not modified there
		String projectPath = "testdata"
		String projectName = "testdata4"
		String projectNameSource = "testdata5"
		String fileNameBase = "repast.simphony.action.display_5.xml"
		String fileNameBaseExpected = "repast.simphony.action.display_5_expected.xml"
		String sourceFileName = projectPath + File.separator + projectNameSource + ".rs" + File.separator + fileNameBase
		String expectedFileName = projectPath + File.separator + projectNameSource + ".rs" + File.separator + fileNameBaseExpected
		String destinationFileName = projectPath + File.separator + projectName + ".rs" + File.separator + fileNameBase
		File testFile = new File(destinationFileName)
		assertTrue(!testFile.exists())
		try {
			FileUtils.getFileUtils().copyFile(sourceFileName, destinationFileName)
		}
		catch (Exception e){
			e.printStackTrace()
		}
		assertTrue(testFile.exists())
		String className = "AnotherLink"
		String basePackageName = "hello"
		trans.modifyDisplayFile(projectPath, projectName, className, basePackageName)
		String destFileContent = new File(destinationFileName).text
		String expectedFileContent = new File(expectedFileName).text
		assertEquals(expectedFileContent,destFileContent)
	}
	
	/**
	 * Tests to see that non default_observer_context contexts are not mistakenly modified.
	 */
	public void testDefaultObserverContextNotFound(){
		String projectPath = "testdata"
		String projectName = "testdata6"
		String className = "Infection"
		String basePackageName = "hello"
		String sep = File.separator
		String contextFilePath = projectPath + sep + "${projectName}.rs" + sep +"context.xml"
		
		trans.checkToModifyContextFile(projectPath, projectName, className, basePackageName, contextFilePath)
		String result = new File(contextFilePath).text
		String expected = '''<?xml version="1.0" encoding="UTF-8"?>
<context xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" id="Zombies_Demo" xsi:noNamespaceSchemaLocation="http://repast.org/scenario/context" xmlns="">
  <context id="not_default_observer_context">
    <projection id="Grid2d" type="grid"/>
    <projection id="Space2d" type="continuous space"/>
    <projection id="DirectedLinks" type="network"/>
    <projection id="UndirectedLinks" type="network"/>
    <projection id="Tracking Network" type="network"/>
    <projection id="UserLink" type="network"/>
    <projection id="Infection" type="network"/>
  </context>
</context>
'''
		assertEquals(expected,result)
	}
	
	/**
	* Tests to see that non default_observer_context contexts are not mistakenly modified.
	*/
   public void testSubContextNotFound(){
	   String projectPath = "testdata"
	   String projectName = "testdata8"
	   String className = "Infection"
	   String basePackageName = "hello"
	   String sep = File.separator
	   String contextFilePath = projectPath + sep + "${projectName}.rs" + sep +"context.xml"
	   
	   trans.checkToModifyContextFile(projectPath, projectName, className, basePackageName, contextFilePath)
	   String result = new File(contextFilePath).text
	   String expected = '''<?xml version="1.0" encoding="UTF-8"?>
<context xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" id="Zombies_Demo" xsi:noNamespaceSchemaLocation="http://repast.org/scenario/context" xmlns="">
</context>
'''
	   assertEquals(expected,result)
   }
	
	/**
	 * context.xml here does not contain TestLink3 but the display file does
	 * This test checks to see that additional TestLink3 entries are not added to the display file.
	 */
	public void testAvoidDuplicationInDisplayFile(){

		// testdata7a.rs is empty to begin with (see tearDown)
		// testdata7.rs has the initial file and the expected file which are used but not modified there
		String projectPath = "testdata"
		String projectNameSource = "testdata7"
		String projectName = "testdata7a"
		String className = "TestLink3"
		String basePackageName = "hello"
		String sep = File.separator
		
		// destination context file path
		String destinationContextFilePath = projectPath + sep + projectName + ".rs" + sep +"context.xml"
		// source context file path
		String contextFilePathSource = projectPath + sep + projectNameSource + ".rs" + sep + "context.xml"
		String fileNameBaseExpected = "repast.simphony.action.display_relogoDefault_expected.xml"
		String fileNameBase = "repast.simphony.action.display_relogoDefault.xml"
		
		// source display file path
		String sourceDisplayFilePath = projectPath + File.separator + projectNameSource + ".rs" + File.separator + fileNameBase
		// expected display file path
		String expectedDisplayFilePath = projectPath + File.separator + projectNameSource + ".rs" + File.separator + fileNameBaseExpected
		// destination display file path (the modifiable file)
		String destinationDisplayFilePath = projectPath + File.separator + projectName + ".rs" + File.separator + fileNameBase
		
		
		File destinationDisplayFile = new File(destinationDisplayFilePath)
		File destinationContextFile = new File(destinationContextFilePath)
		assertTrue(!(destinationDisplayFile.exists() || destinationContextFile.exists()))
		try {
			FileUtils.getFileUtils().copyFile(sourceDisplayFilePath, destinationDisplayFilePath)
			FileUtils.getFileUtils().copyFile(contextFilePathSource, destinationContextFilePath)
		}
		catch (Exception e){
			e.printStackTrace()
		}
		assertTrue(destinationDisplayFile.exists() && destinationContextFile.exists())
		trans.checkToModifyContextFile(projectPath, projectName, className, basePackageName, destinationContextFilePath)
		
		
		String destFileContent = destinationDisplayFile.text
		String expectedFileContent = new File(expectedDisplayFilePath).text
		assertEquals(expectedFileContent,destFileContent)
		
	}
	
	public void testXmlSlurping(){
		String str = '''<?xml version="1.0" encoding="UTF-8"?>
		<context xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" id="Zombies_Demo" xsi:noNamespaceSchemaLocation="http://repast.org/scenario/context" xmlns="">
		  <context id="not_default_observer_context">
		    <projection id="Grid2d" type="grid"/>
		    <projection id="Space2d" type="continuous space"/>
		    <projection id="DirectedLinks" type="network"/>
		    <projection id="UndirectedLinks" type="network"/>
		    <projection id="Tracking Network" type="network"/>
		    <projection id="UserLink" type="network"/>
		    <projection id="Infection" type="network"/>
		  </context>
		</context>
		'''
		def root = new XmlSlurper().parseText(str)
		def listOfDefaultReLogoContexts = root.context.findAll({it.@id.equals("default_observer_context")})
		assertTrue(listOfDefaultReLogoContexts.isEmpty())
		
		String str2 = '''
<root2>
<a>
<b>Hello</b>
<b>There</b>
</a>
<a>
<b>Hello2</b>
<b>There2</b>
</a>
</root2>
'''
		def root2 = new XmlSlurper().parseText(str2)
		def result2 = root2.a.b
		assertTrue(result2.size() == 4)
		def list = ["Hello","There","Hello2","There2"]
		def result3 = result2.findAll{
			it.text().equals("Hello2")
		}
		assertTrue(result3.size() == 1)

		def result4 = root2.a.b.c.d
		assertTrue(result4.isEmpty())
	}
	
	public void tearDown(){
		// testdata4.rs clearing
		File file = new File("testdata/testdata4.rs/repast.simphony.action.display_5.xml")
		if (file.exists()){
			file.delete()
		}
		
		// testdata7a.rs clearing
		file = new File("testdata/testdata7a.rs/context.xml")
		if (file.exists()){
			file.delete()
		}
		file = new File("testdata/testdata7a.rs/repast.simphony.action.display_relogoDefault.xml")
		if (file.exists()){
			file.delete()
		}
		
	}
	
	
	

}
