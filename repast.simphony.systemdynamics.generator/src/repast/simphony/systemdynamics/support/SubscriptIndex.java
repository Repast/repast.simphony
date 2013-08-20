package repast.simphony.systemdynamics.support;

public class SubscriptIndex {
    
    private String subscript;
    private int index;
    
    public SubscriptIndex(String subscript, int index) {
	this.subscript = subscript;
	this.index = index;
    }

    public String getSubscript() {
        return subscript;
    }

    public void setSubscript(String subscript) {
        this.subscript = subscript;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
