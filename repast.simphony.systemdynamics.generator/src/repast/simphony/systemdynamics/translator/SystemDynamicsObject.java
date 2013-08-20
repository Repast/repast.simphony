package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.List;

public class SystemDynamicsObject {

    private String screenName;
    private List<GraphicObject> graphicObjects;
    private List<Equation> equations;
    private List<Arrow> incomingArrows;
    private List<Arrow> outgoingArrows;
   

    public SystemDynamicsObject() {
	graphicObjects = new ArrayList<GraphicObject>();
	equations = new ArrayList<Equation>();
	incomingArrows = new ArrayList<Arrow>();
	outgoingArrows = new ArrayList<Arrow>();
	
    }

    public SystemDynamicsObject(String name) {
	this();
	this.screenName = name.replace("\"", "");
    }
    
    public String getAssociatedVariableName() {
	if (graphicObjects.size() == 0)
	    return null;
	for (GraphicObject go : graphicObjects ) {
	    if (go.getAssociatedVariable() != null)
		return go.getAssociatedVariable().getName();
	}
	return null;
    }
    
    public String getType() {
	if (graphicObjects.size() == 0)
	    return "UNKNOWN";
	else
	    return graphicObjects.get(0).getTypeAsString();
    }
    
    public boolean isStock() {
	if (getType().equals(View.VARIABLE)) {
	    if (equations.size() > 0) {
		return equations.get(0).isStock();
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    public void print() {
	System.out.println("Screen Name: "+screenName);
	System.out.println("\tNumber of Graphic Representations: "+graphicObjects.size());
	System.out.println("\tNumber of Equations: "+equations.size());
	for (Equation eqn : equations) {
		System.out.println("\t\t"+eqn.getEquation());
	}
	System.out.print("\tNumber of In Arrows: "+incomingArrows.size());
	for (Arrow s : incomingArrows)
	    System.out.print(", "+s);
	System.out.print("\n");
	System.out.print("\tNumber of Out Arrows: "+outgoingArrows.size());
	for (Arrow s : outgoingArrows)
	    System.out.print(", "+s);
	System.out.print("\n");
    }
	

    public void addGraphicObject(GraphicObject graphicObject) {
	if (!graphicObjects.contains(graphicObject))
	    graphicObjects.add(graphicObject);
    }

    public void addEquation(Equation equation) {
	if (!equations.contains(equation))
	    equations.add(equation);
    }

    public void addIncomingArrow(Arrow arrow) {
	if (!arrowExists(incomingArrows, arrow)) {
		incomingArrows.add(arrow);
//		System.out.println(screenName+" SDO: AddIn "+arrow+" size "+incomingArrows.size());
	}
//	print();
    }

    public void addOutgoingArrow(Arrow arrow) {
    	if (!arrowExists(outgoingArrows, arrow)) {
		outgoingArrows.add(arrow);
//		System.out.println(screenName+" SDO: AddOut "+arrow+" size "+outgoingArrows.size());
	}
//	print();
    }
    
    public boolean arrowExists(List<Arrow> arrows, Arrow newArrow) {
    	for (Arrow arrow : arrows) {
    		if (arrow.getOtherEnd().equals(newArrow.getOtherEnd()))
    			return true;
    	}
    	
    	return false;
    }

    public String getScreenName() {
	return screenName;
    }

    public void setScreenName(String screenName) {
	this.screenName = screenName;
    }

    public List<GraphicObject> getGraphicObjects() {
	return graphicObjects;
    }

    public void setGraphicObjects(List<GraphicObject> graphicObjects) {
	this.graphicObjects = graphicObjects;
    }

    public List<Equation> getEquations() {
	return equations;
    }

    public void setEquations(List<Equation> equations) {
	this.equations = equations;
    }

	public List<Arrow> getIncomingArrows() {
		return incomingArrows;
	}

	public void setIncomingArrows(List<Arrow> incomingArrows) {
		this.incomingArrows = incomingArrows;
	}

	public List<Arrow> getOutgoingArrows() {
		return outgoingArrows;
	}

	public void setOutgoingArrows(List<Arrow> outgoingArrows) {
		this.outgoingArrows = outgoingArrows;
	}



}
