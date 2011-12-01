package repast.simphony.terracotta.datagrid;

import java.util.List;

import repast.simphony.terracotta.datagrid.DataGridMaster;
import repast.simphony.terracotta.datagrid.RepastWork;

/**
 * This is the class used to start a Master.
 * @author Mark Altaweel
 *
 * @param <T>
 */
public class StartMaster<T> {

	/**A StringBuilder object used to setup the router ids in the Master*/
	private StringBuilder routerIds;
	
	/**The DataGridMaster (i.e. the "Master") object used in the example scenario*/
	private DataGridMaster dataGrid;

	/**
	 * Main method to start the DataGridMaster object used in the example.
	 * @param routingIDs ids of nodes used
	 * @param workItems the items for work.
	 */
	public void startExample(String[] routingIDs,List<RepastWork>workItems) {

		
		String failOverRoutingID = null;
		
		failOverRoutingID = routingIDs[routingIDs.length-1];
		

		StringBuilder builder = new StringBuilder();
		builder.append("Starting...: ");
		if (routingIDs != null) {
		    builder.append("\n  list with routing IDs \t= [ ");
    		for (int i = 0; i < routingIDs.length; i++) {
    			builder.append(routingIDs[i]);
    			builder.append(' ');
    		}
            builder.append("]");
		}
		if (failOverRoutingID != null) {
			builder.append("\n  routing ID for fail-over node = ");
			builder.append(failOverRoutingID);
		}
		this.routerIds=builder;
		System.out.println(routerIds);
		
		this.dataGrid=new DataGridMaster(routingIDs, failOverRoutingID,workItems);
	}

	public StringBuilder getRouterIds() {
		return routerIds;
	}

	public void setRouterIds(StringBuilder routerIds) {
		this.routerIds = routerIds;
	}

	public DataGridMaster getDataGrid() {
		return dataGrid;
	}

	public void setDataGrid(DataGridMaster dataGrid) {
		this.dataGrid = dataGrid;
	}
}