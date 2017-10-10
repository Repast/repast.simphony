package repast.simphony.gis.visualization.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ListModel;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.scenario.data.AgentData;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualization.gui.AgentSelectionStep;

/**
 * Agent selection step for the 2D GIS wizard.  This overrides the parent prepare()
 * method since the layer index ordering in the GIS display is reverse of the other
 * displays. 
 * 
 * TODO Add the layer background index info to the viz registry data so that 
 *      the general wizards will understand the proper layer order.
 * 
 * @author Eric Tatara
 *
 *@deprecated 2D piccolo based code is being removed
 */
public class GISAgentSelectionStep extends AgentSelectionStep {

	@Override
  public void applyState() throws InvalidStateException {
    DisplayDescriptor descriptor = model.getDescriptor();  
    ListModel listModel = getListModel();
    
    boolean reset = false;

    Map<String, Integer> layerOrders = descriptor.getLayerOrders();

//  if (layerOrders.size() == listModel.getSize()) {
//  for (int i = 0, n = listModel.getSize(); i < n; i++) {
//    AgentData agent = (AgentData) listModel.getElementAt(i);
//    Integer val = layerOrders.get(agent.getClassName());
//    if (val == null || val != n - i - 1) {
//      reset = true;
//    }
//  }
//} else {
//  reset = true;
//}

//if (reset) {
    layerOrders.clear();
    for (int i = 0; i < listModel.getSize(); i++) {
    	AgentData agent = (AgentData) listModel.getElementAt(i);

    	// Layer order same as list order
    	descriptor.addLayerOrder(agent.getClassName(), i);
    }

    // TODO Investigate if this code is causing the style not to apply in cases
    //      when a layer is added/removed from an existing display.
    Map<String, String> styles = new HashMap<String, String>(descriptor.getStyles());
//  if (styles.size() > 0) {
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
//  }
    
    // Update any steps whose data are dependent on this step.
    updateListeners();
  }
	
	@Override
  protected List<AgentData> getTargetAgentList(DisplayDescriptor descriptor){
  	 List<AgentData> target = new ArrayList<AgentData>();
    
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
       }
     }

     Collections.sort(orders);
     // Layer order is the same as agent list order
     for (int i : orders){
       target.add(i, orderedMap.get(i));
     }
  	 
  	 return target;
  }
}