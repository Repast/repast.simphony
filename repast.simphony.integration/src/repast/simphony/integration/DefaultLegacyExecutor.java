/*CopyrightHere*/
package repast.simphony.integration;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Execute;

import simphony.util.messages.MessageCenter;

public class DefaultLegacyExecutor implements LegacyExecutor {

	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(DefaultLegacyExecutor.class);

	private CommandLine commandLine;

	private Map environment;

	private Process spawnedProcess;

	private boolean readExitSig;

	public DefaultLegacyExecutor(String executable, String[] args, Map environment) {
		super();

		this.commandLine = new CommandLine(executable);
		for (String arg : args) {
			this.commandLine.addArgument(arg);
		}

		this.environment = environment;
	}

	public int execute() throws IOException {
		Execute exec = new Execute();
		exec.setCommandline(commandLine);
		if (environment != null) {
			exec.setEnvironment(environment);
		}

		return exec.execute();
	}

	public void spawn() throws IOException {
		if (spawnedProcess != null && !readExitSig) {
			LOG
					.debug("Spawning a new process without having read the exit code of the previous process");
		}

		this.readExitSig = false;

		CustomExecute exec = new CustomExecute();
		exec.setCommandline(commandLine);
		if (environment != null) {
			exec.setEnvironment(environment);
		}

		spawnedProcess = exec.spawnAndReturnProcess();

	}

	public int waitForSpawn() throws InterruptedException {
		readExitSig = true;

		return spawnedProcess.waitFor();
	}
	
	public int exitValue() {
		readExitSig = true;

		return spawnedProcess.exitValue();
	}
}
