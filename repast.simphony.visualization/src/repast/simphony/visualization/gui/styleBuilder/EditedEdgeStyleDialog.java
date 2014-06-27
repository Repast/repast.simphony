package repast.simphony.visualization.gui.styleBuilder;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.jscience.physics.amount.Amount;

import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.ui.widget.SquareIcon;
import repast.simphony.visualization.editedStyle.DefaultEditedEdgeStyleData2D;
import repast.simphony.visualization.editedStyle.DefaultEditedEdgeStyleData3D;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.editedStyle.LineStyle;
import repast.simphony.visualization.engine.CartesianDisplayDescriptor;
import repast.simphony.visualization.engine.DisplayType;
import simphony.util.messages.MessageCenter;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * @author Eric Tatara
 */
public class EditedEdgeStyleDialog extends JDialog {
	private boolean save = false;
	private EditedEdgeStyleData userStyleData;
	private static final Set<Class> pTypes = new HashSet<Class>();
	private List<String> methodList;

	private DefaultComboBoxModel sizeModel;
	private DefaultComboBoxModel sizeMinModel;
	private DefaultComboBoxModel sizeMaxModel;
	private DefaultComboBoxModel sizeScaleModel;

	private DefaultComboBoxModel shapeModel;

	private DefaultComboBoxModel variableIconRedColorValueModel;
	private DefaultComboBoxModel variableIconGreenColorValueModel;
	private DefaultComboBoxModel variableIconBlueColorValueModel;
	private DefaultComboBoxModel variableIconRedColorMinModel;
	private DefaultComboBoxModel variableIconGreenColorMinModel;
	private DefaultComboBoxModel variableIconBlueColorMinModel;
	private DefaultComboBoxModel variableIconRedColorMaxModel;
	private DefaultComboBoxModel variableIconGreenColorMaxModel;
	private DefaultComboBoxModel variableIconBlueColorMaxModel;
	private DefaultComboBoxModel variableIconRedColorScaleModel;
	private DefaultComboBoxModel variableIconGreenColorScaleModel;
	private DefaultComboBoxModel variableIconBlueColorScaleModel;

	private String netID;
	private String userStyleName;
	CartesianDisplayDescriptor descriptor;

	private PreviewEdge preview;

	static {
		pTypes.add(int.class);
		pTypes.add(double.class);
		pTypes.add(float.class);
		pTypes.add(long.class);
		pTypes.add(byte.class);
		pTypes.add(short.class);
		pTypes.add(Amount.class);
	}

	public EditedEdgeStyleDialog(Frame owner) {
		super(owner);
		methodList = new ArrayList<String>();
	}

	public EditedEdgeStyleDialog(Dialog owner) {
		super(owner);
		methodList = new ArrayList<String>();
	}

