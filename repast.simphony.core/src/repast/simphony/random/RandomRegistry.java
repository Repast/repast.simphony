/*CopyrightHere*/
package repast.simphony.random;

import cern.jet.random.*;
import cern.jet.random.engine.RandomEngine;

/**
 * A registry of random streams.
 * 
 * @author Jerry Vos
 */
public interface RandomRegistry {

	public static final String DEFAULT_GENERATOR = RandomRegistry.class.getName() + ".DEFAULT_GENERATOR";

	/**
	 * Creates and registers a new random number generator
	 * with the specified name and seed. The generator
	 * can then be retrieved later using #getGenerator(String).<p>
	 *
	 * This will create a MersenneTwister for the generator.
	 *
	 * @param name the name of the generator to create and register
	 * @param seed the new generators seed
	 * @return the new generator itself
	 */
	RandomEngine registerGenerator(String name, int seed);

	/**
	 * Gets a previously registered random number generator.
	 *
	 * @param generatorName the name of the generator to get
	 * @return a previously registered random number generator.
	 */
	RandomEngine getGenerator(String generatorName);

	/**
	 * Gets the seed of the named generator.
	 *
	 * @param generatorName the name of the generator
	 * @return the seed of the named generator.
	 */
	int getSeed(String generatorName);

	/**
	 * Registers the named random number distribution for later retrieval.
	 *
	 * @param name the name of the distribution
	 * @param dist the distribution to register
	 */
	void registerDistribution(String name, AbstractDistribution dist);

	/**
	 * Gets the named previously registered distribution. Calling code
	 * can then cast the distribution into the appropriate type (e.g. a Normal).
	 *
	 * @param name the name of the distribution to get
	 * @return the named previously registered distribution.
	 *
	 */
	AbstractDistribution getDistribution(String name);

	/**
	 * Creates the default Zeta distribution using the default
	 * random number generator.
	 *
	 * @param ro
	 * @param pk
	 * @return the created distribution
	 */
	Zeta createZeta(double ro, double pk);

	/**
	 * Gets the default Zeta distribution.
	 *
	 * @return the default Zeta distribution.
	 */
	Zeta getZeta();

	/**
	 * Creates a default VonMises distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 *
	 * @return the created distribution
	 */
	VonMises createVonMises(double freedom);

	/**
	 * Creates the default uniform distribution using the default
	 * random number generator.
	 *
	 * @param min
	 * @param max
	 * @return the created distribution
	 */
	Uniform createUniform(double min, double max);

	/**
	 * Creates the default Unifrom distribution using the default
	 * random number generator.
	 *
	 * @return the created distribution
	 */
	Uniform createUniform();

	/**
	 * Creates the default StudentT distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 * @return the created distribution
	 */
	StudentT createStudentT(double freedom);

	/**
	 * Creates the default slower Poisson distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @return the created distribution
	 */
	PoissonSlow createPoissonSlow(double mean);

	/**
	 * Creates the default Poisson distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @return the created distribution
	 */
	Poisson createPoisson(double mean);

	/**
	 * Creates the default normal distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @param standardDeviation
	 * @return the created distribution
	 */
	Normal createNormal(double mean, double standardDeviation);

	/**
	 * Creates the default negative binomial distribution using the default
	 * random number generator.
	 *
	 * @param n
	 * @param p
	 * @return the created distribution
	 */
	NegativeBinomial createNegativeBinomial(int n, double p);

	/**
	 * Creates the default logarithmic distribution using the default
	 * random number generator.
	 *
	 * @param p
	 * @return the created distribution
	 */
	Logarithmic createLogarithmic(double p);

	/**
	 * Creates the default hyper geometric distribution using the default
	 * random number generator.
	 *
	 * @param N
	 * @param s
	 * @param n
	 * @return the created distribution
	 */
	HyperGeometric createHyperGeometric(int N, int s, int n);

	/**
	 * Creates the default hyperbolic distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param beta
	 * @return the created distribution
	 */
	Hyperbolic createHyperbolic(double alpha, double beta);

	/**
	 * Creates the default gamma distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param lambda
	 * @return the created distribution
	 */
	Gamma createGamma(double alpha, double lambda);

	/**
	 * Creates the default exponential power distribution using the default
	 * random number generator.
	 *
	 * @param tau
	 * @return the created distribution
	 */
	ExponentialPower createExponentialPower(double tau);

	/**
	 * Creates the default exponential distribution using the default
	 * random number generator.
	 *
	 * @param lambda
	 * @return the created distribution
	 */
	Exponential createExponential(double lambda);

	/**
	 * Creates the default empirical walker distribution using the default
	 * random number generator.
	 *
	 * @param pdf
	 * @param interpolationType
	 * @return the created distribution
	 */
	EmpiricalWalker createEmpiricalWalker(double[] pdf, int interpolationType);

