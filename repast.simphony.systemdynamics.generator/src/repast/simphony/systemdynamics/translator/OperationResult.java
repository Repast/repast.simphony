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
			System.out.println("Setting not OK again");
		this.ok = ok;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		if (this.message == null)
			this.message = message;
		else
			this.message += "\n" + message;
	}
	
	public void clear() {
		ok=true;
		message = null;
	}
	
	public void setErrorMessage(String message) {
		
		setMessage(message);
		System.out.println("ErrorMsg: "+message);
		setOk(false);
	}

}
