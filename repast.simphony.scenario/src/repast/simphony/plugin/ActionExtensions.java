package repast.simphony.plugin;

import org.java.plugin.PluginClassLoader;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;

import repast.simphony.scenario.ControllerActionIO;
import saf.core.runtime.PluginDefinitionException;

import java.util.Iterator;

/**
 * @author Nick Collier
 */
public class ActionExtensions {

  protected ControllerActionExtensions actionExts;
  protected ControllerActionIOExtensions ioExts;

  public ActionExtensions() {
    this.actionExts = new ControllerActionExtensions();
    this.ioExts = new ControllerActionIOExtensions();
  }

  public void loadExtensions(PluginManager manager) throws PluginLifecycleException, PluginDefinitionException {
    loadCompositeActions(manager);
    loadComponentActions(manager);
  }

  private void loadCompositeActions(PluginManager manager) throws PluginLifecycleException, PluginDefinitionException {
    ExtensionPoint extPoint = manager.getRegistry().getExtensionPoint("repast.simphony.core", "composite.action");
    
    for (Iterator iter = extPoint.getConnectedExtensions().iterator(); iter.hasNext();) {
      Extension ext = (Extension) iter.next();
      registerActionExt(manager, ext);
    }
  }

  protected void registerActionExt(PluginManager manager, Extension ext) throws PluginLifecycleException, PluginDefinitionException {
    String pluginID = ext.getDeclaringPluginDescriptor().getId();
    manager.activatePlugin(pluginID);
    String className = ext.getParameter("creatorClass").valueAsString();
    if (!actionExts.parentsContains(className)) {
      PluginClassLoader pluginClassLoader = manager.getPluginClassLoader(ext
              .getDeclaringPluginDescriptor());
      try {
        CompositeControllerActionCreator creator = instantiate(CompositeControllerActionCreator.class, pluginClassLoader, className);
        registerActionExt(creator);
      } catch (ClassCastException e) {
        throw new PluginDefinitionException("creatorAction class '" + className + "'in '" + pluginID +
                "' must implement ParentControllerActionCreator", e);
      } catch (ClassNotFoundException e) {
        throw new PluginDefinitionException("Unable to create class '" + className + "' in composite.action in plugin '"
                + pluginID + "'", e);
      } catch (NoClassDefFoundError e) {
        throw new PluginDefinitionException("Unable to create class '" + className + "' in composite.action in plugin '" +
                pluginID + "'", e);
      } catch (IllegalAccessException e) {
        throw new PluginDefinitionException("Unable to create class '" + className + "' in composite.action in plugin '" +
                pluginID + "'", e);
      } catch (InstantiationException e) {
        throw new PluginDefinitionException("Unable to create class '" + className + "' in composite.action in plugin '" +
                pluginID + "'", e);
      }
    }
  }

  protected <T> T instantiate(Class<T> type, ClassLoader loader, String className)
          throws InstantiationException, IllegalAccessException, ClassNotFoundException,
          ClassCastException {
    Class<?> clazz = Class.forName(className, true, loader);
    return type.cast(clazz.newInstance());
  }

  public void registerActionExt(CompositeControllerActionCreator creator) {
    actionExts.addCompositeActionCreator(creator);
  }

  protected void registerIoExt(PluginManager manager, Extension ext) throws PluginLifecycleException, PluginDefinitionException {
    String pluginID = ext.getDeclaringPluginDescriptor().getId();
    manager.activatePlugin(pluginID);
    String className = ext.getParameter("actionIO").valueAsString();
    PluginClassLoader pluginClassLoader = manager.getPluginClassLoader(ext.getDeclaringPluginDescriptor());
    try {
      ControllerActionIO io = instantiate(ControllerActionIO.class, pluginClassLoader, className);
      ioExts.addControllerActionIO(io);
    } catch (ClassCastException e) {
      throw new PluginDefinitionException("actionIO class '" + className + "'in '" + pluginID +
              "' must implement ControllerActionIO", e);
    } catch (ClassNotFoundException e) {
      throw new PluginDefinitionException("Unable to create class '" + className + "' in component.action in plugin '"
              + pluginID + "'", e);
    } catch (NoClassDefFoundError e) {
      throw new PluginDefinitionException("Unable to create class '" + className + "' in component.action in plugin '" +
              pluginID + "'", e);
    } catch (IllegalAccessException e) {
      throw new PluginDefinitionException("Unable to create class '" + className + "' in component.action in plugin '" +
              pluginID + "'", e);
    } catch (InstantiationException e) {
      throw new PluginDefinitionException("Unable to create class '" + className + "' in component.action in plugin '" +
              pluginID + "'", e);
    }
  }

  public void registerIoExt(ControllerActionIO io) {
    ioExts.addControllerActionIO(io);
  }

  private void loadComponentActions(PluginManager manager) throws PluginLifecycleException, PluginDefinitionException {
    ExtensionPoint extPoint = manager.getRegistry().getExtensionPoint("repast.simphony.core", "component.action");
   
    for (Iterator iter = extPoint.getConnectedExtensions().iterator(); iter.hasNext();) {
      Extension ext = (Extension) iter.next();
      registerIoExt(manager, ext);
    }

  }

  public ControllerActionExtensions getActionExts() {
    return actionExts;
  }

  public ControllerActionIOExtensions getIoExts() {
    return ioExts;
  }
}
