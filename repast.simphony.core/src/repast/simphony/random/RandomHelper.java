/*CopyrightHere*/
package repast.simphony.random;

import cern.jet.random.*;
import cern.jet.random.engine.RandomEngine;
import simphony.util.messages.MessageCenter;

/**
 * A helper class for creating random number streams and adding them to a
 * {@link RandomRegistry}. Working with random numbers consists
 * of two parts: the actual random number generator and the distributions (e.g. Uniform)
 * take the numbers from the generator and return the appropriate values.<p>
 *
 * The RandomHelper provides access to default distributions that are created from a
 * default generator. Non-default distributions that use their own random generator
 * or share the default one can also be created. All the create* and get* methods where
 * a name does <b>NOT</b> have to be provided operate on the default generator and distributions.
 * A default uniform distributions is created automatically and the nextInt* and nextDouble* methods will
 * use this distribution.<p>
 *
 * RandomHelper creates a default random number generator using the
 * current timestamp as the seed. If you do not explicitly set your own seed,
 * the distributions created via the create* calls will use this default
 * generator. However, when the seed is set or reset, all the distributions, both default and
 * non-, are invalidated, and a new random number generator is created as is a new default uniform
 * distribution.<p>
 *
 * All the distributions in RandomHelper are from the colt library. The return types
 * of the get* and create* methods return these colt objects. See the colt library documentation
 * at http://dsd.lbl.gov/~hoschek/colt/.
 *
 *
 *
 */
public class RandomHelper {
	private static final MessageCenter LOG = MessageCenter.getMessageCenter(RandomHelper.class);

	private static DefaultRandomRegistry defaultRegistry = new DefaultRandomRegistry();

	static {
		init();
	}

