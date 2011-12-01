package repast.simphony.dataLoader.ui.wizard.builder;

import javax.swing.JTextField;

import repast.simphony.ui.plugin.editor.UISaver;
import saf.core.ui.util.DoubleDocument;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;

public class RelationshipDescriptorFillerPanel extends UISaver {

	private RelationshipDescriptor descriptor;

	private ContextCreatorCanvas canvas;

	private NetworkDescriptor networkDescriptor;

	private JTextField strengthField;

	DefaultFormBuilder builder;

	public RelationshipDescriptorFillerPanel(ContextCreatorCanvas canvas,
			RelationshipDescriptor descriptor,
			NetworkDescriptor networkDescriptor) {
		this.canvas = canvas;
		this.descriptor = descriptor;
		this.networkDescriptor = networkDescriptor;
		FormLayout layout = FormFactory.createColumnLayout(1, 1,
				new ColumnSpec("right:p"), Sizes.DLUX9, Sizes.DLUX1);

		builder = new DefaultFormBuilder(layout, this);
		builder.setDefaultDialogBorder();
		builder.setLeadingColumnOffset(1);

		builder.appendSeparator(networkDescriptor.getName());
		strengthField = new JTextField();
		strengthField.setDocument(new DoubleDocument());
		strengthField.setText("0.0");
		builder.append("Strength", strengthField);
	}

	@Override
	public String getDialogTitle() {
		return "Add Relationship to " + networkDescriptor.getName();
	}

	@Override
	public boolean save() {
		descriptor.setStrength(Double.parseDouble(strengthField.getText()));
		networkDescriptor.addRelationship(descriptor);
		canvas.addRelationshipDescriptor(networkDescriptor, descriptor);
		return true;
	}

	@Override
	public boolean cancel() {
		return true;
	}

}
