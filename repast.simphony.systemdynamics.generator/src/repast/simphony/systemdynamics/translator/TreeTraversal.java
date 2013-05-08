package repast.simphony.systemdynamics.translator;

public class TreeTraversal {
	
	public static Node getLhs(Node aNode) {
		// by definition, the lhs is the child of a given node
		return aNode.getChild();
	}
	
	public static Node getRhs(Node aNode) {
		// by definition, the rhs is the next sibling of the child of a given node
		Node lhs = aNode.getChild();
		if (lhs != null)
			return lhs.getNext();
		else
			return null;
	}
	
	public static Node getFunctionArgument(Node functionNode, int argNum) {
		
		FunctionManager fm = InformationManagers.getInstance().getFunctionManager();
		
		// first check if this is indeed a function call
		
		if (!fm.isFunction(functionNode.getToken()))
			return null;
		
		// functionNode is the node with the function name
		// argNum is the index of the argument in the vensim world. i.e. no generated args
		// this index starts at 1
		
		FunctionDescription fd = fm.getDescription(functionNode.getToken());
		
		int currentNode = 1;
		
		// get the first argument to the function
		
		Node node = functionNode.getChild();
		
		if (fd.isRequiresName())
			node = node.getNext();
		
		if (fd.isRequiresValue())
			node = node.getNext();
		
		if (fd.isRequiresTime())
			node = node.getNext();
		
		if (fd.isRequiresTimeStep())
			node = node.getNext();
		
		while (currentNode < argNum) {
			node = node.getNext();
			currentNode++;
		}
		
		return node;
		
		
	}

}
