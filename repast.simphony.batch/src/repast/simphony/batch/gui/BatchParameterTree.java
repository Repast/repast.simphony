package repast.simphony.batch.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

@SuppressWarnings("serial")
public class BatchParameterTree extends JTree {

  private ParameterEditorMediator mediator;
  
  private class NodeRenderer extends DefaultTreeCellRenderer {
    
    private Icon constantIcon, parameterIcon;
    
    public NodeRenderer() {
      constantIcon = new ImageIcon(getClass().getResource("leaf.png"));
      parameterIcon = new ImageIcon(getClass().getResource("exec.png"));
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
        boolean expanded, boolean leaf, int row, boolean hasFocus) {
      Icon icon = null;
      if (((AbstractBatchParameterNode)value).isBeanNode()) {
        value = mediator.getParameters().getDisplayName(value.toString());
        icon = parameterIcon;
      }
      
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
      setIcon(icon);
      return this;
    }
  }

  public BatchParameterTree() {
    super();
    setCellRenderer(new NodeRenderer());
    LabelNode root = new LabelNode("root", false);
    DefaultTreeModel model = new DefaultTreeModel(root, true);
    setRootVisible(false);
   
    root.add(new LabelNode("Constants", true));
    root.add(new LabelNode("Parameters", false));
  
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showPopupMenu(e);
        }
      }

      public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
          showPopupMenu(e);
        }
      }
    });
    
    setModel(model);
  }
  
  public void init(ParameterEditorMediator mediator) {
    this.mediator = mediator;
    addTreeSelectionListener(mediator);
    selectNode((MutableTreeNode)((DefaultMutableTreeNode)getModel().getRoot()).getChildAt(0));
  }
  
  /**
   * Selects the specified node in the tree.
   * 
   * @param node the node to select
   */
  public void selectNode(MutableTreeNode node) {
    List<TreeNode> nodes = new ArrayList<TreeNode>();
    nodes.add(node);
    TreeNode parent = node.getParent();
    while (parent != null) {
      nodes.add(0, parent);
      parent = parent.getParent();
    }
    setSelectionPath(new TreePath(nodes.toArray()));
  }
  
  private void showPopupMenu(MouseEvent evt) {
    TreePath path = getPathForLocation(evt.getX(), evt.getY());
    if (path != null) {
      AbstractBatchParameterNode node = (AbstractBatchParameterNode) path.getLastPathComponent();
      JPopupMenu menu = new JPopupMenu();
      for (Action action : node.getMenuActions(mediator)) {
        menu.add(action);
      }
      if (menu.getComponentCount() > 0) {
        menu.show(this, evt.getX(), evt.getY());
      }
    }
  }
}
