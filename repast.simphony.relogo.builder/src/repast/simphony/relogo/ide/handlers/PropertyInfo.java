package repast.simphony.relogo.ide.handlers;

public class PropertyInfo {

	public String readMethod;
	public String writeMethod;
	public String propertyType;
	
	public PropertyInfo(String readMethod, String writeMethod, String propertyType) {
		this.readMethod = readMethod;
		this.writeMethod = writeMethod;
		this.propertyType = propertyType;
	}
		
}
