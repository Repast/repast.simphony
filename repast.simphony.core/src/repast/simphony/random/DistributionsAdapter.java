/*CopyrightHere*/
package repast.simphony.random;

import cern.jet.random.AbstractDistribution;
import cern.jet.random.Distributions;
import cern.jet.random.engine.RandomEngine;
import simphony.util.messages.MessageCenter;

/**
 * A wrapper around the {@link Distributions} class. Instances of this class will implement the
 * nextMethod and most likely have it delegate to some specific nextXYZ method. For instance, the
 * Lambda subclass delegates to the nextLambda method.<p/>
 * 
 * This class's {@link #nextDouble()} method <em>always</em> throws an
 * {@link UnsupportedOperationException}.
 * 
 * @see Lambda
 * 
 * @author Jerry Vos
 */
public class DistributionsAdapter extends AbstractDistribution {
	private static final long serialVersionUID = -1395921661556148374L;
	
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(DistributionsAdapter.class);
	
	/**
	 * Constructs this with the specified random number generator
	 * 
	 * @param randomGenerator
	 *            the random number generator
	 */
	public DistributionsAdapter(RandomEngine randomGenerator) {
		super();
		super.randomGenerator = randomGenerator;
	}

	/**
	 * Throws an {@link UnsupportedOperationException}.
	 */
	@Override
	public double nextDouble() throws UnsupportedOperationException {
		UnsupportedOperationException ex = new UnsupportedOperationException(
				"Cannot call nextDouble on the adapter, must call it on a subclass.");
		LOG.error(ex.getMessage(), ex);
		throw ex;
	}

	/**
	 * Calls the distribution with the specified parameters and this distributions random generator.
	 * 
	 * @param l3
	 *            the distribution's l3 parameter
	 * @param l4
	 *            the distribution's l3 parameter
	 * @return the value from {@link Distributions#nextLambda(double, double, RandomEngine)}
	 */
	public double nextLamdba(double l3, double l4) {
		return Distributions.nextLambda(l3, l4, super.randomGenerator);
	}
	
	public double geometricPdf(int k, double p) {
		return Distributions.geometricPdf(k, p);
	}
	
	public double nextBurr1(double r, int nr) {
		return Distributions.nextBurr1(r, nr, super.randomGenerator);
	}
	
	public double nextBurr2(double r, double k, int nr) {
		return Distributions.nextBurr2(r, k, nr, super.randomGenerator);
	}
	
	public double nextCauchy() {
		return Distributions.nextCauchy(super.randomGenerator);
	}
	
	public double nextErlang(double variance, double mean) {
		return Distributions.nextErlang(variance, mean, super.randomGenerator);
	}
	
	public double nextGeometric(double p) {
		return Distributions.nextGeometric(p, super.randomGenerator);
	}
	
	public double nextLaplace() {
		return Distributions.nextLaplace(super.randomGenerator);
	}
	
	public double nextLogistic() {
		return Distributions.nextLogistic(super.randomGenerator);
	}
	
	public double nextPowLaw(double alpha, double cut) {
		return Distributions.nextPowLaw(alpha, cut, super.randomGenerator);
	}
	
	public double nextTriangular() {
		return Distributions.nextTriangular(super.randomGenerator);
	}
	
	public double nextWeibull(double alpha, double beta) {
		return Distributions.nextWeibull(alpha, beta, super.randomGenerator);
	}
	
	public double nextZipfInt(double z) {
		return Distributions.nextZipfInt(z, super.randomGenerator);
	}
}
