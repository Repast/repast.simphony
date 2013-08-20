package repast.simphony.systemdynamics.sheets;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Text;

/**
 * Applies a fix for https://bugs.eclipse.org/bugs/show_bug.cgi?id=383750. This 
 * bug occurs under OSX. The bug sets the text in a text control to that of the
 * first text control in the composite when this second text control gains focus.
 * 
 * The fix is to add a FocusListener to any effected text fields. This focus listener
 * will set the text to the original text when the field gets focused.
 * 
 * @author Nick Collier
 */
public class Bug383650Fix {
  
  private static boolean IS_OSX = System.getProperty("os.name").toLowerCase().contains("mac");
  
  /**
   * Applies the fix the the specified control.
   * 
   * @param control
   */
  public static void applyFix(Text control) {
    if (IS_OSX) 
      control.addFocusListener(new UpdateTextFocusListener(control));
  }
  
  private static class UpdateTextFocusListener implements FocusListener {
    
    private Text control;
    private String txt;
    
    public UpdateTextFocusListener(Text text) {
      this.control = text;
    }
    
    @Override
    public void focusGained(FocusEvent e) {
      if (txt == null) control.setText(control.getText());
      else control.setText(txt);
    }

    @Override
    public void focusLost(FocusEvent e) {
      txt = control.getText();
    }
  }
}
