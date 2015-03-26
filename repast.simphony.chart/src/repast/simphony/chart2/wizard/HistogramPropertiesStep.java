package repast.simphony.chart2.wizard;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.chart2.OutOfRangeHandling;
import repast.simphony.chart2.engine.HistogramChartDescriptor;
import repast.simphony.chart2.engine.HistogramChartDescriptor.HistogramType;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 */

/**
 * Step for configuring histogram properties.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class HistogramPropertiesStep extends PluginWizardStep {

  private HistogramWizardModel model;
  private JSpinner binCountSpn ;
  private JSpinner minSpn ;
  private JSpinner maxSpn ;
  private JComboBox typeBox ;
  private JComboBox outBox ;
  private JLabel minLbl, maxLbl, ourLbl;

  public HistogramPropertiesStep() {
    super("Histogram Properites", "Please enter the histogram properties.");
  };
    
  @Override
	protected JPanel getContentPanel(){
  	binCountSpn = new JSpinner(new SpinnerNumberModel(10, 1, 9999, 1));
  	minSpn = new JSpinner(new SpinnerNumberModel(0, -10000000000000000.0,
  			10000000000000000.0, 1));
  	maxSpn = new JSpinner(new SpinnerNumberModel(0, -10000000000000000.0,
  			10000000000000000.0, 1));
  	typeBox = new JComboBox(HistogramType.values());
  	outBox = new JComboBox(OutOfRangeHandling.values());
  	  
    FormLayout layout = new FormLayout("left:pref, 3dlu, pref:grow", "");
    DefaultFormBuilder formBuilder = new DefaultFormBuilder(layout);
    formBuilder.append("Bin Count:", binCountSpn);
    formBuilder.nextLine();
    formBuilder.append("Histogram Type:", typeBox);

    layout = new FormLayout("4dlu, left:pref, 3dlu, pref:grow",
        "pref, 3dlu, pref, 3dlu, pref, 3dlu, pref");
    PanelBuilder builder = new PanelBuilder(layout);
    builder.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    CellConstraints cc = new CellConstraints();
    builder.add(formBuilder.getPanel(), cc.xyw(1, 1, 4));
    minLbl = builder.addLabel("Minimum Value:", cc.xy(2, 3));
    builder.add(minSpn, cc.xy(4, 3));
    maxLbl = builder.addLabel("Maximum Value:", cc.xy(2, 5));
    builder.add(maxSpn, cc.xy(4, 5));
    ourLbl = builder.addLabel("Out of Range Handling:", cc.xy(2, 7));
    builder.add(outBox, cc.xy(4, 7));

    addListeners();
    return builder.getPanel();
  }

  public void checkComplete() {
    if ((HistogramType) typeBox.getSelectedItem() == HistogramType.STATIC) {
      double max = (Double) maxSpn.getValue();
      double min = (Double) minSpn.getValue();
      setComplete(max > min);
    } else {
      setComplete(true);
    }
  }

  private void addListeners() {
    typeBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
          updateStaticProps();
          
        }
      }
    });
    
    minSpn.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent arg0) {
        checkComplete();
      }
    });
    
    maxSpn.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent arg0) {
        checkComplete();
      }
    });
  }

  private void updateStaticProps() {
    boolean enabled = typeBox.getSelectedItem().equals(HistogramType.STATIC);
    minLbl.setEnabled(enabled);
    maxLbl.setEnabled(enabled);
    ourLbl.setEnabled(enabled);
    minSpn.setEnabled(enabled);
    maxSpn.setEnabled(enabled);
    outBox.setEnabled(enabled);
    
    checkComplete();
  }

  public void init(WizardModel wizardModel) {
    this.model = (HistogramWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    HistogramChartDescriptor descriptor = model.getDescriptor();
    binCountSpn.setValue(descriptor.getBinCount());
    typeBox.setSelectedItem(descriptor.getHistType());
    if (descriptor.getHistType() == HistogramType.STATIC) {
      minSpn.setValue(descriptor.getMin());
      maxSpn.setValue(descriptor.getMax());
      outBox.setSelectedItem(descriptor.getOutOfRangeHandling());
    }

    updateStaticProps();
    checkComplete();
  }

  public void applyState() throws InvalidStateException {
    super.applyState();
    HistogramChartDescriptor descriptor = model.getDescriptor();
    descriptor.setBinCount((Integer) binCountSpn.getValue());
    descriptor.setHistType((HistogramType) typeBox.getSelectedItem());
    if (descriptor.getHistType() == HistogramType.STATIC) {
      double max = (Double)maxSpn.getValue();
      double min = (Double)minSpn.getValue();
      if (max <= min)
        throw new InvalidStateException("Max Value must be greater than min value.");
      descriptor.setMin(min);
      descriptor.setMax(max);
      descriptor.setOutOfRangeHandling((OutOfRangeHandling) outBox.getSelectedItem());
    }
  }
}
