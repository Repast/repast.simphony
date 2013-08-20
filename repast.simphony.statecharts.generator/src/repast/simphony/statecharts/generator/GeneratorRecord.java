/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IPath;

/**
 * Records the uuids and svg paths of
 * Statecharts and svg files created by a CodeGenerator. 
 * 
 * @author Nick Collier
 */
public class GeneratorRecord {
  
  private Set<String> uuids = new HashSet<String>();
  private Set<IPath> svgs = new HashSet<IPath>();
  
  /**
   * Gets whether or not this contains the specified uuid.
   * 
   * @param uuid
   * @return true if this contains the uuid, otherwise false.
   */
  public boolean containsUUID(String uuid) {
    return uuids.contains(uuid);
  }
  
  /**
   * Gets whether or not this contains the specified svg path.
   * 
   * @param uuid
   * @return true if this contains the svg path, otherwise false.
   */
  public boolean containsSVG(IPath svg) {
    return svgs.contains(svg);
  }
  
  /**
   * Adds the specified UUID.
   * 
   * @param id
   */
  public void addUUID(String id) {
    uuids.add(id);
  }
  
  /**
   * Adds the specified path to an SVG statechart file.
   * 
   * @param svgPath
   */
  public void addSVG(IPath svgPath) {
    svgs.add(svgPath);
  }

}
