/*CopyrightHere*/
package repast.simphony.ui;

import saf.core.ui.GUIBarManager;
import saf.core.ui.IAppConfigurator;
import saf.core.ui.ISAFDisplay;
import saf.core.ui.IWindowCustomizer;
import saf.core.ui.dock.DockingManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author Nick Collier
 */
public class RSAppConfigurator implements IAppConfigurator {

  protected RSApplication application;

  protected File defaultScenario;

  public RSAppConfigurator(RSApplication application, File defaultScenario) {
    this.application = application;
    this.defaultScenario = defaultScenario;
  }

  public boolean preWindowOpen(IWindowCustomizer customizer) {
    customizer.useStoredFrameBounds(800, 600);
    customizer.setWindowMenuLabel("View");
    customizer.setTitle("Repast Simphony");
    application.setupApplication();
    return true;
  }

  public void postWindowOpen(ISAFDisplay isafDisplay) {
    application.getGui().initFrame(isafDisplay.getFrame());
    if (defaultScenario != null)
      application.open(defaultScenario);
  }

  public void postWindowClose() {

  }

  public void createLayout(DockingManager viewManager) {
    application.createLayout(viewManager);
  }

  public void fillBars(GUIBarManager barManager) {
    fillToolBar(barManager);
    application.initGui();
  }

  private void fillToolBar(GUIBarManager configurator) {
    configurator.addToolBarComponent(RSGUIConstants.TICK_GROUP, "", Box.createHorizontalGlue());
    configurator.addToolBarComponent(RSGUIConstants.TICK_GROUP,
            RSGUIConstants.TICK_COUNT_LABEL, new JLabel("Tick Count: 0.0"));
    configurator.addToolBarComponent(RSGUIConstants.TICK_GROUP, "", Box
            .createRigidArea(new Dimension(10, 0)));
  }


  /**
   * @return true if the window can continue to close, false to veto the window close.
   */
  public boolean preWindowClose() {
    return application.close();
  }
}
