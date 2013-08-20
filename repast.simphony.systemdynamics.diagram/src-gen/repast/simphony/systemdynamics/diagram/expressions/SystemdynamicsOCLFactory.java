package repast.simphony.systemdynamics.diagram.expressions;

import java.util.Collections;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.ocl.Environment;
import org.eclipse.ocl.EvaluationEnvironment;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.EcoreFactory;
import org.eclipse.ocl.ecore.OCLExpression;
import org.eclipse.ocl.ecore.Variable;
import org.eclipse.ocl.ecore.OCL.Helper;
import org.eclipse.ocl.options.ParsingOptions;

import repast.simphony.systemdynamics.diagram.part.SystemdynamicsDiagramEditorPlugin;

/**
 * @generated
 */
public class SystemdynamicsOCLFactory {

  /**
   * @generated
   */
  private final SystemdynamicsAbstractExpression[] expressions;

  /**
   * @generated
   */
  private final String[] expressionBodies;

  /**
   * @generated
   */
  protected SystemdynamicsOCLFactory() {
    this.expressions = new SystemdynamicsAbstractExpression[13];
    this.expressionBodies = new String[] {
        "not self.oclIsKindOf(Stock) and not self.oclIsKindOf(Rate) and self.type = VariableType::auxiliary", //$NON-NLS-1$
        "VariableType::auxiliary", //$NON-NLS-1$
        "not self.oclIsKindOf(Cloud) and self.type = VariableType::stock", //$NON-NLS-1$
        "VariableType::stock", //$NON-NLS-1$
        "self.type = VariableType::constant", //$NON-NLS-1$
        "VariableType::constant", //$NON-NLS-1$
        "self.type = VariableType::lookup", //$NON-NLS-1$
        "VariableType::lookup", //$NON-NLS-1$
        "VariableType::rate", //$NON-NLS-1$
        "self.oclIsKindOf(Cloud) or self.oclIsKindOf(Stock)", //$NON-NLS-1$
        "self <> oppositeEnd and (self.oclIsKindOf(Stock) or (self.oclIsKindOf(Cloud) and not oppositeEnd.oclIsKindOf(Cloud)))", //$NON-NLS-1$
        "not self.oclIsKindOf(Cloud)", //$NON-NLS-1$
        "self <> oppositeEnd and not self.oclIsKindOf(Cloud) and self.type <> VariableType::lookup", //$NON-NLS-1$
    };
  }

  /**
   * @generated
   */
  private static SystemdynamicsOCLFactory getInstance() {
    SystemdynamicsOCLFactory instance = SystemdynamicsDiagramEditorPlugin.getInstance()
        .getSystemdynamicsOCLFactory();
    if (instance == null) {
      SystemdynamicsDiagramEditorPlugin.getInstance().setSystemdynamicsOCLFactory(
          instance = new SystemdynamicsOCLFactory());
    }
    return instance;
  }

  /**
   * @generated
   */
  public static String getExpressionBody(int index) {
    return getInstance().expressionBodies[index];
  }

  /**
   * @generated
   */
  public static SystemdynamicsAbstractExpression getExpression(int index, EClassifier context,
      Map<String, EClassifier> environment) {
    SystemdynamicsOCLFactory cached = getInstance();
    if (index < 0 || index >= cached.expressions.length) {
      throw new IllegalArgumentException();
    }
    if (cached.expressions[index] == null) {
      cached.expressions[index] = getExpression(cached.expressionBodies[index], context,
          environment == null ? Collections.<String, EClassifier> emptyMap() : environment);
    }
    return cached.expressions[index];
  }

  /**
   * This is factory method, callers are responsible to keep reference to the return value if they want to reuse parsed expression
   * @generated
   */
  public static SystemdynamicsAbstractExpression getExpression(String body, EClassifier context,
      Map<String, EClassifier> environment) {
    return new Expression(body, context, environment);
  }

  /**
   * This method will become private in the next release
   * @generated
   */
  public static SystemdynamicsAbstractExpression getExpression(String body, EClassifier context) {
    return getExpression(body, context, Collections.<String, EClassifier> emptyMap());
  }

  /**
   * @generated
   */
  private static class Expression extends SystemdynamicsAbstractExpression {

    /**
     * @generated
     */
    private final org.eclipse.ocl.ecore.OCL oclInstance;

    /**
     * @generated
     */
    private OCLExpression oclExpression;

    /**
     * @generated
     */
    public Expression(String body, EClassifier context, Map<String, EClassifier> environment) {
      super(body, context);
      oclInstance = org.eclipse.ocl.ecore.OCL.newInstance();
      initCustomEnv(oclInstance.getEnvironment(), environment);
      Helper oclHelper = oclInstance.createOCLHelper();
      oclHelper.setContext(context());
      try {
        oclExpression = oclHelper.createQuery(body());
        setStatus(IStatus.OK, null, null);
      } catch (ParserException e) {
        setStatus(IStatus.ERROR, e.getMessage(), e);
      }
    }

    /**
     * @generated
     */
    @SuppressWarnings("rawtypes")
    protected Object doEvaluate(Object context, Map env) {
      if (oclExpression == null) {
        return null;
      }
      // on the first call, both evalEnvironment and extentMap are clear, for later we have finally, below.
      EvaluationEnvironment<?, ?, ?, ?, ?> evalEnv = oclInstance.getEvaluationEnvironment();
      // initialize environment
      for (Object nextKey : env.keySet()) {
        evalEnv.replace((String) nextKey, env.get(nextKey));
      }
      try {
        Object result = oclInstance.evaluate(context, oclExpression);
        return oclInstance.isInvalid(result) ? null : result;
      } finally {
        evalEnv.clear();
        oclInstance.setExtentMap(null); // clear allInstances cache, and get the oclInstance ready for the next call
      }
    }

    /**
     * @generated
     */
    private static void initCustomEnv(
        Environment<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, ?, ?, ?> ecoreEnv,
        Map<String, EClassifier> environment) {
      // Use EObject as implicit root class for any object, to allow eContainer() and other EObject operations from OCL expressions
      ParsingOptions.setOption(ecoreEnv, ParsingOptions.implicitRootClass(ecoreEnv),
          EcorePackage.eINSTANCE.getEObject());
      for (String varName : environment.keySet()) {
        EClassifier varType = environment.get(varName);
        ecoreEnv.addElement(varName, createVar(ecoreEnv, varName, varType), false);
      }
    }

    /**
     * @generated
     */
    private static Variable createVar(
        Environment<?, EClassifier, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> ecoreEnv, String name,
        EClassifier type) {
      Variable var = EcoreFactory.eINSTANCE.createVariable();
      var.setName(name);
      var.setType(ecoreEnv.getUMLReflection().getOCLType(type));
      return var;
    }
  }
}
