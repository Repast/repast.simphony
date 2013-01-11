/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.xpand2.XpandExecutionContextImpl;
import org.eclipse.xpand2.XpandFacade;
import org.eclipse.xpand2.output.Outlet;
import org.eclipse.xpand2.output.Output;
import org.eclipse.xpand2.output.OutputImpl;
import org.eclipse.xtend.expression.Variable;
import org.eclipse.xtend.typesystem.emf.EmfRegistryMetaModel;

import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;

/**
 * Generates java / groovy code from the statechart diagram. 
 * 
 * @author Nick Collier
 */
public class CodeGenerator {
  
  private static final String SRC_GEN = "src-gen";
  
  /**
   * Generates the code in the specified project from a diagram in the specified
   * path. The code will be generated in a src-gen directory in the specified project.
   * If src-gen is not on the project's classpath it will be added.
   * 
   * @param project the project to generate the code into
   * @param path the path to the diagram file
   * @param monitor
   * @throws CoreException 
   */
  public IPath run(IProject project, IPath path, IProgressMonitor monitor) throws CoreException {
    try {
      XMIResourceImpl resource = new XMIResourceImpl();
      resource.load(new FileInputStream(path.toFile()), new HashMap<Object, Object>());
      
      StateMachine statemachine = null;
      for (EObject obj : resource.getContents()) {
        if (obj.eClass().equals(StatechartPackage.Literals.STATE_MACHINE)) {
          statemachine = (StateMachine)obj;
          break;
        }
      }
      
      // don't continue the code generation when there machine is missing
      // properties that will cause the generation to fail badly (e.g. there is
      // no class name so we can't construct a file name to write the code to).
      if (new StateMachineValidator().validate(statemachine).getSeverity() == IStatus.ERROR) return null;
      
      IPath srcPath = addSrcPath(project, statemachine.getUuid(), monitor);
      IPath projectLocation = project.getLocation();
      srcPath = projectLocation.append(srcPath.lastSegment());
      
      Output output = new OutputImpl();
      Outlet outlet = new Outlet(srcPath.toPortableString());
      outlet.setOverwrite(true);
      outlet.addPostprocessor(new CodeBeautifier());
      output.addOutlet(outlet);
      
      Map<String, Variable> varsMap = new HashMap<String, Variable>();
      XpandExecutionContextImpl execCtx = new XpandExecutionContextImpl(output, null, varsMap, null, null);
      EmfRegistryMetaModel metamodel = new EmfRegistryMetaModel() {
          @Override
          protected EPackage[] allPackages() {
              return new EPackage[] { StatechartPackage.eINSTANCE,
                  EcorePackage.eINSTANCE,
                  NotationPackage.eINSTANCE};
          }
      };
      execCtx.registerMetaModel(metamodel);
      
      // generate
      XpandFacade facade = XpandFacade.create(execCtx);
      String templatePath = "src::generator::Main";
      facade.evaluate(templatePath, statemachine);
      
      return srcPath;
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

  }
  
  private IPath addSrcPath(IProject project, String uuid, IProgressMonitor monitor) throws CoreException {
    IJavaProject javaProject = JavaCore.create(project);
    
    // workspace relative
    IPath srcPath = javaProject.getPath().append(SRC_GEN + "/");
    // project relative
    IFolder folder = project.getFolder(SRC_GEN);
 
    if (!folder.exists()) {
      // creates within the project
      folder.create(true, true, monitor);
      IClasspathEntry[] entries = javaProject.getRawClasspath();
      boolean found = false;
      for (IClasspathEntry entry : entries) {
        if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE && entry.getPath().equals(srcPath)) {
          found = true;
          break;
        }
      }

      if (!found) {
        IClasspathEntry[] newEntries = new IClasspathEntry[entries.length + 1];
        System.arraycopy(entries, 0, newEntries, 0, entries.length);
        IClasspathEntry srcEntry = JavaCore.newSourceEntry(srcPath, null);
        newEntries[entries.length] = srcEntry;
        javaProject.setRawClasspath(newEntries, null);
      }

    } else {
      DirectoryCleaner cleaner = new DirectoryCleaner();
      //System.out.println("running cleaner on: " + project.getLocation().append(srcPath.lastSegment()).append(pkg.replace(".", "/")).toPortableString());
      cleaner.run(project.getLocation().append(srcPath.lastSegment()).toPortableString(), uuid);
    }
    
    return srcPath;
  }
}
