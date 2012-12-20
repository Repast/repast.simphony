package repast.simphony.systemdynamics.sdmodel.diagram.providers;

import repast.simphony.systemdynamics.sdmodel.diagram.part.SystemdynamicsDiagramEditorPlugin;

/**
 * @generated
 */
public class ElementInitializers {

  protected ElementInitializers() {
    // use #getInstance to access cached instance
  }

  /**
   * @generated
   */
  public static ElementInitializers getInstance() {
    ElementInitializers cached = SystemdynamicsDiagramEditorPlugin.getInstance()
        .getElementInitializers();
    if (cached == null) {
      SystemdynamicsDiagramEditorPlugin.getInstance().setElementInitializers(
          cached = new ElementInitializers());
    }
    return cached;
  }
}
