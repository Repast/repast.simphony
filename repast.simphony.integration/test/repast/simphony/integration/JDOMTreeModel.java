/**
 * 
 */
package repast.simphony.integration;

import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdom.Element;

class JDOMTreeModel implements TreeModel {
	Element root;

	public JDOMTreeModel(Element root) {
		this.root = root;
	}

	public Object getChild(Object parent, int index) {
		if (((Element) parent).getChildren().size() <= index) {
			return "Value: " + ((Element) parent).getContent(0).getValue();
		}
		return ((Element) parent).getChildren().get(index);
	}

	public int getChildCount(Object parent) {
		if (parent instanceof Element && ((Element) parent).getValue() != null) {
			return ((Element) parent).getChildren().size() + 1;
		} else if (parent instanceof Element && ((Element) parent).getValue() == null) {
			return ((Element) parent).getChildren().size();
		} else {
			return 0;
		}
	}

	public int getIndexOfChild(Object parent, Object child) {
		if (((Element) parent).getChildren().indexOf(child) == -1)
			return ((Element) parent).getChildren().size() + 1;
		else
			return ((Element) parent).getChildren().indexOf(child); 
	}

	public Object getRoot() {
		return root;
	}

	public boolean isLeaf(Object node) {
		return false; // whatever
	}

	// etc
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}
	
	public static void showInFrame(Element rootElement, boolean exitOnClose) {
		TreeModel mdl = new JDOMTreeModel(rootElement);
		JTree tree = new JTree(mdl);

		JFrame frame = new JFrame();
		
		frame.add(new JScrollPane(tree));
		
		frame.pack();
		frame.setVisible(true);
		if (exitOnClose) {
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				}
			});
		}
	}
}