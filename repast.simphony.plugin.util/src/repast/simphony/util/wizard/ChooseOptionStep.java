package repast.simphony.util.wizard;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.ui.plugin.editor.PluginWizardStep;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Wizard step used for selecting from a list of options, usually used as the 
 *   first step in a wizard.
 * 
 * @author Jerry Vos
 * @author Eric Tatara
 */
public class ChooseOptionStep extends PluginWizardStep {
  private static final long serialVersionUID = 4474067862691565825L;

  private JScrollPane scrollPane1;
  private JList optionList;
  private DynamicWizardModel model;
  protected DefaultListModel listModel;

  public ChooseOptionStep(String firstStepTitle, String firstStepPrompt) {
    super(firstStepTitle, firstStepPrompt);
  }

  @Override
 	protected JPanel getContentPanel(){
    scrollPane1 = new JScrollPane();
    optionList = new JList();
    CellConstraints cc = new CellConstraints();
    
    FormLayout layout = new FormLayout("default:grow", "fill:default:grow");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    
    scrollPane1.setViewportView(optionList);
    builder.add(scrollPane1, cc.xy(1, 1));

    listModel = new DefaultListModel();

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
    
    return builder.getPanel();
  }

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
