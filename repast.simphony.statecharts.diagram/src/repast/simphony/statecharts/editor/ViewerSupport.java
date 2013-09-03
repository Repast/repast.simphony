/**
 * 
 */
package repast.simphony.statecharts.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

/**
 * Activates the handlers for the cut, paste, code completion
 * actions. 
 * 
 * @author Nick Collier from blog post by David Green:
 * http://greensopinion.blogspot.com/2009/10/key-bindings-in-eclipse-editors.html
 */
public class ViewerSupport implements FocusListener, DisposeListener {
  
  private final TextViewer textViewer;
  private List<IHandlerActivation> handlerActivations = new ArrayList<>();
  private IHandlerService handlerService;

  public ViewerSupport(TextViewer textViewer, IHandlerService service) {
    this.textViewer = textViewer;
    StyledText textWidget = textViewer.getTextWidget();
    textWidget.addFocusListener(this);
    textWidget.addDisposeListener(this);
    this.handlerService = service;

    if (textViewer.getTextWidget().isFocusControl()) {
      activateContext();
    }
  }

  public void focusLost(FocusEvent e) {
    deactivateContext();
  }

  public void focusGained(FocusEvent e) {
    activateContext();
  }

  public void widgetDisposed(DisposeEvent e) {
    deactivateContext();
  }

  protected void activateContext() {
    if (handlerActivations.isEmpty()) {
      activateHandler(ISourceViewer.QUICK_ASSIST, ITextEditorActionDefinitionIds.QUICK_ASSIST);
      activateHandler(ISourceViewer.CONTENTASSIST_PROPOSALS,
          ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
      activateHandler(ITextOperationTarget.CUT, IWorkbenchCommandConstants.EDIT_CUT);
      activateHandler(ITextOperationTarget.COPY,IWorkbenchCommandConstants.EDIT_COPY);
      activateHandler(ITextOperationTarget.PASTE, IWorkbenchCommandConstants.EDIT_PASTE);
      activateHandler(ITextOperationTarget.DELETE, IWorkbenchCommandConstants.EDIT_DELETE);
      activateHandler(ITextOperationTarget.UNDO, IWorkbenchCommandConstants.EDIT_UNDO);
      activateHandler(ITextOperationTarget.REDO, IWorkbenchCommandConstants.EDIT_REDO);
    }
  }

  protected void activateHandler(int operation, String actionDefinitionId) {
    StyledText textWidget = textViewer.getTextWidget();
    IHandler actionHandler = createActionHandler(operation, actionDefinitionId);
    IHandlerActivation handlerActivation = handlerService.activateHandler(actionDefinitionId,
        actionHandler, new ActiveFocusControlExpression(textWidget));

    handlerActivations.add(handlerActivation);
  }

  private IHandler createActionHandler(final int operation, String actionDefinitionId) {
    Action action = new Action() {
      @Override
      public void run() {
        if (textViewer.canDoOperation(operation)) {
          textViewer.doOperation(operation);
        }
      }
    };
    action.setActionDefinitionId(actionDefinitionId);
    return new ActionHandler(action);
  }

  protected void deactivateContext() {
    if (!handlerActivations.isEmpty()) {
      for (IHandlerActivation activation : handlerActivations) {
        handlerService.deactivateHandler(activation);
        activation.getHandler().dispose();
      }
      handlerActivations.clear();
    }
  }

}
