/*CopyrightHere*/
package repast.simphony.integration;

/**
 * Interface representing something that performs both Writing and Reading for outputting and
 * reading in data.
 * 
 * @author Jerry Vos
 */
public interface IntegrationSource extends Reader, Writer {
	/**
	 * Sets the writer to write with.
	 * 
	 * @param writer
	 *            the writer to use
	 */
	void setWriter(Writer writer);

	/**
	 * Retrieves the writer that will be used to perform writes.
	 * 
	 * @return the writer that will be used to perform writes
	 */
	Writer getWriter();

	/**
	 * Sets the reader to write with.
	 * 
	 * @param reader
	 *            the reader to use
	 */
	void setReader(Reader reader);

	/**
	 * Retrieves the reader that will be used to perform reads.
	 * 
	 * @return the reader that will be used to perform reads
	 */
	Reader getReader();
}
