package my.statechart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.junit.Test;

import repast.simphony.statecharts.generator.CodeGeneratorConstants;
import repast.simphony.statecharts.generator.TemplateGenerator;
import repast.simphony.statecharts.scmodel.AbstractState;
import repast.simphony.statecharts.scmodel.State;
import repast.simphony.statecharts.scmodel.StateMachine;
import repast.simphony.statecharts.scmodel.StatechartPackage;
import repast.simphony.statecharts.scmodel.Transition;

public class GeneratorTest {

  @Test
  public void stateActionTest() throws CoreException {

    try {

      IWorkspace workspace = ResourcesPlugin.getWorkspace();

      IProject project = workspace.getRoot().getProject("GeneratorTest");
      if (!project.exists()) {
        project.create(null);
        project.open(null);

        IFolder folder = project.getFolder(CodeGeneratorConstants.SRC_GEN);
        folder.create(false, true, null);
      }

      IPath path = new Path("./test_data/statechart.rsc");
      XMIResourceImpl resource = new XMIResourceImpl();
      resource.load(new FileInputStream(path.toFile()), new HashMap<Object, Object>());

      StateMachine machine = null;
      for (EObject obj : resource.getContents()) {
        if (obj.eClass().equals(StatechartPackage.Literals.STATE_MACHINE)) {
          machine = (StateMachine) obj;
          break;
        }
      }

      State state = null;
      for (AbstractState s : machine.getStates()) {
        if (s instanceof State) {
          state = (State) s;
          break;
        }
      }

      System.out.println(state);

      TemplateGenerator gen = new TemplateGenerator();
      gen.run(project, state);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void guardTest() throws CoreException {

    try {

      IWorkspace workspace = ResourcesPlugin.getWorkspace();

      IProject project = workspace.getRoot().getProject("GeneratorTest");
      if (!project.exists()) {
        project.create(null);
        project.open(null);

        IFolder folder = project.getFolder(CodeGeneratorConstants.SRC_GEN);
        folder.create(false, true, null);
      }

      IPath path = new Path("./test_data/statechart.rsc");
      XMIResourceImpl resource = new XMIResourceImpl();
      resource.load(new FileInputStream(path.toFile()), new HashMap<Object, Object>());

      StateMachine machine = null;
      for (EObject obj : resource.getContents()) {
        if (obj.eClass().equals(StatechartPackage.Literals.STATE_MACHINE)) {
          machine = (StateMachine) obj;
          break;
        }
      }

      Transition trans = machine.getTransitions().get(0);

      TemplateGenerator gen = new TemplateGenerator();
      gen.generateGuard(project, trans);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
