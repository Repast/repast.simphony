/**
 * 
 */
package repast.simphony.terracotta;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JLabel;


import repast.simphony.data.analysis.PluginOutputStream;
import simphony.util.messages.MessageCenter;

import org.apache.commons.lang3.SystemUtils;
import org.pietschy.wizard.PanelWizardStep;

/**
 * This is the final step in the wizard that takes wizard data.
 * and launches Terracotta to enable distributed processes.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public class TerracottaFinishStep extends PanelWizardStep {
	private static final long serialVersionUID =1L;

	/**Log object for class*/
	private MessageCenter LOG = MessageCenter.getMessageCenter(TerracottaFinishStep.class);
	
	/**
	 * Default constructor for final launch run script step
	 */
	public TerracottaFinishStep() {
		super("Finished", "Terracotta Setup Finished");
		add(new JLabel("To launch Terracotta executiong press finish"));
		
		setComplete(true);

	}
	
	/**
	 *Method to launch script file using a system call Runtime.exec(argument)
	 */
	public void fireCommand(){
		String command=BrowseForRunFile.HOME_DIR_FIELD.getText();
		
		if(command!=null && command.length()>0) {
			Process process=null;
			try {
				PluginOutputStream pos;
				
				//windows launches here
				if (SystemUtils.IS_OS_WINDOWS){
					
						try {
							process = Runtime.getRuntime().exec(command);
							process.waitFor();
						} catch (InterruptedException e) {
							LOG.error("Terracotta: Error, could not wait for execution to " +
									"be completed." +" The execution of Terracotta may not have waited or finished properly.", e);
						}
					
				}
	
				//other operating systems launch here
				else{

					ProcessBuilder builder = new ProcessBuilder(command).redirectErrorStream(true);
					process = builder.start();
					pos = new PluginOutputStream(process.getInputStream());
					pos.start();
					OutputStream s=process.getOutputStream();
					s.flush();
				}
				process.getInputStream().close();
			}
		
			catch(IOException e){
				LOG.error("Terracotta: Error, preparing Terracotta execution." +
					" The execution of Terracotta may not occur properly.", e);
				
				PluginOutputStream pos2 = new PluginOutputStream(process.getErrorStream());
				pos2.start();
			}
			process.destroy();
		}
	}
}
