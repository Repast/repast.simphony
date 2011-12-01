package repast.simphony.util;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Stack;

/**
 * This class adds random access capabilities to the {@link repast.simphony.integration.Scanner}
 * class. This contains a mark stack, meaning you can have multiple marks set at once, and jump back
 * up them.
 * 
 * @author Jerry Vos
 * @version $Revision$ $Date$
 */
public class RandomAccessScanner extends Scanner {

	protected FileChannel fileChannel;

	protected Stack<Long> markStack;

	public RandomAccessScanner(FileChannel channel, String charset) {
		super(channel, charset);

		init();

		fileChannel = channel;
	}

	public RandomAccessScanner(FileChannel channel) {
		super(channel);

		init();

		fileChannel = channel;
	}

	public RandomAccessScanner(String source) {
		super(source);

		init();
	}

	private void init() {
		this.markStack = new Stack<Long>();
	}

	private long getFileOffsetInternal() throws IOException {
		if (fileChannel != null) {
			return fileChannel.position();
		}
		return -1;
	}

	/**
	 * Returns the current position of the channel. This accounts for any buffered data, therefore
	 * it returns the true position the scanner is at, not just the position the channel is at.
	 * 
	 * @return the position the scanner is at in the channel's data.
	 * @throws IOException
	 */
	public long getPosition() throws IOException {
		long fileOffset = getFileOffsetInternal();

		if (fileOffset <= 0) {
			return fileOffset;
		}

		return fileOffset - buf.length();
	}

	protected int getBufferLength() {
		return buf.length();
	}

	protected void syncToSource() {
		buf.clear();
		buf.position(0);
		buf.limit(0);
		eof = false;
	}

	/**
	 * Adds a mark to the mark stack.
	 * 
	 * @throws IOException
	 */
	public void mark() throws IOException {
		markStack.push(getPosition());
	}

	/**
	 * Removes a mark from the mark stack. This does not position the stream to the location of the
	 * pop'd mark.
	 * 
	 * @return the value of the mark
	 */
	public Long popMark() {
		if (!markStack.isEmpty()) {
			return markStack.pop();
		} else {
			return null;
		}
	}

	/**
	 * Positions the stream to the position last marked. This does not pop the mark off the mark
	 * stack.
	 * 
	 * @throws IOException
	 */
	public void reset() throws IOException {
		position(markStack.peek());
	}

	/**
	 * Positions the stream to the specified location. This results in a clearing of the internal
	 * buffer and a refilling of it starting at the specified location.
	 * 
	 * @param position
	 *            the position to put the stream at
	 * @throws IOException
	 */
	public void position(long position) throws IOException {
		fileChannel.position(position);
		syncToSource();
		buffer();
	}
}
