package repast.simphony.engine.environment;

import java.util.List;

import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.space.projection.Projection;
import repast.simphony.xml.AbstractConverter;

/**
 * Abstract ProjectionRegistryData implementation that provides basic functionality.
 *    
 * @author Eric Tatara
 *
 * @param <T> the Projection type.
 */
public abstract class AbstractProjectionRegistryData<T extends Projection<?>> implements ProjectionRegistryData<T>{

	protected String typeName;
	protected Class<?> intface;
	protected ProjectionDryer<T> projectionDryer;
	protected AbstractConverter projectionXMLConverter;
	protected List<FreezeDryer<?>> freezedryers;
	
  public AbstractProjectionRegistryData(String typeName, Class<?>intface){
  	this.typeName = typeName;
  	this.intface = intface;
  }
  
  @Override
	public ProjectionDryer<T> getProjectionDryer() {
		return projectionDryer;
	}

  @Override
	public void setProjectionDryer(ProjectionDryer<T> projectionDryer) {
		this.projectionDryer = projectionDryer;
	}

  @Override
	public AbstractConverter getProjectionXMLConverter() {
		return projectionXMLConverter;
	}

  @Override
	public void setProjectionXMLConverter(AbstractConverter projectionXMLConverter) {
		this.projectionXMLConverter = projectionXMLConverter;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}

	@Override
	public Class<?> getInterface() {
		return intface;
	}
	
	@Override
	public List<FreezeDryer<?>> getFreezeDryers(){
		return freezedryers;
	}
	
	@Override
	public void setFreezeDryers(List<FreezeDryer<?>> freezedryers){
		this.freezedryers = freezedryers;
	}
}