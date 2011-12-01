/*CopyrightHere*/
package repast.simphony.random;

import cern.jet.random.Distributions;
import cern.jet.random.engine.RandomEngine;

/**
 * A simple wrapper around {@link Distributions#nextLambda(double, double, RandomEngine)}.
 * 
 * @author Jerry Vos
 */
public class Lambda extends DistributionsAdapter {
	private static final long serialVersionUID = -7063161250387649496L;

	protected double l3;

	protected double l4;

	/**
	 * Constructs this distribution with the specified parameters to pass to the
	 * {@link Distributions#nextLambda(double, double, RandomEngine)} method.
	 * 
	 * @param l3
	 *            the lambda'a l3 parameter
	 * @param l4
	 *            the lambda'a l4 parameter
	 * @param engine
	 *            the lambda's random engine
	 */
	public Lambda(double l3, double l4, RandomEngine engine) {
		super(engine);

		this.l3 = l3;
		this.l4 = l4;
	}

	/**
	 * Returns the result of <code>Distributions.nextLamdba(l3, l4, engine)</code> where these
	 * parameters represent those passed in this distribution's construction.
	 */
	@Override
	public double nextDouble() {
		return nextLamdba(l3, l4);
	}
}
