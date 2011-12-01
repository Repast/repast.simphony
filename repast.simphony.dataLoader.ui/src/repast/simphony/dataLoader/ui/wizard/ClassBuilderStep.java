package repast.simphony.dataLoader.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.util.ClassUtilities;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class ClassBuilderStep extends PanelWizardStep {

  private JComboBox classBox;
  private DataLoaderWizardModel model;
  private DefaultComboBoxModel comboModel = new DefaultComboBoxModel();

  public ClassBuilderStep() {
    super("Class Name", "<HTML>Please provide the name of the context creator class</HTML>");
    setupPanel();
  }

  private void setupPanel() {
    FormLayout layout = new FormLayout("3dlu, pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    builder.appendSeparator("Class Name");
    builder.setLeadingColumnOffset(1);
    builder.nextLine();
    classBox = new JComboBox(comboModel);
    classBox.setRenderer(new ClassRenderer());
    builder.append(classBox);

    JPanel panel = builder.getPanel();
    // need to add it so we can set the layout for the parent
    add(panel);
    panel.getParent().setLayout(new BorderLayout());
    panel.getParent().remove(panel);
    add(panel, BorderLayout.NORTH);
  }

  public void init(WizardModel wizardModel) {
    super.init(wizardModel);
    model = (DataLoaderWizardModel) wizardModel;

    try {
      for (Class<?> clazz : model.getScenario().getContext().getClasspath().getClasses()) {

	if (ContextBuilder.class.isAssignableFrom(clazz)) {
	  ContextBuilder obj = (ContextBuilder) clazz.newInstance();
	  comboModel.addElement(obj);
	}
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace(); // To change body of catch statement use File |
      // Settings | File Templates.
    } catch (InstantiationException e) {
      e.printStackTrace(); // To change body of catch statement use File |
      // Settings | File Templates.
    } finally {
      ClassUtilities.doWarn = true;
    }
  }

  public void prepare() {
    classBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
	setComplete(classBox.getSelectedItem() != null);
	/*
	 * if (paramsPanel != null) remove(paramsPanel); ProbePanelCreator
	 * creator = new ProbePanelCreator(classBox .getSelectedItem()); Probe
	 * probe = creator.getProbe("Creator Parameters"); paramsPanel =
	 * probe.getPanel(); add(paramsPanel, BorderLayout.CENTER);
	 * invalidate(); validate(); repaint(); probe.update();
	 */
      }
    });
    if (comboModel.getSize() > 0)
      classBox.setSelectedIndex(0);
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    ClassContextActionBuilder loader = (ClassContextActionBuilder) model.getBuilder();
    loader.setDataLoader(classBox.getSelectedItem());
  }

  class ClassRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value, int index,
	boolean isSelected, boolean cellHasFocus) {
      if (value != null)
	value = value.getClass().getName();
      return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
  }
}
