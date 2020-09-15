package repast.simphony.space.physics;

import java.util.HashMap;

import org.jogamp.vecmath.Vector3f;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.space.continuous.ContinuousAdder;
import repast.simphony.space.continuous.DefaultContinuousSpace;
import repast.simphony.space.continuous.MultiOccupancyCoordinateAccessor;
import repast.simphony.space.continuous.PointTranslator;

import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Transform;

/**
 * A special 3-dimensional subclass of continuous space that determines the 
 * positions of agents based on physics.
 * 
 * @author Eric Tatara
 *
 * @param <T>
 */
public class DefaultPhysicsSpace<T> extends DefaultContinuousSpace<T> implements
		PhysicsSpace<T> {

	protected CollisionConfiguration collisionConfiguration;
	protected CollisionDispatcher dispatcher;
	protected SequentialImpulseConstraintSolver solver;
	protected DiscreteDynamicsWorld dynamicsWorld;
	protected HashMap<RigidBody, T> bodyToObjectMap;
	protected HashMap<T, RigidBody> objectToBodyMap;
	
	protected float stepSize = 1f /60f; // Hz
	protected int maxSubSteps = 0;
	
	/**
	 * Constructs this space with the specified name, adder, translator,
	 * and size. 
	 * 
	 * @param name the name of the space
	 * @param adder the adder used by the space
	 * @param translator the point translator
	 * @param xdim the size of the x dimension
	 * @param ydim the size of the y dimension
	 * @param zdim the size of the z dimension
	 */
	public DefaultPhysicsSpace(String name, ContinuousAdder<T> adder, 
			PointTranslator translator, double xdim, double ydim, double zdim) {
		super(name, adder, translator, new MultiOccupancyCoordinateAccessor<T>(), 
				xdim, ydim, zdim);
		initPhysics(true);
	}
	
	/**
	 * Constructs this space with the specified name, adder, translator,
	 * size and origin. 
	 * 
	 * @param name the name of the space
	 * @param adder the adder used by the space
	 * @param translator the point translator
	 * @param size the dimensions of the space
	 * @param origin the origin of the space
	 */
	public DefaultPhysicsSpace(String name, ContinuousAdder<T> adder, 
			PointTranslator translator, double[] size, double[] origin) {
		super(name, adder, translator, size, origin);
		
		// TODO handle arguments to enforce 3 dimensions?
		
		initPhysics(true);
  }
	
	protected void initPhysics(boolean scheduleStep){
		bodyToObjectMap = new HashMap<RigidBody, T>();
		objectToBodyMap = new HashMap<T, RigidBody>();
		
		// collision configuration contains default setup for memory, collision
		// setup. Advanced users can create their own configuration.
		collisionConfiguration = new DefaultCollisionConfiguration();

		// use the default collision dispatcher. For parallel processing you
		// can use a diffent dispatcher (see Extras/BulletMultiThreaded)
		dispatcher = new CollisionDispatcher(collisionConfiguration);
		
	  // the maximum size of the collision world. Make sure objects stay
		// within these boundaries
		// Don't make the world AABB size too large, it will harm simulation
		// quality and performance
		
//		TODO user settable?
		Vector3f worldAabbMin = new Vector3f(-1000, -1000, -1000);
		Vector3f worldAabbMax = new Vector3f(1000, 1000, 1000);
		int maxProxies = 4*4096;
		// TODO fix
		AxisSweep3 overlappingPairCache = null; //new AxisSweep3(worldAabbMin, worldAabbMax, 
				//maxProxies);
		
	   // the default constraint solver. For parallel processing you can use a
		// different solver (see Extras/BulletMultiThreaded)
		solver = new SequentialImpulseConstraintSolver();
		
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, 
				solver,	collisionConfiguration);
		// TODO
		// dynamicsWorld.setGravity(new Vector3f(0, -9.8f, 0));
		
		if (scheduleStep){
			ScheduleParameters params = ScheduleParameters.createRepeating(0, 1);
			ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
			schedule.schedule(params, new PhysicsScheduleAction(this));
		}
	}
	
	public boolean addObject(T object, RigidBody body) {
		
		// TODO more thorough checks like AbstractContinuousSpace... 
		
		if (!bodyToObjectMap.values().contains(object)){

			dynamicsWorld.addRigidBody(body);
			
			bodyToObjectMap.put(body, object);
			objectToBodyMap.put(object, body);
		}
		
		Transform trans = new Transform();
		body.getMotionState().getWorldTransform(trans);
		
		// TODO fix
		//return super.moveTo(object, (double)trans.origin.x,	(double)trans.origin.y, 
		//		(double)trans.origin.z);
		return false;
		
	}

	public Transform getTransformForObject(T object){
		return objectToBodyMap.get(object).getWorldTransform(new Transform());
	}

	public void step() {
		dynamicsWorld.stepSimulation(stepSize, maxSubSteps);
		
		for (int j=dynamicsWorld.getNumCollisionObjects()-1; j>=0; j--){
			CollisionObject obj = dynamicsWorld.getCollisionObjectArray().get(j);
			RigidBody body = RigidBody.upcast(obj);
			if (body != null && body.getMotionState() != null) {
				Transform trans = body.getMotionState().getWorldTransform(new Transform());
				
				if (bodyToObjectMap.get(body) != null){
					
				  // TODO handle super.moveTo() here or let user update via PhysicsSpace.getTransform()?
				 // TODO Fix
				  //super.moveTo(bodyToObjectMap.get(body), (double)trans.origin.x, 
				//		(double)trans.origin.y, (double)trans.origin.z);
				}
			}
		}
		
		// TODO Testing for collision event notifications.  The following code checks
		// for collision events between bodies.  Need to implement a watch or equivalent
		// so that agents can execute behaviors in response to collisions.
//		int numManifolds = dynamicsWorld.getDispatcher().getNumManifolds();
//		
//		for (int i=0; i<numManifolds; i++){
//			PersistentManifold man = dynamicsWorld.getDispatcher().getManifoldByIndexInternal(i);
//			
//			int numContacts = man.getNumContacts();
//			
//			RigidBody body0 = RigidBody.upcast((CollisionObject)man.getBody0());
//			RigidBody body1 = RigidBody.upcast((CollisionObject)man.getBody1());
//			
//			for (int j=0; j<numContacts; j++){
//				ManifoldPoint point = man.getContactPoint(j);
//				
//				if (point.getDistance() < 0.f){
//					System.out.println("Collision: " + bodyToObjectMap.get(body0) + " -> " 
//							+ bodyToObjectMap.get(body1));
////					break;
//				}
//				
//			}
//			
//		}
		
	}
	
  public void setLinearVelocity(T object, float x, float y, float z ){
	  // TODO
  	// objectToBodyMap.get(object).setLinearVelocity(new Vector3f(x,y,z));
  }
	
	public float[] getLinearVelocity(T object){
		float[] v = new float[3];
		/// TODO
		// objectToBodyMap.get(object).getLinearVelocity(new Vector3f(0,0,0)).get(v);
		return v;
	}
	
	public void setGravity(float x, float y, float z){
		// TODO
		// dynamicsWorld.setGravity(new Vector3f(x,y,z));
	}
	
	public float[] getGravity(){
		float[] g = new float[3];
		// TODO
		//dynamicsWorld.getGravity(new Vector3f(0,0,0)).get(g);
		return g;
	}
	
	public float getStepSize() {
		return stepSize;
	}

	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
	}

	public int getMaxSubSteps() {
		return maxSubSteps;
	}

	public void setMaxSubSteps(int maxSubSteps) {
		this.maxSubSteps = maxSubSteps;
	}
}