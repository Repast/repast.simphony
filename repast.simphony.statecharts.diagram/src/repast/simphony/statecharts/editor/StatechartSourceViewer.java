/**
 * 
 */
package repast.simphony.statecharts.editor;

import org.eclipse.swt.custom.StyledText;

/**
 * Interface for classes that implement viewers
 * for statechart code properties. 
 * 
 * @author Nick Collier
 */
public interface StatechartSourceViewer {

  StyledText getTextWidget();

  void ignoreAutoIndent(boolean ignore);

}
