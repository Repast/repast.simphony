package repast.simphony.statecharts.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;

import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;

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

    store.setDefault(IPreferenceConstants.PREF_SHOW_GRID, true);
    store.setDefault(IPreferenceConstants.PREF_SNAP_TO_GRID, true);
    store.setDefault(IPreferenceConstants.PREF_SNAP_TO_GEOMETRY, true);

    //store.setDefault(WorkspaceViewerProperties.GRIDLINECOLOR, SWT.COLOR_BLACK);
    //store.setDefault(WorkspaceViewerProperties.GRIDORDER, false);
  }

  /**
   * @generated
   */
  protected IPreferenceStore getPreferenceStore() {
    return StatechartDiagramEditorPlugin.getInstance().getPreferenceStore();
  }
}
