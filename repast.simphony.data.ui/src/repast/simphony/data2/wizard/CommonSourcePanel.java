/**
 * 
 */
package repast.simphony.data2.wizard;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import repast.simphony.data2.engine.DataSetDescriptor;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
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
    
    FormLayout layout = new FormLayout(
    		"3dlu, pref:grow, 3dlu, pref:grow", 
    		"3dlu, pref, 3dlu, pref, 3dlu, pref");
    CellConstraints cc = new CellConstraints();
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
   
    tickBox = new JCheckBox("Tick Count");
    builder.add(tickBox, cc.xy(2, 2));
   
    seedBox = new JCheckBox("Random Seed");
    builder.add(seedBox, cc.xy(2, 4));
   
    runBox = new JCheckBox("Run Number");
    builder.add(runBox, cc.xy(2, 6));
    
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
