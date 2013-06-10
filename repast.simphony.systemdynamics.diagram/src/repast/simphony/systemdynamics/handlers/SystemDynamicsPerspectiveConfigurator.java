package repast.simphony.systemdynamics.handlers;

import org.eclipse.ui.IPageLayout;

import repast.simphony.eclipse.IRepastPerspectiveConfigurator;

/**
 * Configures the Repast perspective with system dynamics wizard.
 * 
 * @author Nick Collier
 * @author jozik
 */
public class SystemDynamicsPerspectiveConfigurator implements IRepastPerspectiveConfigurator {

  /* (non-Javadoc)
   * @see repast.simphony.eclipse.IRepastPerspectiveConfigurator#configurePerspective(org.eclipse.ui.IPageLayout)
   */
  @Override
  public void configurePerspective(IPageLayout layout) {
    layout.addNewWizardShortcut("repast.simphony.systemdynamics.diagram.part.SystemdynamicsCreationWizardID");
  }
}
