package repast.simphony.ui.tree;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.Scenario;
import repast.simphony.ui.DefaultActionUI;
import repast.simphony.ui.plugin.ActionUI;
import repast.simphony.ui.plugin.UIActionExtensions;
import repast.simphony.ui.plugin.editor.Editor;
import repast.simphony.util.collections.Pair;
import repast.simphony.util.collections.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * @author Nick Collier
 */
public class ScenarioTree extends JTree {

  private static ScenarioTreeCellRenderer SCENARIO_TREE_CELL_RENDERER;

  static {
    SCENARIO_TREE_CELL_RENDERER = new ScenarioTreeCellRenderer();
  }

  private static final long serialVersionUID = 1L;

  private ControllerRegistry registry;
  private Scenario scenario;
  private UIActionExtensions exts;
  private Map<Object, Pair<Object, ScenarioNode>> nodeMap = new HashMap<Object, Pair<Object, ScenarioNode>>();

  public ScenarioTree(UIActionExtensions exts) {
    super(new DefaultTreeModel(new ScenarioNode(new DefaultActionUI("Empty Tree"), "")));
    this.exts = exts;
    this.setRowHeight(17); 

    this.setCellRenderer(SCENARIO_TREE_CELL_RENDERER);

    addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
          showDialog(e.getPoint());
        }

