/**
 * 
 */
package repast.simphony.data2.wizard;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import repast.simphony.data2.engine.DataSetDescriptor;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Panel that includes check boxes for selecting
 * tick count etc. as data sources. 
 * 
 * @author Nick Collier
 */
@SuppressWarnings("serial")
public class CommonSourcePanel extends JPanel {
  
  private JCheckBox tickBox, seedBox, runBox;
  
  public CommonSourcePanel() {
    super(new BorderLayout());
    
    FormLayout layout = new FormLayout("pref:grow, 3dlu, pref:grow", "");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    tickBox = new JCheckBox("Tick Count");
    builder.append(tickBox);
    seedBox = new JCheckBox("Random Seed");
    builder.append(seedBox);
    builder.nextLine();
    runBox = new JCheckBox("Run Number");
    builder.append(runBox);
    
    add(builder.getPanel(), BorderLayout.CENTER);
  }
  
  public void init(DataSetDescriptor descriptor) {
    tickBox.setSelected(descriptor.includeTick());
    seedBox.setSelected(descriptor.includeRandomSeed());
    runBox.setSelected(descriptor.includeBatchRun());
  }
  
  public void apply(DataSetDescriptor descriptor) {
    descriptor.setIncludeBatchRun(runBox.isSelected());
    descriptor.setIncludeRandomSeed(seedBox.isSelected());
    descriptor.setIncludeTick(tickBox.isSelected());
  }
}
