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
    
    public Node(Node anotherNode) {
    	// make an exact copy;
    	this.parent = anotherNode.parent;
    	this.child = anotherNode.child;
    	this.next = anotherNode.next;
    	this.previous = anotherNode.previous;
    	this.token = new String(anotherNode.token);
    }
    
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
        return InformationManagers.getInstance().getUnitsManager().getUnits(token);
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
    	sb.append(spacing+"parent: "+parent+"\n");
    	sb.append(spacing+"child: "+child+"\n");
    	sb.append(spacing+"next: "+next+"\n");
    	sb.append(spacing+"previous: "+previous+"\n");

    	return sb.toString(); // replacedToken;
    }
    
    public String getInfo(String blanks) {
	StringBuffer sb = new StringBuffer();
	
	sb.append(blanks+"token: "+token+"\n");
	sb.append(blanks+"units: "+InformationManagers.getInstance().getUnitsManager().getUnits(token)+"\n");
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
	sb.append(blanks+"units: "+InformationManagers.getInstance().getUnitsManager().getUnits(token)+"\n");
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
    
    public static String expand(Node node) {
    	StringBuffer sb = new StringBuffer();
    	expand(node, sb);
    	return sb.toString();
    }
	
	private static void expand(Node node, StringBuffer sb) {
	    if (node == null)
	    	return;
	    expand(node.getChild(), sb);
	    String vensimVar = InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(node.getToken());
	    sb.append(vensimVar);
	    expand(node.getNext(), sb);
	}
	
	public static String generateExpression(Node node) {
		StringBuffer sb = new StringBuffer();
		NativeDataTypeManager ndtm = InformationManagers.getInstance().getNativeDataTypeManager();

		if (node != null) {

			// node is the root node of an expression
			// if an operator:
			// left child
			// operator
			// right child
			
			if (Parser.isArithmeticOperator(node.getToken()) || Parser.isEqualSign(node.getToken())) {
				if (!Parser.isUnaryOperator(node.getToken())) {
				sb.append(generateExpression(TreeTraversal.getLhs(node)));
				sb.append(ndtm.getOriginalNameQuotedIfNecessary(node.getToken()));
				sb.append(generateExpression(TreeTraversal.getRhs(node)));
				} else {
					sb.append(Parser.translateUnaryOperator(node.getToken()));
					sb.append(generateExpression(node.getChild()));
				}
				return sb.toString();

			} else {
				sb.append(ndtm.getOriginalNameQuotedIfNecessary(node.getToken()));
			}

		}
		return sb.toString();
	}
	
	public String generateExpression() {
		
		Node node = this;
		StringBuffer sb = new StringBuffer();
		NativeDataTypeManager ndtm = InformationManagers.getInstance().getNativeDataTypeManager();

		if (node != null) {

			// node is the root node of an expression
			// if an operator:
			// left child
			// operator
			// right child
			
			if (Parser.isArithmeticOperator(node.getToken()) || Parser.isEqualSign(node.getToken())) {
				if (!Parser.isUnaryOperator(node.getToken())) {
					sb.append(generateExpression(TreeTraversal.getLhs(node)));
					sb.append(ndtm.getOriginalNameQuotedIfNecessary(node.getToken()));
					sb.append(generateExpression(TreeTraversal.getRhs(node)));
				} else {
					sb.append(Parser.translateUnaryOperator(node.getToken()));
					sb.append(generateExpression(node.getChild()));
				}
				return sb.toString();

			} else {
				sb.append(ndtm.getOriginalNameQuotedIfNecessary(node.getToken()));
			}

		}
		return sb.toString();
	}

}
