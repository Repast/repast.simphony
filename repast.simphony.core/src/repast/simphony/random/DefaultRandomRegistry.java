/*CopyrightHere*/
package repast.simphony.random;

import cern.jet.random.*;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import simphony.util.messages.MessageCenter;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry that stores random number generators.
 * 
 * @author Jerry Vos
 */
public class DefaultRandomRegistry implements RandomRegistry {

	private static final MessageCenter log = MessageCenter.getMessageCenter(DefaultRandomRegistry.class);

	private static class GeneratorInfo {

		private RandomEngine generator;
		private int seed;


		public GeneratorInfo(RandomEngine generator, int seed) {
			this.generator = generator;
			this.seed = seed;
		}
	}

	protected int defaultSeed;
	private RandomEngine generator;

	private Map<String, GeneratorInfo> generators = new HashMap<String, GeneratorInfo>();
	private Map<String, AbstractDistribution> nonDefaultDistributions = new HashMap<String, AbstractDistribution>();

	private Beta beta;
  private Binomial binomial;
  private BreitWigner breitWigner;
  private BreitWignerMeanSquare breitWignerMeanSquare;
  private ChiSquare chiSquare;
  private Empirical empirical;
  private EmpiricalWalker empiricalWalker;
  private Exponential exponential;
  private ExponentialPower exponentialPower;
  private Gamma gamma;
  private Hyperbolic hyperbolic;
  private HyperGeometric hyperGeometric;
  private Logarithmic logarithmic;
  private NegativeBinomial negativeBinomial;
  private Normal normal;
  private Poisson poisson;
  private PoissonSlow poissonSlow;
  private StudentT studentT;
  private Uniform uniform;
  private VonMises vonMises;
  private Zeta zeta;
  private Map<Thread,Uniform> uniformValues = new HashMap<Thread,Uniform>();
	/**
	 * Default constructor
	 */
	public DefaultRandomRegistry() {
		generator = registerGenerator(DEFAULT_GENERATOR, (int)System.currentTimeMillis());
	}

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
	public RandomEngine registerGenerator(String name, int seed) {
		RandomEngine engine = new MersenneTwister(seed);
		generators.put(name, new GeneratorInfo(engine, seed));
		return engine;
	}

	/**
	 * Gets a previously registered random number generator.
	 *
	 * @param generatorName the name of the generator to get
	 * @return a previously registered random number generator.
	 */
	public RandomEngine getGenerator(String generatorName) {
		return generators.get(generatorName).generator;
	}

	/**
	 * Gets the seed of the named generator.
	 *
	 * @param generatorName the name of the generator
	 * @return the seed of the named generator.
	 */
	public int getSeed(String generatorName) {
		return generators.get(generatorName).seed;
	}

	/**
	 * Registers the named random number distribution for later retrieval.
	 *
	 * @param name the name of the distribution
	 * @param dist the distribution to register
	 */
	public void registerDistribution(String name, AbstractDistribution dist) {
		nonDefaultDistributions.put(name, dist);
	}

	/**
	 * Gets the named previously registered distribution. Calling code
	 * can then cast the distribution into the appropriate type (e.g. a Normal).
	 *
	 * @param name the name of the distribution to get
	 * @return the named previously registered distribution.
	 *
	 */
	public AbstractDistribution getDistribution(String name) {
		return nonDefaultDistributions.get(name);
	}

	/**
	 * Creates the default Zeta distribution using the default
	 * random number generator.
	 * 
	 * @param ro
	 * @param pk
	 * @return the created distribution
	 */
	public Zeta createZeta(double ro, double pk) {
		zeta = new Zeta(ro, pk, generator);
		return zeta;
	}

	/**
	 * Gets the default Zeta distribution.
	 *
	 * @return the default Zeta distribution.
	 */
	public Zeta getZeta() {
		return zeta;
	}


	/**
	 * Creates a default VonMises distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 *
	 * @return the created distribution
	 */
  public VonMises createVonMises(double freedom) {
    vonMises = new VonMises(freedom, generator);
		return vonMises;
  }

	/**
	 * Creates the default uniform distribution using the default
	 * random number generator.
	 *
	 * @param min
	 * @param max
	 * @return the created distribution
	 */
  public Uniform createUniform(double min, double max) {
    uniform = new Uniform(min, max, generator);
	return uniform;
  }

	/**
	 * Creates the default Unifrom distribution using the default
	 * random number generator.
	 *
	 * @return the created distribution
	 */
  public Uniform createUniform() {
   uniform = new Uniform(generator);
	return uniform;
  }

	/**
	 * Creates the default StudentT distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 * @return the created distribution
	 */
  public StudentT createStudentT(double freedom) {
    studentT = new StudentT(freedom, generator);
		return studentT;
  }

