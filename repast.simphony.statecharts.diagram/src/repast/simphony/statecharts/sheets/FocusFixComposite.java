/**
 * 
 */
package repast.simphony.statecharts.sheets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Extends Composite to fix some focus issues. In particular, if nothing has
 * focus in the composite and a text control is clicked on then focus
 * moves to the first control and never goes back to the control
 * that was clicked on. 
 * 
 * @author Nick Collier
 */
public class FocusFixComposite extends Composite {
  
  protected List<Control> focusableControls = new ArrayList<Control>();

  /**
   * @param parent
   * @param style
   */
  public FocusFixComposite(Composite parent, int style) {
    super(parent, style);
  }
  
  /**
   * Overrides setFocus to keep focus on control that current has focus,
   * if any, rather than setting focus on the first control.
   */
  public boolean setFocus() {
    for (Control control : focusableControls) {
      if (control.isFocusControl()) return true;
    }
    return super.setFocus();
  }
}
