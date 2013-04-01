/**
 * 
 */
package repast.simphony.statecharts.sheets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * SWT VerifyListener that only allows doubles.
 * 
 * @author Nick Collier
 */
public class DoubleVerifier implements VerifyListener {

  @Override
  public void verifyText(VerifyEvent evt) {

    switch (evt.keyCode) {
    case SWT.BS:
    case SWT.DEL:
    case SWT.HOME:
    case SWT.END:
    case SWT.ARROW_LEFT:
    case SWT.ARROW_RIGHT:
      return;
    }

    try {
      Double.parseDouble(evt.text);
    } catch (NumberFormatException ex) {
      if (!evt.text.equals(".")) {
        evt.doit = false;
        return;
      }
    }

    String txt = ((Text) evt.widget).getText();
    if (!isReplace(evt, txt.length())) {
      boolean hasDec = txt.contains(".");
      if (hasDec && evt.text.contains(".")) {
        evt.doit = false;
        return;
      }
    }
  }

  private boolean isReplace(VerifyEvent evt, int textLength) {
    return (evt.start <= 0 && evt.end >= textLength);
  }
}
