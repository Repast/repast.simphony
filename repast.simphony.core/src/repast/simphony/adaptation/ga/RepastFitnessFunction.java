package repast.simphony.adaptation.ga;

/* @author Michael J. North */

import java.lang.reflect.Method;

import org.jgap.Chromosome;
import org.jgap.FitnessFunction;

public class RepastFitnessFunction extends FitnessFunction {

	Object fitnessFunction = null;
	Method fitnessFunctionMethod = null;

	public RepastFitnessFunction(Object fitnessFunction,
			String fitnessFunctionName) {

		this.fitnessFunction = fitnessFunction;

		double[] geneValues = new double[0];
		try {
			this.fitnessFunctionMethod = this.fitnessFunction.getClass()
					.getDeclaredMethod(fitnessFunctionName,
							geneValues.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int evaluate(Chromosome chromosome) {

		double[] geneValues = RepastGA.chromosomeToGene(chromosome);
		if (geneValues != null) {

			try {
				Object arguments[] = new Object[1];
				arguments[0] = geneValues;
				Object result = this.fitnessFunctionMethod.invoke(
						this.fitnessFunction, arguments);
				if (result instanceof Integer) {
					return ((Integer) result).intValue();
				} else {
					Exception e = new Exception(
							"Genetic Algorithm fitness functions must return an integer. A "
									+ result.getClass() + " of value \""
									+ result + "\" was returned instead.");
					e.fillInStackTrace();
					throw e;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return 0;

		} else {

			return 0;

		}

	}
}
