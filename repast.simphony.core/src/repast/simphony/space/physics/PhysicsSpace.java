package repast.simphony.space.physics;

import org.jogamp.vecmath.Vector3f;

import repast.simphony.space.projection.Projection;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

/**
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public interface PhysicsSpace<T> extends Projection<T> {

	/**
	 * Manually step the physics space dynamics
	 * 
	 * @param timeStep
	 */
	void step();
	
	Transform getTransformForObject(T object);
	
	public boolean addObject(T object, RigidBody body);
	
	public void setGravity(float x, float y, float z);
	
	public float[] getGravity();
	
	public float getStepSize();

	public void setStepSize(float stepSize);

	public int getMaxSubSteps();

	public void setMaxSubSteps(int maxSubSteps);

	public void setLinearVelocity(T object, float x, float y, float z);
	
	public float[] getLinearVelocity(T Object);
}