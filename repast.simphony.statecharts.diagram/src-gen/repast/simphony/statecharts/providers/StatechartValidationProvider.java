package repast.simphony.statecharts.providers;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.validation.AbstractModelConstraint;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.emf.validation.model.IClientSelector;
import org.eclipse.emf.validation.service.IBatchValidator;
import org.eclipse.emf.validation.service.ITraversalStrategy;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;

import repast.simphony.statecharts.edit.parts.CompositeState2EditPart;
import repast.simphony.statecharts.edit.parts.CompositeStateEditPart;
import repast.simphony.statecharts.edit.parts.PseudoState2EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState3EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState4EditPart;
import repast.simphony.statecharts.edit.parts.PseudoState5EditPart;
import repast.simphony.statecharts.edit.parts.PseudoStateEditPart;
import repast.simphony.statecharts.edit.parts.StateMachineEditPart;
import repast.simphony.statecharts.edit.parts.TransitionEditPart;
import repast.simphony.statecharts.generator.StateMachineValidator;
import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;
import repast.simphony.statecharts.part.StatechartVisualIDRegistry;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.PseudoState;
import repast.simphony.statecharts.scmodel.PseudoStateTypes;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;
import repast.simphony.statecharts.util.StatechartsModelUtil;
import repast.simphony.statecharts.validation.CompositeStateValidator;
import repast.simphony.statecharts.validation.TransitionValidator;
import repast.simphony.statecharts.validation.Validator;

/**
 * @generated
 */
public class StatechartValidationProvider {

  /**
   * @generated
   */
  private static boolean constraintsActive = false;

  /**
   * @generated
   */
  public static boolean shouldConstraintsBePrivate() {
    return false;
  }

  /**
   * @generated
   */
  public static void runWithConstraints(TransactionalEditingDomain editingDomain, Runnable operation) {
    final Runnable op = operation;
    Runnable task = new Runnable() {
      public void run() {
        try {
          constraintsActive = true;
          op.run();
        } finally {
          constraintsActive = false;
        }
      }
    };
    if (editingDomain != null) {
      try {
        editingDomain.runExclusive(task);
      } catch (Exception e) {
        StatechartDiagramEditorPlugin.getInstance().logError("Validation failed", e); //$NON-NLS-1$
      }
    } else {
      task.run();
    }
  }

  /**
   * @generated
   */
  static boolean isInDefaultEditorContext(Object object) {
    if (shouldConstraintsBePrivate() && !constraintsActive) {
      return false;
    }
    if (object instanceof View) {
      return constraintsActive
          && StateMachineEditPart.MODEL_ID.equals(StatechartVisualIDRegistry
              .getModelID((View) object));
    }
    return true;
  }

  /**
   * @generated
   */
  public static class DefaultCtx implements IClientSelector {

    /**
     * @generated
     */
    public boolean selects(Object object) {
      return isInDefaultEditorContext(object);
    }
  }

  /**
   * @generated
   */
  public static class Ctx_2006_3006 implements IClientSelector {

    /**
     * @generated
     */
    public boolean selects(Object object) {
      if (isInDefaultEditorContext(object) && object instanceof View) {
        final int id = StatechartVisualIDRegistry.getVisualID((View) object);
        boolean result = false;
        result = result || id == PseudoState2EditPart.VISUAL_ID;
        result = result || id == PseudoState4EditPart.VISUAL_ID;
        return result;
      }
      return false;
    }
  }

  /**
   * @generated
   */
  public static class Ctx_2004_3002 implements IClientSelector {

    /**
     * @generated
     */
    public boolean selects(Object object) {
      if (isInDefaultEditorContext(object) && object instanceof View) {
        final int id = StatechartVisualIDRegistry.getVisualID((View) object);
        boolean result = false;
        result = result || id == CompositeStateEditPart.VISUAL_ID;
        result = result || id == CompositeState2EditPart.VISUAL_ID;
        return result;
      }
      return false;
    }
  }

  /**
   * @generated
   */
  public static class Ctx_4001 implements IClientSelector {

    /**
     * @generated
     */
    public boolean selects(Object object) {
      if (isInDefaultEditorContext(object) && object instanceof View) {
        final int id = StatechartVisualIDRegistry.getVisualID((View) object);
        boolean result = false;
        result = result || id == TransitionEditPart.VISUAL_ID;
        return result;
      }
      return false;
    }
  }