	public void init(ContextData context, String netID, String userStyleName,
			CartesianDisplayDescriptor descriptor) {
		this.netID = netID;
		this.userStyleName = userStyleName;
		for (ProjectionData proj: context.projections()) {
			if (proj.getType().equals(ProjectionData.NETWORK_TYPE)) {

				for (Attribute attrib : proj.attributes()) {
					findAttributes(attrib);
				}
			}
		}
		if (!methodList.contains("getWeight"))
			methodList.add("getWeight");

		String displayType = descriptor.getDisplayType();
		userStyleData = EditedStyleUtils.getEdgeStyle(descriptor.getNetworkEditedStyleName(netID));

		// TODO Projections init from viz registry data
		if (displayType.equals(DisplayType.TWO_D)) {
			if (userStyleData == null)
				userStyleData = new DefaultEditedEdgeStyleData2D();
			
			shapeModel = new DefaultComboBoxModel(new Object[] { 
					new LineStyleIcon(LineStyle.SOLID, 3f),
					new LineStyleIcon(LineStyle.DASH, 3f), 
					new LineStyleIcon(LineStyle.DASH_DOT, 3f),
					new LineStyleIcon(LineStyle.DASH_DASH_DOT, 3f) });

		} else {
			if (userStyleData == null)
				userStyleData = new DefaultEditedEdgeStyleData3D();
			shapeModel = new DefaultComboBoxModel(new Object[] { new LineStyleIcon(LineStyle.SOLID, 3f) });
		}

		for (int i = 0; i < shapeModel.getSize(); i++) {
			LineStyleIcon icon = (LineStyleIcon) shapeModel.getElementAt(i);

			if (icon.getLineStyle().equals(userStyleData.getLineStyle())) {
				shapeModel.setSelectedItem(icon);
				break;
			}
		}

		sizeModel = new DefaultComboBoxModel();
		sizeMinModel = new DefaultComboBoxModel();
		sizeMaxModel = new DefaultComboBoxModel();
		sizeScaleModel = new DefaultComboBoxModel();
		variableIconRedColorValueModel = new DefaultComboBoxModel();
		variableIconGreenColorValueModel = new DefaultComboBoxModel();
		variableIconBlueColorValueModel = new DefaultComboBoxModel();
		variableIconRedColorMinModel = new DefaultComboBoxModel();
		variableIconGreenColorMinModel = new DefaultComboBoxModel();
		variableIconBlueColorMinModel = new DefaultComboBoxModel();
		variableIconRedColorMaxModel = new DefaultComboBoxModel();
		variableIconGreenColorMaxModel = new DefaultComboBoxModel();
		variableIconBlueColorMaxModel = new DefaultComboBoxModel();
		variableIconRedColorScaleModel = new DefaultComboBoxModel();
		variableIconGreenColorScaleModel = new DefaultComboBoxModel();
		variableIconBlueColorScaleModel = new DefaultComboBoxModel();

		sizeMinModel.addElement(userStyleData.getSizeMin());
		sizeMaxModel.addElement(userStyleData.getSizeMax());
		sizeScaleModel.addElement(userStyleData.getSizeScale());

		// Add available methods to appropriate combo box models
		for (String method : methodList) {
			sizeModel.addElement(method);
			sizeMinModel.addElement(method);
			sizeMaxModel.addElement(method);
			sizeScaleModel.addElement(method);

			variableIconRedColorValueModel.addElement(method);
			variableIconGreenColorValueModel.addElement(method);
			variableIconBlueColorValueModel.addElement(method);
			// variableIconRedColorMinModel.addElement(method);
			// variableIconGreenColorMinModel.addElement(method);
			// variableIconBlueColorMinModel.addElement(method);
			// variableIconRedColorMaxModel.addElement(method);
			// variableIconGreenColorMaxModel.addElement(method);
			// variableIconBlueColorMaxModel.addElement(method);
			// variableIconRedColorScaleModel.addElement(method);
			// variableIconGreenColorScaleModel.addElement(method);
			// variableIconBlueColorScaleModel.addElement(method);
		}

		if (userStyleData.getSizeMethodName() != null)
			sizeModel.setSelectedItem(userStyleData.getSizeMethodName());
		else {
			sizeModel.addElement(userStyleData.getSize());
			sizeModel.setSelectedItem(userStyleData.getSize());
		}

		if (userStyleData.getRedMethod() != null)
			variableIconRedColorValueModel.setSelectedItem(userStyleData.getRedMethod());
		else {
			variableIconRedColorValueModel.addElement(userStyleData.getColor()[0]);
			variableIconRedColorValueModel.setSelectedItem(userStyleData.getColor()[0]);
		}
		if (userStyleData.getGreenMethod() != null)
			variableIconGreenColorValueModel.setSelectedItem(userStyleData.getGreenMethod());
		else {
			variableIconGreenColorValueModel.addElement(userStyleData.getColor()[1]);
			variableIconGreenColorValueModel.setSelectedItem(userStyleData.getColor()[1]);
		}
		if (userStyleData.getBlueMethod() != null)
			variableIconBlueColorValueModel.setSelectedItem(userStyleData.getBlueMethod());
		else {
			variableIconBlueColorValueModel.addElement(userStyleData.getColor()[2]);
			variableIconBlueColorValueModel.setSelectedItem(userStyleData.getColor()[2]);
		}

		variableIconRedColorMinModel.addElement(userStyleData.getColorMin()[0]);
		variableIconRedColorMinModel.setSelectedItem(userStyleData.getColorMin()[0]);
		variableIconGreenColorMinModel.addElement(userStyleData.getColorMin()[1]);
		variableIconGreenColorMinModel.setSelectedItem(userStyleData.getColorMin()[1]);
		variableIconBlueColorMinModel.addElement(userStyleData.getColorMin()[2]);
		variableIconBlueColorMinModel.setSelectedItem(userStyleData.getColorMin()[2]);

		variableIconRedColorMaxModel.addElement(userStyleData.getColorMax()[0]);
		variableIconRedColorMaxModel.setSelectedItem(userStyleData.getColorMax()[0]);
		variableIconGreenColorMaxModel.addElement(userStyleData.getColorMax()[1]);
		variableIconGreenColorMaxModel.setSelectedItem(userStyleData.getColorMax()[1]);
		variableIconBlueColorMaxModel.addElement(userStyleData.getColorMax()[2]);
		variableIconBlueColorMaxModel.setSelectedItem(userStyleData.getColorMax()[2]);

		variableIconRedColorScaleModel.addElement(userStyleData.getColorScale()[0]);
		variableIconRedColorScaleModel.setSelectedItem(userStyleData.getColorScale()[0]);
		variableIconGreenColorScaleModel.addElement(userStyleData.getColorScale()[1]);
		variableIconGreenColorScaleModel.setSelectedItem(userStyleData.getColorScale()[1]);
		variableIconBlueColorScaleModel.addElement(userStyleData.getColorScale()[2]);
		variableIconBlueColorScaleModel.setSelectedItem(userStyleData.getColorScale()[2]);

		initComponents();
		initMyComponents(displayType);
	}

