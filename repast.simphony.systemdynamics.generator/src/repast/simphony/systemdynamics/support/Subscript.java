package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.List;

public class Subscript {
    
    // language independent
    
    public static String NAMED = "Named"; // this has subvalue
    public static final String TERMINAL = "Terminal"; // this is a terminal value
    
    private String type;
    private String value;
    private int startPos;
    private int endPos;
    private List<Subscript> children;
    
    public Subscript(String value, String type) {
	this.value = value;
	this.type = type;
    }
    
    public void addChild(Subscript s) {
	if (children == null)
	    children = new ArrayList<Subscript>();
	children.add(s);
    }
    
    public int getNumberOfTerminal() {
	return endPos - startPos + 1;
    }
    
    public void printDetail() {
	if (isNamed()) {
	    System.out.println("Type: "+type+" Value: "+value);
	    String spacing = "    ";
	    for (Subscript s : children) {
		s.printDetail(spacing);
	    }
	} else {
	    System.out.println("Type: "+type+" Value: "+value);
	}
    }
    
    private void printDetail(String spacing) {
	if (isNamed()) {
	    System.out.println(spacing+" Type: "+type+" Value: "+value);
	    spacing += "    ";
	    for (Subscript s : children) {
		s.printDetail(spacing);
	    }
	} else {
	    System.out.println(spacing+" Type: "+type+" Value: "+value);
	}
    }
    
    public void addOffset(int offset) {
	startPos += offset;
	endPos += offset;
    }
    
    public void computeInitialStartEnd() {
	startPos = 0;
	// determine the number of termal nodes below me
	endPos = computeEnd(this)-1;
    }
    
    public int computeEnd(Subscript subscript) {
	if (subscript.getType().equals(Subscript.TERMINAL))
	    return 1;
	int end = 0;
	for (Subscript s : subscript.getChildren()) {
	    end += computeEnd(s);
	}
	return end;
	
    }
    
    public boolean isTerminal() {
	return type.equals(Subscript.TERMINAL);
    }
    
    public boolean isNamed() {
	return type.equals(Subscript.NAMED);
    }
    
    public String toString() {
	return value +","+type+","+startPos+","+endPos;
    }
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getStartPos() {
        return startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public List<Subscript> getChildren() {
        return children;
    }

    public void setChildren(List<Subscript> children) {
        this.children = children;
    }

}
