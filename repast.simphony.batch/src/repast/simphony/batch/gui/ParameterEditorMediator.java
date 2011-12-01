package repast.simphony.batch.gui;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import repast.simphony.parameter.Parameters;

/**
 * Mediates between the different GUI parts of the ParameterEditor.
 * 
 * @author Nick Collier
 */
public class ParameterEditorMediator implements TreeSelectionListener {

  private static final String EDIT_INDEX = "EDIT";
  private static final String CONSTANT_INDEX = "CONSTANT";
  private static final String PARAM_INDEX = "PARAMETER";

  private Parameters params;
  private BatchParameterTree tree;

  private String defaultParamName = "";
  private boolean isNew = false;
  private TreeNode selectedNode;

  private JPanel infoPanel;
  private ParameterEditPanel editPanel;
  private JPanel constPanel = new ConstantInfoPanel();
  private JPanel paramPanel = new ParameterInfoPanel();
  private AddParameterAction addAction;
  private DeleteParameterAction delAction;

  /**
   * @param params
   */
  public ParameterEditorMediator(Parameters params, BatchParameterTree tree, JPanel infoPanel) {
    this.params = params;
    this.tree = tree;
    this.infoPanel = infoPanel;
    List<String> names = new ArrayList<String>();
    for (String name : params.getSchema().parameterNames()) {
      names.add(params.getDisplayName(name));
    }
    Collections.sort(names);
    // first display name, now need to get the
    // id for that.
    String displayName = names.get(0);
    for (String name : params.getSchema().parameterNames()) {
      if (displayName.equals(params.getDisplayName(name)))
        defaultParamName = name;
    }

    editPanel = new ParameterEditPanel();
    editPanel.init(this);
    infoPanel.add(editPanel, EDIT_INDEX);
    infoPanel.add(constPanel, CONSTANT_INDEX);
    infoPanel.add(paramPanel, PARAM_INDEX);

  }

  public Action getAddAction() {
    if (addAction == null)
      addAction = new AddParameterAction(null, this);
    return addAction;
  }

  public Action getDeleteAction() {
    if (delAction == null)
      delAction = new DeleteParameterAction(null, this);
    return delAction;
  }

  public String getDisplayNameFor(String paramName) {
    return params.getDisplayName(paramName);
  }

  /**
   * Gets the parameters.
   * 
   * @return the parameters.
   */
  public Parameters getParameters() {
    return params;
  }

  /**
   * Deletes the specified node and adds any of its children to the nodes
   * parent.
   * 
   * @param node
   *          the node to delete
   */
  public void deleteNode(AbstractBatchParameterNode node) {
    List<DefaultMutableTreeNode> children = new ArrayList<DefaultMutableTreeNode>();
    for (int i = 0; i < node.getChildCount(); i++) {
      children.add((DefaultMutableTreeNode) node.getChildAt(i));
    }
    node.removeAllChildren();
    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
    node.removeFromParent();
    for (DefaultMutableTreeNode child : children) {
      parent.add(child);
    }
    ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(parent);
    tree.selectNode(parent);
  }

  /**
   * Deletes the specified node and its children.
   * 
   * @param node
   *          the node to delete
   */
  public void deleteNodes(AbstractBatchParameterNode node) {
    node.removeAllChildren();
    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
    node.removeFromParent();
    ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(parent);
    tree.selectNode(parent);
  }

  /**
   * Adds a new constant to the specified parent node.
   * 
   * @param node
   */
  public void addConstant(AbstractBatchParameterNode parent) {
    if (selectedNode == null || editPanel.saveChanges()) {
      BatchParameterBean bean = new BatchParameterBean(true);
      addParameterNode(parent, bean, false);
    }
  }

  public void nodeChanged() {
    ((DefaultTreeModel) tree.getModel()).nodeChanged(selectedNode);
  }

  private void addParameterNode(AbstractBatchParameterNode parent, BatchParameterBean bean,
      boolean allowsChildren) {
    bean.setName(defaultParamName);
    bean.setType(params.getSchema().getDetails(defaultParamName).getType().getSimpleName());
    bean.setValue(params.getValueAsString(defaultParamName));
    BatchParameterNode node = new BatchParameterNode(bean, allowsChildren);
    parent.add(node);
    ((DefaultTreeModel) tree.getModel()).nodesWereInserted(parent, new int[] { parent
        .getChildCount() - 1 });
    isNew = true;
    tree.selectNode(node);
    selectedNode = node;
    isNew = false;
  }

  /**
   * Adds a new parameter to the specified parent.
   * 
   * @param parent
   *          the parent to add the node to
   */
  public void addParameter(AbstractBatchParameterNode parent) {
    if (parent.toString().equals("Constants"))
      addConstant(parent);
    else {
      if (selectedNode == null || editPanel.saveChanges()) {
        BatchParameterBean bean = new BatchParameterBean();
        addParameterNode(parent, bean, true);
      }
    }
  }

  public void valueChanged(TreeSelectionEvent e) {
    TreePath path = e.getPath();
    AbstractBatchParameterNode node = (AbstractBatchParameterNode) path.getLastPathComponent();
    addAction.setParent(node);
    delAction.setNode(node);
    if (!node.equals(selectedNode)) {
      if (selectedNode != null && !editPanel.saveChanges()) {
        tree.selectNode((MutableTreeNode) selectedNode);
      } else if (node instanceof BatchParameterNode) {
        ((CardLayout) infoPanel.getLayout()).show(infoPanel, EDIT_INDEX);
        selectedNode = (TreeNode) path.getLastPathComponent();
        BatchParameterBean bean = (BatchParameterBean) ((BatchParameterNode) path
            .getLastPathComponent()).getUserObject();
        editPanel.resetBean(bean, isNew);
        addAction.setEnabled(!bean.isConstant());
        delAction.setEnabled(true);
      } else {
        if (node.toString().equals("Constants")) {
          ((CardLayout) infoPanel.getLayout()).show(infoPanel, CONSTANT_INDEX);
        } else {
          ((CardLayout) infoPanel.getLayout()).show(infoPanel, PARAM_INDEX);
        }
        editPanel.setEnabled(false);
        selectedNode = null;
        addAction.setEnabled(true);
        delAction.setEnabled(false);
      }
    }
  }
}
