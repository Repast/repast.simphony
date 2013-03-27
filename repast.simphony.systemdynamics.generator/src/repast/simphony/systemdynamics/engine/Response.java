package repast.simphony.systemdynamics.engine;

public class Response {

	private boolean ok;
	private String message;

	public Response() {
		ok = true;
		message = "";
	}

	public Response(boolean ok, String message) {
		this.ok = ok;
		this.message = message;
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

}
