/*CopyrightHere*/
package repast.simphony.freezedry;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import simphony.util.messages.MessageCenter;

/**
 * An action that will freeze dry a context of a specified id when it is executed.
 * 
 * @author Jerry Vos
 */
public class ContextFreezeDryingAction implements IAction {
	public static final Object USE_ROOT_ID = "_useRootContext_";
	
	private static final long serialVersionUID = -275222637255697926L;

	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(ContextFreezeDryingAction.class);

	protected Object contextId;
	
	protected transient FreezeDryedRegistry registry;
	
	protected FreezeDryedDataSource dataSource;
	
	
	public ContextFreezeDryingAction(Object contextId, FreezeDryedDataSource dataSource) {
		super();
		this.contextId = contextId;
		this.dataSource = dataSource;
	}
	
	public ContextFreezeDryingAction(FreezeDryedDataSource dataSource) {
		this(USE_ROOT_ID, dataSource);
	}

	public void execute() {
		if (registry == null) {
			registry = new FreezeDryedRegistry();
			registry.setDataSource(dataSource);
		}
		Context context;
		if (contextId.equals(USE_ROOT_ID)) {
			context = RunState.getInstance().getMasterContext();
			if (context == null) {
				LOG.warn("Appears no master context exists, cannot"
						+ " freezedry it.");
				return;
			}
		} else {
			context = RunState.getInstance().getMasterContext().findContext(contextId);
			if (context == null) {
				LOG.warn("Could not find a context with the specified id '" + contextId + "', cannot"
						+ " freezedry it.");
				return;
			}
		}
		
		try {
			registry.reset();
			registry.freezeDry(context);
		} catch (FreezeDryingException e) {
			LOG.warn("Error freeze drying context with id '" + contextId + "'.", e);
		}
	}
	
	public FreezeDryedDataSource getDataSource() {
		return dataSource;
	}
}
