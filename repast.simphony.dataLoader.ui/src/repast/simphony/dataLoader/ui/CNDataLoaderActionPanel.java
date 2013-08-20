package repast.simphony.dataLoader.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.dataLoader.engine.ClassNameContextBuilder;
import repast.simphony.dataLoader.engine.ClassNameDataLoaderAction;
import repast.simphony.scenario.Scenario;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.ui.plugin.editor.UISaver;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * UISaver for setting the CNDataLoader object as well as parameters for that
 * object.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class CNDataLoaderActionPanel extends UISaver {

  private String name;
  private DefaultComboBoxModel listModel = new DefaultComboBoxModel();
  private ContextBuilder loader;
  private ClassNameDataLoaderAction action;
  private Scenario scenario;

  public CNDataLoaderActionPanel(ClassNameDataLoaderAction action, Scenario scenario, Classpath classPath) {
    setLayout(new BorderLayout());
    this.scenario = scenario;
    this.action = action;
    this.loader = action.getBuilder().getDelegateDataLoader();
    name = loader.getClass().getName();
    name = name.substring(name.lastIndexOf(".") + 1, name.length());
    add(this.createTitlePanel(name), BorderLayout.NORTH);
    fillListModel(loader, classPath);

    FormLayout layout = new FormLayout("3dlu, pref:grow", "pref, 3dlu, pref");
    DefaultFormBuilder builder = new DefaultFormBuilder(layout);
    builder.setDefaultDialogBorder();
    CellConstraints cc = new CellConstraints();
    builder.addSeparator("Class Name", cc.xyw(1, 1, 2));
    final JComboBox box = new JComboBox(listModel);
    box.setRenderer(new DefaultListCellRenderer() {
      public Component getListCellRendererComponent(JList list, Object value, int index,
	  boolean isSelected, boolean cellHasFocus) {
	if (value != null)
	  value = value.getClass().getName();
	return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      }
    });
    builder.add(box, cc.xy(2, 3));
    final JPanel panel = new JPanel(new BorderLayout());
    panel.add(builder.getPanel(), BorderLayout.NORTH);

    box.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
	Object obj = box.getSelectedItem();
	CNDataLoaderActionPanel.this.loader = (ContextBuilder) obj;
      }
    });

    add(panel, BorderLayout.CENTER);
    box.setSelectedIndex(0);
  }

  private void fillListModel(Object current, Classpath path) {
    listModel.addElement(current);
    try {

      String name = current.getClass().getName();
      for (Class<?> clazz : path.getClasses()) {

	try {
	  if (!clazz.getName().equals(name) && ContextBuilder.class.isAssignableFrom(clazz)) {
	    Object obj = clazz.newInstance();
	    listModel.addElement(obj);
	  }
	} catch (IllegalAccessException e) {
	  e.printStackTrace();
	} catch (InstantiationException e) {
	  e.printStackTrace();
	}
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public boolean cancel() {

    loader = null;
    return true;
  }

  public String getDialogTitle() {
    return name;
  }

  public boolean save() {
    action.setBuilder(new ClassNameContextBuilder(loader));
    scenario.setDirty(true);
    return true;
  }
}
