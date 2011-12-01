/*CopyrightHere*/
package repast.simphony.util;

import simphony.util.messages.MessageCenter;

import java.io.IOException;
import java.io.StringReader;
import java.nio.BufferUnderflowException;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that will take in an a Readable object and will perform scanning functions on it. This
 * provides the ability to grab a String of characters up to a delimiter, following a pattern, or a
 * certain length.<br/>
 * 
 * This is somewhat a replacement for java's built in {@link java.util.Scanner} class, but isn't
 * final and provides some extra methods, while excluding the next[data type] type methods.
 * 
 * @author Jerry Vos
 * 
 * @see RandomAccessScanner
 */
public class Scanner implements Iterator<String> {
	public static final int BUF_SIZE = 1024;

	private static final MessageCenter msgCenter = MessageCenter.getMessageCenter(Scanner.class);

	protected CharBuffer buf;

	protected boolean eof = false;

	protected Readable source;

	private Matcher delimMatcher;

	private Pattern delimPattern;

	private IOException ioException;

	private Scanner() {
		buf = CharBuffer.allocate(BUF_SIZE);
		buf.limit(0);
		// by default use whitespace as delimiters
		useDelimiter("\\s");
	}

	public Scanner(String source) {
		this();
		this.source = new StringReader(source);
	}

	public Scanner(Readable source) {
		this();
		this.source = source;
	}

	public Scanner(ReadableByteChannel channel) {
		this();
		this.source = getReadable(channel, null);
	}

	public Scanner(ReadableByteChannel channel, String charset) {
		this();
		this.source = getReadable(channel, charset);
	}

	private Readable getReadable(ReadableByteChannel channel, String charset) {
		if (charset == null) {
			return Channels.newReader(channel, Charset.defaultCharset().name());
		} else {
			return Channels.newReader(channel, charset);
		}
	}

	private void syncDelimMatcherToBuf() {
		delimMatcher.region(0, buf.length());
	}

	protected void buffer() {
		/*
		 * What needs to happen the unneeded data from the buffer should be got rid of new data
		 * should be loaded in reset the buffer to the new data's position [0, dataEnd]
		 * 
		 * How this should happen: move all the data from position to limit to the beginning of the
		 * buffer buf.compact load new data into the cleared space set the buffer's position to 0,
		 * and limit to tne end of the new data
		 */
		buf.compact();

		int availLength = buf.length();
		int readLength;
		try {
			readLength = source.read(buf);

			if (availLength != readLength) {
				eof = true;
			}
		} catch (IOException e) {
			// assume this means EOF
			handleIOException(e);
		}

		buf.limit(buf.position());
		buf.position(0);
		syncDelimMatcherToBuf();
	}

	protected void handleIOException(IOException e) {
		eof = true;
		ioException = e;
	}

	protected void bufferMore() {
		/*
		 * What needs to happen the unneeded data from the buffer should be got rid of grow the
		 * buffer copy old data new data should be loaded in reset the buffer to the new data's
		 * position [0, dataEnd]
		 * 
		 * How this should happen: move all the data from position to limit to the beginning of the
		 * buffer buf.compact double the size of the buffer let buffer() handle the rest
		 */

		buf.compact();
		CharBuffer grownBuf = CharBuffer.allocate(buf.capacity() * 2);

		// copy the old buffer stuff over
		int oldPosition = buf.position();
		buf.position(0);
		buf.limit(oldPosition);
		grownBuf.put(buf);

		buf = grownBuf;

		// update the delimiter matcher with the new buffer
		if (delimMatcher != null) {
			delimMatcher.reset(buf);
		}

		buffer();
	}

	/**
	 * Returns true if there are some characters left to be returned, otherwise false.
	 * 
	 * @return true if there are characters left
	 */
	public boolean hasNext() {
		// This method is so simple because of the fact if we don't have a match for the delimiter,
		// as long as we have some buffer left we can just return that.
		if (eof && buf.position() == buf.limit()) {
			return false;
		}

		return true;
	}

	/**
	 * Same as {@link #getNextDelimited()}.
	 * 
	 * @see #getNextDelimited()
	 */
	public String next() {
		return getNextDelimited();
	}

