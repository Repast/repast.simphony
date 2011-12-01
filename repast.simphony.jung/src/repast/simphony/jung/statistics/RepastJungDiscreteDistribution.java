package repast.simphony.jung.statistics;

import java.util.Collection;
import java.util.Iterator;

/**
 * Adaptation of DegreeDistribution class from JUNG 1.7.6 release since
 * this functionality is not available in the alpha 2 release
 * 
 * @author Eric Tatara
 *
 */
public class RepastJungDiscreteDistribution{

	public static double KullbackLeibler(double[] dist, double[] reference){
		double distance = 0;

		checkLengths(dist, reference);

		for (int i = 0; i < dist.length; i++){
			if (dist[i] > 0 && reference[i] > 0)
				distance += dist[i] * Math.log(dist[i] / reference[i]);
		}
		return distance;
	}

	
	public static double symmetricKL(double[] dist, double[] reference){
		return KullbackLeibler(dist, reference)
		+ KullbackLeibler(reference, dist);
	}

	public static double squaredError(double[] dist, double[] reference){
		double error = 0;

		checkLengths(dist, reference);

		for (int i = 0; i < dist.length; i++){
			double difference = dist[i] - reference[i];
			error += difference * difference;
		}
		return error;
	}

	public static double cosine(double[] dist, double[] reference){
		double v_prod = 0; // dot product x*x
		double w_prod = 0; // dot product y*y
		double vw_prod = 0; // dot product x*y

		checkLengths(dist, reference);

		for (int i = 0; i < dist.length; i++){
			vw_prod += dist[i] * reference[i];
			v_prod += dist[i] * dist[i];
			w_prod += reference[i] * reference[i];
		}
		// cosine distance between v and w
		return vw_prod / (Math.sqrt(v_prod) * Math.sqrt(w_prod));
	}

	public static double entropy(double[] dist){
		double total = 0;

		for (int i = 0; i < dist.length; i++){
			if (dist[i] > 0)
				total += dist[i] * Math.log(dist[i]);
		}
		return -total;
	}

	protected static void checkLengths(double[] dist, double[] reference){
		if (dist.length != reference.length)
			throw new IllegalArgumentException("Arrays must be of the same length");
	}

	public static void normalize(double[] counts, double alpha){
		double total_count = 0;

		for (int i = 0; i < counts.length; i++)
			total_count += counts[i];

		for (int i = 0; i < counts.length; i++)
			counts[i] = (counts[i] + alpha)
			/ (total_count + counts.length * alpha);
	}

	public static double[] mean(Collection distributions){
		if (distributions.isEmpty())
			throw new IllegalArgumentException("Distribution collection must be non-empty");
		Iterator iter = distributions.iterator();
		double[] first = (double[])iter.next();
		double[][] d_array = new double[distributions.size()][first.length];
		d_array[0] = first;
		for (int i = 1; i < d_array.length; i++)
			d_array[i] = (double[])iter.next();

		return mean(d_array);
	}
	
	public static double[] mean(double[][] distributions){
		double[] d_mean = new double[distributions[0].length];
		for (int j = 0; j < d_mean.length; j++)
			d_mean[j] = 0;

		for (int i = 0; i < distributions.length; i++)
			for (int j = 0; j < d_mean.length; j++)
				d_mean[j] += distributions[i][j] / distributions.length;

		return d_mean;
	}
}