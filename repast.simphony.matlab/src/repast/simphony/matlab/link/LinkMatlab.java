package repast.simphony.matlab.link;

/*
* 
* Method calls to JMatLink 1.0
* See http://www.held-mueller.de/JMatLink/download.html for futher information on JMatLink
* 
* Project Admins: st_mueller
* Operating System: All 32-bit MS Windows (95/98/NT/2000/XP), All POSIX (Linux/BSD/UNIX-like OSes)
* License: Apache Software License
* Category: Interface Engine/Protocol Translator
*
*/

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import jmatlink.JMatLink;
import jmatlink.JMatLinkException;
import repast.simphony.data2.DataConstants;
import repast.simphony.data2.DataSetRegistry;
import repast.simphony.engine.controller.Controller;
import repast.simphony.ui.RSApplication;
import saf.core.ui.Workspace;
import saf.core.ui.actions.ISAFAction;
import simphony.util.messages.MessageCenter;

/**
 * Object used to call native MatLab functions via RepastSimphony
 * 
 * Note:  Currently only works for Windows 98 and up
 * @author Mark Altaweel
 *
 */
@SuppressWarnings("serial")
public class LinkMatlab extends AbstractAction implements ISAFAction<RSApplication>{

	private static JMatLink jmatlink;
	protected Workspace<RSApplication> workspace;
	private static MessageCenter LOG = MessageCenter.getMessageCenter(LinkMatlab.class);
	
	
	/**
	 * Method just to open connection to matlab
	 */
	public static synchronized void openMatlab() {
		//open matlab
		boolean open=false;
		if(jmatlink==null)jmatlink=new JMatLink();
		
		int n=0; //usded to remove recurrent loops which occur when open session has not been closed properly
		
		while(!open) {
			try {
				jmatlink.engOpen();
				open=true;
			}
			catch(JMatLinkException e) {
				if(n==3)jmatlink.engClose();
				LOG.warn
				("Could not open Matlab");
				System.out.println("Did not open, try again");
			}
			
			catch (UnsatisfiedLinkError e2) {
				System.out.println("Matlab not found, check to make sure Matlab is installed");
				e2.printStackTrace();
				break;
			}
			
			n++;
		}
	}
	
	/**
	 * Method to return single double variable from a given Matlab method already declared
	 * @param scalarV the string of the variable wanted
	 * @return a double value of the variable
	 */
	public static double getScalarValue(String scalarV){
		return jmatlink.engGetScalar(scalarV);
	}
	/**
	 * Method called to evaluate specific Matlab function
	 * @param eval the string representing the MatLab command
	 * @return an output from MatLab
	 */
	private static synchronized String callMatlab(String eval) {
		 
		String output="";
		try {

        		//return outputs from workspace
        		jmatlink.engOutputBuffer();
	        	
        		//call the specific function using the string command
	        	jmatlink.engEvalString(eval); 
	        	
	        	//return model ouput value
	        	 output=jmatlink.engGetOutputBuffer();
	        	
	        }
	        catch(Exception e) {
	        	e.printStackTrace();
	        }
	        
	        return output;
	}
	
	/**
	 * Method takes a string of the command string used in matlab
	 * @param function the command string in matlab
	 * @return a matlab string output
	 */
	public static String evaluateMatlabFunction(String function) {
		String output;
		synchronized (jmatlink) {
			output=callMatlab(function);
		}
		return output;
	}
	
	/**
	 * simple call to close matlab externally
	 */
	public static synchronized void closeMatlab() {
		//close matlab
		
		if (jmatlink != null)
		  jmatlink.engClose();
	}
	
	public void initialize(Workspace<RSApplication> workspace) {
		this.workspace = workspace;
		
	}

	public void actionPerformed(ActionEvent e) {
		if (workspace.getApplicationMediator().getController() == null
				|| workspace.getApplicationMediator().getController()
						.getCurrentRunState() == null) {
			JOptionPane.showMessageDialog(null,
					"Please load a model and initialize the simulation "
							+ "before attempting to invoke MatLab");
			return;
		}

		Controller controller = workspace.getApplicationMediator()
				.getController();
		DataSetRegistry registry = (DataSetRegistry) controller.getCurrentRunState().getFromRegistry(DataConstants.REGISTRY_KEY);

		
		MatlabWizard wizard = new MatlabWizard(registry, true,false);
		
		if (wizard.showDialogModal()) {
			// run matlab
			runMatlab(wizard);
			
		}
		
		else
		{
			closeMatlab();
		}
		
	}

	private void runMatlab(MatlabWizard wizard) {
		try {
			String fName=wizard.getMatLabOutputter();
			openMatlab();
			callMatlab("load" +"("+fName+")");
			callMatlab("plot(100)");
			wizard.clearOutputters();
		}
		
		catch (Exception e) {
			LOG.error("Could not open MatLab, error in MatLab or input file",e);
			e.printStackTrace();
		}
	}
	
}
