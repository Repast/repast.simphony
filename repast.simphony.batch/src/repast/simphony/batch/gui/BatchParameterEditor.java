/*
 * Created by JFormDesigner on Thu Jul 30 10:39:48 EDT 2009
 */

package repast.simphony.batch.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * @author User #1
 */
public class BatchParameterEditor extends JDialog {
  
  
  private Parameters params;
  public BatchParameterEditor(Frame owner) {
    super(owner);
    initComponents();
    int height = (int)toolBar1.getPreferredSize().getHeight();
    toolBar1.setFloatable(false);
    infoPanel.setBorder(BorderFactory.createEmptyBorder(height, 0, 0, 0));
    addListeners();
  }

  public BatchParameterEditor(Dialog owner) {
    super(owner);
    initComponents();
    int height = (int)toolBar1.getPreferredSize().getHeight();
    toolBar1.setFloatable(false);
    infoPanel.setBorder(BorderFactory.createEmptyBorder(height, 0, 0, 0));
    addListeners();
  }
  
  private void addListeners() {
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        writeParams();
      }
    });
  }
  
  private void writeParams() {
    ParameterFileWriter writer = new ParameterFileWriter(params);
    DefaultTreeModel model = (DefaultTreeModel) batchParameterTree1.getModel();
    LabelNode constants = (LabelNode) ((TreeNode)model.getRoot()).getChildAt(0);
    LabelNode parameters = (LabelNode) ((TreeNode)model.getRoot()).getChildAt(1);
    writer.write(constants, parameters);
  }
  
  public void init(Parameters params) {
    this.params = params;
    ParameterEditorMediator mediator = new ParameterEditorMediator(params, batchParameterTree1,
        infoPanel);
    addBtn.setEnabled(false);
    delBtn.setEnabled(false);
    addBtn.setAction(mediator.getAddAction());
    delBtn.setAction(mediator.getDeleteAction());
    addBtn.setText("Add");
    delBtn.setText("Delete");
    batchParameterTree1.init(mediator);
  }
  
  

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner non-commercial license
    dialogPane = new JPanel();
    contentPanel = new JPanel();
    splitPane1 = new JSplitPane();
    panel1 = new JPanel();
    scrollPane1 = new JScrollPane();
    batchParameterTree1 = new BatchParameterTree();
    toolBar1 = new JToolBar();
    addBtn = new JButton();
    delBtn = new JButton();
    infoPanel = new JPanel();
    buttonBar = new JPanel();
    okButton = new JButton();
    cancelButton = new JButton();
    CellConstraints cc = new CellConstraints();

    //======== this ========
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    //======== dialogPane ========
    {
      dialogPane.setBorder(Borders.DIALOG_BORDER);
      dialogPane.setLayout(new BorderLayout());

      //======== contentPanel ========
      {
        contentPanel.setLayout(new FormLayout(
          "default:grow",
          "fill:default:grow"));

        //======== splitPane1 ========
        {
          splitPane1.setResizeWeight(0.4);

          //======== panel1 ========
          {
            panel1.setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {
              scrollPane1.setViewportView(batchParameterTree1);
            }
            panel1.add(scrollPane1, BorderLayout.CENTER);

            //======== toolBar1 ========
            {

              //---- addBtn ----
              addBtn.setText("Add");
              toolBar1.add(addBtn);

              //---- delBtn ----
              delBtn.setText("Delete");
              toolBar1.add(delBtn);
            }
            panel1.add(toolBar1, BorderLayout.NORTH);
          }
          splitPane1.setLeftComponent(panel1);

          //======== infoPanel ========
          {
            infoPanel.setLayout(new CardLayout());
          }
          splitPane1.setRightComponent(infoPanel);
        }
        contentPanel.add(splitPane1, cc.xy(1, 1));
      }
      dialogPane.add(contentPanel, BorderLayout.CENTER);

      //======== buttonBar ========
      {
        buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
        buttonBar.setLayout(new FormLayout(
          new ColumnSpec[] {
            FormFactory.GLUE_COLSPEC,
            FormFactory.BUTTON_COLSPEC,
            FormFactory.RELATED_GAP_COLSPEC,
            FormFactory.BUTTON_COLSPEC
          },
          RowSpec.decodeSpecs("pref")));

        //---- okButton ----
        okButton.setText("OK");
        buttonBar.add(okButton, cc.xy(2, 1));

        //---- cancelButton ----
        cancelButton.setText("Cancel");
        buttonBar.add(cancelButton, cc.xy(4, 1));
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
  private JSplitPane splitPane1;
  private JPanel panel1;
  private JScrollPane scrollPane1;
  private BatchParameterTree batchParameterTree1;
  private JToolBar toolBar1;
  private JButton addBtn;
  private JButton delBtn;
  private JPanel infoPanel;
  private JPanel buttonBar;
  private JButton okButton;
  private JButton cancelButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables


  public static void main(String[] args) {
    ParametersCreator creator = new ParametersCreator();
    creator.addParameter("aCount", "Agent Count", int.class, 10, false);
    creator.addParameter("mValue", "Median Value", double.class, 20.5, false);
    creator.addParameter("String Value", String.class, "Hello", false);
    
    Parameters params = creator.createParameters();
    
    JFrame frame = new JFrame();
    BatchParameterEditor editor = new BatchParameterEditor(frame);
    editor.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    editor.pack();
    editor.init(params);
    editor.setVisible(true);
  }
}
