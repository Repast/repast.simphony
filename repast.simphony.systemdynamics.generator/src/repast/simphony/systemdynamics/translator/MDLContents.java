package repast.simphony.systemdynamics.translator;

import java.util.List;

public class MDLContents {

    private List<String> rawEquations;
    private List<String> graphicsInformation;
    
    public MDLContents(List<String> rawEquations, List<String> graphicsInformation) {
	this.rawEquations = rawEquations;
	this.graphicsInformation = graphicsInformation;
    }

    public List<String> getRawEquations() {
        return rawEquations;
    }

    public List<String> getGraphicsInformation() {
        return graphicsInformation;
    }
}
