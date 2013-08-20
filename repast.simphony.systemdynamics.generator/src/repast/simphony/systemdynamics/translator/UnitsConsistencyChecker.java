package repast.simphony.systemdynamics.translator;

public class UnitsConsistencyChecker {

    public static void main(String[] args) {
	
	String mdlFile = args[0];
	String resultsFile = args[1];

	Translator translator = new Translator(mdlFile, resultsFile);
	translator.checkUnitsConsistency();
    }
}
