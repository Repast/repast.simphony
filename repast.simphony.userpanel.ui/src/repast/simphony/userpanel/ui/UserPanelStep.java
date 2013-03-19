<<<<<<< HEAD
package repast.simphony.userpanel.ui;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.util.wizard.ModelAwarePanelStep;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

public class UserPanelStep extends ModelAwarePanelStep<UserPanelWizardModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6174640577518209183L;

	public UserPanelStep() {
		super("User Panel", "Select a class implementing UserPanelCreator.");
		initComponents();
	}

	private void initComponents() {
		label1 = new JLabel();
		label2 = new JLabel();
		userPanelCreatorClassBox = new JComboBox();
		
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
						FormSpec.DEFAULT_GROW) }, new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				// FormSpecs.RELATED_GAP_ROWSPEC,
				// FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC }));

		// ---- label1 ----
		label1.setText("UserPanelCreator");
		add(label1, cc.xy(1, 1));
		add(userPanelCreatorClassBox, cc.xy(3, 1));
		
		label2.setText("");
		

		addCompleteListener(userPanelCreatorClassBox);
	}

	private JLabel label1;
	private JLabel label2;
	private JComboBox userPanelCreatorClassBox;
	

	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();

		model.setUserPanelCreatorClass((Class<?>) userPanelCreatorClassBox
				.getSelectedItem());
	}

	@SuppressWarnings("serial")
	@Override
	public void prepare() {
		super.prepare();
		if (model.getClasses().size() > 0) {
			userPanelCreatorClassBox.setModel(new DefaultComboBoxModel(model
					.getClasses().toArray()));
			userPanelCreatorClassBox.setRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					return super.getListCellRendererComponent(list,
							((Class<?>) value).getSimpleName(), index,
							isSelected, cellHasFocus);
				}
			});

			if (model.getUserPanelCreatorClass() != null) {
				userPanelCreatorClassBox.setSelectedItem(model
						.getUserPanelCreatorClass());
			}
		}
		updateComplete();
	}

	@Override
	protected void updateComplete() {
		setComplete(userPanelCreatorClassBox.getSelectedIndex() >= 0);
	}
}
=======
package repast.simphony.userpanel.ui;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

import org.pietschy.wizard.InvalidStateException;

import repast.simphony.util.wizard.ModelAwarePanelStep;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

public class UserPanelStep extends ModelAwarePanelStep<UserPanelWizardModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6174640577518209183L;

	public UserPanelStep() {
		super("User Panel", "Select a class implementing UserPanelCreator.");
		initComponents();
	}

	private void initComponents() {
		label1 = new JLabel();
		label2 = new JLabel();
		userPanelCreatorClassBox = new JComboBox();
		
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.PREF_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT,
						FormSpec.DEFAULT_GROW) }, new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				// FormSpecs.RELATED_GAP_ROWSPEC,
				// FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC }));

		// ---- label1 ----
		label1.setText("UserPanelCreator");
		add(label1, cc.xy(1, 1));
		add(userPanelCreatorClassBox, cc.xy(3, 1));
		
		label2.setText("");
		

		addCompleteListener(userPanelCreatorClassBox);
	}

	private JLabel label1;
	private JLabel label2;
	private JComboBox userPanelCreatorClassBox;
	

	@Override
	public void applyState() throws InvalidStateException {
		super.applyState();

		model.setUserPanelCreatorClass((Class<?>) userPanelCreatorClassBox
				.getSelectedItem());
	}

	@SuppressWarnings("serial")
	@Override
	public void prepare() {
		super.prepare();
		if (model.getClasses().size() > 0) {
			userPanelCreatorClassBox.setModel(new DefaultComboBoxModel(model
					.getClasses().toArray()));
			userPanelCreatorClassBox.setRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					return super.getListCellRendererComponent(list,
							((Class<?>) value).getSimpleName(), index,
							isSelected, cellHasFocus);
				}
			});

			if (model.getUserPanelCreatorClass() != null) {
				userPanelCreatorClassBox.setSelectedItem(model
						.getUserPanelCreatorClass());
			}
		}
		updateComplete();
	}

	@Override
	protected void updateComplete() {
		setComplete(userPanelCreatorClassBox.getSelectedIndex() >= 0);
	}
}
>>>>>>> branch 'system_dynamics' of ssh://bragen@git.code.sf.net/p/repast/repast-simphony
