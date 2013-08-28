/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.emf.EmfRegistryMetaModel;

import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

/**
 * Generates the dummy template class files that are used by
 * editors to edit code properties.
 * 
 * @author Nick Collier
 */
public class TemplateGenerator {

  private IPath srcPath = null;
  private String pkgPath = null;
  private XpandFacade facade;

  private void init() {
    Output output = new OutputImpl();
    Outlet outlet = new Outlet(srcPath.toPortableString());
    outlet.setOverwrite(true);
    output.addOutlet(outlet);

    Map<String, Variable> varsMap = new HashMap<String, Variable>();
    XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(output, null, varsMap, null,
        null);
    EmfRegistryMetaModel metamodel = new EmfRegistryMetaModel() {
      @Override
      protected EPackage[] allPackages() {
        return new EPackage[] { StatechartPackage.eINSTANCE, EcorePackage.eINSTANCE,
            NotationPackage.eINSTANCE };
      }
    };
    
    execCtx.registerMetaModel(metamodel);
    facade = XpandFacade.create(execCtx);
  }
  
  private void preRun(IProject project, EObject obj) {
    try {
      // this will only create the path if it doesn't already exist
      PathUtils.createSrcPath(project, CodeGeneratorConstants.SRC_GEN, new NullProgressMonitor());
    } catch (CoreException ex) {
      ex.printStackTrace();
    }
    StateMachine machine = GeneratorUtil.findStateMachine(obj);
    String pkg = machine.getPackage();
    String pkgPath = pkg.replaceAll("\\.", "/");

    // workspace relative
    
    if (this.pkgPath == null || !this.pkgPath.equals(pkgPath)) {
      this.pkgPath = pkgPath;
      if (srcPath == null) {
        srcPath = project.getLocation().append(CodeGeneratorConstants.SRC_GEN);
      }
      init();
    }
  }

  /**
   * Generates the template class code for the specified state.
   */
  public IPath run(IProject project, AbstractState state) {
    preRun(project, state);

    String templatePath = "src::action_template::Main";
    facade.evaluate(templatePath, state);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.STATE_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + state.getLanguage());
  }
  
  public IPath generateGuard(IProject project, Transition transition) {
    preRun(project, transition);
    String templatePath = "src::action_template::CreateGuard";
    facade.evaluate(templatePath, transition);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.GUARD_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + transition.getTriggerCodeLanguage());
  }
  
  public IPath generateTriggerCondition(IProject project, Transition transition) {
    preRun(project, transition);
    String templatePath = "src::action_template::CreateTriggerCondition";
    facade.evaluate(templatePath, transition);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.TRIGGER_COND_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + transition.getTriggerCodeLanguage());
  }
  
  public IPath generateTriggerDbl(IProject project, Transition transition) {
    preRun(project, transition);
    String templatePath = "src::action_template::CreateTriggerDouble";
    facade.evaluate(templatePath, transition);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.TRIGGER_DBL_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + transition.getTriggerCodeLanguage());
  }
  
  public IPath generateMessageEq(IProject project, Transition transition) {
    preRun(project, transition);
    String templatePath = "src::action_template::CreateMessageEq";
    facade.evaluate(templatePath, transition);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.TRIGGER_MESSAGE_EQ_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + transition.getTriggerCodeLanguage());
  }
  
  public IPath generateMessageCond(IProject project, Transition transition) {
    preRun(project, transition);
    String templatePath = "src::action_template::CreateMessageCond";
    facade.evaluate(templatePath, transition);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.TRIGGER_MESSAGE_COND_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + transition.getTriggerCodeLanguage());
  }
  
  public IPath generateOnTrans(IProject project, Transition transition) {
    preRun(project, transition);
    String templatePath = "src::action_template::CreateOnTrans";
    facade.evaluate(templatePath, transition);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.ON_TRANS_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + transition.getTriggerCodeLanguage());
  }
}
