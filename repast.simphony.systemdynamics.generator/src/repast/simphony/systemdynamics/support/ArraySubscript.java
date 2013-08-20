package repast.simphony.systemdynamics.support;

import java.util.ArrayList;
import java.util.List;

public class ArraySubscript {
    
    List<Subscript> subscripts;
    
    public ArraySubscript() {
	subscripts = new ArrayList<Subscript>();
    }
    
    public void addSubscript(Subscript subscript) {
	subscripts.add(subscript);
    }

    public List<Subscript> getSubscripts() {
        return subscripts;
    }

    public void setSubscripts(List<Subscript> subscripts) {
        this.subscripts = subscripts;
    }

}
