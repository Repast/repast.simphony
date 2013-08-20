package repast.simphony.systemdynamics.sheet;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import repast.simphony.systemdynamics.sdmodel.InfluenceLink;
import repast.simphony.systemdynamics.sdmodel.SDModelFactory;
import repast.simphony.systemdynamics.sdmodel.SystemModel;
import repast.simphony.systemdynamics.sdmodel.Variable;
import repast.simphony.systemdynamics.sdmodel.VariableType;
import repast.simphony.systemdynamics.sheets.VariableSheet;

public class EquationEditorTest {

  public void run(Composite parent) {
    FormToolkit ft = new FormToolkit(parent.getDisplay());
    VariableSheet sheet = new VariableSheet(ft, parent);

    SystemModel model = SDModelFactory.eINSTANCE.createSystemModel();
    Variable var = SDModelFactory.eINSTANCE.createVariable();
    var.setName("Population");
    var.setType(VariableType.RATE);
    model.getVariables().add(var);

    Variable var2 = SDModelFactory.eINSTANCE.createVariable();
    var2.setName("births");
    model.getVariables().add(var2);

    Variable var3 = SDModelFactory.eINSTANCE.createVariable();
    var3.setName("deaths");
    model.getVariables().add(var3);

    InfluenceLink link = SDModelFactory.eINSTANCE.createInfluenceLink();
    link.setFrom(var2);
    link.setTo(var);
    model.getLinks().add(link);

    link = SDModelFactory.eINSTANCE.createInfluenceLink();
    link.setFrom(var3);
    link.setTo(var);
    model.getLinks().add(link);

    sheet.bindModel(new EMFDataBindingContext(), var);
  }

  public static void main(String[] args) {
    final Display d = new Display();
    final Shell s = new Shell(d);

    Realm.runWithDefault(SWTObservables.getRealm(d), new Runnable() {
      public void run() {
        s.setLayout(new GridLayout(1, true));
        GridDataFactory.fillDefaults().grab(true, true).applyTo(s);
        
        new EquationEditorTest().run(s);
        s.pack();
        s.open();
        while (!s.isDisposed()) {
          if (!d.readAndDispatch()) {
            d.sleep();
          }
        }
      }
    });
    
    d.dispose();
  }

}
