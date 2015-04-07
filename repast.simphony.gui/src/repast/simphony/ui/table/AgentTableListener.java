package repast.simphony.ui.table;

/**
 * Agent table listeners listen for table events that need special handling, eg
 *   further handling of JComponent listeners in the table.
 *   
 * @author Eric Tatara
 *
 */
public interface AgentTableListener {

	/**
	 * Occurs when the docking frame enclosing the table is closed which destroys
	 *   the table.
	 */
	public void tableClosed();
	
}
