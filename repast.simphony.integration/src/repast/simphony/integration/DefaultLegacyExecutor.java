/*CopyrightHere*/
package repast.simphony.integration;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

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
		
		if (args != null){
			for (String arg : args) {
				this.commandLine.addArgument(arg);
			}
		}
		this.environment = environment;
	}

	public int execute() throws IOException {
		Executor exec = new DefaultExecutor();
	
		if (environment != null)
		  return exec.execute(commandLine, environment);
		else
			return exec.execute(commandLine);
	}

	public void spawn() throws IOException {
		if (spawnedProcess != null && !readExitSig) {
			LOG
					.debug("Spawning a new process without having read the exit code of the previous process");
		}

		this.readExitSig = false;

		CustomExecute exec = new CustomExecute();
		exec.setCommandLine(commandLine);
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
	
	public static void main (String[] args){
		String command = "C:\\Program Files (x86)\\Microsoft Office\\Office14\\EXCEL.EXE";
		String commandargs[] = new String[1];
		
		// Test a simple command line and command line arguments.
		// Forces Excel to start without displaying the startup screen and creating 
		// a new workbook (Book1.xls).
		commandargs[0] = "/e"; 
	
		DefaultLegacyExecutor exec = new DefaultLegacyExecutor(command, commandargs, null);
		
//		try {
//			exec.execute();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			exec.spawn();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
