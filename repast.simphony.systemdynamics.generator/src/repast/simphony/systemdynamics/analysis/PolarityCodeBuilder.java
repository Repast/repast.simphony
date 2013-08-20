package repast.simphony.systemdynamics.analysis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import repast.simphony.systemdynamics.translator.Equation;
import repast.simphony.systemdynamics.translator.InformationManagers;
import repast.simphony.systemdynamics.translator.Node;

public class PolarityCodeBuilder {
    
    private Equation equation;
    private Map<String, String> idToNameMap;
    private Node polarityRoot;
    private String generatedCode;
    
    // getCopyOfTree
    
    public PolarityCodeBuilder(Equation equation) {
	this.equation = equation;
	idToNameMap = new HashMap<String, String>();
	polarityRoot = equation.getCopyOfTree();
	insertIDs();
	generatedCode = new String(build());
    }
    
    private void insertIDs() {
	
	// first change lhs
	Node lhs = polarityRoot.getChild();
	String lhsVar = "lhs";
	idToNameMap.put(lhsVar, lhs.getToken());
	lhs.setToken(lhsVar);
	
	
	
	int id = 0;
	Iterator<String> iter = equation.getRHSVariables().iterator();
	while(iter.hasNext()) {
	    String variable = iter.next();
	    replaceVariableWithID(polarityRoot, InformationManagers.getInstance().getNativeDataTypeManager().getLegalNameWithSubscripts(equation, variable), id);
	    replaceVariableWithID(polarityRoot, variable, id);
	    id++;
	}
	
    }
    
    private void replaceVariableWithID(Node node, String variable, int id) {
	if (node == null)
	    return;
	if (node.getToken().equals(variable)) {
	    String rhsVar = "rhs["+id+"]";
	    node.setToken(rhsVar);
	    idToNameMap.put(rhsVar, variable);
	}
	replaceVariableWithID(node.getChild(), variable, id);
	replaceVariableWithID(node.getNext(), variable, id);
	
    }
    
    public String build() {
	StringBuffer sb = new StringBuffer();
	
	sb.append(equation.getEquationFromTree(polarityRoot));
	
	return sb.toString();
	
    }

    public String getGeneratedCode() {
        return generatedCode;
    }

}
