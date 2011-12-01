/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import org.pietschy.wizard.InvalidStateException;
import repast.simphony.freezedry.gui.ClassRetrievable;
import repast.simphony.freezedry.gui.DFDirectoryChooserStep;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Jerry Vos
 */
public class DFDataLoaderDirectoryChooserStep extends DFDirectoryChooserStep<DataLoaderWizardModel> implements ActionListener, ClassRetrievable {
	private static final long serialVersionUID = 8299821807954637639L;
	
	public DFDataLoaderDirectoryChooserStep() {
		super("Delimited Files Data Source", "Select the location of the delimited files and their details.", true);
	}

	@Override
	public void prepare() {
		super.prepare();
		
		ContextActionBuilder builder = model.getBuilder();
		if (builder != null && builder instanceof DFDataLoaderContextActionBuilder) {
			DFDataLoaderContextActionBuilder dfBuilder = (DFDataLoaderContextActionBuilder) builder;
			
			if (dfBuilder.getDirectoryName() != null) {
				setDir(dfBuilder.getDirectoryName());
			}
			selectDelimiter(dfBuilder.getDelimiter());
		}
	}

	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		DFDataLoaderContextActionBuilder loader = (DFDataLoaderContextActionBuilder) model.getBuilder();
		loader.setDirectoryName(getFileName());
		loader.setDelimiter(getDelimiter());
	}
	
	public static void main(String[] args) {
		DFDataLoaderDirectoryChooserStep step = new DFDataLoaderDirectoryChooserStep();
		JFrame frame = new JFrame();
		frame.add(step);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
