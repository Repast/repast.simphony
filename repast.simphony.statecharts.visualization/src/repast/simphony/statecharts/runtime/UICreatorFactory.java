/**
 * 
 */
package repast.simphony.statecharts.runtime;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import repast.simphony.statecharts.DefaultStateChart;
import repast.simphony.ui.RSApplication;
import repast.simphony.ui.probe.FieldPropertyDescriptor;
import repast.simphony.ui.probe.PPUICreatorFactory;
import repast.simphony.ui.probe.ProbedPropertyUICreator;

import com.jgoodies.binding.PresentationModel;


/**
 * PPUICreatorFactory for creating the UI probe component for a
 * statechart. 
 * 
 * @author Nick Collier
 */
public class UICreatorFactory implements PPUICreatorFactory {

  /* (non-Javadoc)
   * @see repast.simphony.ui.probe.PPUICreatorFactory#init(repast.simphony.ui.RSApplication)
   */
  @Override
  public void init(RSApplication app) {
    // TODO Auto-generated method stub
    
  }

  /* (non-Javadoc)
   * @see repast.simphony.ui.probe.PPUICreatorFactory#createUICreator(java.lang.Object, repast.simphony.ui.probe.FieldPropertyDescriptor)
   */
  @Override
  public ProbedPropertyUICreator createUICreator(Object obj, FieldPropertyDescriptor fpd)
      throws IllegalAccessException, IllegalArgumentException {
    return new PPUICreator((DefaultStateChart<?>)fpd.getField().get(obj), fpd.getDisplayName());
  }
  
  
  private static class PPUICreator implements ProbedPropertyUICreator {
    
    private DefaultStateChart<?> statechart;
    private String name;
    
    public PPUICreator(DefaultStateChart<?> statechart, String name) {
      this.statechart = statechart;
      this.name = name;
    }

    /* (non-Javadoc)
     * @see repast.simphony.ui.probe.ProbedPropertyUICreator#getDisplayName()
     */
    @Override
    public String getDisplayName() {
      return name;
    }

    @Override
    public JComponent getComponent(PresentationModel<Object> model) {
    	JPanel panel = new JPanel();
    	panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    	JButton button = new JButton("Display");
    	button.setPreferredSize(new Dimension(65,20));
    	button.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Object agent = statechart.getAgent();
					new StateChartSVGDisplayController(agent,statechart).createAndShowDisplay();
				}
			});
    	panel.add(button);
      return panel;
    }
  }
}
