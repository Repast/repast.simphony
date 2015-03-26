package repast.simphony.chart2.wizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.chart2.engine.HistogramChartDescriptor;
import repast.simphony.ui.plugin.editor.SquareIcon;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


/**
 * Step for configuring non-aggregate data to be displayed by a chart.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class HistogramChartPropertiesStep extends PanelWizardStep  {

  private HistogramWizardModel model;
  
  private JTextField titleFld = new JTextField();
  private JTextField xFld = new JTextField();
  private JTextField yFld = new JTextField();
  private JButton bkColorBtn = new JButton();
  private JButton gridColorBtn = new JButton();
  private JButton barColorBtn = new JButton();
  private JCheckBox showGridChk = new JCheckBox();
  private JLabel gridLineLabel;

  public HistogramChartPropertiesStep() {
    super("Configure Chart Properites", "Please configure the chart's display properties.");
    this.setLayout(new BorderLayout());
    FormLayout layout = new FormLayout("4dlu, pref, 3dlu, pref:grow(0.1), pref:grow(0.9)", 
        "pref, 5dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 5dlu, pref, 3dlu, " +
        "pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    CellConstraints cc = new CellConstraints();
    builder.addSeparator("Chart Labels", cc.xyw(1, 1, 5));
    builder.addLabel("Title:", cc.xy(2, 3));
    builder.add(titleFld, cc.xyw(4, 3, 2));
    builder.addLabel("X-Axis:", cc.xy(2, 5));
    builder.add(xFld, cc.xyw(4, 5, 2));
    builder.addLabel("Y-Axis:", cc.xy(2, 7));
    builder.add(yFld, cc.xyw(4, 7, 2));
    
    builder.addSeparator("Plot Properties", cc.xyw(1, 9, 5));
    builder.addLabel("Bar Color:", cc.xy(2, 11));
    barColorBtn.setToolTipText("Click to change color");
    builder.add(barColorBtn, cc.xy(4, 11));
    
    builder.addLabel("Background Color:", cc.xy(2, 13));
    bkColorBtn.setToolTipText("Click to change color");
    builder.add(bkColorBtn, cc.xy(4, 13));
    builder.addLabel("Show Grid Lines:", cc.xy(2, 15));
    builder.add(showGridChk, cc.xy(4, 15));
    gridLineLabel = builder.addLabel("Grid Line Color:", cc.xy(2, 17));
    
    gridColorBtn.setToolTipText("Click to change color");
    builder.add(gridColorBtn, cc.xy(4, 17));
    
    add(builder.getPanel(), BorderLayout.CENTER);
    addListeners();
  }
  
  private void addListeners() {
    showGridChk.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        gridColorBtn.setEnabled(showGridChk.isSelected());
        gridLineLabel.setEnabled(showGridChk.isSelected());
      }
    });
    
    bkColorBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Color color = ((SquareIcon)bkColorBtn.getIcon()).getColor();
        color = JColorChooser.showDialog(HistogramChartPropertiesStep.this, "Chart Background Color", color);
        if (color != null) bkColorBtn.setIcon(new SquareIcon(20, 10, color));
      }
    });
    
    gridColorBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Color color = ((SquareIcon)gridColorBtn.getIcon()).getColor();
        color = JColorChooser.showDialog(HistogramChartPropertiesStep.this, "Chart Grid Color", color);
        if (color != null) gridColorBtn.setIcon(new SquareIcon(20, 10, color));
      }
    });
    
    barColorBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Color color = ((SquareIcon)barColorBtn.getIcon()).getColor();
        color = JColorChooser.showDialog(HistogramChartPropertiesStep.this, "Bar Color", color);
        if (color != null) barColorBtn.setIcon(new SquareIcon(20, 10, color));
      }
    });
  }

  public void init(WizardModel wizardModel) {
    this.model = (HistogramWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    HistogramChartDescriptor descriptor = model.getDescriptor();
    titleFld.setText(descriptor.getChartTitle());
    xFld.setText(descriptor.getXAxisLabel());
    yFld.setText(descriptor.getYAxisLabel());
    bkColorBtn.setIcon(new SquareIcon(20, 10, descriptor.getBackground()));
    showGridChk.setSelected(descriptor.isShowGrid());
    gridColorBtn.setIcon(new SquareIcon(20, 10, descriptor.getGridLineColor()));
    barColorBtn.setIcon(new SquareIcon(20, 10, descriptor.getBarColor()));
    
    if (!descriptor.isShowGrid()) {
      gridColorBtn.setEnabled(false);
      gridLineLabel.setEnabled(false);
    }
    
    setComplete(true);
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    HistogramChartDescriptor descriptor = model.getDescriptor();
    descriptor.setChartTitle(titleFld.getText().trim());
    descriptor.setXAxisLabel(xFld.getText().trim());
    descriptor.setYAxisLabel(yFld.getText().trim());
    descriptor.setBackground(((SquareIcon)bkColorBtn.getIcon()).getColor());
    descriptor.setGridLineColor(((SquareIcon)gridColorBtn.getIcon()).getColor());
    descriptor.setBarColor(((SquareIcon)barColorBtn.getIcon()).getColor());
    descriptor.setShowGrid(showGridChk.isSelected());
  }
}
