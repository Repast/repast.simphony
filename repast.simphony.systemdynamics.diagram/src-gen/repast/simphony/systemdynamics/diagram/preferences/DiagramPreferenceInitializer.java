package repast.simphony.systemdynamics.diagram.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramPreferenceInitializer extends AbstractPreferenceInitializer {

  /**
   * @generated NOT
   */
  public void initializeDefaultPreferences() {
    IPreferenceStore store = getPreferenceStore();
    DiagramGeneralPreferencePage.initDefaults(store);
    DiagramAppearancePreferencePage.initDefaults(store);
    DiagramConnectionsPreferencePage.initDefaults(store);
    DiagramPrintingPreferencePage.initDefaults(store);
    DiagramRulersAndGridPreferencePage.initDefaults(store);

    store.setDefault(IPreferenceConstants.PREF_DEFAULT_FONT,
        new FontData("", SWT.NORMAL, 12).toString());

  }

  /**
   * @generated
   */
  protected IPreferenceStore getPreferenceStore() {
    return SystemdynamicsDiagramEditorPlugin.getInstance().getPreferenceStore();
  }
}