	/**
	 * Fetches the next String of characters up to the current delimiter. If the end of the buffer
	 * is reached while trying to find the delimiter, the String of characters from the current
	 * position to the end of the stream will be returned. If we're at the end of the stream a
	 * NoSuchElementException will be thrown.
	 * 
	 * @see #hasNext()
	 * @return the next String of characters up to the delimiter or end of the stream, whichever
	 *         comes first
	 */
	public String getNextDelimited() {
		if (!hasNext()) {
			NoSuchElementException ex = new NoSuchElementException("No more elements");
			msgCenter.error("Scanner.getNextDelimiter: No more elements", ex);
			throw ex;
		}

		verifyDataAvailable();

		syncDelimMatcherToBuf();

		String matchedString = null;
		while (matchedString == null) {
			boolean sawMatch = delimMatcher.find();

			if (!sawMatch && eof) {
				// couldn't find the delimiter and hit eof so return the rest of the buffer
				return getStringFromBuf(0, buf.length());
			} else if (!sawMatch && !eof) {
				bufferMore();
				continue;
			}

			// sawMatch
			if (delimMatcher.hitEnd() && !eof) {
				// make sure the pattern doesn't go beyond the end of the buffer
				bufferMore();
				continue;
			}

			// found the delimiter somewhere
			int delimStart = delimMatcher.start();
			int delimEnd = delimMatcher.end();

			// grab data
			matchedString = getStringFromBuf(0, delimStart);

			// skip over the delimiter, NOTE: This disagrees with java's Scanner behavior
			buf.position(buf.position() + delimEnd - delimStart);
		}

		return matchedString;
	}

	/**
	 * Retrieves a sequence of characters that matches the specified pattern. This sequence must
	 * begin with the next character in the stream. If the pattern isn't matched (or isn't matched
	 * including the first character) an InputMismatchException will be thrown.
	 * 
	 * @see InputMismatchException
	 * 
	 * @param pattern
	 *            the pattern to fetch based off of
	 * @return a String that matches the specified pattern
	 */
	public String getNextPattern(String pattern) {
		verifyDataAvailable();

		Matcher matcher = Pattern.compile(pattern, Pattern.DOTALL).matcher(buf);

		String matchedString = null;
		while (matchedString == null) {
			boolean sawMatch = matcher.lookingAt();

			if (!sawMatch && !matcher.hitEnd()) {
				// The match didn't begin at the beginning of the buffer, therefore it never
				// can, even if we read in more data.
				InputMismatchException ex = new InputMismatchException("Didn't find match");
				msgCenter.error("Couldn't find a match for the specified pattern", ex);
				throw ex;
			}

			if (matcher.hitEnd() && !eof) {
				// make sure the pattern doesn't go beyond the end of the buffer
				bufferMore();
				matcher.reset();
				continue;
			}

			// grab data
			matchedString = getStringFromBuf(0, matcher.end());
		}

		return matchedString;
	}

	/**
	 * Retrieves the next n characters from the stream, where n is the specified length. If there
	 * are not enough characters left an exception will be thrown.
	 * 
	 * @see BufferUnderflowException
	 * 
	 * @param length
	 *            how many characters to fetch
	 * @return a string of the specified number of characters
	 */
	public String getNextLength(int length) {
		try {
			verifyDataAvailable();

			return getStringFromBuf(0, length);
		} catch (BufferUnderflowException ex) {
			NoSuchElementException except = new NoSuchElementException("Could not retrieve "
					+ length + " characters");
			msgCenter.error("Scanner.getNextLength: no more elements", except);
			throw except;
		}
	}

	private String getStringFromBuf(int start, int length) {
		return getStringFromBuf(start, length, true);
	}

	private String getStringFromBuf(int start, int length, boolean moveBufPos) {
		char[] matchedChars = new char[length];
		buf.get(matchedChars, start, length);
		if (!moveBufPos) {
			buf.position(buf.position() - length);
		}
		return new String(matchedChars);
	}

	private void verifyDataAvailable() {
		// this is needed in the case where we just started using the Scanner
		if (buf.limit() == 0) {
			buffer();
		}

		if (eof && buf.position() == buf.limit()) {
			throw new NoSuchElementException("At end of file");
		}
	}

	/**
	 * Sets the delimiter pattern used for {@link #next()} and {@link #getNextDelimited()}. This is
	 * a regular expression and will be converted to a Pattern.
	 * 
	 * @see Pattern
	 * @return the previous delimiter pattern
	 */
	public Pattern useDelimiter(String delimiter) {
		return useDelimiter(Pattern.compile(delimiter));
	}

	/**
	 * Sets the delimiter pattern used for {@link #next()} and {@link #getNextDelimited()}.
	 * 
	 * @return the previous delimiter pattern
	 */
	public Pattern useDelimiter(Pattern delimiterPattern) {
		Pattern oldDelimiter = this.delimPattern;

		this.delimPattern = delimiterPattern;
		this.delimMatcher = delimPattern.matcher(buf);

		return oldDelimiter;
	}

	/**
	 * Returns the delimiter pattern used for {@link #next()} and {@link #getNextDelimited()}.
	 * 
	 * @see #useDelimiter(Pattern)
	 * @see #useDelimiter(String)
	 * @return the current delimiter pattern
	 */
	public Pattern delimiter() {
		return this.delimPattern;
	}

	/**
	 * Currently unsupported. Throws an {@link UnsupportedOperationException}.
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove is not supported");
	}

	/**
	 * Returns the last IOException that occurred.
	 * 
	 * @return the last IOException that occurred
	 */
	public IOException ioException() {
		return ioException;
	}
}
