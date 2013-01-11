package repast.simphony.systemdynamics.diagram.providers;

import org.eclipse.emf.ecore.util.EcoreUtil;

import repast.simphony.systemdynamics.diagram.expressions.SystemdynamicsAbstractExpression;
import repast.simphony.systemdynamics.diagram.expressions.SystemdynamicsOCLFactory;
import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;
import repast.simphony.systemdynamics.sdmodel.Rate;
import repast.simphony.systemdynamics.sdmodel.SDModelPackage;
import repast.simphony.systemdynamics.sdmodel.Stock;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;

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
  public void init_Variable_2001(Variable instance) {
    try {
      Object value_0 = uuid_Variable_2001(instance);
      instance.setUuid((String) value_0);
      Object value_1 = SystemdynamicsOCLFactory.getExpression(1,
          SDModelPackage.eINSTANCE.getVariable(), null).evaluate(instance);

      value_1 = SystemdynamicsAbstractExpression.performCast(value_1,
          SDModelPackage.eINSTANCE.getVariableType());
      instance.setType((VariableType) value_1);
    } catch (RuntimeException e) {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_Stock_2003(Stock instance) {
    try {
      Object value_0 = uuid_Stock_2003(instance);
      instance.setUuid((String) value_0);
      Object value_1 = SystemdynamicsOCLFactory.getExpression(3,
          SDModelPackage.eINSTANCE.getStock(), null).evaluate(instance);

      value_1 = SystemdynamicsAbstractExpression.performCast(value_1,
          SDModelPackage.eINSTANCE.getVariableType());
      instance.setType((VariableType) value_1);
    } catch (RuntimeException e) {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_Variable_2004(Variable instance) {
    try {
      Object value_0 = uuid_Variable_2004(instance);
      instance.setUuid((String) value_0);
      Object value_1 = SystemdynamicsOCLFactory.getExpression(5,
          SDModelPackage.eINSTANCE.getVariable(), null).evaluate(instance);

      value_1 = SystemdynamicsAbstractExpression.performCast(value_1,
          SDModelPackage.eINSTANCE.getVariableType());
      instance.setType((VariableType) value_1);
    } catch (RuntimeException e) {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated
   */
  public void init_Rate_4003(Rate instance) {
    try {
      Object value_0 = uuid_Rate_4003(instance);
      instance.setUuid((String) value_0);
      Object value_1 = SystemdynamicsOCLFactory.getExpression(6,
          SDModelPackage.eINSTANCE.getRate(), null).evaluate(instance);

      value_1 = SystemdynamicsAbstractExpression.performCast(value_1,
          SDModelPackage.eINSTANCE.getVariableType());
      instance.setType((VariableType) value_1);
    } catch (RuntimeException e) {
      SystemdynamicsDiagramEditorPlugin.getInstance().logError("Element initialization failed", e); //$NON-NLS-1$						
    }
  }

  /**
   * @generated NOT
   */
  private String uuid_Variable_2001(Variable self) {
    return EcoreUtil.generateUUID();
  }

  /**
   * @generated NOT
   */
  private String uuid_Stock_2003(Stock self) {
    return EcoreUtil.generateUUID();
  }

  /**
   * @generated NOT
   */
  private String uuid_Variable_2004(Variable self) {
    return EcoreUtil.generateUUID();
  }

  /**
   * @generated NOT
   */
  private String uuid_Rate_4003(Rate self) {
    return EcoreUtil.generateUUID();
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
