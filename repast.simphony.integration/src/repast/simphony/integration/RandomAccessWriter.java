/*CopyrighHere*/
package repast.simphony.integration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.util.Stack;

/**
 * A {@link java.io.PrintWriter} that writes to a file channel, allowing it to do random access
 * types of writes. This supports a stack of marks (and therefore resets). It also allows for
 * jumping to offsets in the file, determining the current file offset, and truncating the file.
 * 
 * @author Jerry Vos
 */
public class RandomAccessWriter extends PrintWriter {
	private FileChannel fileChannel;

	private Stack<Long> markStack;

	/**
	 * Opens this writer on the specified file.
	 * 
	 * @param file
	 *            the file to write to
	 * @throws FileNotFoundException
	 */
	public RandomAccessWriter(File file) throws FileNotFoundException {
		super(file);
		init(null, file);
	}

	/**
	 * Opens this writer on the specified file.
	 * 
	 * @param fileName
	 *            the name of the file to open
	 * @throws FileNotFoundException
	 */
	public RandomAccessWriter(String fileName) throws FileNotFoundException {
		super(fileName);
		init(fileName, null);
	}

	private void init(String fileName, File file) throws FileNotFoundException {
		markStack = new Stack<Long>();
		captureFileChannel(fileName, file);
	}

	private void captureFileChannel(String fileName, File file) throws FileNotFoundException {
		FileOutputStream outStream;
		if (fileName != null) {
			outStream = new FileOutputStream(fileName);
		} else {
			outStream = new FileOutputStream(file);
		}
		super.out = new BufferedWriter(new OutputStreamWriter(outStream));

		this.fileChannel = outStream.getChannel();
	}

	private long getFileOffsetInternal() throws IOException {
		super.flush();
		return fileChannel.position();
	}

	/**
	 * Retrieves the actual offset of the file, disregarding any buffering. A result of this call is
	 * that all buffered data is flushed before this function returns.
	 * 
	 * @return the current offset of the file
	 * @throws IOException
	 */
	public long getTrueOffset() throws IOException {
		long fileOffset = getFileOffsetInternal();

		if (fileOffset <= 0) {
			return fileOffset;
		}

		return fileOffset;
	}

	/**
	 * Adds a mark to the mark queue at the current location in the file. Since this used
	 * {@link #getTrueOffset()}, this causes the underlying data stream to flush.
	 * 
	 * @throws IOException
	 */
	public void mark() throws IOException {
		markStack.push(getTrueOffset());
	}

	/**
	 * Removes a mark from the mark queue and returns it.
	 * 
	 * @return the top of the mark queue
	 */
	public Long popMark() {
		if (!markStack.isEmpty()) {
			return markStack.pop();
		} else {
			return null;
		}
	}

	/**
	 * This jumps the file offset back to the previously marked position. This does not check if the
	 * mark stack is empty, so when calling this with an empty mark stack, an exception will be
	 * thrown. This does <em>not</em> pop the mark that was used off the stack.
	 * 
	 * @throws IOException
	 */
	public void reset() throws IOException {
		super.flush();
		position(markStack.peek());
	}

	/**
	 * Positions the file to the specified position.
	 * 
	 * @param position
	 *            the position to set the write mark to
	 * @throws IOException
	 */
	public void position(long position) throws IOException {
		fileChannel.position(position);
	}

	/**
	 * Truncates the file to the current offset. This is the same as using {@link #truncate(long)}
	 * with the value from {@link #getTrueOffset()}.
	 * 
	 * @return the truncated channel
	 * @throws IOException
	 */
	public FileChannel truncateToPosition() throws IOException {
		return truncate(getTrueOffset());
	}

	/**
	 * Truncates the file to the specified size.
	 * 
	 * @param size
	 *            the size to truncate the file to
	 * @return the truncated channel
	 * @throws IOException
	 */
	public FileChannel truncate(long size) throws IOException {
		super.flush();
		return fileChannel.truncate(size);
	}
	
	/**
	 * 
	 */
	@Override
	public void close() {
		try {
			super.close();
			fileChannel.close();
		} catch (Exception ex) {
		}
	}
}
