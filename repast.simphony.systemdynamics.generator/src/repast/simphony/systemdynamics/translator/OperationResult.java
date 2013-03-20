package repast.simphony.systemdynamics.translator;

public class OperationResult {
	
	private boolean ok;
	private String message;
	
	public OperationResult() {
		
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void clear() {
		ok=true;
		message = null;
	}

}
