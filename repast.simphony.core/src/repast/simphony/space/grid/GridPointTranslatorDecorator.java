/*CopyrightHere*/
package repast.simphony.space.grid;

/**
 * This represents a {@link repast.simphony.space.continuous.PointTranslator} that performs an operation using its
 * methods, and then after it has finished doing its translations will pass its result to another
 * {@link repast.simphony.space.continuous.PointTranslator}.
 * 
 * @see http://en.wikipedia.org/wiki/Decorator_pattern
 * 
 * @author Jerry Vos
 */
public interface GridPointTranslatorDecorator extends GridPointTranslator {
	/**
	 * Retrieves the translator being decorated.
	 * 
	 * @return the translator being decorated
	 */
	public GridPointTranslator getDecorated();

	/**
	 * Sets the translator to decorate.
	 * 
	 * @param decorated
	 *            the translator to decorates
	 */
	public void decorate(GridPointTranslator decorated);
}
