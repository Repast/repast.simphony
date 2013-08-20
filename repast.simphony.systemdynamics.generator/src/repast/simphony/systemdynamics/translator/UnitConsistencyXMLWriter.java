package repast.simphony.systemdynamics.translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;

public class UnitConsistencyXMLWriter {
    
    private int equationCount;
    private Set<UnitExpression> inconsistentEquations;
    
    public UnitConsistencyXMLWriter() {
	inconsistentEquations = new HashSet<UnitExpression>();
    }
    
    public void write(String file) {
	BufferedWriter bw = Translator.openReport(file);
	writeHeader(bw);
	writeSummary(bw);
	writeInconsistencies(bw);
	writeTail(bw);
    }
    
    public void writeReport(String file) {
	BufferedWriter bw = Translator.openReport(file);
	
	writeSummaryReport(bw);
	writeInconsistenciesReport(bw);
	
	
    }
    
    public void addInconsistent(UnitExpression ue) {
	inconsistentEquations.add(ue);
    }
    
    public void writeHeader(BufferedWriter bw) {
	try {
	    bw.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
	    bw.append("<UnitConsistency xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void writeTail(BufferedWriter bw) {
	try {
	    bw.append("</UnitConsistency>\n");
	    bw.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void writeSummary(BufferedWriter bw) {
	try {
	    bw.append("\t<Summary>\n");
	    bw.append("\t\t<EquationsChecked>");
	    bw.append(Integer.toString(getEquationCount()));
	    bw.append("</EquationsChecked>\n");
	    bw.append("\t\t<Consistent>");
	    bw.append(Integer.toString(getEquationCount() - getNumInconsistent()));
	    bw.append("</Consistent>\n");
	    bw.append("\t\t<Inconsistent>");
	    bw.append(Integer.toString(getNumInconsistent()));
	    bw.append("</Inconsistent>\n");
	    bw.append("\t</Summary>\n");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void writeSummaryReport(BufferedWriter bw) {
	try {
	    bw.append("Units Consistency Summary Report\n\n");
	    bw.append("Number of EquationsChecked: ");
	    bw.append(Integer.toString(getEquationCount()));
	    bw.append("\n");
	    bw.append("Number Consistent:          ");
	    bw.append(Integer.toString(getEquationCount() - getNumInconsistent()));
	    bw.append("\n");
	    bw.append("Number Inconsistent:        ");
	    bw.append(Integer.toString(getNumInconsistent()));
	    bw.append("\n");
	    bw.append("\n");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void writeInconsistencies(BufferedWriter bw) {

    	try {
    		bw.append("\t<Inconsistencies>\n");
    		for (UnitExpression ue : inconsistentEquations) {
    			bw.append("\t<Equations>\n");
    			bw.append("\t\t<Equation>\n");

    			bw.append("\t\t\t<VensimEquation>");
    			bw.append(StringEscapeUtils.escapeHtml(ue.getVensimEquation().split("~")[0]));
    			bw.append("</VensimEquation>\n\n");

    			bw.append("\t\t\t<Lhs>");
    			bw.append(StringEscapeUtils.escapeHtml(ue.getEquationLHS()));
    			bw.append("</Lhs>\n\n");

    			bw.append("\t\t\t<LhsUnits>");
    			bw.append(StringEscapeUtils.escapeHtml(ue.getLhsUnitsString()));
    			bw.append("</LhsUnits>\n\n");

    			bw.append("\t\t\t<RhsUnits>");
    			bw.append(StringEscapeUtils.escapeHtml(ue.getRhsUnitsString()));
    			bw.append("</RhsUnits>\n\n");

    			bw.append("\t\t\t<RhsUnitsComplete>");
    			bw.append(StringEscapeUtils.escapeHtml(ue.getCompleteRhsUnitsString()));
    			bw.append("</RhsUnitsComplete>\n");

    			//		bw.append("\t\t\t<Message>");
    			//		bw.append(ue.getInconsistentMessage());
    			//		bw.append("</Message>\n");

    			bw.append("\t\t</Equation>\n");
    			bw.append("\t</Equations>\n");
    		}
    		bw.append("\t</Inconsistencies>\n");
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    public void writeInconsistenciesReport(BufferedWriter bw) {
	if (inconsistentEquations.size() == 0)
	    return;
	try {
	    bw.append("\n=====================================\n");
	    bw.append("Inconsistent Unit Equations\n\n");
	    for (UnitExpression ue : inconsistentEquations) {
		bw.append("Equation:\n");
		bw.append("\t"+StringEscapeUtils.escapeHtml(ue.getVensimEquation().split("~")[0]));
		bw.append("\n\n");
		    
		bw.append("LHS Units:\n");
		bw.append("\t"+ue.getLhsUnitsString());
		bw.append("\n\n");
		
		bw.append("RHS Units:\n");
		bw.append("\t"+ue.getRhsUnitsString());
		bw.append("\n\n");
		
		bw.append("Complete RHS Units:\n");
		bw.append("\t"+ue.getCompleteRhsUnitsString());
		bw.append("\n=====================================\n");
		
	    }
	    bw.close();
	    
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public int getNumInconsistent() {
	return inconsistentEquations.size();
    }

    public int getEquationCount() {
        return equationCount;
    }

    public void setEquationCount(int equationCount) {
        this.equationCount = equationCount;
    }

}