	/**
	 * Creates the default empirical distribution using the default
	 * random number generator.
	 *
	 * @param pdf
	 * @param interpolationType
	 * @return the created distribution
	 */
	Empirical createEmpirical(double[] pdf, int interpolationType);

	/**
	 * Creates the default chi square distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 * @return the created distribution
	 */
	ChiSquare createChiSquare(double freedom);

	/**
	 * Creates the default Breit Wigner mean square state distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @param gamma
	 * @param cut
	 * @return the created distribution
	 */
	BreitWignerMeanSquare createBreitWignerMeanSquareState(double mean,
      double gamma, double cut);

	/**
	 * Creates the default Breit Wigner distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @param gamma
	 * @param cut
	 * @return the created distribution
	 */
	BreitWigner createBreitWigner(double mean, double gamma, double cut);

	/**
	 * Creates the default binomial distribution using the default
	 * random number generator.
	 *
	 * @param n
	 * @param p
	 */
	Binomial createBinomial(int n, double p);

	/**
	 * Creates the default beta distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param beta
	 * @return the created distribution
	 */
	Beta createBeta(double alpha, double beta);

	/**
	 * Gets the default beta distribution.
	 *
	 * @return the default beta distribution.
	 */
	Beta getBeta();

	/**
	 * Gets the default binomial distribution.
	 *
	 * @return the default binomial distribution.
	 */
	Binomial getBinomial();

	/**
	 * Gets the default BreitWigner distribution.
		 *
		 * @return the default BreitWigner distribution.
		 */
	BreitWigner getBreitWigner();

	/**
	 * Gets the default BreitWignerMeanSquare distribution.
	 *
	 * @return the default BreitWignerMeanSquare distribution.
	 */
	BreitWignerMeanSquare getBreitWignerMeanSquare();

	/**
	 * Gets the default Zeta distribution.
	 *
	 * @return the default Zeta distribution.
	 */
	ChiSquare getChiSquare();

	/**
	 * Gets the default ChiSquare distribution.
	 *
	 * @return the default ChiSquare distribution.
	 */
	Empirical getEmpirical();

	/**
	 * Gets the default EmpiricalWalker distribution.
	 *
	 * @return the default EmpiricalWalker distribution.
	 */
	EmpiricalWalker getEmpiricalWalker();

	/**
	 * Gets the default Exponential distribution.
	 *
	 * @return the default Exponential distribution.
	 */
	Exponential getExponential();

	/**
	 * Gets the default exponentialPower distribution.
	 *
	 * @return the default exponentialPower distribution.
	 */
	ExponentialPower getExponentialPower();

	/**
	 * Gets the default gamma distribution.
	 *
	 * @return the default gamma distribution.
	 */
	Gamma getGamma();

	/**
	 * Gets the default hyperbolic distribution.
	 *
	 * @return the default hyperbolic distribution.
	 */
	Hyperbolic getHyperbolic();

	/**
	 * Gets the default hyperGeometric distribution.
	 *
	 * @return the default hyperGeometric distribution.
	 */
	HyperGeometric getHyperGeometric();

	/**
	 * Gets the default logarithmic distribution.
	 *
	 * @return the default logarithmic distribution.
	 */
	Logarithmic getLogarithmic();

	/**
	 * Gets the default negativeBinomial distribution.
	 *
	 * @return the default negativeBinomial distribution.
	 */
	NegativeBinomial getNegativeBinomial();

	/**
	 * Gets the default normal distribution.
	 *
	 * @return the default normal distribution.
	 */
	Normal getNormal();

	/**
	 * Gets the default poisson distribution.
	 *
	 * @return the default poisson distribution.
	 */
	Poisson getPoisson();

	/**
	 * Gets the default slow poisson distribution.
	 *
	 * @return the default slow poisson distribution.
	 */
	PoissonSlow getPoissonSlow();

	/**
	 * Gets the default studentT distribution.
	 *
	 * @return the default studentT distribution.
	 */
	StudentT getStudentT();

	/**
	 * Gets the default uniform distribution.
	 *
	 * @return the default uniform distribution.
	 */
	Uniform getUniform();

	/**
	 * Gets the default vonMises distribution.
	 *
	 * @return the default vonMises distribution.
	 */
	VonMises getVonMises();

	/**
	 * Sets the seed for the default random number generator.
	 * Any previously created default distributions will be
	 * invalidated.
	 *
	 * @param seed the new seed
	 */
	void setSeed(int seed);

	/**
	 * Resets this random registry by doing the following:
	 *
	 * <ol>
	 * <li> invalidates any live default distributions
	 * <li> removes (de-registers) any non-default distributions
	 * <li> removes (de-registers) any non-default random number generators
	 * <li> creates a new default random number generator with the current time
	 * as its seed
	 * <li> creates a new default uniform distributions
	 * </ol>
	 */
	void reset();
}