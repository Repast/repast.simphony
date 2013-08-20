package my.statechart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.gmf.runtime.notation.NotationPackage;

import repast.simphony.statecharts.scmodel.StatechartPackage;

public class GeneratorTest {
  
  public static void main(String[] args) {
    XMIResourceImpl resource = new XMIResourceImpl();
    try {
      EPackage.Registry.INSTANCE.put(StatechartPackage.eNS_URI, StatechartPackage.eINSTANCE);
      EPackage.Registry.INSTANCE.put(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
      //Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION, StatechartR.Factory.INSTANCE);

      resource.load(new FileInputStream(new File("./test-data/composite_test.rsc")), new HashMap<Object, Object>());
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
