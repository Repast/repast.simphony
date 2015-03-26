package repast.simphony.chart2.wizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.chart2.engine.TimeSeriesChartDescriptor;
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
public class ChartPropertiesStep extends PanelWizardStep  {

  private TimeSeriesWizardModel model;
  private JTextField titleFld = new JTextField();
  private JTextField xFld = new JTextField();
  private JTextField yFld = new JTextField();
  private JButton bkColorBtn = new JButton();
  private JButton gridColorBtn = new JButton();
  private JCheckBox showGridChk = new JCheckBox();
  private JCheckBox showLegendChk = new JCheckBox();
  private JSpinner rangeSpn = new JSpinner();
  private JLabel gridLineLabel;

  public ChartPropertiesStep() {
    super("Configure Chart Properites", "Please configure the chart's display properties.");
    this.setLayout(new BorderLayout());
    FormLayout layout = new FormLayout("4dlu, pref, 3dlu, pref, pref:grow", 
        "pref, 5dlu, pref, 3dlu, pref, 3dlu, pref, 3dlu, pref, 5dlu, pref, 3dlu, " +
        "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
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
    builder.addLabel("Background Color:", cc.xy(2, 11));
    
    bkColorBtn.setToolTipText("Click to change color");
    builder.add(bkColorBtn, cc.xy(4, 11));
    builder.addLabel("Show Grid Lines:", cc.xy(2, 13));
    builder.add(showGridChk, cc.xy(4, 13));
    gridLineLabel = builder.addLabel("Grid Line Color:", cc.xy(2, 15));
    
    gridColorBtn.setToolTipText("Click to change color");
    builder.add(gridColorBtn, cc.xy(4, 15));
    builder.addLabel("X-Axis Range:", cc.xy(2, 17));
    builder.add(rangeSpn, cc.xy(4, 17));
    rangeSpn.setModel(new SpinnerNumberModel(-1, -1, 9000, 1));
    
    builder.addLabel("Show Legend:", cc.xy(2, 19));
    builder.add(showLegendChk, cc.xy(4, 19));
    
    
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
        color = JColorChooser.showDialog(ChartPropertiesStep.this, "Chart Background Color", color);
        if (color != null) bkColorBtn.setIcon(new SquareIcon(20, 10, color));
      }
    });
    
    gridColorBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        Color color = ((SquareIcon)gridColorBtn.getIcon()).getColor();
        color = JColorChooser.showDialog(ChartPropertiesStep.this, "Chart Grid Color", color);
        if (color != null) gridColorBtn.setIcon(new SquareIcon(20, 10, color));
      }
    });
  }

  public void init(WizardModel wizardModel) {
    this.model = (TimeSeriesWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
    titleFld.setText(descriptor.getChartTitle());
    xFld.setText(descriptor.getXAxisLabel());
    yFld.setText(descriptor.getYAxisLabel());
    bkColorBtn.setIcon(new SquareIcon(20, 10, descriptor.getBackground()));
    showGridChk.setSelected(descriptor.isShowGrid());
    gridColorBtn.setIcon(new SquareIcon(20, 10, descriptor.getGridLineColor()));
    rangeSpn.setValue(descriptor.getPlotRangeLength());
    showLegendChk.setSelected(descriptor.doShowLegend());
    
    if (!descriptor.isShowGrid()) {
      gridColorBtn.setEnabled(false);
      gridLineLabel.setEnabled(false);
    }
    
    setComplete(true);
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    TimeSeriesChartDescriptor descriptor = model.getDescriptor();
    descriptor.setChartTitle(titleFld.getText().trim());
    descriptor.setXAxisLabel(xFld.getText().trim());
    descriptor.setYAxisLabel(yFld.getText().trim());
    descriptor.setBackground(((SquareIcon)bkColorBtn.getIcon()).getColor());
    descriptor.setGridLineColor(((SquareIcon)gridColorBtn.getIcon()).getColor());
    descriptor.setShowGrid(showGridChk.isSelected());
    descriptor.setShowLegend(showLegendChk.isSelected());
    descriptor.setPlotRangeLength(((Integer)rangeSpn.getValue()).intValue());
  }
}
