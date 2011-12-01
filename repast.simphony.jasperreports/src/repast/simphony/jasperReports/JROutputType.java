
package repast.simphony.jasperReports;

public enum JROutputType {
	PDF("PDF File"),
	HTML("HTML File"),
	XML("XML File"),
	VIEWER("Viewer");
	
	private String description;
	
	JROutputType(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return description;
	}
}