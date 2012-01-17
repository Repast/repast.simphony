package repast.simphony.relogo;

import cern.colt.function.Double9Function;

public class DiffuseBody implements Double9Function {

	double diffusionCoeff;

	public DiffuseBody(double diffusionCoeff) {
		this.diffusionCoeff = diffusionCoeff;
	}

	public double apply(
			double a00, double a01, double a02,
			double a10, double a11, double a12,
			double a20, double a21, double a22) {
		return a11 * (1 - diffusionCoeff)
				+ (a00 + a01 + a02 + a10 + a12 + a20 + a21 + a22)
				* diffusionCoeff / 8.0;

	}

}
