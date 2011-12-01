package repast.simphony.relogo.util

import java.util.List;
import java.util.Map;

class PrimitiveCategory {
	String name;
	List methods = []
	Map methodRefs = [:]
	
	public PrimitiveCategory(String name){
		this.name = name
	}
	
	public void addMethodAndRef(String methodName, String methodRef){
		methods.add(methodName);
		methodRefs.put(methodName, methodRef)
	}
	
	public List getMethods(){
		return methods;
	}
	
	public String getRefForMethod(String method){
		return (methodRefs.get(method))
	}
}
