package repast.simphony.space.gis;

import java.util.HashSet;
import java.util.Set;

import com.vividsolutions.jts.geom.Geometry;


/**
 * A layer in a geographic projection. Each layer is associated with objects of the same
 * type (Java class) and geometry (point, polygon etc.)
 */
public class Layer<T> {

	private Set<T> agentSet = new HashSet<T>();
	private Class<? extends T> agentType;
	private Class<? extends Geometry> geomType;
  private String name;

  /**
   * Creates a layer with no agent type and no geometry.
   */
  public Layer(String name) {
    this.name = name;

  }

  /**
   * Creates a layer with the specified agent type and geometry.
   *
   * @param agentType the type of agent in this layer
   * @param geomType the geometric type associated with this layer
   */
  public Layer(String name, Class<? extends T> agentType, Class<? extends Geometry> geomType) {
    this(name);
    this.agentType = agentType;
		this.geomType = geomType;
	}

  /**
   * Gets the name of this layer.
   *
   * @return the name of this layer.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the set of agents in this layer.
   *
   * @return the set of agents in this layer.
   */
  public Set<T> getAgentSet() {
		return agentSet;
	}

	protected void setAgentSet(Set<T> agentSet) {
		this.agentSet = agentSet;
	}

	public Class<? extends T> getAgentType() {
		return agentType;
	}

	protected void setAgentType(Class<? extends T> agentType) {
		this.agentType = agentType;
	}

	public Class<? extends Geometry> getGeomType() {
		return geomType;
	}

	protected void setGeomType(Class<? extends Geometry> geomType) {
		this.geomType = geomType;
	}

}
