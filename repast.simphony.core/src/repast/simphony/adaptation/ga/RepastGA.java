package repast.simphony.adaptation.ga;

/* @author Michael J. North */

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.InvalidConfigurationException;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;

public class RepastGA {

	Configuration configuration = new DefaultConfiguration();

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Genotype getPopulation() {
		return population;
	}

	public void setPopulation(Genotype population) {
		this.population = population;
	}

	Genotype population = null;

	public RepastGA(Object fitnessFunction, String fitnessFunctionName,
			int populationSize, Gene[] sampleGenes) {

		try {

			if ((fitnessFunction instanceof FitnessFunction)
					&& (fitnessFunctionName.equals("evaluate"))) {
				this.configuration
						.setFitnessFunction((FitnessFunction) fitnessFunction);
			} else {
				this.configuration
						.setFitnessFunction(new RepastFitnessFunction(
								fitnessFunction, fitnessFunctionName));
			}

			Chromosome sampleChromosome = new Chromosome(this.configuration,
					sampleGenes);
			this.configuration.setSampleChromosome(sampleChromosome);
			this.configuration.setPopulationSize(populationSize);
			this.population = Genotype
					.randomInitialGenotype(this.configuration);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public double[] getBestSolution(int cycles) {

		this.population.evolve();
		return RepastGA
				.chromosomeToGene(this.population.getFittestChromosome());

	}

	public Gene[] getBestSolutionAsGene(int cycles) {

		this.population.evolve();
		return this.population.getFittestChromosome().getGenes();

	}

	public Chromosome getBestSolutionAsChromosome(int cycles) {

		this.population.evolve();
		return this.population.getFittestChromosome();

	}

	public void reset() {

		try {
			this.population = Genotype
					.randomInitialGenotype(this.configuration);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}

	}

	public static double[] chromosomeToGene(Chromosome chromosome) {

		int geneCount = chromosome.getGenes().length;
		if (geneCount > 0) {
			double[] geneValues = new double[geneCount];
			int i = 0;
			for (Gene gene : chromosome.getGenes()) {
				if (gene instanceof IntegerGene) {
					geneValues[i] = ((IntegerGene) gene).intValue();
				} else if (gene instanceof DoubleGene) {
					geneValues[i] = ((DoubleGene) gene).doubleValue();
				} else {
					geneValues[i] = 0;
				}
				i++;
			}

			return geneValues;

		} else {

			return null;

		}

	}

}