        // todo is this true?
        // linux I think does the trigger in clicked
        else if (e.isPopupTrigger()) {
          ScenarioTree.this.setSelectionPath(getPathForLocation(e.getX(), e.getY()));
          showPopupMenu(e);
        }
      }

      // for OS X
      public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
          ScenarioTree.this.setSelectionPath(getPathForLocation(e.getX(), e.getY()));
          showPopupMenu(e);
        }
      }

      public void mouseReleased(MouseEvent e) {
        // windows, I think, does the trigger in released
        if (e.isPopupTrigger()) {
          ScenarioTree.this.setSelectionPath(getPathForLocation(e.getX(), e.getY()));
          showPopupMenu(e);
        }
      }
    });

    addKeyListener(new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE) {
          deleteNodes();
        }
      }
    });
  }

  private void deleteNodes() {
    ScenarioNode[] nodes = getSelectedNodes();
    if (nodes == null) {
      return;
    }

    Object root = getModel().getRoot();

    // build the string representation of these nodes
    StringBuilder builder = new StringBuilder();
    boolean foundAtLeastOne = true;
    for (int i = 0; i < nodes.length; i++) {
      // for now just disallow deleting top level context and root
      if (nodes[i].equals(root) || nodes[i].getParent().equals(root)) {
        continue;
      }
      foundAtLeastOne = true;
      if (i != 0) {
        builder.append(", ");
      }
      builder.append(nodes[i].getUIRepresentation().getLabel());
    }

    if (foundAtLeastOne) {
      int res = JOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(ScenarioTree.this),
          "Delete '" + builder.toString() + "'?", "Delete Action Tree Item",
          JOptionPane.YES_NO_OPTION);
      if (res == JOptionPane.YES_OPTION) {
        // for now just disallow deleting top level context and root
        for (ScenarioNode node : nodes) {
          if (!node.equals(root) && !node.getParent().equals(root)) {
            removeNode(findActionForNode(node));
          }
        }
      }
    }
  }

  private ControllerAction findActionForNode(ScenarioNode node) {
    for (Map.Entry<Object, Pair<Object, ScenarioNode>> entry : nodeMap.entrySet()) {
      if (entry.getValue().getSecond().equals(node))
        return (ControllerAction) entry.getKey();
    }
    return null;
  }

  private ScenarioNode getSelectedNode() {
    TreePath path = getSelectionPath();
    if (path != null) {
      return (ScenarioNode) path.getLastPathComponent();
    }
    return null;
  }

  private ScenarioNode[] getSelectedNodes() {
    TreePath[] paths = getSelectionPaths();
    if (paths == null) {
      return null;
    }
    ScenarioNode[] nodes = new ScenarioNode[paths.length];
    for (int i = 0; i < paths.length; i++) {
      nodes[i] = (ScenarioNode) paths[i].getLastPathComponent();
    }
    return nodes;
  }

  private void showPopupMenu(MouseEvent evt) {
    TreePath path = getPathForLocation(evt.getX(), evt.getY());
    if (path != null) {
      int row = getRowForPath(path);
      ScenarioNode node = (ScenarioNode) path.getLastPathComponent();
      ActionUI rep = node.getUIRepresentation();
      Object root = getModel().getRoot();
      JPopupMenu menu = rep.getPopupMenu(new ScenarioTreeEvent(node.getContextID(), scenario,
          registry, this, row));
      if (menu != null) {
        menu.show(this, evt.getX(), evt.getY());
      } else if (!node.equals(root) && !node.getParent().equals(root) && node.isLeaf()) {
        menu = createDelPopupMenu();
        menu.show(this, evt.getX(), evt.getY());
      }

      /*
       * else { ScenarioTreeEvent scenarioEvt = new
       * ScenarioTreeEvent(node.getContextID(), scenario, registry, this,
       * this.getRowForPath(path)); Editor editor = rep.getEditor(scenarioEvt);
       * if (editor != null) { menu = new PropertiesMenu(editor);
       * menu.show(this, evt.getX(), evt.getY()); } }
       */
    }
  }

  /**
   * A simple popup menu for node deletion
   * 
   * @return
   */
  private JPopupMenu createDelPopupMenu() {

    JPopupMenu menu = new JPopupMenu();
    menu.add((Action) new DeleteAction());

    return menu;
  }

  /**
   * Action for node deletion menu item
   * 
   * @author tatara
   * 
   */
  private class DeleteAction extends AbstractAction {

    public DeleteAction() {
      super("Delete");
    }

    public void actionPerformed(ActionEvent e) {
      deleteNodes();
    }
  }

  @SuppressWarnings("serial")
  protected class PropertiesMenu extends JPopupMenu {
    public PropertiesMenu(final Editor editor) {
      JMenuItem item = new JMenuItem("Properties");
      item.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          editor.display((JFrame) SwingUtilities.getWindowAncestor(ScenarioTree.this));
        }
      });
      add(item);
    }
  }

  private void showDialog(Point pt) {
    TreePath path = getPathForLocation(pt.x, pt.y);
    if (path != null) {
      ScenarioNode node = (ScenarioNode) path.getLastPathComponent();
      ActionUI rep = node.getUIRepresentation();
      ScenarioTreeEvent evt = new ScenarioTreeEvent(node.getContextID(), scenario, registry, this,
          this.getRowForPath(path));
      Editor editor = rep.getEditor(evt);
      if (editor != null) {
        editor.display((JFrame) SwingUtilities.getWindowAncestor(this));
        if (!editor.wasCanceled()) {
          ((DefaultTreeModel) getModel()).nodeChanged(node);
        }
      }
    }
  }

  public void setControllerRegistry(Scenario scenario, ControllerRegistry registry) {
    this.registry = registry;
    this.scenario = scenario;
    DefaultTreeModel model = (DefaultTreeModel) getModel();
    ScenarioNode root = (ScenarioNode) model.getRoot();
    root.removeAllChildren();
    root = new ScenarioNode(new DefaultActionUI(registry.getName()), "");
    model.setRoot(root);
    nodeMap.clear();
    fillTree(root, registry);
    model.nodeStructureChanged(root);
    expandAll();
  }

  public void removeNode(ControllerAction action) {
    Pair<Object, ScenarioNode> pair = nodeMap.remove(action);
    ScenarioNode node = pair.getSecond();
    ScenarioNode parent = (ScenarioNode) node.getParent();
    parent.remove(node);
    registry.removeAction(pair.getFirst(), action);
    DefaultTreeModel model = (DefaultTreeModel) getModel();
    model.nodeStructureChanged(parent);
    scenario.setDirty(true);
  }

  private void fillTree(ScenarioNode root, ControllerRegistry registry) {
    Tree<Object> contextGraph = registry.getContextIdTree();
    Object contextRoot = contextGraph.getRoot();
    addNode(root, contextRoot, contextGraph);
  }

  private void addNode(ScenarioNode parent, Object contextID, Tree<Object> graph) {
    ScenarioNode contextNode = new ScenarioNode(new DefaultActionUI(contextID.toString()),
        contextID);
    parent.add(contextNode);

    Tree<ControllerAction> actionGraph = registry.getActionTree(contextID);
    ControllerAction action = actionGraph.getRoot();
    fillActions(contextNode, action, null, actionGraph, contextID);

    for (Object obj : graph.getChildren(contextID)) {
      addNode(contextNode, obj, graph);
    }
  }

  private void fillActions(ScenarioNode parent, ControllerAction action, ActionUI uiRep,
      Tree<ControllerAction> actionGraph, Object contextID) {

		// If the ActionUI is null, then the ControllerAction has not been registered
		//   because it is not part of a r.s.gui extension plugin.
  	if (uiRep != null) {
  		ScenarioNode actionNode = new ScenarioNode(uiRep, contextID);
  		nodeMap.put(action, new Pair<Object, ScenarioNode>(contextID, actionNode));
  		parent.add(actionNode);
  		parent = actionNode;
    }

    // first build the action ui's so we can sort by them
    HashMap<ActionUI, ControllerAction> actionMap = new HashMap<ActionUI, ControllerAction>();

    ArrayList<ActionUI> childUIActions = new ArrayList<ActionUI>();
    for (ControllerAction child : actionGraph.getChildren(action)) {
      ActionUI ui = createUIRepresentation(child);
      if (ui != null){
      	actionMap.put(ui, child);
      	childUIActions.add(ui);
      }
    }

    // now sort them so they show up in the GUI in a reasonable order
    Collections.sort(childUIActions, new LabelComparator());

    for (ActionUI childUI : childUIActions) {
      fillActions(parent, actionMap.get(childUI), childUI, actionGraph, contextID);
    }
  }

  public static class LabelComparator implements Comparator<ActionUI> {
    public int compare(ActionUI o1, ActionUI o2) {
      return o1.getLabel().compareTo(o2.getLabel());
    }
  }

  private ActionUI createUIRepresentation(ControllerAction action) {
    ActionUI uiRep = exts.getEditor(action);

		// The ActionUI can be null if the ControllerAction has not been registered
		//   because it is not part of a r.s.gui extension plugin.

    return uiRep;
  }

  public void expandAll() {
    TreeNode root = (TreeNode) getModel().getRoot();

    // Traverse tree from root
    expandAll(new TreePath(root), true);
  }

  private void expandAll(TreePath parent, boolean expand) {
    // Traverse children
    TreeNode node = (TreeNode) parent.getLastPathComponent();
    if (node.getChildCount() >= 0) {
      for (Enumeration e = node.children(); e.hasMoreElements();) {
        TreeNode n = (TreeNode) e.nextElement();
        TreePath path = parent.pathByAddingChild(n);
        expandAll(path, expand);
      }
    }

    // Expansion or collapse must be done bottom-up
    if (expand) {
      expandPath(parent);
    } else {
      collapsePath(parent);
    }
  }

  public void addControllerAction(int row, ControllerAction action) {
    TreePath path = getPathForRow(row);
    if (path == null)
      throw new IllegalArgumentException("Invalid tree row");
    ScenarioNode parent = (ScenarioNode) path.getLastPathComponent();
    ActionUI uiRep = createUIRepresentation(action);
    Object contextID = parent.getContextID();
    ScenarioNode actionNode = new ScenarioNode(uiRep, contextID);
    nodeMap.put(action, new Pair<Object, ScenarioNode>(contextID, actionNode));
    parent.add(actionNode);
    if (!isExpanded(path)) {
      expandPath(path);
    }
    ((DefaultTreeModel) getModel()).nodeStructureChanged(parent);
  }
}