	/**
	 * Method loads an edge class and adds get methods or just adds the getWeight
	 * method as default
	 * 
	 * @param a
	 *          a SAttribute from the score file
	 */
	public void findAttributes(Attribute a) {
		if (a.getId().equalsIgnoreCase("edgeClass")) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(a.getValue());
			} catch (ClassNotFoundException e) {
				MessageCenter.getMessageCenter(EditedEdgeStyleDialog.class).error(
						"Problems finding" + "class to load", e);
				e.printStackTrace();
			}
			if (clazz != null) {
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					if (method.getName().startsWith("get") && pTypes.contains(method.getReturnType()))
						methodList.add(method.getName());
				}
			}
		}
	}

	public void initMyComponents(String displayType) {
		CellConstraints cc = new CellConstraints();

		shapeComboBox.setModel(shapeModel);
		sizeComboBox.setModel(sizeModel);
		sizeMinComboBox.setModel(sizeMinModel);
		sizeMaxComboBox.setModel(sizeMaxModel);
		sizeScaleComboBox.setModel(sizeScaleModel);

		redValueComboBox.setModel(variableIconRedColorValueModel);
		greenValueComboBox.setModel(variableIconGreenColorValueModel);
		blueValueComboBox.setModel(variableIconBlueColorValueModel);
		redMinComboBox.setModel(variableIconRedColorMinModel);
		greenMinComboBox.setModel(variableIconGreenColorMinModel);
		blueMinComboBox.setModel(variableIconBlueColorMinModel);
		redMaxComboBox.setModel(variableIconRedColorMaxModel);
		greenMaxComboBox.setModel(variableIconGreenColorMaxModel);
		blueMaxComboBox.setModel(variableIconBlueColorMaxModel);
		redScaleComboBox.setModel(variableIconRedColorScaleModel);
		greenScaleComboBox.setModel(variableIconGreenColorScaleModel);
		blueScaleComboBox.setModel(variableIconBlueColorScaleModel);

		// TODO Projection: init from viz registry data
		if (displayType.equals(DisplayType.TWO_D)) {
			this.setTitle("2D Edge Style Editor");
			preview = new PreviewEdge2D();
			previewPanel.add((PreviewEdge2D) preview, cc.xy(1, 1));
		} else {
			this.setTitle("3D Edge Style Editor");
			preview = new PreviewEdge3D();
			shapeComboBox.setEnabled(false); // TODO for now there are no 3D line
			// style
		}

		// TODO
		// preview.setMark((userStyleData).getLineStyle());
		// preview.setMarkSize(userStyleData.getSize());

		float iconColor[] = userStyleData.getColor();
		Color iconPaint = new Color(iconColor[0], iconColor[1], iconColor[2]);
		iconColorbutton.setIcon(new SquareIcon(15, 15, iconPaint));

	}

	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t += st.nextElement();
		return t;
	}

	public void writeStyleData() {
		XStream xstream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
			protected boolean useXStream11XmlFriendlyMapper() {
				return true;
			}
		};

		File file = null;
		try {
			File dir = new File(EditedStyleUtils.getStyleDirName());

			if (!dir.exists())
				dir.mkdir();

			if (userStyleName != null)
				file = new File(dir, userStyleName);

			else {
				int cnt = 0;
				userStyleName = removeSpaces(netID) + ".style_" + cnt + ".xml";

				file = new File(dir, userStyleName);
				while (file.exists()) {
					userStyleName = removeSpaces(netID) + ".style_" + cnt + ".xml";

					file = new File(dir, userStyleName);
					cnt++;
				}
			}

			FileWriter fw = new FileWriter(file);

			xstream.toXML(userStyleData, fw);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean doSave() {
		return save;
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		save = true;
		writeStyleData();
		dispose();
	}

	private void shapeComboBoxActionPerformed(ActionEvent e) {
		userStyleData.setLineStyle(((LineStyleIcon) shapeComboBox.getSelectedItem()).getLineStyle());

		// preview.setMark((String)shapeComboBox.getSelectedItem());
	}

	private void sizeComboBoxActionPerformed(ActionEvent e) {
		Object selection = sizeComboBox.getSelectedItem();

		if (selection instanceof Number) {
			userStyleData.setSize((Float) selection);
			// preview.setMarkSize((Float)selection);
			userStyleData.setSizeMethodName(null);
		} else if (isUserTypedNumber(selection)) {
			userStyleData.setSize(new Float((String) selection));
			// preview.setMarkSize(new Float((String)selection));
			userStyleData.setSizeMethodName(null);
		} else
			userStyleData.setSizeMethodName((String) selection);
	}

	// private void sizeComboBoxActionPerformed(ActionEvent e) {

	// Object selection = sizeComboBox.getSelectedItem();

	// if (selection instanceof Number){
	// userStyleData.setSize((Float)selection);
	// //preview.setMarkSize((Float)selection);
	// }
	// else
	// userStyleData.setSizeMethodName((String)selection);
	// }

	private void sizeMinComboBoxActionPerformed(ActionEvent e) {
		Object selection = sizeMinComboBox.getSelectedItem();

		if (selection instanceof Number) {
			userStyleData.setSizeMin((Float) selection);
			userStyleData.setSizeMinMethodName(null);
		} else if (isUserTypedNumber(selection)) {
			userStyleData.setSizeMin(new Float((String) selection));
			userStyleData.setSizeMinMethodName(null);
		} else
			userStyleData.setSizeMinMethodName((String) selection);
	}

	private void sizeMaxComboBoxActionPerformed(ActionEvent e) {
		Object selection = sizeMaxComboBox.getSelectedItem();

		if (selection instanceof Number) {
			userStyleData.setSizeMax((Float) selection);
			userStyleData.setSizeMaxMethodName(null);
		} else if (isUserTypedNumber(selection)) {
			userStyleData.setSizeMax(new Float((String) selection));
			userStyleData.setSizeMaxMethodName(null);
		} else
			userStyleData.setSizeMaxMethodName((String) selection);
	}

	private void sizeScaleComboBoxActionPerformed(ActionEvent e) {
		Object selection = sizeScaleComboBox.getSelectedItem();

		if (selection instanceof Number) {
			userStyleData.setSizeScale((Float) selection);
			userStyleData.setSizeScaleMethodName(null);
		} else if (isUserTypedNumber(selection)) {
			userStyleData.setSizeScale(new Float((String) selection));
			userStyleData.setSizeScaleMethodName(null);
		} else
			userStyleData.setSizeScaleMethodName((String) selection);
	}

	private void iconColorbuttonActionPerformed(ActionEvent e) {
		float iconColor[] = userStyleData.getColor();
		Color iconPaint = new Color(iconColor[0], iconColor[1], iconColor[2]);

		Color color = JColorChooser.showDialog(EditedEdgeStyleDialog.this, "Choose an Icon Color",
				iconPaint);
		if (color != null) {
			float col[] = color.getRGBColorComponents(null);
			userStyleData.setColor(col);
			userStyleData.setRedMethod(null);
			userStyleData.setGreenMethod(null);
			userStyleData.setBlueMethod(null);
			variableIconRedColorValueModel.addElement(col[0]);
			variableIconGreenColorValueModel.addElement(col[1]);
			variableIconBlueColorValueModel.addElement(col[2]);
			variableIconRedColorValueModel.setSelectedItem(col[0]);
			variableIconGreenColorValueModel.setSelectedItem(col[1]);
			variableIconBlueColorValueModel.setSelectedItem(col[2]);

			iconColorbutton.setIcon(new SquareIcon(20, 20, color));
			preview.setColor(color);
		}
	}

	private void redValueComboBoxActionPerformed(ActionEvent e) {
		Object selection = redValueComboBox.getSelectedItem();

		if (selection instanceof Number) {

		} else if (isUserTypedNumber(selection)) {

		} else
			userStyleData.setRedMethod((String) selection);
	}

	private void greenValueComboBoxActionPerformed(ActionEvent e) {
		Object selection = greenValueComboBox.getSelectedItem();

		if (selection instanceof Number) {

		} else if (isUserTypedNumber(selection)) {

		} else
			userStyleData.setGreenMethod((String) selection);
	}

	private void blueValueComboBoxActionPerformed(ActionEvent e) {
		Object selection = blueValueComboBox.getSelectedItem();

		if (selection instanceof Number) {

		} else if (isUserTypedNumber(selection)) {

		} else
			userStyleData.setBlueMethod((String) selection);
	}

	private void redMinComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorMin();
		c[0] = (Float) redMinComboBox.getSelectedItem();
		userStyleData.setColorMin(c);
	}

	private void greenMinComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorMin();
		c[1] = (Float) greenMinComboBox.getSelectedItem();
		userStyleData.setColorMin(c);
	}

	private void blueMinComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorMin();
		c[2] = (Float) blueMinComboBox.getSelectedItem();
		userStyleData.setColorMin(c);
	}

	private void redMaxComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorMax();
		c[0] = (Float) redMaxComboBox.getSelectedItem();
		userStyleData.setColorMax(c);
	}

	private void greenMaxComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorMax();
		c[1] = (Float) greenMaxComboBox.getSelectedItem();
		userStyleData.setColorMax(c);
	}

	private void blueMaxComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorMax();
		c[2] = (Float) blueMaxComboBox.getSelectedItem();
		userStyleData.setColorMax(c);
	}

	private void redScaleComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorScale();
		c[0] = (Float) redScaleComboBox.getSelectedItem();
		userStyleData.setColorScale(c);
	}

	private void greenScaleComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorScale();
		c[1] = (Float) greenScaleComboBox.getSelectedItem();
		userStyleData.setColorScale(c);
	}

	private void blueScaleComboBoxActionPerformed(ActionEvent e) {
		float[] c = userStyleData.getColorScale();
		c[2] = (Float) blueScaleComboBox.getSelectedItem();
		userStyleData.setColorScale(c);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		panel1 = new JPanel();
		shapePanel = new JPanel();
		shapeComboBox = new JComboBox();
		iconColorbutton = new JButton();
		panel3 = new JPanel();
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		sizeComboBox = new JComboBox();
		sizeMinComboBox = new JComboBox();
		sizeMaxComboBox = new JComboBox();
		sizeScaleComboBox = new JComboBox();
		panel6 = new JPanel();
		label15 = new JLabel();
		label16 = new JLabel();
		label17 = new JLabel();
		label18 = new JLabel();
		label12 = new JLabel();
		redValueComboBox = new JComboBox();
		redMinComboBox = new JComboBox();
		redMaxComboBox = new JComboBox();
		redScaleComboBox = new JComboBox();
		label13 = new JLabel();
		greenValueComboBox = new JComboBox();
		greenMinComboBox = new JComboBox();
		greenMaxComboBox = new JComboBox();
		greenScaleComboBox = new JComboBox();
		label14 = new JLabel();
		blueValueComboBox = new JComboBox();
		blueMinComboBox = new JComboBox();
		blueMaxComboBox = new JComboBox();
		blueScaleComboBox = new JComboBox();
		previewPanel = new JPanel();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
			setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG);
			dialogPane.setLayout(new BorderLayout());

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_PAD);
				buttonBar.setLayout(new FormLayout(
						new ColumnSpec[] { FormSpecs.GLUE_COLSPEC, FormSpecs.BUTTON_COLSPEC,
								FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.BUTTON_COLSPEC }, RowSpec
								.decodeSpecs("pref")));

				// ---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);

			// ======== panel1 ========
			{
				panel1.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.GROWING_BUTTON_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC }, new RowSpec[] {
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC,
						FormSpecs.LINE_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC }));

				// ======== shapePanel ========
				{
					shapePanel.setBorder(new TitledBorder("Edge Style and Color"));
					shapePanel.setLayout(new FormLayout(new ColumnSpec[] { new ColumnSpec(Sizes.dluX(66)),
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.GROWING_BUTTON_COLSPEC },
							RowSpec.decodeSpecs("default")));

					// ---- shapeComboBox ----
					shapeComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							shapeComboBoxActionPerformed(e);
						}
					});
					shapePanel.add(shapeComboBox, cc.xy(1, 1));

					// ---- iconColorbutton ----
					iconColorbutton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							iconColorbuttonActionPerformed(e);
						}
					});
					shapePanel.add(iconColorbutton, cc.xy(5, 1));
				}
				panel1.add(shapePanel, cc.xywh(1, 1, 11, 1));

				// ======== panel3 ========
				{
					panel3.setBorder(new TitledBorder("Edge Thickness"));
					panel3.setLayout(new FormLayout(new ColumnSpec[] {ColumnSpec.decode("max(pref;66dlu)"),
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC },
							new RowSpec[] { FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC }));

					// ---- label1 ----
					label1.setText("Value");
					panel3.add(label1, cc.xy(1, 1));

					// ---- label2 ----
					label2.setText("Minimum");
					panel3.add(label2, cc.xy(5, 1));

					// ---- label3 ----
					label3.setText("Maximum");
					panel3.add(label3, cc.xy(9, 1));

					// ---- label4 ----
					label4.setText("Scaling");
					panel3.add(label4, cc.xy(13, 1));

					// ---- sizeComboBox ----
					sizeComboBox.setEditable(true);
					sizeComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							sizeComboBoxActionPerformed(e);
						}
					});
					panel3.add(sizeComboBox, cc.xy(1, 3));

					// ---- sizeMinComboBox ----
					sizeMinComboBox.setEditable(true);
					sizeMinComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							sizeMinComboBoxActionPerformed(e);
						}
					});
					panel3.add(sizeMinComboBox, cc.xy(5, 3));

					// ---- sizeMaxComboBox ----
					sizeMaxComboBox.setEditable(true);
					sizeMaxComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							sizeMaxComboBoxActionPerformed(e);
						}
					});
					panel3.add(sizeMaxComboBox, cc.xy(9, 3));

					// ---- sizeScaleComboBox ----
					sizeScaleComboBox.setEditable(true);
					sizeScaleComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							sizeScaleComboBoxActionPerformed(e);
						}
					});
					panel3.add(sizeScaleComboBox, cc.xy(13, 3));
				}
				panel1.add(panel3, cc.xywh(1, 3, 11, 1));

				// ======== panel6 ========
				{
					panel6.setBorder(new TitledBorder("Variable Icon Color"));
					panel6.setLayout(new FormLayout(new ColumnSpec[] { FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
							FormSpecs.LABEL_COMPONENT_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC },
							new RowSpec[] { FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC, FormSpecs.LINE_GAP_ROWSPEC,
							FormSpecs.DEFAULT_ROWSPEC }));

					// ---- label15 ----
					label15.setText("Value");
					panel6.add(label15, cc.xy(3, 1));

					// ---- label16 ----
					label16.setText("Minimum");
					panel6.add(label16, cc.xy(7, 1));

					// ---- label17 ----
					label17.setText("Maximum");
					panel6.add(label17, cc.xy(11, 1));

					// ---- label18 ----
					label18.setText("Scaling");
					panel6.add(label18, cc.xy(15, 1));

					// ---- label12 ----
					label12.setText("Red");
					panel6.add(label12, cc.xy(1, 3));

					// ---- redValueComboBox ----
					redValueComboBox.setEditable(true);
					redValueComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							redValueComboBoxActionPerformed(e);
						}
					});
					panel6.add(redValueComboBox, cc.xy(3, 3));

					// ---- redMinComboBox ----
					redMinComboBox.setEditable(true);
					redMinComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							redMinComboBoxActionPerformed(e);
						}
					});
					panel6.add(redMinComboBox, cc.xy(7, 3));

					// ---- redMaxComboBox ----
					redMaxComboBox.setEditable(true);
					redMaxComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							redMaxComboBoxActionPerformed(e);
						}
					});
					panel6.add(redMaxComboBox, cc.xy(11, 3));

					// ---- redScaleComboBox ----
					redScaleComboBox.setEditable(true);
					redScaleComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							redScaleComboBoxActionPerformed(e);
						}
					});
					panel6.add(redScaleComboBox, cc.xy(15, 3));

					// ---- label13 ----
					label13.setText("Green");
					panel6.add(label13, cc.xy(1, 5));

					// ---- greenValueComboBox ----
					greenValueComboBox.setEditable(true);
					greenValueComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							greenValueComboBoxActionPerformed(e);
						}
					});
					panel6.add(greenValueComboBox, cc.xy(3, 5));

					// ---- greenMinComboBox ----
					greenMinComboBox.setEditable(true);
					greenMinComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							greenMinComboBoxActionPerformed(e);
						}
					});
					panel6.add(greenMinComboBox, cc.xy(7, 5));

					// ---- greenMaxComboBox ----
					greenMaxComboBox.setEditable(true);
					greenMaxComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							greenMaxComboBoxActionPerformed(e);
						}
					});
					panel6.add(greenMaxComboBox, cc.xy(11, 5));

					// ---- greenScaleComboBox ----
					greenScaleComboBox.setEditable(true);
					greenScaleComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							greenScaleComboBoxActionPerformed(e);
						}
					});
					panel6.add(greenScaleComboBox, cc.xy(15, 5));

					// ---- label14 ----
					label14.setText("Blue");
					panel6.add(label14, cc.xy(1, 7));

					// ---- blueValueComboBox ----
					blueValueComboBox.setEditable(true);
					blueValueComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							blueValueComboBoxActionPerformed(e);
						}
					});
					panel6.add(blueValueComboBox, cc.xy(3, 7));

					// ---- blueMinComboBox ----
					blueMinComboBox.setEditable(true);
					blueMinComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							blueMinComboBoxActionPerformed(e);
						}
					});
					panel6.add(blueMinComboBox, cc.xy(7, 7));

					// ---- blueMaxComboBox ----
					blueMaxComboBox.setEditable(true);
					blueMaxComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							blueMaxComboBoxActionPerformed(e);
						}
					});
					panel6.add(blueMaxComboBox, cc.xy(11, 7));

					// ---- blueScaleComboBox ----
					blueScaleComboBox.setEditable(true);
					blueScaleComboBox.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							blueScaleComboBoxActionPerformed(e);
						}
					});
					panel6.add(blueScaleComboBox, cc.xy(15, 7));
				}
				panel1.add(panel6, cc.xy(1, 5));

				// ======== previewPanel ========
				{
					previewPanel.setBorder(new TitledBorder("Icon Preview"));
					previewPanel.setLayout(new FormLayout("default:grow", "default:grow"));
				}
				panel1.add(previewPanel, cc.xywh(1, 15, 11, 1));
			}
			dialogPane.add(panel1, BorderLayout.CENTER);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		setSize(615, 530);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	private JPanel panel1;
	private JPanel shapePanel;
	private JComboBox shapeComboBox;
	private JButton iconColorbutton;
	private JPanel panel3;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JComboBox sizeComboBox;
	private JComboBox sizeMinComboBox;
	private JComboBox sizeMaxComboBox;
	private JComboBox sizeScaleComboBox;
	private JPanel panel6;
	private JLabel label15;
	private JLabel label16;
	private JLabel label17;
	private JLabel label18;
	private JLabel label12;
	private JComboBox redValueComboBox;
	private JComboBox redMinComboBox;
	private JComboBox redMaxComboBox;
	private JComboBox redScaleComboBox;
	private JLabel label13;
	private JComboBox greenValueComboBox;
	private JComboBox greenMinComboBox;
	private JComboBox greenMaxComboBox;
	private JComboBox greenScaleComboBox;
	private JLabel label14;
	private JComboBox blueValueComboBox;
	private JComboBox blueMinComboBox;
	private JComboBox blueMaxComboBox;
	private JComboBox blueScaleComboBox;
	private JPanel previewPanel;

	// JFormDesigner - End of variables declaration //GEN-END:variables

	public String getUserStyleName() {
		return userStyleName;
	}

	class LineStyleIcon implements Icon {

		protected float[] dash;
		protected float width;
		LineStyle lineStyle;

		public LineStyleIcon(LineStyle lineStyle, float width) {

			if (lineStyle == LineStyle.DASH)
				dash = new float[] { 10f, 10f };
			else if (lineStyle == LineStyle.DASH_DOT)
				dash = new float[] { 10f, 4f, 2f, 4f };
			else if (lineStyle == LineStyle.DASH_DASH_DOT)
				dash = new float[] { 10f, 4f, 10f, 4f, 2f, 4f };
			else
				dash = null;

			this.width = width;
			this.lineStyle = lineStyle;
		}

		public int getIconHeight() {
			return 20;
		}

		public int getIconWidth() {
			return 100;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {

			g.setColor(Color.WHITE);
			g.fillRect(x, y, getIconWidth(), getIconHeight());
			g.setColor(Color.BLACK);

			((Graphics2D) g).setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT,
					BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));

			g.drawLine(0, getIconHeight() / 2, getIconWidth(), getIconHeight() / 2);
		}

		public LineStyle getLineStyle() {
			return lineStyle;
		}
	}

	private boolean isUserTypedNumber(Object obj) {
		String validChars = "0123456789";
		boolean isNumber = true;

		if (obj instanceof String) {

			String s = (String) obj;

			char c = s.charAt(0);

			if (validChars.indexOf(c) == -1)
				return false;

			else
				return true;
		}
		return false;
	}
}
