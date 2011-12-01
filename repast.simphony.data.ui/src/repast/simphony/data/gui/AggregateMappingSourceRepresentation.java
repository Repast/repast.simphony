package repast.simphony.data.gui;

import repast.simphony.data.logging.gather.AggregateDataMapping;

/**
 * @author Nick Collier
 */
public interface AggregateMappingSourceRepresentation {
	AggregateDataMapping createMapping();
	boolean equalsMappingSource(AggregateDataMapping mapping);
	
	boolean isMappingEditable();
}
