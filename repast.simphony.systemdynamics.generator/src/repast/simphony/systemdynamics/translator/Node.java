package repast.simphony.systemdynamics.translator;

public class Node {
    
    private Node parent = null;
    private Node child = null;
    private Node next = null;
    private Node previous = null;
    private String token;
    
    private boolean placeHolder = false;
    private boolean deleted = false;
    
    private String resultsVariable = null;
    private StringBuffer generatedCodeHead = new StringBuffer();
    private StringBuffer generatedCodeTail = new StringBuffer();
    private StringBuffer generatedCodeElse = new StringBuffer();
    
    private String replacedToken = "@";
    
    private boolean initialized = false;
    
    public Node(String token) {
	this.token = token;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public String getToken() {
        return token;
    }
    
    public String getUnits() {
        return UnitsManager.getUnits(token);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInfo() {
	StringBuffer sb = new StringBuffer();
	String spacing = "      ";
	sb.append(spacing+"token: "+token+"\n");
	sb.append(spacing+"units: "+getUnits()+"\n");
	sb.append(spacing+"placeHolder: "+(placeHolder ? "True" : "False")+"\n");
	sb.append(spacing+"results variable: "+resultsVariable+"\n");
	sb.append(spacing+"head: "+generatedCodeHead+"\n");
	sb.append(spacing+"else: "+generatedCodeElse+"\n");
	sb.append(spacing+"tail: "+generatedCodeTail+"\n");
        return sb.toString(); // replacedToken;
    }
    
    public String getInfo(String blanks) {
	StringBuffer sb = new StringBuffer();
	
	sb.append(blanks+"token: "+token+"\n");
	sb.append(blanks+"units: "+UnitsManager.getUnits(token)+"\n");
	sb.append(blanks+"placeHolder: "+(placeHolder ? "True" : "False")+"\n");
	sb.append(blanks+"results variable: "+resultsVariable+"\n");
	sb.append(blanks+"head: "+generatedCodeHead+"\n");
	sb.append(blanks+"else: "+generatedCodeElse+"\n");
	sb.append(blanks+"tail: "+generatedCodeTail+"\n");
        return sb.toString(); // replacedToken;
    }
    
    public String getInfoTerse(String blanks) {
	StringBuffer sb = new StringBuffer();
	
	sb.append(blanks+"token: "+token+"\n");
	sb.append(blanks+"units: "+UnitsManager.getUnits(token)+"\n");
        return sb.toString(); // replacedToken;
    }

    public void setReplacedToken(String replacedToken) {
        this.replacedToken = replacedToken;
    }

    public StringBuffer getGeneratedCodeHead() {
        return generatedCodeHead;
    }

    public void setGeneratedCodeHead(StringBuffer generatedCode) {
        this.generatedCodeHead = generatedCode;
    }

    public StringBuffer getGeneratedCodeTail() {
        return generatedCodeTail;
    }

    public void setGeneratedCodeTail(StringBuffer generatedCodeTail) {
        this.generatedCodeTail = generatedCodeTail;
    }

    public String getResultsVariable() {
        return resultsVariable;
    }

    public void setResultsVariable(String resultsVariable) {
        this.resultsVariable = resultsVariable;
    }

    public StringBuffer getGeneratedCodeElse() {
        return generatedCodeElse;
    }

    public void setGeneratedCodeElse(StringBuffer generatedCodeElse) {
        this.generatedCodeElse = generatedCodeElse;
    }

    public boolean isPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(boolean placeHolder) {
        this.placeHolder = placeHolder;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
    
    public boolean isTerminal() {
	if (child == null)
	    return true;
	else
	    return false;
    }
    
    public String toString() {
	return token;
    }

}
