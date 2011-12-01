/*CopyrightHere*/
package repast.simphony.space.continuous;

/**
 * This represents a {@link PointTranslator} that performs an operation using its
 * methods, and then after it has finished doing its translations will pass its result to another
 * {@link PointTranslator}.
 * 
 * @see http://en.wikipedia.org/wiki/Decorator_pattern
 * 
 * @author Jerry Vos
 */
public interface PointTranslatorDecorator extends PointTranslator {
	/**
	 * Retrieves the translator being decorated.
	 * 
	 * @return the translator being decorated
	 */
	public PointTranslator getDecorated();

	/**
	 * Sets the translator to decorate.
	 * 
	 * @param decorated
	 *            the translator to decorates
	 */
	public void decorate(PointTranslator decorated);
}
