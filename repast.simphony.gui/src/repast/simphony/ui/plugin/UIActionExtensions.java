package repast.simphony.ui.plugin;

import java.util.Iterator;

import org.java.plugin.PluginClassLoader;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.plugin.ActionExtensions;
import repast.simphony.plugin.RandomCompositeActionCreator;
import repast.simphony.scenario.ControllerActionIO;
import saf.core.runtime.PluginDefinitionException;

/**
 * @author Nick Collier
 */
public class UIActionExtensions extends ActionExtensions {

  private CompositeActionEditorExtensions compositeEditorExts;
  private ComponentActionEditorExtensions compExts;

  public UIActionExtensions() {
    super();
    compExts = new ComponentActionEditorExtensions();
    compositeEditorExts = new CompositeActionEditorExtensions();
    compositeEditorExts.addLabel(RandomCompositeActionCreator.class.getName(), "Random Streams");
  }

  public void loadExtensions(PluginManager manager) throws PluginLifecycleException,
      PluginDefinitionException {
    super.loadExtensions(manager);
    loadCompositeActions(manager);
    loadComponentActions(manager);
  }

  private void createActionEditorCreator(Extension ext, PluginManager manager)
      throws ClassNotFoundException, IllegalAccessException, InstantiationException,
      PluginDefinitionException {
    String className = ext.getParameter("editorCreator").valueAsString();
    String ioName = ext.getParameter("actionIO").valueAsString();
    String pluginID = ext.getDeclaringPluginDescriptor().getId();
    PluginClassLoader pluginClassLoader = manager.getPluginClassLoader(ext
        .getDeclaringPluginDescriptor());
    try {
      Class clazz = Class.forName(className, true, pluginClassLoader);
      ActionEditorCreator creator = (ActionEditorCreator) clazz.newInstance();
      for (ControllerActionIO io : super.ioExts.controllerActionsIOs()) {
        if (io.getClass().getName().equals(ioName)) {
          compExts.addActionEditorCreator(io.getActionClass().getName(), creator);
          break;
        }
      }
    } catch (ClassCastException e) {
      throw new PluginDefinitionException("editorCreator class '" + className + "'in '" + pluginID
          + "' must implement ActionEditorCreator", e);
    }
  }

  private void createMenuItem(Extension ext, PluginManager manager) throws ClassNotFoundException,
      IllegalAccessException, InstantiationException, PluginDefinitionException {
    String parentID = ext.getParameter("parentID").valueAsString();
    String pluginID = ext.getDeclaringPluginDescriptor().getId();
    PluginClassLoader pluginClassLoader = manager.getPluginClassLoader(ext
        .getDeclaringPluginDescriptor());

    String classNames = ext.getParameter("parentMenuItem").valueAsString();
    for (String className : classNames.split(",")) {
      try {
        Class<?> clazz = Class.forName(className.trim(), true, pluginClassLoader);
        EditorMenuItem item = (EditorMenuItem) clazz.newInstance();
        item.init(manager);
        compositeEditorExts.addEditorMenuItem(parentID, item);
      } catch (ClassCastException e) {
        throw new PluginDefinitionException("parentMenuItem class '" + className + "'in '"
            + pluginID + "' must implement EditorMenuItem", e);
      }
    }
  }

  private void loadComponentActions(PluginManager manager) throws PluginLifecycleException,
      PluginDefinitionException {
    ExtensionPoint extPoint = manager.getRegistry().getExtensionPoint("repast.simphony.gui",
        "component.action");
    /*
     * <parameter id="actionIO"
     * value="repast.simphony.scenario.DisplayControllerActionIO" /> <parameter
     * id="editorCreator" value="foo" /> <parameter id="parentMenuItem"
     * value="foo" /> <parameter id="parentID" value="foo" />
     */
    for (Iterator iter = extPoint.getConnectedExtensions().iterator(); iter.hasNext();) {
      Extension ext = (Extension) iter.next();
      // System.out.println("gui:" + ext.getExtendedPluginId());
      // if (!ext.getExtendedPluginId().equals("repast.simphony.gui")) {
      // continue;
      // }
      registerIoExt(manager, ext);

      String pluginID = ext.getDeclaringPluginDescriptor().getId();
      manager.activatePlugin(pluginID);
      String className = ext.getParameter("editorCreator").valueAsString();
      try {
        createActionEditorCreator(ext, manager);
        if (ext.getParameter("parentMenuItem") != null) {
          className = ext.getParameter("parentMenuItem").valueAsString();
          createMenuItem(ext, manager);
        }
      } catch (ClassCastException e) {
        throw new PluginDefinitionException("editorCreator class '" + className + "'in '"
            + pluginID + "' must implement ActionEditorCreator", e);
      } catch (ClassNotFoundException e) {
        throw new PluginDefinitionException("Unable to create classes '" + className
            + "' in component.action in plugin '" + pluginID + "'", e);
      } catch (NoClassDefFoundError e) {
        throw new PluginDefinitionException("Unable to create classes '" + className
            + "' in component.action in plugin '" + pluginID + "'", e);
      } catch (IllegalAccessException e) {
        throw new PluginDefinitionException("Unable to create classes '" + className
            + "' in component.action in plugin '" + pluginID + "'", e);
      } catch (InstantiationException e) {
        throw new PluginDefinitionException("Unable to create classes '" + className
            + "' in component.action in plugin '" + pluginID + "'", e);
      }
    }

  }

  private void loadCompositeActions(PluginManager manager) throws PluginLifecycleException,
      PluginDefinitionException {
    ExtensionPoint extPoint = manager.getRegistry().getExtensionPoint("repast.simphony.gui",
        "composite.action");
    /*

		*/
    for (Iterator iter = extPoint.getConnectedExtensions().iterator(); iter.hasNext();) {
      Extension ext = (Extension) iter.next();
      // System.out.println("gui" + ext.getExtendedPluginId());
      // if (!ext.getExtendedPluginId().equals("repast.simphony.gui")) {
      // continue;
      // }

      String className = ext.getParameter("creatorClass").valueAsString();
      String label = ext.getParameter("label").valueAsString();

      registerActionExt(manager, ext);
      compositeEditorExts.addLabel(className, label);
    }

  }

  public CompositeActionEditorExtensions getCompositeEditorExts() {
    return compositeEditorExts;
  }

  public ActionUI getEditor(ControllerAction action) {
    ActionUI editor = compositeEditorExts.getUI(action);
    if (editor == null) {
      return compExts.getUI(action);
    }
    return editor;
  }

  public void addDefaultUI(Class clazz, String label) {
    compExts.addDefaultUI(clazz, label);
  }
}
