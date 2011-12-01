/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import org.pietschy.wizard.InvalidStateException;
import repast.simphony.freezedry.gui.DFDirectoryChooserStep;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * @author Jerry Vos
 */
public class DFFreezerDirectoryChooserStep extends DFDirectoryChooserStep<FreezeDryWizardModel> implements ActionListener {
	private static final long serialVersionUID = 8299821807954637639L;
	
	
	public DFFreezerDirectoryChooserStep() {
		super("Delimited File Freezer", "Select the location of the delimited files and their details.", false);
	}

	public void prepare() {
		super.prepare();
		
		DataSourceBuilder builder = model.getBuilder();
		if (builder != null && builder instanceof DFDataSourceBuilder) {
			DFDataSourceBuilder dfBuilder = (DFDataSourceBuilder) builder;
			
			if (dfBuilder.getDirectoryName() != null) {
				setDir(dfBuilder.getDirectoryName());
			}
			selectDelimiter(dfBuilder.getDelimiter());
		}
	}
	
	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();
		DFDataSourceBuilder loader = (DFDataSourceBuilder) model.getBuilder();
		loader.setDirectoryName(getFileName());
		loader.setDelimiter(getDelimiter());
	}
	
	public static void main(String[] args) {
		DFFreezerDirectoryChooserStep step = new DFFreezerDirectoryChooserStep(/*null*/);
		JFrame frame = new JFrame();
		frame.add(step);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
