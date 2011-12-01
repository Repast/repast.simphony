/*CopyrightHere*/
package repast.simphony.util.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Jerry Vos
 */
public class ChooseOptionStep extends PanelWizardStep {
  private static final long serialVersionUID = 4474067862691565825L;

  protected DefaultListModel listModel;

  public ChooseOptionStep() {
    initComponents();
  }

  public ChooseOptionStep(String firstStepTitle, String firstStepPrompt) {
    super(firstStepTitle, firstStepPrompt);
    initComponents();
  }

  @SuppressWarnings("serial")
  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY
    // //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    scrollPane1 = new JScrollPane();
    optionList = new JList();
    CellConstraints cc = new CellConstraints();

    // ======== this ========
    setLayout(new FormLayout("default:grow", "fill:default:grow"));

    // ======== scrollPane1 ========
    {
      scrollPane1.setViewportView(optionList);
    }
    add(scrollPane1, cc.xy(1, 1));
    // JFormDesigner - End of component initialization //GEN-END:initComponents

    this.listModel = new DefaultListModel();

    optionList.setModel(listModel);

    optionList.setCellRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
          boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
          value = ((WizardOption) value).getTitle();
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    });

    optionList.addListSelectionListener(new ListSelectionListener() {

      public void valueChanged(ListSelectionEvent arg0) {
        if (optionList.getSelectedIndex() >= 0) {
          setComplete(true);
        }
      }

    });
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JScrollPane scrollPane1;
  private JList optionList;
  // JFormDesigner - End of variables declaration //GEN-END:variables

  private DynamicWizardModel model;

  public void setOptions(Iterable<WizardOption> options) {
    listModel.clear();

    List<WizardOption> list = new ArrayList<WizardOption>();
    for (WizardOption option : options) {
      list.add(option);
    }

    Collections.sort(list, new Comparator<WizardOption>() {
      @Override
      public int compare(WizardOption o1, WizardOption o2) {
        return o1.getTitle().compareTo(o2.getTitle());
      }
    });

    // load up the possible options
    for (WizardOption option : list) {
      listModel.addElement(option);
    }

    if (listModel.size() > 0) {
      optionList.setSelectedIndex(0);
    }
  }

  @Override
  public void init(WizardModel model) {
    super.init(model);

    this.model = (DynamicWizardModel) model;
  }

  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();

    model.setChosenOption(getSelectedOption());
  }

  public WizardOption getSelectedOption() {
    return (WizardOption) optionList.getSelectedValue();
  }
}
