/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard.builder;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.graph.Network;

public class BuilderContextBuilder implements ContextBuilder {

	private transient ObjectFiller filler = new ObjectFiller();
	private transient Map<AgentDescriptor, Object> agentMap = new HashMap<AgentDescriptor, Object>();
	private ContextDescriptor descriptor;

	public BuilderContextBuilder(ContextDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	private void setup() {
		if (filler == null) {
			filler = new ObjectFiller();
		}
		if (agentMap == null) {
			agentMap = new HashMap<AgentDescriptor, Object>();
		}
	}

	/**
	 * Builds and returns a context. Building a context consists of filling it with
	 * agents, adding projects and so forth. When this is called for the master context
	 * the system will pass in a created context based on information given in the
	 * model.score file. When called for subcontexts, each subcontext that was added
	 * when the master context was built will be passed in.
	 *
	 * @param context
	 * @return the built context.
	 */
	public Context build(Context context) {
		try {
			setup();

			for (AgentDescriptor agentDescriptor : descriptor
					.getAgentDescriptors()) {
				Object agent = initializeAgent(agentDescriptor);
				context.add(agent);
				agentMap.put(agentDescriptor, agent);
			}

			for (NetworkDescriptor networkDescriptor : descriptor.getNetworkDescriptors()) {
				Network network = NetworkFactoryFinder.createNetworkFactory(null).createNetwork(
								networkDescriptor.getName(), context, descriptor.isDirected());
				for (RelationshipDescriptor relDesc : networkDescriptor
						.getRelationships()) {
					network.addEdge(agentMap.get(relDesc.getSource()), agentMap.get(relDesc
							.getTarget()), relDesc.getStrength());
				}
			}
//			for (SProjection proj : descriptor.getProjectionInfos()) {
//				if (context.getProjection(proj.getName()) == null) {
//					if (proj.getType().equals(SProjectionType.NETWORK_LITERAL)) {
//						context.addProjection(NetworkFactoryFinder.createNetworkFactory(null)
//								.createNetwork(proj.getName(), context, false));
//					} else if (proj.getType().equals(SProjectionType.CONTINUOUS_LITERAL)) {
//						SContinuous continuous = (SContinuous) proj;
//
//						context.addProjection(ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null).createContinuousSpace(proj.getName(), proj.get))
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return context;

	}



	private Object initializeAgent(AgentDescriptor agentDescriptor) {
		Class agentClass = agentDescriptor.getAgentClass();
		Object agent = null;
		try {
			agent = agentClass.newInstance();
			filler.fillObject(agent, agentDescriptor.getProperties());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return agent;
	}

	public ContextDescriptor getDescriptor() {
		return descriptor;
	}
}
