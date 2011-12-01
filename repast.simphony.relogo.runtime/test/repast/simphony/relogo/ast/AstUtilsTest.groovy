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
	
	public void tearDown(){
		File file = new File("testdata/testdata4.rs/repast.simphony.action.display_5.xml")
		if (file.exists()){
			file.delete()
		}
	}
	
	
	

}
