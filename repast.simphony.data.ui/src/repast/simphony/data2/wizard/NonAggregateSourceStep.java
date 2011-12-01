package repast.simphony.data2.wizard;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

import repast.simphony.data2.engine.DataSetDescriptor;

/**
 * 
 */

/**
 * General Step for creating data sets.
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class NonAggregateSourceStep extends PanelWizardStep implements CompletableStep {

  private DataSetWizardModel model;
  private CommonSourcePanel cSourcePanel;
  private MethodSourcePanel mSourcePanel;
  private CustomDataSourcePanel customSourcePanel;
  private boolean mComplete, cdComplete;

  public NonAggregateSourceStep() {
    super("Select Data Sources", "Please choose the data sources to add to this data set.");
    this.setLayout(new BorderLayout());

    JTabbedPane tabs = new JTabbedPane();
    add(tabs, BorderLayout.CENTER);
    cSourcePanel = new CommonSourcePanel();
    tabs.add("Standard Sources", cSourcePanel);
    mSourcePanel = new MethodSourcePanel();
    tabs.add("Method Data Sources", mSourcePanel);
    customSourcePanel = new CustomDataSourcePanel();
    tabs.add("Custom Data Sources", customSourcePanel);
  }

  public void init(WizardModel wizardModel) {
    this.model = (DataSetWizardModel) wizardModel;
  }

  public void prepare() {
    super.prepare();
    DataSetDescriptor descriptor = model.getDescriptor();
    cSourcePanel.init(descriptor);
    customSourcePanel.init(this, descriptor);
    mSourcePanel.init(this, model);
  }

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
