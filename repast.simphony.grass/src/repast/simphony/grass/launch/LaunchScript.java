package repast.simphony.grass.launch;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.SystemUtils;

import repast.simphony.data.analysis.PluginOutputStream;
import simphony.util.messages.MessageCenter;

/**
 * Class to launch Grass module from input command.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 *
 * @param <T>
 */
public class LaunchScript<T> {

	
	/**The error message log for the object*/
	private MessageCenter LOG = MessageCenter.getMessageCenter(LaunchScript.class);
	
	/**The command argument given to start Grass module*/
	String commandArgument;
	
	/**
	 * The general constructor that takes the command argument for Grass.
	 * @param command the command to launch a Grass process
	 */
	public LaunchScript(String command){
		this.commandArgument=command;
	}
	
	public String getCommandArgument() {
		return commandArgument;
	}

	public void setCommandArgument(String commandArgument) {
		this.commandArgument = commandArgument;
	}
	
	/**
	 * Method launches Grass application. 
	 * In theory, you can use this to launch Grass modules during runtime 
	 * or post-modeling analysis.
	 */
	public void launch(){
		Process process=null;
		try {
			PluginOutputStream pos;
			
			//launch command for windows OS
			if (SystemUtils.IS_OS_WINDOWS){
				
					try {
						process = Runtime.getRuntime().exec(commandArgument);
						process.waitFor();
					} catch (InterruptedException e) {
						LOG.error("Grass: Error, could not wait for execution to " +
								"be completed." +" The execution of Grass may not have waited or finished properly.", e);
					}
				
			}
			
			//otherwise for non-windows launch here
			else{

				ProcessBuilder builder = new ProcessBuilder(commandArgument).redirectErrorStream(true);
				process = builder.start();
				pos = new PluginOutputStream(process.getInputStream());
				pos.start();
				OutputStream s=process.getOutputStream();
				s.flush();
			}
			process.getInputStream().close();
		}
	
		catch(IOException e){
			LOG.error("Grass: Error, preparing Grass execution." +
				" The execution of Grass may not occur properly.", e);
			
			PluginOutputStream pos2 = new PluginOutputStream(process.getErrorStream());
			pos2.start();
		}
		process.destroy();
	}
	
	//TODO Add additional (if relevant) run options via Grass commands (both runtime modeling and post-modeling analysis)
}
