/*CopyrightHere*/
package repast.simphony.integration;

/**
 * Interface representing a class that will write data.
 * 
 * @author Jerry Vos
 */
public interface Writer {
	void write() throws Exception;
	
	void setWrittenObject(Queryable queryable);
}
