package repast.simphony.statecharts.handlers;

import org.eclipse.ui.IPageLayout;

import repast.simphony.eclipse.IRepastPerspectiveConfigurator;

/**
 * Configures the Repast perspective with statechart wizard.
 * 
 * @author Nick Collier
 */
public class StatechartPerspectiveConfigurator implements IRepastPerspectiveConfigurator {

  /* (non-Javadoc)
   * @see repast.simphony.eclipse.IRepastPerspectiveConfigurator#configurePerspective(org.eclipse.ui.IPageLayout)
   */
  @Override
  public void configurePerspective(IPageLayout layout) {
    layout.addNewWizardShortcut("repast.simphony.statecharts.part.StatechartCreationWizardID");
  }
}
