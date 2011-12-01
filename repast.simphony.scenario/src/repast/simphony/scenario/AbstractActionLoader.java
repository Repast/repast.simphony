package repast.simphony.scenario;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import repast.simphony.engine.environment.ControllerRegistry;
import simphony.util.messages.MessageCenter;

import com.thoughtworks.xstream.XStream;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:11:55 $
 */
public abstract class AbstractActionLoader implements ActionLoader {
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(AbstractActionLoader.class);
	
	protected File file;
	protected Object contextID;

	public AbstractActionLoader(File file, Object contextID) {
		this.file = file;
		this.contextID = contextID;
	}

	public void loadAction(XStream xstream, Scenario scenario, ControllerRegistry registry) throws FileNotFoundException {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			performLoad(bufferedReader, xstream, scenario, registry);
		} catch (FileNotFoundException ex) {
			LOG.warn("Error attempting to load file '" + file
					+ "'. Not loading the action from that file.", ex);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();					
				}
			} catch (IOException e) {}
		}
	}

	protected abstract void performLoad(Reader reader, XStream xstream, Scenario scenario, ControllerRegistry registry);
}
