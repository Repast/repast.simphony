package repast.simphony.essentials;

import java.io.IOException;
import java.io.InputStream;

public class DevNull extends Thread {

	protected InputStream inputStream = null;

	public DevNull(InputStream newInputStream) {
		this.inputStream = newInputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void run() {

		try {
			while (this.inputStream.available() > 0) {
				this.inputStream.read();
			}
		} catch (IOException e) {
		}

	}

}
