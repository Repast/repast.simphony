package repast.simphony.space.physics;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

/**
 * 
 * @author Eric Tatara
 *
 */
public class RigidBodyFactory {

	public static RigidBody createSphereBody(float radius, float mass, float[] loc){
		CollisionShape colShape = new SphereShape(radius);
		RigidBodyConstructionInfo rbInfo = ConstructBodyInfo(colShape, mass, loc);
		return new RigidBody(rbInfo);
	}	
	
	public static RigidBody createCubeBody(float edgeLength, float mass, float[] loc){
		
		return createBoxBody(edgeLength, edgeLength, edgeLength, mass, loc);
	}	
	
	public static RigidBody createBoxBody(float xLength, float yLength, 
			float zLength, float mass, float[] loc){
		
		// BoxShape uses half-lengths
		Vector3f extents = new Vector3f(xLength/2,yLength/2,zLength/2);
		CollisionShape colShape = new BoxShape(extents);
		RigidBodyConstructionInfo rbInfo = ConstructBodyInfo(colShape, mass, loc);
		return new RigidBody(rbInfo);
	}	
	
	protected static RigidBodyConstructionInfo ConstructBodyInfo(CollisionShape colShape, 
			float mass, float[] loc){
		
		Vector3f localInertia = new Vector3f(0, 0, 0);
		
		// don't calculate mass for static objects.
		if (mass != 0)
		  colShape.calculateLocalInertia(mass, localInertia);
		
		Transform startTransform = new Transform();
  	startTransform.setIdentity();
		startTransform.origin.set(new Vector3f(loc));
		DefaultMotionState myMotionState = new DefaultMotionState(startTransform);
		
		return new RigidBodyConstructionInfo(mass,myMotionState, colShape, localInertia);
	}
}