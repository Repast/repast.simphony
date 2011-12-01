package repast.simphony.context;


public abstract class AbstractSpatialContext<T, V> extends AbstractContext<T>
		implements SpatialContext<T, V> {

	public abstract V getLocation(Object object);

	protected double[] rotate(double[] plane, double angle) {
		double x = plane[0];
		double y = plane[1];
		plane[0] = x * Math.cos(angle) - y * Math.sin(angle);
		plane[1] = y * Math.cos(angle) + x * Math.sin(angle);
		return plane;
	}

	protected double[] scale(double[] point, double scale) {
		for (int i = 0; i < point.length; i++) {
			point[i] = point[i] * scale;
		}
		return point;
	}

	protected double[] getDisplacement(int unitDimension, double scale,
			double... anglesInRadians) {
		double[] displacement = new double[dimensions().length];
		displacement[unitDimension] = 1;
		double[] tmp = new double[2];
		int c = 0;
		for (int i = 0; i < dimensions().length; i++) {
			if (i == unitDimension) {
				continue;
			} else if (i > unitDimension) {
				tmp[0] = displacement[unitDimension];
				tmp[1] = displacement[i];
				rotate(tmp, anglesInRadians[c]);
				displacement[unitDimension] = tmp[0];
				displacement[i] = tmp[1];
			} else if (i < unitDimension) {
				tmp[0] = displacement[i];
				tmp[1] = displacement[unitDimension];
				rotate(tmp, anglesInRadians[c]);
				displacement[unitDimension] = tmp[1];
				displacement[i] = tmp[0];
			}
			c++;
		}
		scale(displacement, scale);
		return displacement;
	}

	public abstract double[] dimensions();

}
