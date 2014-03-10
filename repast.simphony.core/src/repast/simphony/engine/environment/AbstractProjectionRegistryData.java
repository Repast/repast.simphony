package repast.simphony.engine.environment;

import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.space.projection.Projection;
import repast.simphony.xml.AbstractConverter;

public abstract class AbstractProjectionRegistryData implements ProjectionRegistryData {

	protected String typeName;
	protected Class<?> intface;
	protected ProjectionDryer<? extends Projection<?>> projectionDryer;
	protected AbstractConverter projectionXMLConverter;
	
  public AbstractProjectionRegistryData(String typeName, Class<?>intface){
  	this.typeName = typeName;
  	this.intface = intface;
  }
  
  @Override
	public ProjectionDryer<? extends Projection<?>> getProjectionDryer() {
		return projectionDryer;
	}

  @Override
	public void setProjectionDryer(
			ProjectionDryer<? extends Projection<?>> projectionDryer) {
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
}