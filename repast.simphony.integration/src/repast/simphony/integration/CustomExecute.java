/*CopyrightHere*/
package repast.simphony.integration;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.OS;
import org.apache.commons.exec.PumpStreamHandler;

import simphony.util.messages.MessageCenter;

/**
 * An extension of {@link org.apache.commons.exec.Execute} that adds the ability to retrieve a
 * spanwed process.
 * 
 * @author Jerry Vos
 */
public class CustomExecute extends DefaultExecutor {
	private static final MessageCenter msgCenter = MessageCenter.getMessageCenter(CustomExecute.class);

	private File workingDirectory;
	private CommandLine commandLine;
	private Map environment;

	@Override
	public void setWorkingDirectory(File wd) {
		super.setWorkingDirectory(wd);
		this.workingDirectory = wd;
	}

	/**
	 * Starts a process defined by the command line. Ant will not wait for this process, nor log its
	 * output. This code was taken from the super's spawn method and differs in that it returns the
	 * spawn process.
	 * 
	 * @throws java.io.IOException
	 *             The exception is thrown, if launching of the subprocess failed
	 */
	public Process spawnAndReturnProcess() throws IOException {
		if (workingDirectory != null && !workingDirectory.exists()) {
			throw new IOException(workingDirectory + " doesn't exist.");
		}
		final Process process = launch(commandLine, environment, workingDirectory);
		if (OS.isFamilyWindows()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				msgCenter.debug("interruption in the sleep after " + "having spawned a process");
			}
		}

		OutputStream dummyOut = new OutputStream() {
			public void write(final int b) {
			}
		};

		ExecuteStreamHandler streamHandler = new PumpStreamHandler(dummyOut);
		streamHandler.setProcessErrorStream(process.getErrorStream());
		streamHandler.setProcessOutputStream(process.getInputStream());
		streamHandler.start();
		process.getOutputStream().close();

		msgCenter.debug("spawned process " + process.toString());

		return process;
	}

	public void setCommandLine(CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	public void setEnvironment(Map environment) {
		this.environment = environment;
	}
	
	
}