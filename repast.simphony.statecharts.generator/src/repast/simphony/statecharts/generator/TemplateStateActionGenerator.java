/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
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

/**
 * Generates the dummy template class file that is used by the editor of state
 * action properties.
 * 
 * @author Nick Collier
 */
public class TemplateStateActionGenerator {

  private IPath srcPath = null;
  private String pkgPath = null;
  private XpandFacade facade;

  private void init(AbstractState state) {
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

  /**
   * Generates the template class code for the specified state.
   */
  public IPath run(IProject project, AbstractState state) {
    StateMachine machine = GeneratorUtil.findStateMachine(state);
    String pkg = machine.getPackage();
    String pkgPath = pkg.replaceAll("\\.", "/");

    // workspace relative
    
    if (this.pkgPath == null || !this.pkgPath.equals(pkgPath)) {
      this.pkgPath = pkgPath;
      if (srcPath == null) {
        srcPath = project.getLocation().append(CodeGeneratorConstants.SRC_GEN);
      }
      init(state);
    }

    String templatePath = "src::action_template::Main";
    facade.evaluate(templatePath, state);
    // return the project relative path
    return new Path(CodeGeneratorConstants.SRC_GEN).append(pkgPath).
        append(CodeGeneratorConstants.STATE_ACTION_NAME + GeneratorUtil.getLastCounter() + "." + state.getLanguage());
  }
}
