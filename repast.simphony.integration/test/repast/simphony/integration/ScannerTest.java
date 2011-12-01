package repast.simphony.integration;


import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junitx.util.PrivateAccessor;

import org.jmock.MockObjectTestCase;

import repast.simphony.util.Scanner;

public class ScannerTest extends MockObjectTestCase {

	private Scanner scanner;

	@Override
	protected void setUp() throws Exception {
		scanner = new Scanner("blah\nfoo:bar");
	}
	
	/*
	 * Test method for 'repast.simphony.integration.Scanner.Scanner(String)'
	 */
	public void testScannerString() throws NoSuchFieldException, IOException {
		scanner = new Scanner("blah");
		StringReader reader = (StringReader) PrivateAccessor.getField(scanner, "source");
		
		char[] charBuf = new char[1];

		reader.read(charBuf);
		assertEquals('b', charBuf[0]);
		reader.read(charBuf);
		assertEquals('l', charBuf[0]);
		reader.read(charBuf);
		assertEquals('a', charBuf[0]);
		reader.read(charBuf);
		assertEquals('h', charBuf[0]);
		
		// make sure get EOF
		assertEquals(-1, reader.read());
	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.Scanner(Readable)'
	 */
	public void testScannerReadable() throws NoSuchFieldException {
		Readable readable = (Readable) mock(Readable.class).proxy();
		
		scanner = new Scanner(readable);
		
		assertSame(readable, PrivateAccessor.getField(scanner, "source"));
	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.Scanner(ReadableByteChannel)'
	 */
	public void testScannerReadableByteChannel() {

	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.Scanner(ReadableByteChannel, String)'
	 */
	public void testScannerReadableByteChannelString() {

	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.hasNext()'
	 */
	public void testHasNext() {
		scanner.useDelimiter("\n");
		
		assertTrue(scanner.hasNext());
		scanner.next();
		assertTrue(scanner.hasNext());
		scanner.next();
		assertFalse(scanner.hasNext());
	}
	
	/*
	 * Test method for 'repast.simphony.integration.Scanner.next()'
	 */
	public void testNext() {
		scanner.useDelimiter("\n");
		
		assertEquals("blah", scanner.next());
		assertEquals("foo:bar", scanner.next());
		try {
			scanner.next();
			fail();
		} catch (NoSuchElementException ex) {
			// should throw this
		}
	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.getNextDelimited()'
	 */
	public void testGetNextDelimited() {
		scanner.useDelimiter("\n");
		
		assertEquals("blah", scanner.getNextDelimited());
		assertEquals("foo:bar", scanner.getNextDelimited());
	}

	public void testMixedGets() {
		scanner.useDelimiter("\n");
		assertEquals("b", scanner.getNextPattern("."));
		assertEquals("lah", scanner.getNextDelimited());
		assertEquals("fo", scanner.getNextLength(2));
		assertEquals("o:", scanner.getNextPattern(".*:"));
		assertEquals("bar", scanner.getNextDelimited());
	}
	
	/*
	 * Test method for 'repast.simphony.integration.Scanner.getNextPattern(String)'
	 */
	public void testGetNextPattern() {
		assertEquals("blah", scanner.getNextPattern("blah"));
		assertEquals("\n", scanner.getNextPattern("."));
		assertEquals("foo:bar", scanner.getNextPattern(".+"));
	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.getNextLength(int)'
	 */
	public void testGetNextLength() {
		assertEquals("blah", scanner.getNextLength(4));
		assertEquals("\nfoo:bar", scanner.getNextLength("\nfoo:bar".length()));
		
		try {
			scanner.getNextLength(1);
			fail();
		} catch (NoSuchElementException ex) {
			// we've already read everything so this should get thrown
		}
	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.getNextLength(int)'
	 */
	public void testGetNextLengthOverflow() {
		assertEquals("blah", scanner.getNextLength(4));
		try {
			assertEquals("\nfoo:bar", scanner.getNextLength("\nfoo:bar".length() + 1));
			fail();
		} catch (NoSuchElementException ex) {
			// this should be thrown because we're grabbing too much data
		}
	}
	
	/*
	 * Test method for 'repast.simphony.integration.Scanner.useDelimiter(String)'
	 */
	public void testUseDelimiter() throws NoSuchFieldException {
		String delimiter = "asdfasdfasdf";
		scanner.useDelimiter(delimiter);
		
		assertEquals(delimiter, ((Pattern) PrivateAccessor.getField(scanner, "delimPattern")).pattern());
		
		assertEquals(delimiter, ((Matcher) PrivateAccessor.getField(scanner, "delimMatcher")).pattern().pattern());
	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.delimiter()'
	 */
	public void testDelimiter() throws NoSuchFieldException {
		Pattern delimiter = Pattern.compile("asdfasdfasdf");
		PrivateAccessor.setField(scanner, "delimPattern", delimiter);
		assertEquals(delimiter, scanner.delimiter());
	}

	/*
	 * Test method for 'repast.simphony.integration.Scanner.remove()'
	 */
	public void testRemove() {
		try {
			scanner.remove();
			fail();
		} catch (UnsupportedOperationException ex) {
			// this should be thrown per the javadocs
		}
	}

}
