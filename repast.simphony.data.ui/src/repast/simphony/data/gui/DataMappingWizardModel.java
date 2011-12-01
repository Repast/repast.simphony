/*CopyrightHere*/
package repast.simphony.data.gui;

import org.pietschy.wizard.models.Path;

import repast.simphony.data.logging.gather.aggregate.AbstractStatsAggregateMapping;
import repast.simphony.util.wizard.DynamicWizardModel;

public class DataMappingWizardModel extends DynamicWizardModel {

	private MappingSourceRepresentation mappingRepresentation;
	private Class<?> agentClass;
	private AbstractStatsAggregateMapping aggregator;

	public DataMappingWizardModel(Path path, Class<?> agentClass) {
		super(path, null, null);
		this.agentClass = agentClass;
    this.setLastAvailable(false);
    this.setLastVisible(false);
  }

	public MappingSourceRepresentation getMappingRepresentation() {
		return mappingRepresentation;
	}
	
	public void setMappingRepresentation(MappingSourceRepresentation mappingRepresentation) {
		this.mappingRepresentation = mappingRepresentation;
	}

	public Class<?> getAgentClass() {
		return agentClass;
	}

	public void setAgentClass(Class<?> agentClass) {
		this.agentClass = agentClass;
	}

	public AbstractStatsAggregateMapping getAggregator() {
		return aggregator;
	}
	
	public void setAggregator(AbstractStatsAggregateMapping aggregator) {
		this.aggregator = aggregator;
	}
}
