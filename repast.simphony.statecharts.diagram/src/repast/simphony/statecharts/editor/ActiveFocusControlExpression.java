package repast.simphony.statecharts.editor;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.ExpressionInfo;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISources;

public class ActiveFocusControlExpression extends Expression {

  private Control focusControl;

  public ActiveFocusControlExpression(Control control) {
    focusControl = control;
  }

  @Override
  public void collectExpressionInfo(ExpressionInfo info) {
    info.markDefaultVariableAccessed(); // give it a very high priority
    info.addVariableNameAccess(ISources.ACTIVE_SHELL_NAME);
    info.addVariableNameAccess(ISources.ACTIVE_WORKBENCH_WINDOW_NAME);
  }

  @Override
  public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
    if (Display.getCurrent() != null && focusControl.isFocusControl()) {
      return EvaluationResult.TRUE;
    }
    return EvaluationResult.FALSE;
  }

}
