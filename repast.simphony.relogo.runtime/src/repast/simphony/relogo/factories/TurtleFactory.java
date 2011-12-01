package repast.simphony.relogo.factories;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import repast.simphony.relogo.BaseTurtle;
import repast.simphony.relogo.Observer;
import repast.simphony.relogo.Plural;
import repast.simphony.relogo.Turtle;
import repast.simphony.relogo.Utility;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.ui.RSApplication;
import simphony.util.messages.MessageCenter;

public class TurtleFactory {
	@SuppressWarnings("unused")
	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(TurtleFactory.class);
	
	private Observer observer = null;

	Map<String, Class<? extends BaseTurtle>> turtleTypesMap = new ConcurrentHashMap<String,Class<? extends BaseTurtle>>();
	
	public Class<?> getTurtleTypeClass(String turtleType){
		String standardizedTurtleType = turtleType.toLowerCase();
		return (turtleTypesMap.get(standardizedTurtleType));
	}
	
	Map<String, String> defaultShapeMap = new ConcurrentHashMap<String,String>();
	
	public Map<String, String> getDefaultShapeMap() {
		return defaultShapeMap;
	}

	public TurtleFactory(Class<? extends BaseTurtle> turtleType, Collection<Class<? extends BaseTurtle>> turtleTypes){
		for (Class<?> clazz : turtleTypes){
			if (!BaseTurtle.class.isAssignableFrom(clazz)){
				throw new RuntimeException("Argument "+ clazz +" to TurtleFactoryImpl constructor needs to extend BaseTurtle.");
			}
		}
		if (!BaseTurtle.class.isAssignableFrom(turtleType)){
			throw new RuntimeException("Argument "+ turtleType +" to TurtleFactoryImpl constructor needs to extend BaseTurtle.");
		}
		
		// Assign the turtleType as the "default" turtle type
		this.turtleTypesMap.put("default", turtleType);
		defaultShapeMap.put("default", "default");
		// Use the turtle class name with all lower case letters as the singular turtle type name
		String defaultSingularName = turtleType.getSimpleName(); 
		this.turtleTypesMap.put(defaultSingularName.toLowerCase(), turtleType);
		// set the default shape for the turtle type as "default"
		defaultShapeMap.put(defaultSingularName.toLowerCase(), "default");
		// If the Plural annotation is used, use the supplied plural name (all lowercase) as the plural
		Annotation ann = turtleType.getAnnotation(Plural.class);
		String defaultPluralName;
		if (ann != null){
			Plural pluralAnn = (Plural) ann;
			defaultPluralName = pluralAnn.value();
			
		}
		else {
			defaultPluralName = defaultSingularName + "s";
		}
		this.turtleTypesMap.put(defaultPluralName.toLowerCase(), turtleType);
		// set the default shape for the turtle type's plural as "default"
		defaultShapeMap.put(defaultPluralName.toLowerCase(), "default");
		
		// For the additional turtleTypes, do the same as above, except for the "default" turtle type
		for (Class<? extends BaseTurtle> clazz : turtleTypes){
			// Use the turtle class name with all lower case letters as the singular turtle type name
			String singularName = clazz.getSimpleName(); 
			this.turtleTypesMap.put(singularName.toLowerCase(), clazz);
			// set the default shape for the turtle type as "default"
			defaultShapeMap.put(singularName.toLowerCase(), "default");
			ann = clazz.getAnnotation(Plural.class);
			String pluralName;
			if (ann != null){
				// If the Plural annotation is used, use the supplied plural name (all lowercase) as the plural
				Plural pluralAnn = (Plural) ann;
				pluralName = pluralAnn.value();
				
			}
			else {
				pluralName = singularName + "s";
			}
			this.turtleTypesMap.put(pluralName.toLowerCase(), clazz);
			// set the default shape for the turtle type's plural as "default"
			defaultShapeMap.put(pluralName.toLowerCase(), "default");
		}
	}
	
	public TurtleFactory(Class<? extends BaseTurtle> turtleType){
		this(turtleType,ReLogoImplementingClassesFinder.find(BaseTurtle.class));
//		this(turtleType,new ArrayList<Class<? extends BaseTurtle>>());
	}
	
	public void init(Observer observer) {
		this.observer = observer;
	}

	public Turtle createIdenticalTurtle(Turtle parent) { 
		String parentTurtleType = parent.getClass().getSimpleName(); 
		return createIdenticalTurtle(parent, parentTurtleType);
	}
	
	public Turtle createIdenticalTurtle(Turtle parent, String childType) {
		BaseTurtle newTurtle = (BaseTurtle) createTurtle(childType, parent.getHeading(), parent.getColor(), new NdPoint(parent.getTurtleLocation().toDoubleArray(null)));
		newTurtle.setUserDefinedVariables(parent);
		return newTurtle;
	}

	public Turtle createTurtle() {
		return createTurtle("default", Utility.randomFloat(360), Utility.randomFloat(140), new NdPoint(0.0, 0.0));
	}

	public Turtle createTurtle(String turtleType) {
		return createTurtle(turtleType, Utility.randomFloat(360), Utility.randomFloat(140), new NdPoint(0.0, 0.0));
	}

	public Turtle createTurtle(NdPoint loc) {
		return createTurtle("default", Utility.randomFloat(360), Utility.randomFloat(140), loc);
	}
	
	public Turtle createTurtle(String turtleType, NdPoint loc) {
		return createTurtle(turtleType, Utility.randomFloat(360), Utility.randomFloat(140), loc);
	}

	public Turtle createTurtle(double heading) {
		return createTurtle("default", heading, Utility.randomFloat(140), new NdPoint(0.0,0.0));
	}
	
	public Turtle createTurtle(String turtleType, double heading) {
		return createTurtle(turtleType, heading, Utility.randomFloat(140), new NdPoint(0.0,0.0));
	}

	public Turtle createTurtle(String turtleType, double heading, double color,
			NdPoint loc) {
		if (observer == null){
			throw(new RuntimeException("The TurtleFactoryImpl init method needs to be called before instantiating Turtles."));
		}
		BaseTurtle turtle = null;
		String standardizedTurtleType = turtleType.toLowerCase();
		try {
			// for NL compatibility
			if (standardizedTurtleType.equals("turtles")){
				standardizedTurtleType = "default";
			}
			Class<? extends BaseTurtle> turtleClass = turtleTypesMap.get(standardizedTurtleType);
			if (turtleClass == null){
				System.out.println("the turtleType " + turtleType + " is not in the turtleTypesMap");
			}
			turtle = turtleClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		String defaultTypeShape = defaultShapeMap.get(standardizedTurtleType);
		turtle.setBaseTurtleProperties(observer, this, defaultTypeShape, heading, color, loc);
		
		return turtle;
	}
	
	public void setDefaultShape(String typeName, String shapeName){
		String standardizedTurtleType = typeName.toLowerCase();
		if (standardizedTurtleType.equals("turtles")){
			//set all the types' default shapes
			Set<String> allTypes = defaultShapeMap.keySet();
			for (String type : allTypes){
				defaultShapeMap.put(type, shapeName);
			}
		}
		else {
			//set just that type's default shape to shapeName
			Class<? extends BaseTurtle> turtleClass = turtleTypesMap.get(standardizedTurtleType);
			List<String> list = new ArrayList<String>();
			// get all keys with entry turtleClass in turtleTypesMap
			for(String k:turtleTypesMap.keySet()){
		        if(turtleTypesMap.get(k).equals(turtleClass)) {
		            list.add(k);
		        }
		    }
			for (String k: list){
				defaultShapeMap.put(k, shapeName);
			}
		}
	}
}
