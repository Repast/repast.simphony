/**
 * 
 */
package repast.simphony.ui.parameters;

import java.awt.BorderLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.stream.XMLStreamException;

import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.Parameters;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Nick Collier
 */
public class ParametersUI {

	public static final String DEFAULT_PARAM_GROUP = "Other";
	public static final String NAME = "NAME";
	public static final double LAST = Double.MAX_VALUE;

	private Map<String, List<ParameterBinder>> bindersMap = new LinkedHashMap<String, List<ParameterBinder>>();
	// reference to the current set of parameters used duing a run.
	private Parameters params;
	private JPanel topPanel;

	public ParametersUI(Parameters params) {
		this.params = params;
	}

	public void addBinder(String group, ParameterBinder creator, double displayOrder) {
		creator.setDisplayOrder(displayOrder);
    List<ParameterBinder> binders = bindersMap.get(group);
    if (binders == null) {
      binders = new ArrayList<ParameterBinder>();
      bindersMap.put(group, binders);
    }
    binders.add(creator);
  }

	/**
	 * Updates the panel with the latest parameters from the parameters file.
	 * 
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 */
	public void updatePanel(File paramsFile) throws FileNotFoundException, XMLStreamException {
		createPanel(paramsFile);
	}

	/**
	 * Creates a JPanel that will display the specified parameters. This assumes the
	 * passed in parameters are identical to those used to make the creators that
	 * were added with addBinder.
	 * 
	 * @param params
	 * 
	 * @return the created JPanel.
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 */
	public JPanel createPanel(String parametersFile) throws FileNotFoundException, XMLStreamException {
		return createPanel(new File(parametersFile));
	}

	/**
	 * Creates a JPanel that will display the specified parameters. This assumes the
	 * passed in parameters are identical to those used to make the creators that
	 * were added with addCreator.
	 * 
	 * @param params
	 * 
	 * @return the created JPanel.
	 * @throws XMLStreamException
	 * @throws FileNotFoundException
	 */
	public JPanel createPanel(File parametersFile) throws FileNotFoundException, XMLStreamException {

		bindersMap.clear();

		ParametersUIParser parser = new ParametersUIParser();
		parser.read(this, parametersFile);

		if (bindersMap.size() == 1) {
			return createPanel(params, bindersMap.values().iterator().next());
		}

// TODO use this when have sections in the parameters that can be 
// put into or demarkated in someway.
//    JPanel panel = new JPanel(new BorderLayout());
//    JTabbedPane tabs = new JTabbedPane();
//    panel.add(tabs, BorderLayout.CENTER);
//
//    List<String> groups = new ArrayList<String>(bindersMap.keySet());
//    Collections.reverse(groups);
//    for (String group : groups) {
//      tabs.addTab(group, createPanel(params, bindersMap.get(group)));
//    }

		return topPanel;
	}
	
	private void addComponent(DefaultFormBuilder builder, ParameterBinder creator) {
		builder.leadingColumnOffset(0);
		builder.append(new JLabel(creator.getLabel() + ":"), 2);
		builder.leadingColumnOffset(1);
		JComponent comp = creator.getComponent(params);
		comp.putClientProperty(NAME, creator.getName());
		comp.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if (ParametersUI.this.params != null) {
					commitParameters();
				}
			}
		});
		builder.append(creator.getComponent(params));
	}

	private JPanel createPanel(Parameters params, List<ParameterBinder> binders) {
		
		List<ParameterBinder> unordered = new ArrayList<>();
		List<ParameterBinder> ordered = new ArrayList<>();
		for (ParameterBinder binder : binders) {
			if (binder.getDisplayOrder() == LAST) {
				unordered.add(binder);
			} else {
				ordered.add(binder);
			}
		}

		Collections.sort(unordered, new Comparator<ParameterBinder>() {
			@Override
			public int compare(ParameterBinder o1, ParameterBinder o2) {
				return o1.getLabel().compareTo(o2.getLabel());
			}
		});
		
		Collections.sort(ordered, new Comparator<ParameterBinder>() {
			@Override
			public int compare(ParameterBinder o1, ParameterBinder o2) {
				double v1 = o1.getDisplayOrder();
				double v2 = o2.getDisplayOrder();
				return v1 > v2 ? 1 : v1 == v2 ? 0 : -1;
			}
		});

		FormLayout layout = new FormLayout("6dlu, pref:grow", "");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.border(Borders.DIALOG);

		for (ParameterBinder creator : ordered) {
			addComponent(builder, creator);
		}
		
		for (ParameterBinder creator : unordered) {
			addComponent(builder, creator);
		}

		if (topPanel == null)
			topPanel = new JPanel(new BorderLayout());
		else
			topPanel.removeAll();
		JScrollPane scrollPane = new JScrollPane(builder.getPanel());
		// on OSX java 7, the scrollpane has a white background and
		// this looks odd when any of the widgets have a gray background
		scrollPane.getViewport().setBackground(topPanel.getBackground());
		topPanel.add(scrollPane, BorderLayout.CENTER);

		return topPanel;
	}

	/**
	 * Commits any changes made using the parameter JComponents to the specified
	 * params.
	 * 
	 * @param params the Parameters to commmit the changes to
	 */
	public void commitParameters() {
		for (List<ParameterBinder> binders : bindersMap.values()) {
			for (ParameterBinder binder : binders) {
				binder.toParameter();
			}
		}
	}

	public void resetParameters() {
		for (String name : params.getSchema().parameterNames()) {
			Object val = params.getSchema().getDetails(name).getDefaultValue();
			if (name.equals(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME) && val.equals(Parameters.NULL)) {
				int seed = (int) System.currentTimeMillis();
				if (seed < 0)
					val = Math.abs(seed);
				params.setValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, seed);
			} else if (!val.equals(Parameters.NULL)) {
				params.setValue(name, val);
			}
		}

		for (List<ParameterBinder> binders : bindersMap.values()) {
			for (ParameterBinder binder : binders) {
				binder.resetToDefault();
			}
		}
	}

	/**
	 * Gets the parameter xml representation of the currently created parameter UI.
	 * The current values in the UI will become the default values.
	 * 
	 * @return the XML.
	 */
	public String toXML() {

		StringBuilder buf = new StringBuilder("<parameters>");
		buf.append(String.format("%n"));
		for (List<ParameterBinder> binders : bindersMap.values()) {
			for (ParameterBinder binder : binders) {
				buf.append("\t");
				buf.append(binder.toXML());
				buf.append(String.format("%n"));
			}
		}
		buf.append("</parameters>");
		return buf.toString();

	}
}
