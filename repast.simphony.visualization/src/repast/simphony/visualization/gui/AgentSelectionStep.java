package repast.simphony.visualization.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import repast.simphony.scenario.data.AgentData;
import repast.simphony.ui.plugin.editor.PluginWizardStep;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;

/**
 * Display wizard step for selecting agents to style.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 * 
 */
@SuppressWarnings("serial")
public class AgentSelectionStep extends PluginWizardStep {

  protected static class AgentDataRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
        boolean isSelected, boolean cellHasFocus) {
      if (value != null)
        value = ((AgentData) value).getShortName();
      return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }
  }

  protected AgentSelectionPanel panel;
  protected DisplayWizardModel model;

  protected ListDataListener dataListener = new ListDataListener() {

    // Each time the state of the list is changed, we need to apply the state
    // so that changes are saved to the display descriptor.

    public void contentsChanged(ListDataEvent e) {
      // setComplete(panel.target.getModel().getSize() > 0);
      try {
        applyState();
      } catch (InvalidStateException e1) {
        e1.printStackTrace();
      }
    }

    public void intervalAdded(ListDataEvent e) {
      // setComplete(panel.target.getModel().getSize() > 0);
      try {
        applyState();
      } catch (InvalidStateException e1) {
        e1.printStackTrace();
      }
    }

    public void intervalRemoved(ListDataEvent e) {
      // setComplete(panel.target.getModel().getSize() > 0);
      try {
        applyState();
      } catch (InvalidStateException e1) {
        e1.printStackTrace();
      }
    }
  };

  public AgentSelectionStep() {
    super("Agent Selection", "Please select the agent types to display and the "
        + "order in which the layers (2D) will appear");
    
    this.setComplete(false);
  }
  
  @Override
 	protected  JPanel getContentPanel(){ 
    panel = new AgentSelectionPanel();
    
    panel.source.setCellRenderer(new AgentDataRenderer());
    panel.target.setCellRenderer(new AgentDataRenderer());
    
    return panel;
  }

  @Override
  public void init(WizardModel wizardModel) {
    this.model = (DisplayWizardModel) wizardModel;
  }

  @Override
  public void applyState() throws InvalidStateException {
    DisplayDescriptor descriptor = model.getDescriptor();
    ListModel listModel = panel.target.getModel();
     
    boolean reset = false;
    
    Map<String, Integer> layerOrders = descriptor.getLayerOrders();
    
//    if (layerOrders.size() == listModel.getSize()) {
//      for (int i = 0, n = listModel.getSize(); i < n; i++) {
//        AgentData agent = (AgentData) listModel.getElementAt(i);
//        Integer val = layerOrders.get(agent.getClassName());
//        if (val == null || val != n - i - 1) {
//          reset = true;
//        }
//      }
//    } else {
//      reset = true;
//    }

//    if (reset) {
      layerOrders.clear();
      for (int i = 0; i < listModel.getSize(); i++) {
        AgentData agent = (AgentData) listModel.getElementAt(i);
        
        // Layer order reverse of list order
        descriptor.addLayerOrder(agent.getClassName(), listModel.getSize() - i - 1);
      }
      
      // TODO Investigate if this code is causing the style not to apply in cases
      //      when a layer is added/removed from an existing display.
      Map<String, String>styles = new HashMap<String, String>(descriptor.getStyles());
//    if (styles.size() > 0) {
      // we are editing an existing display so we need to add a default style
      // for any unstyled class as it is possible to close the editor before 
    	// picking out the style.
      for (String classname : descriptor.getLayerOrders().keySet()) {
        if (!styles.containsKey(classname)) {
          String styleClass = model.getDefaultStyle();
          if (styleClass != null)
            descriptor.addStyle(classname, styleClass);
        }
      }
//    }
      
      // Update any steps whose data are dependent on this step.
      updateListeners();
  }

  /**
   * Check if there are existing styles in the display.  This would happen if 
   * the wizard is opened on an existing display.
   * 
   * Subclass can override to provide the agent list in different order.
   * 
   * @return
   */
  protected List<AgentData> getTargetAgentList(DisplayDescriptor descriptor){
  	 List<AgentData> target = new ArrayList<AgentData>();
     List<AgentData> unorderedTarget = new ArrayList<AgentData>();
     Map<String, String> styles = descriptor.getStyles();
     List<Integer> orders = new ArrayList<Integer>();
     Map<Integer, AgentData> orderedMap = new HashMap<Integer, AgentData>();

     // check each style in the descriptor
     if (styles.keySet().size() > 0) {
       for (String className : styles.keySet()) {

         // if the layer is ordered, save it in order.
         if (descriptor.getLayerOrder(className) != null) {
           int order = descriptor.getLayerOrder(className);

           orderedMap.put(order, new AgentData(className));
           orders.add(order);
         }
         // else add it to a temp list;
         else
           unorderedTarget.add(new AgentData(className));
       }
     }

     Collections.sort(orders);
     for (int i=0; i<orders.size(); i++)
       target.add(i, orderedMap.get(orders.get(i)));

     // NOTE that order layer starts with background at index 0, while the
     // lists should be displayed with the foreground at index 0, so it needs
     // to be reversed.
     Collections.reverse(target);

     // append the unordered layers to the end of the ordered list.
     target.addAll(unorderedTarget);
  	 
  	 return target;
  }
  
  @Override
  public void prepare() {
    DisplayDescriptor descriptor = model.getDescriptor();
    
    // Disable layer ordering buttons for 3D since they don't apply.
    // TODO Projections: instead of DisplayType, check if descriptor is layerable?
    if (descriptor.getDisplayType().equals(DisplayType.THREE_D)) {
      panel.upBtn.setEnabled(false);
      panel.downBtn.setEnabled(false);
    }

    List<AgentData> target = getTargetAgentList(descriptor);

    // the list of all agents available to the runtime.
    List<AgentData> source = model.getContext().getAgentData(true);

    // sort available agent list alphabetically.
    Collections.sort(source, new Comparator<AgentData>() {
      public int compare(AgentData o1, AgentData o2) {
        return o1.getShortName().compareTo(o2.getShortName());
      }
    });

    // remove selected agents from the available agents list.
    panel.target.getModel().removeListDataListener(dataListener);
    source.removeAll(target);
    panel.init(source, target);
    panel.target.getModel().addListDataListener(dataListener);

    // need to fire an initial apply state to prepare descriptor for style step.
    try {
      applyState();
    } catch (InvalidStateException e) {
      e.printStackTrace();
    }

    // need to set complete true otherwise the other steps won't show up in
    // editor dialog.
    setComplete(true);
  }
  
  public ListModel getListModel(){
  	return  panel.target.getModel();
  }
}