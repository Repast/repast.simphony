package repast.simphony.space.gis;


public class GeographyParameters<T> {

	private String crsCode;

	private GISAdder<T> adder;

	public GeographyParameters(){
	  this.adder = new SimpleAdder<T>();
	}
	
	public GeographyParameters(GISAdder<T> adder){
	  this.adder = adder;
	}
	
	public GISAdder<T> getAdder() {
		return adder;
	}

	public void setAdder(GISAdder<T> adder) {
		this.adder = adder;
	}

	public String getCrs() {
		return crsCode;
	}

	/**
	 * Sets the CRS code used to generate the Coordinate Referencing System.
	 * 
	 * @param crsCode the CRS code which is in the format "EPSG:xxxx"
	 */
	public void setCrs(String crsCode) {
		this.crsCode = crsCode;
	}

}
