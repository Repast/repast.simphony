package repast.simphony.statecharts.editor;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;

/**
 * UpdatevalueStrategy that turns off the auto indenting on the specified viewer
 * when the text is set on the widget. This prevents the incorrect looking auto-indenting
 * that occurs when a StyledText widget is set with the text from the model.
 * 
 * @author Nick Collier
 */
public class CodeUpdateStrategy extends UpdateValueStrategy {
  
  private JavaSourceViewer viewer;
  
  public CodeUpdateStrategy(JavaSourceViewer viewer) {
    this.viewer = viewer;
  }

  @Override
  protected IStatus doSet(IObservableValue observableValue, Object value) {
    viewer.ignoreAutoIndent(true);
    IStatus status = super.doSet(observableValue, value);
    viewer.ignoreAutoIndent(false);
    return status;
  }
}