  /**
   * @generated
   */
  public static class Ctx_2007 implements IClientSelector {

    /**
     * @generated
     */
    public boolean selects(Object object) {
      if (isInDefaultEditorContext(object) && object instanceof View) {
        final int id = StatechartVisualIDRegistry.getVisualID((View) object);
        boolean result = false;
        result = result || id == PseudoState5EditPart.VISUAL_ID;
        return result;
      }
      return false;
    }
  }

  /**
   * @generated
   */
  public static class Ctx_2005_3003 implements IClientSelector {

    /**
     * @generated
     */
    public boolean selects(Object object) {
      if (isInDefaultEditorContext(object) && object instanceof View) {
        final int id = StatechartVisualIDRegistry.getVisualID((View) object);
        boolean result = false;
        result = result || id == PseudoStateEditPart.VISUAL_ID;
        result = result || id == PseudoState3EditPart.VISUAL_ID;
        return result;
      }
      return false;
    }
  }

  /**
   * @generated
   */
  public static ITraversalStrategy getNotationTraversalStrategy(IBatchValidator validator) {
    return new CtxSwitchStrategy(validator);
  }

  /**
   * @generated
   */
  private static class CtxSwitchStrategy implements ITraversalStrategy {

    /**
     * @generated
     */
    private ITraversalStrategy defaultStrategy;

    /**
     * @generated
     */
    private int currentSemanticCtxId = -1;

    /**
     * @generated
     */
    private boolean ctxChanged = true;

    /**
     * @generated
     */
    private EObject currentTarget;

    /**
     * @generated
     */
    private EObject preFetchedNextTarget;

    /**
     * @generated
     */
    private final int[] contextSwitchingIdentifiers;

    /**
     * @generated
     */
    CtxSwitchStrategy(IBatchValidator validator) {
      this.defaultStrategy = validator.getDefaultTraversalStrategy();
      this.contextSwitchingIdentifiers = new int[] { PseudoState2EditPart.VISUAL_ID,
          PseudoState4EditPart.VISUAL_ID, CompositeStateEditPart.VISUAL_ID,
          CompositeState2EditPart.VISUAL_ID, TransitionEditPart.VISUAL_ID,
          PseudoState5EditPart.VISUAL_ID, PseudoStateEditPart.VISUAL_ID,
          PseudoState3EditPart.VISUAL_ID };
      Arrays.sort(this.contextSwitchingIdentifiers);
    }

    /**
     * @generated
     */
    public void elementValidated(EObject element, IStatus status) {
      defaultStrategy.elementValidated(element, status);
    }

    /**
     * @generated
     */
    public boolean hasNext() {
      return defaultStrategy.hasNext();
    }

    /**
     * @generated
     */
    public boolean isClientContextChanged() {
      if (preFetchedNextTarget == null) {
        preFetchedNextTarget = next();
        prepareNextClientContext(preFetchedNextTarget);
      }
      return ctxChanged;
    }

    /**
     * @generated
     */
    public EObject next() {
      EObject nextTarget = preFetchedNextTarget;
      if (nextTarget == null) {
        nextTarget = defaultStrategy.next();
      }
      this.preFetchedNextTarget = null;
      return this.currentTarget = nextTarget;
    }

    /**
     * @generated
     */
    public void startTraversal(Collection traversalRoots, IProgressMonitor monitor) {
      defaultStrategy.startTraversal(traversalRoots, monitor);
    }

    /**
     * @generated
     */
    private void prepareNextClientContext(EObject nextTarget) {
      if (nextTarget != null && currentTarget != null) {
        if (nextTarget instanceof View) {
          final int id = StatechartVisualIDRegistry.getVisualID((View) nextTarget);
          int nextSemanticId = (id != -1 && Arrays.binarySearch(contextSwitchingIdentifiers, id) >= 0) ? id
              : -1;
          if ((currentSemanticCtxId != -1 && currentSemanticCtxId != nextSemanticId)
              || (nextSemanticId != -1 && nextSemanticId != currentSemanticCtxId)) {
            this.ctxChanged = true;
          }
          currentSemanticCtxId = nextSemanticId;
        } else {
          // context of domain model
          this.ctxChanged = currentSemanticCtxId != -1;
          currentSemanticCtxId = -1;
        }
      } else {
        this.ctxChanged = false;
      }
    }
  }

