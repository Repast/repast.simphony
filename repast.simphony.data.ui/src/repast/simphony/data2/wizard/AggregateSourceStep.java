package repast.simphony.data2.wizard;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.data2.engine.DataSetDescriptor;
import repast.simphony.ui.plugin.editor.PluginWizardStep;

/**
 * General Step for creating data sets.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class AggregateSourceStep extends PluginWizardStep implements CompletableStep {

  private DataSetWizardModel model;
  private CommonSourcePanel cSourcePanel;
  private AggMethodSourcePanel mSourcePanel;
  private CustomDataSourcePanel customSourcePanel;
  private boolean mComplete, cdComplete;

  public AggregateSourceStep() {
    super("Select Data Sources",
        "Please choose the data sources to add to this data set.");
  }

  @Override
 	protected JPanel getContentPanel(){ 
  	JPanel panel = new JPanel(new BorderLayout());
    
    JTabbedPane tabs = new JTabbedPane();
    panel.add(tabs, BorderLayout.CENTER);
    cSourcePanel = new CommonSourcePanel();
    tabs.add("Standard Sources", cSourcePanel);
    mSourcePanel = new AggMethodSourcePanel();
    tabs.add("Method Data Sources", mSourcePanel);
    customSourcePanel = new CustomDataSourcePanel();
    tabs.add("Custom Data Sources", customSourcePanel);
   
    return panel;
  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DataSetWizardModel) wizardModel;
  }

  @Override
  public void prepare() {
    super.prepare();
    DataSetDescriptor descriptor = model.getDescriptor();
    cSourcePanel.init(descriptor);
    customSourcePanel.init(this, descriptor);
    mSourcePanel.init(this, model); 
  }
  
  @Override
  public void applyState() throws InvalidStateException {
    super.applyState();
    DataSetDescriptor descriptor = model.getDescriptor();
    cSourcePanel.apply(descriptor);
    mSourcePanel.apply(descriptor);
    customSourcePanel.apply(descriptor);
  }
  
  @Override
  public void complete(Object source, boolean complete) {
    if (source.equals(mSourcePanel)) mComplete = complete;
    else if (source.equals(customSourcePanel)) cdComplete = complete;
    
    setComplete(mComplete || cdComplete);
  }
}