	/**
	 * Creates the default slower Poisson distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @return the created distribution
	 */
  public PoissonSlow createPoissonSlow(double mean) {
    poissonSlow = new PoissonSlow(mean, generator);
		return poissonSlow;
  }

	/**
	 * Creates the default Poisson distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @return the created distribution
	 */
  public Poisson createPoisson(double mean) {
    poisson = new Poisson(mean, generator);
		return poisson;
  }

	/**
	 * Creates the default normal distribution using the default
	 * random number generator.
	 *
	 * @param mean
	 * @param standardDeviation
	 * @return the created distribution
	 */
  public Normal createNormal(double mean, double standardDeviation) {
    normal = new Normal(mean, standardDeviation, generator);
		return normal;
  }

	/**
	 * Creates the default negative binomial distribution using the default
	 * random number generator.
	 *
	 * @param n
	 * @param p
	 * @return the created distribution
	 */
  public NegativeBinomial createNegativeBinomial(int n, double p) {
    negativeBinomial = new NegativeBinomial(n, p, generator);
		return negativeBinomial;
  }

	/**
	 * Creates the default logarithmic distribution using the default
	 * random number generator.
	 *
	 * @param p
	 * @return the created distribution
	 */
  public Logarithmic createLogarithmic(double p) {
    logarithmic = new Logarithmic(p, generator);
		return logarithmic;
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
  public HyperGeometric createHyperGeometric(int N, int s, int n) {
    hyperGeometric = new HyperGeometric(N, s, n, generator);
		return hyperGeometric;
  }

	/**
	 * Creates the default hyperbolic distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param beta
	 * @return the created distribution
	 */
  public Hyperbolic createHyperbolic(double alpha, double beta) {
    hyperbolic = new Hyperbolic(alpha, beta, generator);
		return hyperbolic;
  }

	/**
	 * Creates the default gamma distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param lambda
	 * @return the created distribution
	 */
  public Gamma createGamma(double alpha, double lambda) {
    gamma = new Gamma(alpha, lambda, generator);
		return gamma;
  }

	/**
	 * Creates the default exponential power distribution using the default
	 * random number generator.
	 *
	 * @param tau
	 * @return the created distribution
	 */
  public ExponentialPower createExponentialPower(double tau) {
    exponentialPower = new ExponentialPower(tau, generator);
		return exponentialPower;
  }

	/**
	 * Creates the default exponential distribution using the default
	 * random number generator.
	 *
	 * @param lambda
	 * @return the created distribution
	 */
  public Exponential createExponential(double lambda) {
    exponential = new Exponential(lambda, generator);
		return exponential;
  }

	/**
	 * Creates the default empirical walker distribution using the default
	 * random number generator.
	 *
	 * @param pdf
	 * @param interpolationType
	 * @return the created distribution
	 */
  public EmpiricalWalker createEmpiricalWalker(double[] pdf, int interpolationType) {
    empiricalWalker = new EmpiricalWalker(pdf, interpolationType, generator);
		return empiricalWalker;
  }

	/**
	 * Creates the default empirical distribution using the default
	 * random number generator.
	 *
	 * @param pdf
	 * @param interpolationType
	 * @return the created distribution
	 */
  public Empirical createEmpirical(double[] pdf, int interpolationType) {
    empirical = new Empirical(pdf, interpolationType, generator);
		return empirical;
  }

	/**
	 * Creates the default chi square distribution using the default
	 * random number generator.
	 *
	 * @param freedom
	 * @return the created distribution
	 */
  public ChiSquare createChiSquare(double freedom) {
    chiSquare = new ChiSquare(freedom, generator);
		return chiSquare;
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
  public BreitWignerMeanSquare createBreitWignerMeanSquareState(double mean,
      double gamma, double cut)
  {
    breitWignerMeanSquare = new BreitWignerMeanSquare(mean, gamma, cut, generator);
	  return breitWignerMeanSquare;
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
  public BreitWigner createBreitWigner(double mean, double gamma, double cut) {
    breitWigner = new BreitWigner(mean, gamma, cut, generator);
		return breitWigner;
  }

	/**
	 * Creates the default binomial distribution using the default
	 * random number generator.
	 *
	 * @param n
	 * @param p
	 */
  public Binomial createBinomial(int n, double p) {
    binomial = new Binomial(n, p, generator);
		return binomial;
  }

	/**
	 * Creates the default beta distribution using the default
	 * random number generator.
	 *
	 * @param alpha
	 * @param beta
	 * @return the created distribution
	 */
  public Beta createBeta(double alpha, double beta) {
    this.beta = new Beta(alpha, beta, generator);
		return this.beta;
  }

	/**
	 * Gets the default beta distribution.
	 *
	 * @return the default beta distribution.
	 */
	public Beta getBeta() {
		return beta;
	}

	/**
	 * Gets the default binomial distribution.
	 *
	 * @return the default binomial distribution.
	 */
	public Binomial getBinomial() {
		return binomial;
	}
/**
	 * Gets the default BreitWigner distribution.
	 *
	 * @return the default BreitWigner distribution.
	 */
	public BreitWigner getBreitWigner() {
		return breitWigner;
	}

	/**
	 * Gets the default BreitWignerMeanSquare distribution.
	 *
	 * @return the default BreitWignerMeanSquare distribution.
	 */
	public BreitWignerMeanSquare getBreitWignerMeanSquare() {
		return breitWignerMeanSquare;
	}

	/**
	 * Gets the default Zeta distribution.
	 *
	 * @return the default Zeta distribution.
	 */
	public ChiSquare getChiSquare() {
		return chiSquare;
	}

	/**
	 * Gets the default ChiSquare distribution.
	 *
	 * @return the default ChiSquare distribution.
	 */
	public Empirical getEmpirical() {
		return empirical;
	}

	/**
	 * Gets the default EmpiricalWalker distribution.
	 *
	 * @return the default EmpiricalWalker distribution.
	 */
	public EmpiricalWalker getEmpiricalWalker() {
		return empiricalWalker;
	}

	/**
	 * Gets the default Exponential distribution.
	 *
	 * @return the default Exponential distribution.
	 */
	public Exponential getExponential() {
		return exponential;
	}

	/**
	 * Gets the default exponentialPower distribution.
	 *
	 * @return the default exponentialPower distribution.
	 */
	public ExponentialPower getExponentialPower() {
		return exponentialPower;
	}

	/**
	 * Gets the default gamma distribution.
	 *
	 * @return the default gamma distribution.
	 */
	public Gamma getGamma() {
		return gamma;
	}

	/**
	 * Gets the default hyperbolic distribution.
	 *
	 * @return the default hyperbolic distribution.
	 */
	public Hyperbolic getHyperbolic() {
		return hyperbolic;
	}

	/**
	 * Gets the default hyperGeometric distribution.
	 *
	 * @return the default hyperGeometric distribution.
	 */
	public HyperGeometric getHyperGeometric() {
		return hyperGeometric;
	}

	/**
	 * Gets the default logarithmic distribution.
	 *
	 * @return the default logarithmic distribution.
	 */
	public Logarithmic getLogarithmic() {
		return logarithmic;
	}

	/**
	 * Gets the default negativeBinomial distribution.
	 *
	 * @return the default negativeBinomial distribution.
	 */
	public NegativeBinomial getNegativeBinomial() {
		return negativeBinomial;
	}

	/**
	 * Gets the default normal distribution.
	 *
	 * @return the default normal distribution.
	 */
	public Normal getNormal() {
		return normal;
	}

	/**
	 * Gets the default poisson distribution.
	 *
	 * @return the default poisson distribution.
	 */
	public Poisson getPoisson() {
		return poisson;
	}

	/**
	 * Gets the default slow poisson distribution.
	 *
	 * @return the default slow poisson distribution.
	 */
	public PoissonSlow getPoissonSlow() {
		return poissonSlow;
	}

	/**
	 * Gets the default studentT distribution.
	 *
	 * @return the default studentT distribution.
	 */
	public StudentT getStudentT() {
		return studentT;
	}

	/**
	 * Gets the default uniform distribution.
	 *
	 * @return the default uniform distribution.
	 */
	public Uniform getUniform() {
		if(uniform==null)
			uniform=createUniform();
		return uniform;
	}

	/**
	 * Gets the default vonMises distribution.
	 *
	 * @return the default vonMises distribution.
	 */
	public VonMises getVonMises() {
		return vonMises;
	}

	/**
	 * Sets the seed for the default random number generator.
	 * Any previously created default distributions will be
	 * invalidated.
	 *
	 * @param seed the new seed
	 */
	public void setSeed(int seed) {
		invalidateDefaultDistributions();
		generator = registerGenerator(DEFAULT_GENERATOR, seed);
	}

	private void invalidateDefaultDistributions() {
    beta = null;
    binomial = null;
    breitWigner = null;
    breitWignerMeanSquare = null;
    chiSquare = null;
    empirical = null;
    empiricalWalker = null;
    exponential = null;
    exponentialPower = null;
    gamma = null;
    hyperbolic = null;
    hyperGeometric = null;
    logarithmic = null;
    negativeBinomial = null;
    normal = null;
    poisson = null;
    poissonSlow = null;
    studentT = null;
    uniform = null;
    vonMises = null;
    zeta = null;
  }

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
	public void reset() {
		invalidateDefaultDistributions();
		nonDefaultDistributions.clear();
		generators.clear();
		setSeed((int) System.currentTimeMillis());
		createUniform();
	}
}
