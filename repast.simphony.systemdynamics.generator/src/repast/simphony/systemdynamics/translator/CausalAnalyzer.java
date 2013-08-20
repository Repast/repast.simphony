package repast.simphony.systemdynamics.translator;

import java.util.Map;


public class CausalAnalyzer {

    public CausalAnalyzer() {

    }

    public void generateCausalTrees(SystemDynamicsObjectManager sdObjectManager) {
	Node root = null;
	for (SystemDynamicsObject stock : sdObjectManager.stockVariables()) {
	    root = new Node(stock.getScreenName());
	    extend(root, sdObjectManager);
	    System.out.println("\n###################################################\n");
	    printTreeCausal(root, 0, sdObjectManager);
	}
    }

    private void extend(Node node, SystemDynamicsObjectManager sdObjectManager) {
	if (node == null)
	    return;
	Node currentNode = null;
	for (Arrow in : sdObjectManager.getIncomingArrows(node.getToken())) {
	    Node n = new Node(in.getOtherEnd());
	    if (currentNode == null) {
		node.setChild(n);
	    } else {
		currentNode.setNext(n);
	    }
	    currentNode = n;
	    
	    SystemDynamicsObject obj = sdObjectManager.getObjectWithName(in.getOtherEnd());
	    if (obj != null && !obj.isStock()) {
		extend(n, sdObjectManager);
	    }
	    
	}
    }

    public static void printTreeCausal(Node node, int level, SystemDynamicsObjectManager sdObjectManager) {
	if (node == null)
	    return;
	String blanks="";
	for (int i = 0; i < level; i++)
	    blanks += "    ";
	
	String stock = "";
	if (sdObjectManager.getObjectWithName(node.getToken()).isStock())
	    stock = " <STOCK>";
	
	System.out.println(blanks+"("+level + ") "+node.getToken()+stock);
	
	printTreeCausal(node.getChild(), level+1, sdObjectManager);
	printTreeCausal(node.getNext(), level, sdObjectManager);
    }


}
