package repast.simphony.systemdynamics.analysis;

import repast.simphony.systemdynamics.translator.Equation;

public class PolarityRunner {

	private Equation equation;
	private PolarityResults polarityResults;
	private ArgumentBuilder argumentBuilder; 

	public PolarityRunner(Equation equation) {
		this.equation = equation;
	}

	public PolarityResults run() {

		int numArgs = equation.getRHSVariables().size();
		argumentBuilder = new ArgumentBuilder(numArgs);



		return polarityResults;
	}

}
