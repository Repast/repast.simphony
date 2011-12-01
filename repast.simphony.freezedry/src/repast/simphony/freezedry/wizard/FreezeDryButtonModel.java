package repast.simphony.freezedry.wizard;

import com.thoughtworks.xstream.converters.Converter;
import repast.simphony.engine.environment.RunState;
import repast.simphony.freezedry.FreezeDryedDataSource;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.ui.RSApplication;
import repast.simphony.util.wizard.DynamicWizard;
import repast.simphony.xml.XMLSerializer;
import saf.core.ui.Workspace;
import saf.core.ui.actions.ISAFAction;
import simphony.util.messages.MessageCenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class FreezeDryButtonModel extends AbstractAction implements ISAFAction<RSApplication> {

  private static final long serialVersionUID = 6166590309802641118L;

  @SuppressWarnings("unused")
  private MessageCenter LOG = MessageCenter.getMessageCenter(FreezeDryButtonModel.class);

  protected Workspace<RSApplication> workspace;


  public void initialize(Workspace<RSApplication> workspace) {
    this.workspace = workspace;
  }

  public void actionPerformed(ActionEvent e) {
    if (workspace.getApplicationMediator().getController() == null
            || workspace.getApplicationMediator().getController()
            .getCurrentRunState() == null) {
      JOptionPane.showMessageDialog(null,
              "Please load a model and initialize the simulation "
                      + "before attempting to freezedry");
      return;
    }

    execute(workspace.getApplicationMediator().getController().getCurrentRunState(), e);
  }

  @SuppressWarnings("unchecked")
  protected void execute(final RunState runState, ActionEvent e) {
    final DynamicWizard wiz = FreezeDryWizardPluginUtil.create(workspace.getApplicationMediator()
            .getCurrentScenario(), runState.getMasterContext().getId(), false,
            "Press finish to execute the freeze drying.");

    Component component = null;
    if (e.getSource() instanceof Component) {
      component = SwingUtilities.getWindowAncestor((Component) e.getSource());
    }
    wiz.showDialog(component, "Freeze dry current model state");
    if (wiz.wasCancelled()) {
      return;
    }

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        FreezeDryWizardModel model = (FreezeDryWizardModel) wiz.getModel();
        if (model.getBuilder() == null) {
          XMLSerializer xmler = new XMLSerializer();
          for (Converter converter : model.converters()) {
            xmler.registerConverter(converter);
          }
          try {
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(model.getXMLFile())));
            if (!model.useRoot()) {
              xmler.toXML(runState.getMasterContext().findContext(model.getFreezeDryedContextId()), out);
            } else {
              xmler.toXML(runState.getMasterContext(), out);
            }
            out.flush();
            out.close();
          } catch (Exception ex) {
            LOG.warn("Error while xml freezedrying", ex);
          }

        } else {
          FreezeDryedRegistry registry = new FreezeDryedRegistry();
          FreezeDryedDataSource dryedDataSource = model.getBuilder().getDataSource();
          registry.setDataSource(dryedDataSource);
          dryedDataSource.reset();
          try {
            if (!model.useRoot()) {
              registry.freezeDry(runState.getMasterContext().findContext(model.getFreezeDryedContextId()));
            } else {
              registry.freezeDry(runState.getMasterContext());
            }
          } catch (Exception ex) {
            if (model.useRoot()) {
              LOG.warn("Could not freeze dry root context.", ex);
            } else {
              LOG.warn("Could not freeze dry context with id '"
                      + model.getFreezeDryedContextId() + "'.", ex);
            }
          }
        }
      }
    });
  }
}
