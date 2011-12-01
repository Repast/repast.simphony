package repast.simphony.visualization.gis3D;

/**
 * 
 * @author Eric Tatara
 *
 */
public class GIS3DShapeFactory {

	/**
	 * Create a sphere with the given properties.
	 * 
	 * @param radius the radius.
	 * @param slices the number of longitudinal divisions.
	 * @param rings the number latitudinal divisions.
	 * @return the sphere.
	 */
	public static RenderableShape createSphere(float radius, int slices, int rings){
		return new SphereShape(radius, slices, rings);
	}
	
	/**
	 * Create a sphere with the given properties.
	 * 
	 * @param radius the radius.
	 * @param slices the number of longitudinal divisions.
	 * @return the sphere.
	 */
	public static RenderableShape createSphere(float radius, int slices){
		return createSphere(radius, slices, 10);
	}
	
	/**
	 * Create a sphere with the given properties.
	 * 
	 * @param radius the radius.
	 * @return the sphere.
	 */
	public static RenderableShape createSphere(float radius){
		return createSphere(radius, 10, 10);
	}
	
	/**
	 * Create a box.
	 * 
	 * @param size the size of the box.
	 * @return the box.
	 */
	public static RenderableShape createBox(float size){
		return new CubeShape(size);
	}
	
	/**
	 * Create a disc with the given properties.
	 * 
	 * @param radius the radius.
	 * @param slices the number of subdivisions around the z axis.
	 * @param rings the number of concentric rings about the origin into which 
	 *        the disk is	subdivided.
	 * @return the disc.
	 */
	public static RenderableShape createDisc(float radius, int slices, int rings){
		return new DiscShape(radius, 0, slices, rings);
	}
	
	/**
	 * Create a disc with the given properties.
	 * 
	 * @param radius the radius.
	 * @param slices the number of subdivisions around the z axis.
	 * @return the disc.
	 */
	public static RenderableShape createDisc(float radius, int slices){
		return createDisc(radius, slices, 10);
	}
	
	/**
	 * Create a disc with the given properties.
	 * 
	 * @param radius the radius.
	 * @return the disc.
	 */
	public static RenderableShape createDisc(float radius){
		return createDisc(radius, 10, 10);
	}
	
	/**
	 * Create a ring with the given properties.
	 * 
	 * @param outerRadius the outer radius.
	 * @param innerRadius the inner radius.
	 * @param slices the number of subdivisions around the z axis.
	 * @param rings the number of concentric rings about the origin into which 
	 *        the disk is	subdivided.
	 * @return the ring.
	 */
	public static RenderableShape createRing(float outerRadius, float innerRadius, 
			int slices, int rings){
		return new DiscShape(outerRadius, innerRadius, slices, rings);
	}
	
	/**
	 * Create a ring with the given properties.
	 * 
	 * @param outerRadius the outer radius.
	 * @param innerRadius the inner radius.
	 * @param slices the number of subdivisions around the z axis.
	 * @return the ring.
	 */
	public static RenderableShape createRing(float outerRadius, float innerRadius, 
			int slices){
		return createRing(outerRadius, innerRadius, slices, 10);
	}
	
	/**
	 * Create a ring with the given properties.
	 * 
	 * @param outerRadius the outer radius.
	 * @param innerRadius the inner radius.
	 * @return the ring.
	 */
	public static RenderableShape createRing(float outerRadius, float innerRadius){
		return createRing(outerRadius, innerRadius, 10, 10);
	}
	
	/**
	 * Create a ring with the given properties.
	 * 
	 * @param outerRadius the outer radius.
	 * @return the ring.
	 */
	public static RenderableShape createRing(float outerRadius){
		return createRing(outerRadius, outerRadius/2, 10, 10);
	}
	
	/**
	 * Create a cylinder with the given properties.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 * @param slices the number of subdivisions around the z axis.
	 * @param rings the number of concentric rings about the origin into which the
	 *        cylinder is subdivided.
	 * @return the cylinder.
	 */
	public static RenderableShape createCylinder(float radius, float height, int slices, int rings){
		return new CylinderShape(radius, height, slices, rings);
	}
	
	/**
	 * Create a cylinder with the given properties.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 * @param slices the number of subdivisions around the z axis.
	 * @return the cylinder.
	 */
	public static RenderableShape createCylinder(float radius, float height, int slices){
		return createCylinder(radius, height, slices, 10);
	}
	
	/**
	 * Create a cylinder with the given properties.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 */
	public static RenderableShape createCylinder(float radius, float height){
		return createCylinder(radius, height, 10, 10);
	}
	
	/**
	 * Create a cylinder with the given properties.
	 * 
	 * @param radius the radius.
	 */
	public static RenderableShape createCylinder(float radius){
		return createCylinder(radius, 2*radius, 10, 10);
	}
	
	/**
	 * Create a cone with the given properties.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 * @param slices the number of subdivisions around the z axis.
	 * @param rings the number of concentric rings about the origin into which the
	 *        cone is subdivided.
	 * @return the cone.
	 */
	public static RenderableShape createCone(float radius, float height, int slices, int rings){
		return new ConeShape(radius, height, slices, rings);
	}
	
	/**
	 * Create a cone with the given properties.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 * @param slices the number of subdivisions around the z axis.
	 * @return the cone.
	 */
	public static RenderableShape createCone(float radius, float height, int slices){
		return createCone(radius, height, slices, 10);
	}
	
	/**
	 * Create a cone with the given properties.
	 * 
	 * @param radius the radius.
	 * @param height the height.
	 * @return the cone.
	 */
	public static RenderableShape createCone(float radius, float height){
		return createCone(radius, height, 10, 10);
	}
	
	/**
	 * Create a cone with the given properties.
	 * 
	 * @param radius the radius.
	 * @return the cone.
	 */
	public static RenderableShape createCone(float radius){
		return createCone(radius, 2*radius, 10, 10);
	}
}