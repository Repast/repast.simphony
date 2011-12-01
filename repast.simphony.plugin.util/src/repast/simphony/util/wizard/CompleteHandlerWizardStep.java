/*CopyrightHere*/
package repast.simphony.util.wizard;

import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.pietschy.wizard.PanelWizardStep;

public class CompleteHandlerWizardStep extends PanelWizardStep {
	private static final long serialVersionUID = 4245781803370442061L;

	protected DocumentListener documentCompleteListener;

	protected ItemListener itemCompleteListener;

	
	public CompleteHandlerWizardStep(java.lang.String name,
            java.lang.String summary,
            javax.swing.Icon icon) {
		super(name, summary, icon);
		setup();
	}

	public CompleteHandlerWizardStep(String name, String summary) {
		super(name, summary);
		setup();
	}

	public CompleteHandlerWizardStep() {
		super();
		setup();
	}

	private void setup() {
		this.documentCompleteListener = new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateComplete();
			}

			public void insertUpdate(DocumentEvent e) {
				updateComplete();
			}

			public void removeUpdate(DocumentEvent e) {
				updateComplete();
			}
		};
		itemCompleteListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateComplete();
			}
		};
	}
	
	protected void updateComplete() {

	}
	
	protected void addCompleteListener(JTextComponent textComp) {
		textComp.getDocument().addDocumentListener(documentCompleteListener);
	}
	
	protected void addCompleteListener(ItemSelectable selectable) {
		selectable.addItemListener(itemCompleteListener);
	}
}
