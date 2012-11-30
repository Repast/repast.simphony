package repast.simphony.statecharts.providers;

import repast.simphony.statecharts.expressions.StatechartAbstractExpression;
import repast.simphony.statecharts.expressions.StatechartOCLFactory;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.scmodel.History;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.StatechartPackage;

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
  public void init_PseudoState_2005(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(2,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      instance.setId("Initial State Pointer");
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_2006(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(4,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      instance.setId("choice");
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_2007(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(8,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      instance.setId("Entry State Pointer");
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_3003(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(2,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      instance.setId("Initial State Pointer");
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_PseudoState_3006(PseudoState instance) {
    try {
      Object value_0 = StatechartOCLFactory.getExpression(4,
          StatechartPackage.eINSTANCE.getPseudoState(), null).evaluate(instance);

      value_0 = StatechartAbstractExpression.performCast(value_0,
          StatechartPackage.eINSTANCE.getPseudoStateTypes());
      instance.setType((PseudoStateTypes) value_0);
      instance.setId("choice");
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_History_3008(History instance) {
    try {
      instance.setShallow(true);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_History_3009(History instance) {
    try {
      instance.setShallow(false);
    } catch (RuntimeException e) {
      StatechartDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public static ElementInitializers getInstance() {
    ElementInitializers cached = StatechartDiagramEditorPlugin.getInstance()
        .getElementInitializers();
    if (cached == null) {
      StatechartDiagramEditorPlugin.getInstance().setElementInitializers(
          cached = new ElementInitializers());
    }
    return cached;
  }
}
