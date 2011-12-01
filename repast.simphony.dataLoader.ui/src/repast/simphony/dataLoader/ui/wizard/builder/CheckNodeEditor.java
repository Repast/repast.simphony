package repast.simphony.dataLoader.ui.wizard.builder;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;

/**
 * 
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:24:11 $
 * @author Nick Collier
 */
public class CheckNodeEditor extends DefaultTreeCellEditor {

  public CheckNodeEditor(JTree tree, DefaultTreeCellRenderer renderer, TreeCellEditor editor) {
    super(tree, renderer, editor);
  }

  public CheckNodeEditor(JTree tree, DefaultTreeCellRenderer renderer) {
    super(tree, renderer);
  }
}

