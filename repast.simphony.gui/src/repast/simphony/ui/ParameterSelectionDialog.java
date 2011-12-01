/*
 * Created by JFormDesigner on Fri Aug 10 11:37:21 EDT 2007
 */

package repast.simphony.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.Schema;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.jidesoft.swing.CheckBoxList;
import com.jidesoft.swing.CheckBoxListSelectionModel;

/**
 * @author User #2
 */
public abstract class ParameterSelectionDialog<P extends Parameters> extends JDialog {

  private class BoxElement implements Comparable<BoxElement> {
    private String id, display;


    public BoxElement(String id, String display) {
      this.id = id;
      this.display = display;
    }

    public String toString() {
      return display;
    }


    public int compareTo(BoxElement o) {
      return display.compareTo(o.display);
    }
   }

  protected P params;

  public ParameterSelectionDialog(Frame owner,String titleText) {
    super(owner);
    initComponents(titleText);
  }

  public ParameterSelectionDialog(Dialog owner,String titleText) {
    super(owner);
    initComponents(titleText);
  }

  private void addListeners() {
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ParameterSelectionDialog.this.dispose();
 
        CheckBoxListSelectionModel chkModel = paramsBox.getCheckBoxListSelectionModel();
        ListModel model = paramsBox.getModel();
        List<String> selectedItems = new ArrayList<String>();
        for (int i = 0; i < model.getSize(); i++) {
            if (chkModel.isSelectedIndex(i)) {
              BoxElement element = (BoxElement) model.getElementAt(i);
              selectedItems.add(element.id);
            }
          }
  	  
       doOKaction(selectedItems);
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ParameterSelectionDialog.this.dispose();
      }
    });
  }
  protected abstract void doOKaction(List<String> selectedParamNames);

  /**
   * Initialize the list for the Parameters object.
   * <P>
   * Subclass implementations should call init(params,includeRandomSeed,selectAll) to get
   * the list initialized appropriately
   * @param params The Parameters object to provide selection from
   */
  public abstract void init(P params);
  
  protected void init(P params,boolean includeRandomSeed, boolean selectAll) {
    addListeners();
    this.params = params;
    java.util.List<BoxElement> elements = new ArrayList<BoxElement>();
    Schema schema = params.getSchema();
    for (String name : schema.parameterNames()) {
    	if (includeRandomSeed || !name.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
    		elements.add(new BoxElement(name, params.getDisplayName(name)));
    	}
    }
    Collections.sort(elements);
    DefaultListModel model = new DefaultListModel();
    for (BoxElement element : elements) {
      model.addElement(element);
    }
    paramsBox.setModel(model);
    CheckBoxListSelectionModel chkModel = paramsBox.getCheckBoxListSelectionModel();
    if (selectAll) {
    	chkModel.addSelectionInterval(0, elements.size() - 1);
    	if (includeRandomSeed) {
		    for (int i = 0; i < elements.size(); i++) {
		      BoxElement element = (BoxElement) model.getElementAt(i);
		      if (element.id.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME)) {
		        chkModel.removeSelectionInterval(i, i);
		        break;
		      }
		    }
    	}
    }
  }

  private void initComponents(String titleText) {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
    dialogPane = new JPanel();
    contentPanel = new JPanel();
    panel1 = new JPanel();
    title1 = compFactory.createTitle(titleText);
    scrollPane1 = new JScrollPane();
    paramsBox = new CheckBoxList();
    buttonBar = new JPanel();
    okButton = new JButton();
    cancelButton = new JButton();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setModal(true);
    setTitle("Reset Parameter Defaults");
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
      dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
      dialogPane.setLayout(new BorderLayout());

      //======== contentPanel ========
      {
        contentPanel.setLayout(new FormLayout(
                ColumnSpec.decodeSpecs("default:grow"),
                new RowSpec[]{
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.RELATED_GAP_ROWSPEC,
                        new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                }));

        //======== panel1 ========
        {
          panel1.setBackground(Color.white);
          panel1.setBorder(new LineBorder(Color.black));
          panel1.setLayout(new FlowLayout());

          //---- title1 ----
          title1.setBackground(Color.white);
          title1.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
          panel1.add(title1);
        }
        contentPanel.add(panel1, cc.xy(1, 1));

        //======== scrollPane1 ========
        {
          scrollPane1.setViewportView(paramsBox);
        }
        contentPanel.add(scrollPane1, cc.xy(1, 3));
      }
      dialogPane.add(contentPanel, BorderLayout.CENTER);

      //======== buttonBar ========
      {
        buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
        buttonBar.setLayout(new GridBagLayout());
        ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
        ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

        //---- okButton ----
        okButton.setText("OK");
        buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
      }
      dialogPane.add(buttonBar, BorderLayout.SOUTH);
    }
    contentPane.add(dialogPane, BorderLayout.CENTER);
    pack();
    setLocationRelativeTo(getOwner());
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner non-commercial license
  private JPanel dialogPane;
  private JPanel contentPanel;
  private JPanel panel1;
  private JLabel title1;
  private JScrollPane scrollPane1;
  private CheckBoxList paramsBox;
  private JPanel buttonBar;
  private JButton okButton;
  private JButton cancelButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