  /**
   * @generated
   */
  public static class Adapter1 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      StateMachineValidator validator = new StateMachineValidator();
      StateMachine machine = (StateMachine) ctx.getTarget();
      IStatus status = validator.validateAgentType(machine);
      if (status.getSeverity() == IStatus.ERROR)
        return ctx.createFailureStatus("Error in Agent Type Property: " + status.getMessage());

      status = validator.validatePackage(machine);
      if (status.getSeverity() == IStatus.ERROR)
        return ctx.createFailureStatus("Error in Package Property: " + status.getMessage());

      status = validator.validateClassName(machine);
      if (status.getSeverity() == IStatus.ERROR)
        return ctx.createFailureStatus("Error in Class Name Property: " + status.getMessage());

      int count = 0;
      for (AbstractState state : machine.getStates()) {
        if (state.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE)
            && ((PseudoState) state).getType().equals(PseudoStateTypes.ENTRY)) {
          ++count;
        }
      }

      if (count > 1)
        return ctx.createFailureStatus("State Chart has multiple entry points");
      if (count == 0)
        return ctx.createFailureStatus("State Chart is missing required entry point marker");

      return ctx.createSuccessStatus();
    }
  }

  /**
   * @generated
   */
  public static class Adapter2 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      Node node = (Node) ctx.getTarget();
      EObject obj = node.getElement();
      if (obj.eClass().equals(StatechartPackage.Literals.PSEUDO_STATE)
          && ((PseudoState) obj).getType() == PseudoStateTypes.CHOICE) {

        int count = 0;
        for (Object edge : node.getSourceEdges()) {
          Transition t = (Transition) ((View) edge).getElement();
          if (t.isDefaultTransition())
            count++;
        }

        if (count != 1)
          return ctx.createFailureStatus("Choice must have a single default branch");
      }

      return ctx.createSuccessStatus();
    }
  }

  /**
   * @generated
   */
  public static class Adapter3 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      CompositeStateValidator validator = new CompositeStateValidator();
      return validator.checkForWarnings(ctx);
    }
  }

  /**
   * @generated
   */
  public static class Adapter4 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      TransitionValidator validator = new TransitionValidator();
      return validator.checkForErrors(ctx);
    }
  }

  /**
   * @generated
   */
  public static class Adapter5 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      CompositeStateValidator validator = new CompositeStateValidator();
      return validator.checkForErrors(ctx);
    }
  }

  /**
   * @generated
   */
  // AbstractState warnings.
  public static class Adapter6 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      AbstractState state = (AbstractState) ctx.getTarget();
      if (!state.eClass().equals(StatechartPackage.Literals.COMPOSITE_STATE)) {
        return Validator.validateIncoming(ctx, state);
      }
      return ctx.createSuccessStatus();
    }
  }

  /**
   * @generated
   */
  // AbstractState errors
  public static class Adapter7 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      AbstractState state = (AbstractState) ctx.getTarget();
      if (!state.eClass().equals(StatechartPackage.Literals.COMPOSITE_STATE)) {
        IStatus status = Validator.validateCode(ctx, state); 
        if (!status.isOK()) return status;
        
        return Validator.validateID(ctx, state);
      }
      return ctx.createSuccessStatus();
    }
  }

  /**
   * @generated
   */
  // Entry state missing outgoing transition, error
  public static class Adapter8 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      Node context = (Node) ctx.getTarget();
      if (StatechartsModelUtil.getOutgoing(context.getElement()).size() == 0) {
        return ctx.createFailureStatus(context);
      }
      return ctx.createSuccessStatus();
    }
  }

  /**
   * @generated
   */
  // initial state missing outgoing transition, warning
  public static class Adapter9 extends AbstractModelConstraint {

    /**
     * @generated NOT
     */
    public IStatus validate(IValidationContext ctx) {
      Node context = (Node) ctx.getTarget();
      if (StatechartsModelUtil.getOutgoing(context.getElement()).size() == 0) {
        return ctx.createFailureStatus(context);
      }
      return ctx.createSuccessStatus();
    }
  }

  /**
   * @generated
   */
  static String formatElement(EObject object) {
    return EMFCoreUtil.getQualifiedName(object, true);
  }

}
