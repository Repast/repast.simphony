package repast.simphony.systemdynamics.translator;

public class OperationResult {
	
	private boolean ok = true;
	private String message;
	
	public OperationResult() {
		
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		if (!ok)
			System.out.println("Setting not OK");
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
	
	public void setErrorMessage(String message) {
		
		setMessage(message);
		if (ok)
			System.out.println("Setting not OK message = "+message);
		setOk(false);
		System.exit(1);
	}

}
