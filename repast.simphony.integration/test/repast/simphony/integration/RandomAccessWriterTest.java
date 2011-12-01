/*CopyrightHere*/
package repast.simphony.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Tests for {@link repast.simphony.integration.RandomAccessWriter}.
 * 
 * @author Jerry Vos
 */
public class RandomAccessWriterTest extends TestCase {

	private File testFile;

	private void checkFile(String expected) throws IOException {
		FileInputStream file = new FileInputStream(testFile);
		byte[] b = new byte[file.available()];
		file.read(b);
		file.close();
		String result = new String(b);
		assertEquals(expected, result);
		file.close();
	}

	@Override
	protected void setUp() throws Exception {
		testFile = File.createTempFile("randomaccesswritertest", ".txt");
		testFile.deleteOnExit();
	}
	
	@Override
	protected void tearDown() throws Exception {
		if (testFile.exists()) {
			testFile.delete();
		}
	}

	public void testMarkWriteResetTruncate() throws IOException {
		RandomAccessWriter writer = new RandomAccessWriter(testFile);
		writer.mark();
		writer.write("asdfasdfasdfasdf");
		writer.reset();
		writer.write("1234");
		writer.truncate("asdfasdfasdfasdf".length());
		writer.close();
		checkFile("1234asdfasdfasdf");
	}

	/*
	 * Test method for 'repast.simphony.integration.RandomAccessWriter.RandomAccessWriter(String)'
	 */
	public void testWriteMarkResetTruncate() throws IOException {
		RandomAccessWriter writer = new RandomAccessWriter(testFile);
		writer.write("asdf");
		writer.mark();
		
		writer.reset();
		writer.truncateToPosition();
		writer.close();
		
		checkFile("asdf");
	}

	/*
	 * Test method for 'repast.simphony.integration.RandomAccessWriter.getTrueOffset()'
	 */
	public void testAgain() throws IOException {
		RandomAccessWriter writer = new RandomAccessWriter(testFile);
		writer.write("asdf");
		writer.mark();
		writer.write("asdf");
		writer.reset();
		writer.close();
//		writer.truncateToPosition();
		
		checkFile("asdfasdf");
	}
	
	public void testAgain2() throws Exception {
		RandomAccessWriter writer = new RandomAccessWriter(testFile.getPath());
		writer.write("asdf");
		writer.mark();
		writer.write("as34");
		writer.reset();
		writer.write("12");
		writer.mark();
		writer.write("df");
		writer.reset();
		writer.write("34");
		writer.popMark();
		writer.popMark();
		writer.popMark();
		
		writer.close();
		
		checkFile("asdf1234");
	}
	
	public void testAgain3() throws Exception {
		RandomAccessWriter writer = new RandomAccessWriter(testFile.getPath());
		writer.write("asdf");
		writer.mark();
		writer.write("as34");
		writer.reset();
		writer.truncateToPosition();
		writer.write("xyzw");
		writer.truncate("asdfxy".length());
		writer.reset();
		writer.close();
		
		checkFile("asdfxy");
	}
}
