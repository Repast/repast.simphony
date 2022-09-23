package repast.simphony.visualization.gui.styleBuilder;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.measure.Quantity;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.visualization.editedStyle.EditedEdgeStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.engine.DisplayDescriptor;
import simphony.util.messages.MessageCenter;

/**
 * Abstract dialog for edited styles
 * 
 * @author Eric Tatara
 * 
 * TODO Currently only used by edited edge dialog - we should clean up the others
 *
 */
public abstract class AbstractStyleDialog extends JDialog{
	protected static final Set<Class> pTypes = new HashSet<Class>();
	
	static {
		pTypes.add(int.class);
		pTypes.add(double.class);
		pTypes.add(float.class);
		pTypes.add(long.class);
		pTypes.add(byte.class);
		pTypes.add(short.class);
		pTypes.add(Quantity.class);
	}
	
	protected EditedEdgeStyleData userStyleData;
	protected String userStyleName;
	protected String netID;
	protected boolean save = false;	
	protected List<String> methodList;
	protected DisplayDescriptor descriptor;

	protected PreviewEdge preview;

	protected DefaultComboBoxModel sizeModel;
	protected DefaultComboBoxModel sizeMinModel;
	protected DefaultComboBoxModel sizeMaxModel;
	protected DefaultComboBoxModel sizeScaleModel;

	protected DefaultComboBoxModel variableIconRedColorValueModel;
	protected DefaultComboBoxModel variableIconGreenColorValueModel;
	protected DefaultComboBoxModel variableIconBlueColorValueModel;
	protected DefaultComboBoxModel variableIconRedColorMinModel;
	protected DefaultComboBoxModel variableIconGreenColorMinModel;
	protected DefaultComboBoxModel variableIconBlueColorMinModel;
	protected DefaultComboBoxModel variableIconRedColorMaxModel;
	protected DefaultComboBoxModel variableIconGreenColorMaxModel;
	protected DefaultComboBoxModel variableIconBlueColorMaxModel;
	protected DefaultComboBoxModel variableIconRedColorScaleModel;
	protected DefaultComboBoxModel variableIconGreenColorScaleModel;
	protected DefaultComboBoxModel variableIconBlueColorScaleModel;
	
	public AbstractStyleDialog(Frame owner) {
		super(owner);
		methodList = new ArrayList<String>();
	}
	
	public AbstractStyleDialog(Dialog owner) {
		super(owner);
		methodList = new ArrayList<String>();
	}
	
	public String getUserStyleName() {
		return userStyleName;
	}

	public abstract void init(ContextData context, String netID, String userStyleName,
			DisplayDescriptor descriptor);

	public boolean doSave() {
    return save;
  }
	
	protected void cancelButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	protected void okButtonActionPerformed(ActionEvent e) {
		save = true;
		writeStyleData();
		dispose();
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
	
	private String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t += st.nextElement();
		return t;
	}
	
	protected boolean isUserTypedNumber(Object obj) {
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
				MessageCenter.getMessageCenter(AbstractStyleDialog.class).error(
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
}
