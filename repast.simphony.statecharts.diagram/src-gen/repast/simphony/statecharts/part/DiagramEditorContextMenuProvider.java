package repast.simphony.statecharts.part;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.providers.DiagramContextMenuProvider;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @generated
 */
public class DiagramEditorContextMenuProvider extends DiagramContextMenuProvider {

  /**
   * @generated
   */
  private IWorkbenchPart part;

  /**
   * @generated
   */
  private DeleteElementAction deleteAction;

  /**
   * @generated NOT
   */
  public DiagramEditorContextMenuProvider(IWorkbenchPart part, EditPartViewer viewer) {
    super(part, viewer);
    this.part = part;
    deleteAction = new DeleteElementAction(part);
    deleteAction.init();
    
    // exclude unwanted items from the context menu
    @SuppressWarnings("unchecked")
    Set<String> exclusion = super.getExclusionSet();
    exclusion.add("org.eclipse.gmf.ecore.editor.LoadResourceAction");
    exclusion.add("repast.simphony.statecharts.diagram.LoadResourceAction");
    exclusion.add("groovyfile");
    exclusion.add("groovyresource");
    
    super.setExclusionSet(exclusion);
  }

  /**
   * @generated
   */
  public void dispose() {
    if (deleteAction != null) {
      deleteAction.dispose();
      deleteAction = null;
    }
    super.dispose();
  }

  /**
   * @generated
   */
  public void buildContextMenu(final IMenuManager menu) {

    getViewer().flush();
    try {
      TransactionUtil.getEditingDomain((EObject) getViewer().getContents().getModel())
          .runExclusive(new Runnable() {

            public void run() {
              ContributionItemService.getInstance().contributeToPopupMenu(
                  DiagramEditorContextMenuProvider.this, part);
              menu.remove(ActionIds.ACTION_DELETE_FROM_MODEL);
              menu.appendToGroup("editGroup", deleteAction);
              menu.remove("properties");
            }
          });
    } catch (Exception e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Error building context menu", e);
    }
  }
  
  
}
