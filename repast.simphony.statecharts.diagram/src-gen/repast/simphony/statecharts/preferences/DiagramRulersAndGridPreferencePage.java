package repast.simphony.statecharts.preferences;

import org.eclipse.gmf.runtime.diagram.ui.preferences.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.preferences.RulerGridPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;

import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;

/**
 * @generated
 */
public class DiagramRulersAndGridPreferencePage extends RulerGridPreferencePage {

  /**
   * @generated NOT
   */
  public DiagramRulersAndGridPreferencePage() {
    setPreferenceStore(StatechartDiagramEditorPlugin.getInstance().getPreferenceStore());
    IPreferenceStore store = getPreferenceStore();
    if (store.getDefaultDouble(IPreferenceConstants.PREF_GRID_SPACING) == 0) {
      store.setDefault(IPreferenceConstants.PREF_GRID_SPACING, 0.125);
    }
  }
}
