package repast.simphony.ui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.UIManager;

import org.apache.velocity.app.Velocity;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.PluginAttribute;
import org.java.plugin.registry.PluginDescriptor;

import com.jidesoft.plaf.LookAndFeelFactory;

import repast.simphony.plugin.ModelPluginLoader;
import repast.simphony.plugin.PluginLifecycleHandler;
import repast.simphony.plugin.ScenarioCreatorExtensions;
import repast.simphony.ui.plugin.UIActionExtensions;
import saf.core.runtime.IApplicationRunnable;
import saf.core.ui.GUICreator;
import saf.core.ui.IAppConfigurator;
import saf.core.ui.ISAFDisplay;
import saf.core.ui.Workspace;
import saf.core.ui.osx.MacOSAdapter;
import simphony.util.ThreadUtilities;
import simphony.util.messages.MessageCenter;

/**
 * SAF UI plugin for repast.simphony simphony gui.
 * 
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/06 22:27:26 $
 */
public class RSUIPlugin extends Plugin implements IApplicationRunnable {

    static {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        // System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Repast
        // Simphony");
        javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }

    private MessageCenter msgCenter = MessageCenter.getMessageCenter(RSUIPlugin.class);

    protected void doStart() throws Exception {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    protected void doStop() throws Exception {
        // To change body of implemented methods use File | Settings | File
        // Templates.
    }

    public void run(final String[] args) {
        try {

            Properties props = new Properties();
            props.put("resource.loader", "class");
            props.put("class.resource.loader.description", "Velocity Classpath Resource Loader");
            props.put("class.resource.loader.class",
                    "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
            Velocity.init(props);

            ThreadUtilities.runInEventThread(new Runnable() {
                public void run() {
                    try {
                        UIManager.put("ClassLoader", getClass().getClassLoader());
                        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();

                        PluginManager manager = getManager();
                        removeModelPlugins(manager);

                        UIActionExtensions ext = new UIActionExtensions();
                        ext.loadExtensions(manager);

                        ScenarioCreatorExtensions creatorExt = new ScenarioCreatorExtensions();
                        creatorExt.loadExtensions(manager);

                        RSApplication app = new RSApplication(ext, new ModelPluginLoader(manager));
                        File scenario = null;
                        if (args.length > 0) {
                            File f = new File(args[0]);
                            if (f.isDirectory()) {
                                scenario = f;
                            }
                        }
                        final IAppConfigurator configurator = new RSAppConfigurator(app, scenario);
                        final Workspace<RSApplication> workspace = new Workspace<RSApplication>(app);

                        ISAFDisplay display = GUICreator.createDisplay(configurator, workspace);
                        if (display != null) {
                            GUICreator.runDisplay(configurator, display);
                            BufferedImage img = ImageIO.read(
                                    RSApplication.class.getClassLoader().getResourceAsStream("network128x128.PNG"));
                            registerOSX(img);
                            display.getFrame().setIconImage(img);
                        }
                    } catch (Throwable ex) {
                        msgCenter.fatal("Fatal error starting Repast", ex);
                    }
                }
            });
        } catch (Throwable e) {
            msgCenter.fatal("Fatal error starting Repast", e);
        }
    }

    private int getMajorVersion() {
        String version = System.getProperty("java.version");
        String[] items = version.split("\\.");
        return Integer.parseInt(items[0]);
    }

    private void registerOSX(Image img) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, 
        NoSuchMethodException, SecurityException  {
        
        if (System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
            int v = getMajorVersion();
            MacOSAdapter adapter = null;
            if (v == 1) {
                Class<?> osxAdapter = getClass().getClassLoader().loadClass("saf.core.ui.osx.MacOSAdapterJava1x");
                adapter = (MacOSAdapter) osxAdapter.getConstructor().newInstance();
            } else {
                Class<?> osxAdapter = getClass().getClassLoader().loadClass("saf.core.ui.osx.MacOSAdapterJava9P");
                adapter = (MacOSAdapter) osxAdapter.getConstructor().newInstance();
            }
            adapter.registerMacOSXApplication();
            adapter.enablePrefs();
            adapter.registerDockImage(img);
        }
    }

    // de-registers any model specific plugins that
    // may have been picked up in the course of loading
    // the application plugins
    private void removeModelPlugins(PluginManager manager) {
        List<String> ids = new ArrayList<String>();
        for (Object obj : manager.getRegistry().getPluginDescriptors()) {
            PluginDescriptor descriptor = (PluginDescriptor) obj;
            PluginAttribute attribute = descriptor.getAttribute(PluginLifecycleHandler.MODEL_PLUGIN);
            if (attribute != null && Boolean.parseBoolean(attribute.getValue())) {
                ids.add(descriptor.getId());
            }
        }

        if (ids.size() > 0) {
            String[] idsArray = new String[ids.size()];
            ids.toArray(idsArray);
            manager.getRegistry().unregister(idsArray);
        }
    }
}
