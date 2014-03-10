/**
 * 
 */
package repast.simphony.scenario.data;

import repast.simphony.space.IGeography;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.valueLayer.ValueLayer;

public enum ProjectionType_old {
  
  GRID {
    public String getName() {
      return "grid";
    }
    
    public Class<?> getInterface() {
      return Grid.class;
    }
  },
  
  CONTINUOUS_SPACE {
    public String getName() {
      return "continuous space";
    }
    
    public Class<?> getInterface() {
      return ContinuousSpace.class;
    }
  }, 
  
  NETWORK {
    public String getName() {
      return "network";
    }
    
    public Class<?> getInterface() {
      return Network.class;
    }
  }, 
  
  GEOGRAPHY {
    public String getName() {
      return "geography";
    }
    
    public Class<?> getInterface() {
      return IGeography.class;
    }
  },
  
  VALUE_LAYER {
    public String getName() {
      return "value layer";
    }
    
    public Class<?> getInterface() {
      return ValueLayer.class;
    }
  };
 
  
  public abstract String getName();
  
  public abstract Class<?> getInterface();

  public static ProjectionType_old getType(String name) {
    if (name.equalsIgnoreCase("grid")) return GRID;
    if (name.equals("network")) return NETWORK;
    if (name.equals("continuous space")) return CONTINUOUS_SPACE;
    if (name.equals("geography")) return GEOGRAPHY;
    if (name.equals("value layer")) return VALUE_LAYER;
    
    throw new IllegalArgumentException("Invalid projection type '" + name + "'");
  }
}