	/**
	 * Creates the default beta distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param beta
	 * @return the created distribution
	 */
	public static Beta createBeta(double alpha, double beta) {
		return defaultRegistry.createBeta(alpha, beta);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default binomial distribution using the default
	 * random number generator.
	 *
	 * @param n
	 * @param p
	 */
	public static Binomial createBinomial(int n, double p) {
		return defaultRegistry.createBinomial(n, p);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default Breit Wigner distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @param gamma
	 * @param cut
	 * @return the created distribution
	 */
	public static BreitWigner createBreitWigner(double mean, double gamma, double cut) {
		return defaultRegistry.createBreitWigner(mean, gamma, cut);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default Breit Wigner mean square state distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @param gamma
	 * @param cut
	 * @return the created distribution
	 */
	public static BreitWignerMeanSquare createBreitWignerMeanSquare(double mean, double gamma, double cut) {
		return defaultRegistry.createBreitWignerMeanSquareState(mean, gamma, cut);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default chi square distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 * @return the created distribution
	 */
	public static ChiSquare createChiSquare(double freedom) {
		return defaultRegistry.createChiSquare(freedom);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default empirical distribution using the default
	 * random number generator.
	 *
	 * @param pdf
	 * @param interpolationType
	 * @return the created distribution
	 */
	public static Empirical createEmpirical(double[] pdf, int interpolationType) {
		return defaultRegistry.createEmpirical(pdf, interpolationType);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default empirical walker distribution using the default
	 * random number generator.
	 *
	 * @param pdf
	 * @param interpolationType
	 * @return the created distribution
	 */
	public static EmpiricalWalker createEmpiricalWalker(double[] pdf, int interpolationType) {
		return defaultRegistry.createEmpiricalWalker(pdf, interpolationType);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default exponential distribution using the default
	 * random number generator.
	 *
	 * @param lambda
	 * @return the created distribution
	 */
	public static Exponential createExponential(double lambda) {
		return defaultRegistry.createExponential(lambda);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default exponential power distribution using the default
	 * random number generator.
	 *
	 * @param tau
	 * @return the created distribution
	 */
	public static ExponentialPower createExponentialPower(double tau) {
		return defaultRegistry.createExponentialPower(tau);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default gamma distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param lambda
	 * @return the created distribution
	 */
	public static Gamma createGamma(double alpha, double lambda) {
		return defaultRegistry.createGamma(alpha, lambda);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default hyperbolic distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param beta
	 * @return the created distribution
	 */
	public static Hyperbolic createHyperbolic(double alpha, double beta) {
		return defaultRegistry.createHyperbolic(alpha, beta);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default hyper geometric distribution using the default
	 * random number generator.
	 *
	 * @param N
	 * @param s
	 * @param n
	 * @return the created distribution
	 */
	public static HyperGeometric createHyperGeometric(int N, int s, int n) {
		return defaultRegistry.createHyperGeometric(N, s, n);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default logarithmic distribution using the default
	 * random number generator.
	 *
	 * @param p
	 * @return the created distribution
	 */
	public static Logarithmic createLogarithmic(double p) {
		return defaultRegistry.createLogarithmic(p);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default negative binomial distribution using the default
	 * random number generator.
	 *
	 * @param n
	 * @param p
	 * @return the created distribution
	 */
	public static NegativeBinomial createNegativeBinomial(int n, double p) {
		return defaultRegistry.createNegativeBinomial(n, p);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default normal distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @param standardDeviation
	 * @return the created distribution
	 */
	public static Normal createNormal(double mean, double standardDeviation) {
		return defaultRegistry.createNormal(mean, standardDeviation);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default Poisson distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @return the created distribution
	 */
	public static Poisson createPoisson(double mean) {
		return defaultRegistry.createPoisson(mean);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default slower Poisson distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @return the created distribution
	 */
	public static PoissonSlow createPoissonSlow(double mean) {
		return defaultRegistry.createPoissonSlow(mean);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default StudentT distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 * @return the created distribution
	 */
	public static StudentT createStudentT(double freedom) {
		return defaultRegistry.createStudentT(freedom);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default Unifrom distribution using the default
	 * random number generator.
	 *
	 * @return the created distribution
	 */
	public static Uniform createUniform() {
		return defaultRegistry.createUniform();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates the default uniform distribution using the default
	 * random number generator.
	 *
	 * @param min
	 * @param max
	 * @return the created distribution
	 */
	public static Uniform createUniform(double min, double max) {
		return defaultRegistry.createUniform(min, max);    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Creates a default VonMises distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 * @return the created distribution
	 */
	public static VonMises createVonMises(double freedom) {
		return defaultRegistry.createVonMises(freedom);    //To change body of overridden methods use File | Settings | File Templates.
	}
	
	public static Zeta createZeta(double ro, double pk) {
		return defaultRegistry.createZeta(ro, pk);
	}

	/**
	 * Gets the default beta distribution.
	 *
	 * @return the default beta distribution.
	 */
	public static Beta getBeta() {
		return defaultRegistry.getBeta();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default binomial distribution.
	 *
	 * @return the default binomial distribution.
	 */
	public static Binomial getBinomial() {
		return defaultRegistry.getBinomial();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default BreitWigner distribution.
	 *
	 * @return the default BreitWigner distribution.
	 */
	public static BreitWigner getBreitWigner() {
		return defaultRegistry.getBreitWigner();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default BreitWignerMeanSquare distribution.
	 *
	 * @return the default BreitWignerMeanSquare distribution.
	 */
	public static BreitWignerMeanSquare getBreitWignerMeanSquare() {
		return defaultRegistry.getBreitWignerMeanSquare();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default Zeta distribution.
	 *
	 * @return the default Zeta distribution.
	 */
	public static ChiSquare getChiSquare() {
		return defaultRegistry.getChiSquare();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default ChiSquare distribution.
	 *
	 * @return the default ChiSquare distribution.
	 */
	public static Empirical getEmpirical() {
		return defaultRegistry.getEmpirical();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default EmpiricalWalker distribution.
	 *
	 * @return the default EmpiricalWalker distribution.
	 */
	public static EmpiricalWalker getEmpiricalWalker() {
		return defaultRegistry.getEmpiricalWalker();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default Exponential distribution.
	 *
	 * @return the default Exponential distribution.
	 */
	public static Exponential getExponential() {
		return defaultRegistry.getExponential();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default exponentialPower distribution.
	 *
	 * @return the default exponentialPower distribution.
	 */
	public static ExponentialPower getExponentialPower() {
		return defaultRegistry.getExponentialPower();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default gamma distribution.
	 *
	 * @return the default gamma distribution.
	 */
	public static Gamma getGamma() {
		return defaultRegistry.getGamma();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default hyperbolic distribution.
	 *
	 * @return the default hyperbolic distribution.
	 */
	public static Hyperbolic getHyperbolic() {
		return defaultRegistry.getHyperbolic();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default hyperGeometric distribution.
	 *
	 * @return the default hyperGeometric distribution.
	 */
	public static HyperGeometric getHyperGeometric() {
		return defaultRegistry.getHyperGeometric();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default logarithmic distribution.
	 *
	 * @return the default logarithmic distribution.
	 */
	public static Logarithmic getLogarithmic() {
		return defaultRegistry.getLogarithmic();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default negativeBinomial distribution.
	 *
	 * @return the default negativeBinomial distribution.
	 */
	public static NegativeBinomial getNegativeBinomial() {
		return defaultRegistry.getNegativeBinomial();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default normal distribution.
	 *
	 * @return the default normal distribution.
	 */
	public static Normal getNormal() {
		return defaultRegistry.getNormal();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default poisson distribution.
	 *
	 * @return the default poisson distribution.
	 */
	public static Poisson getPoisson() {
		return defaultRegistry.getPoisson();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default slow poisson distribution.
	 *
	 * @return the default slow poisson distribution.
	 */
	public static PoissonSlow getPoissonSlow() {
		return defaultRegistry.getPoissonSlow();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default studentT distribution.
	 *
	 * @return the default studentT distribution.
	 */
	public static StudentT getStudentT() {
		return defaultRegistry.getStudentT();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default uniform distribution.
	 *
	 * @return the default uniform distribution.
	 */
	public static Uniform getUniform() {
		return defaultRegistry.getUniform();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default vonMises distribution.
	 *
	 * @return the default vonMises distribution.
	 */
	public static VonMises getVonMises() {
		return defaultRegistry.getVonMises();    //To change body of overridden methods use File | Settings | File Templates.
	}

	/**
	 * Gets the default Zeta distribution.
	 *
	 * @return the default Zeta distribution.
	 */
	public static Zeta getZeta() {
		return defaultRegistry.getZeta();    //To change body of overridden methods use File | Settings | File Templates.
	}


	/**
	 * This sets the seed for the default stream. This is the same as
	 * <code>DEFAULT_REGISTRY.setSeed(RandomHelper.DEFAULT_STREAM, seed);</code><p/>
	 * <p/>
	 * The default stream will be created if it has not yet been.
	 *
	 * @param seed the seed
	 */
	public static void setSeed(int seed) {
		defaultRegistry.setSeed(seed);
		createUniform();
	}


	/**
	 * This retrieves the next double in the specified range from the default uniform stream.
	 *
	 * @param from the start of the range (exclusive)
	 * @param to   the end of the range (exclusive)
	 * @return the next double from the default uniform stream
	 */
	public static double nextDoubleFromTo(double from, double to) {
		return getUniform().nextDoubleFromTo(from, to);
	}

	/**
	 * This retrieves the next double from the default uniform stream. T
	 *
	 * @return the next double from the default uniform stream
	 */
	public static double nextDouble() {
		return getUniform().nextDouble();
	}

	/**
	 * This retrieves the next integer in the specified range from the default uniform stream.
	 *
	 * @param from the start of the range (inclusive)
	 * @param to   the end of the range (inclusive)
	 * @return the next int from the default uniform stream
	 */
	public static int nextIntFromTo(int from, int to) {
		return getUniform().nextIntFromTo(from, to);
	}

	/**
	 * This retrieves the next integer from the default uniform stream.
	 *
	 * @return the next int from the default uniform stream
	 */
	public static int nextInt() {
		return getUniform().nextInt();
	}

	/**
	 * Initializes the random helper. This will invalidate
	 * any previously created distributions, both default and custom)
	 * and set the current seed for default generator to the
	 * current system time. This will also recreate the default uniform
	 * distribution.
	 */
	public static void init() {
		defaultRegistry = new DefaultRandomRegistry();
		setSeed((int) System.currentTimeMillis());
		createUniform();
	}

	/**
	 * Gets the default random registry.
	 *
	 * @return the default random registry
	 */
	public static RandomRegistry getDefaultRegistry() {
		return defaultRegistry;
	}

	/**
	 * Gets the random number generator used by the default distributions.
	 *
	 * @return the random number generator used by the default distributions.
	 */
	public static RandomEngine getGenerator() {
		return defaultRegistry.getGenerator(RandomRegistry.DEFAULT_GENERATOR);
	}


	/**
	 * Gets the seed used by the default random number generator.
	 *
	 * @return the seed used by the default random number generator.
	 */
	public static int getSeed() {
		return defaultRegistry.getSeed(RandomRegistry.DEFAULT_GENERATOR);
	}

	/**
	 * Creates and registers a new random number generator
	 * with the specified name and seed. The generator
	 * can then be retrieved later using #getGenerator(String).<p>
	 *
	 * @param name the name of the generator to create and register
	 * @param seed the new generators seed
	 * @return the new generator itself
	 */
	public static RandomEngine registerGenerator(String name, int seed) {
		return defaultRegistry.registerGenerator(name, seed);
	}

	/**
	 * Gets a previously registered random number generator.
	 *
	 * @param generatorName the name of the generator to get
	 * @return a previously registered random number generator.
	 */
	public static RandomEngine getGenerator(String generatorName) {
		return defaultRegistry.getGenerator(generatorName);
	}

	/**
	 * Gets the seed of the named generator.
	 *
	 * @param generatorName the name of the generator
	 * @return the seed of the named generator.
	 */
	public static int getSeed(String generatorName) {
		return defaultRegistry.getSeed(generatorName);
	}

	/**
	 * Registers the named random number distribution for later retrieval.
	 *
	 * @param name the name of the distribution
	 * @param dist the distribution to register
	 */
	public static void registerDistribution(String name, AbstractDistribution dist) {
		defaultRegistry.registerDistribution(name, dist);
	}

	/**
	 * Gets the named previously registered distribution. Calling code
	 * can then cast the distribution into the appropriate type (e.g. a Normal).
	 *
	 * @param name the name of the distribution to get
	 * @return the named previously registered distribution.
	 */
	public static AbstractDistribution getDistribution(String name) {
		return defaultRegistry.getDistribution(name);
	}
}
