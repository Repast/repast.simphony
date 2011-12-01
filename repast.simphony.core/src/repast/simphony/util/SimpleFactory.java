/*CopyrightHere*/
package repast.simphony.util;

/**
 * A simple interface for factories that create some type.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
 */
public interface SimpleFactory<T> {
	/**
	 * Creates and returns an instance of the objects of type <T>.
	 * 
	 * @return a T instance
	 */
	T create();
}
