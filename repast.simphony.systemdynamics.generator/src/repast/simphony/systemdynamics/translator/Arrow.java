package repast.simphony.systemdynamics.translator;

public class Arrow {
	
	public static final String IN = "IN";
	public static final String OUT = "OUT";
	public static final String FLOW = "FLOW";
	public static final String INFLUENCE = "INFLUENCE";
	
	private String direction;
	private String type;
	private String otherEnd;
//	private String to;
	
	public Arrow(String otherEnd, String direction, String type) {
		this.otherEnd = otherEnd;
		this.direction = direction;
		this.type = type;
	}
	
	public String toString() {
		return isIncoming() ? "FROM <"+otherEnd+"/"+direction+"/"+type+">"
				: "TO <"+otherEnd+"/"+direction+"/"+type+">";
	}
	
	public boolean isIncoming() {
		return direction.equals(IN);
	}
	
	public boolean outIncoming() {
		return direction.equals(OUT);
	}
	
	public boolean isFlow() {
		return type.equals(FLOW);
	}
	
	public boolean isInfluence() {
		return type.equals(INFLUENCE);
	}
	
	
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOtherEnd() {
		return otherEnd;
	}
	public void setOtherEnd(String otherEnd) {
		this.otherEnd = otherEnd;
	}
	
	
	

}
