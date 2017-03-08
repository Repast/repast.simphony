package repast.simphony.plugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Library;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginPrerequisite;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.standard.StandardPluginClassLoader;

/**
 * Repast plugin ClassLoader implementation.
 * 
 * @author Nick Collier
 *
 */
public class ExtendablePluginClassLoader extends StandardPluginClassLoader {

  private Set<PluginDescriptor> descriptors = new HashSet<PluginDescriptor>();
  private Set<String> addedPaths = new HashSet<String>();


  public ExtendablePluginClassLoader(final PluginManager aManager, final PluginDescriptor descr,
                                     final ClassLoader parent) {
    super(aManager, descr, parent);
    descriptors.add(descr);
    collectImports();
    resourceLoader = getResourceLoader(aManager, descriptors);
    collectFilters();
    libraryCache = new HashMap();
  }
  
  /**
   * Override java.langClassLoader package definition method to prevent null
   * values in java.lang.Package attributes.  The JPF classloader does not process
   * the META-INF/MANIFEST.MF files inside Jars that contain metadata about the 
   * library.  Null values can cause some libraries such as JAI to fail since they
   * require non-null, but arbitrary, meta-data to instantiate properly.
   * 
   * @see java.lang.ClassLoader#defindPackage
   */
  @Override
  protected Package definePackage(String name, String specTitle,
  		String specVersion, String specVendor, String implTitle, String implVersion,
  		String implVendor, URL sealBase) throws IllegalArgumentException {

  	// Replace all null entries with empty string
  	
  	if (specTitle == null) specTitle = "";
  	if (specVersion == null) specVersion = "";
  	if (specVendor == null) specVendor = "";
  	if (implTitle == null) implTitle = "";
  	if (implVersion == null) implVersion = "";
  	if (implVendor == null) implVendor = "";
  	
  	return super.definePackage(name, specTitle, specVersion, specVendor,
  		implTitle, implVersion, implVendor, sealBase);
  }
  
  public void addDescriptor(PluginDescriptor descriptor) {
    // addURLS from passed in descriptor to this descriptor
    if (!descriptors.contains(descriptor)) {
      descriptors.add(descriptor);

      URL[] newUrls = getUrls(getPluginManager(), descriptor, getURLs());
      for (int i = 0; i < newUrls.length; i++) {
        addURL(newUrls[i]);
      }
      collectImports();
      resourceLoader = getResourceLoader(getPluginManager(), descriptors);
      collectFilters();
      for (Iterator it = libraryCache.entrySet().iterator(); it.hasNext();) {
        if (((Map.Entry) it.next()).getValue() == null) {
          it.remove();
        }
      }


      List<PluginDescriptor> descsToAdd = new ArrayList<PluginDescriptor>();
      for (Iterator it = descriptor.getPrerequisites().iterator(); it.hasNext();) {
        PluginPrerequisite pre = (PluginPrerequisite) it.next();
        if (!pre.matches()) {
          continue;
        }
        PluginDescriptor preDescr = getPluginDescriptor().getRegistry().getPluginDescriptor(pre.getPluginId());
        descsToAdd.add(preDescr);
      }

      for (PluginDescriptor pd : descsToAdd) {
        addDescriptor(pd);
      }
    }
  }

  protected void collectFilters() {
    if (resourceFilters == null) {
      resourceFilters = new HashMap();
    } else {
      resourceFilters.clear();
    }
    for (PluginDescriptor descriptor : descriptors) {
      for (Iterator it = descriptor.getLibraries().iterator();
           it.hasNext();) {
        Library lib = (Library) it.next();
        resourceFilters.put(getPluginManager().getPathResolver().resolvePath(lib,
                lib.getPath()), new StandardPluginClassLoader.ResourceFilter(lib));
      }
    }
  }

  public void appendPaths(String paths) throws IOException {
    StringTokenizer tok = new StringTokenizer(paths, ";", false);
    while (tok.hasMoreTokens()) {
      String path = tok.nextToken();
      if (!addedPaths.contains(path)) {
        URL url = new File(path).getCanonicalFile().toURL();
        addURL(url);
        resourceLoader.addURL(url);
        addedPaths.add(path);
      }
    }
  }

  protected void collectImports() {
    // collect imported plug-ins (exclude duplicates)
    if (descriptors == null) {
      descriptors = new HashSet<PluginDescriptor>();
    }
    Map publicImportsMap = new HashMap(); //<plug-in ID, PluginDescriptor>
    Map privateImportsMap = new HashMap(); //<plug-in ID, PluginDescriptor>
    PluginRegistry registry = getPluginDescriptor().getRegistry();
    for (PluginDescriptor descriptor : descriptors) {
      for (Iterator it = descriptor.getPrerequisites().iterator(); it.hasNext();) {
        PluginPrerequisite pre = (PluginPrerequisite) it.next();
        if (!pre.matches()) {
          continue;
        }
        PluginDescriptor preDescr = registry.getPluginDescriptor(pre.getPluginId());
        if (pre.isExported()) {
          publicImportsMap.put(preDescr.getId(), preDescr);
        } else {
          privateImportsMap.put(preDescr.getId(), preDescr);
        }
      }
    }


    publicImports = (PluginDescriptor[]) publicImportsMap.values().toArray(
            new PluginDescriptor[publicImportsMap.size()]);


    privateImports =
            (PluginDescriptor[]) privateImportsMap.values().toArray(
                    new PluginDescriptor[privateImportsMap.size()]);
    // collect reverse look up plug-ins (exclude duplicates)
    Map reverseLookupsMap = new HashMap(); //<plug-in ID, PluginDescriptor>
    for (Iterator it = registry.getPluginDescriptors().iterator();
         it.hasNext();) {
      PluginDescriptor descr = (PluginDescriptor) it.next();
      if (descr.equals(getPluginDescriptor())
              || publicImportsMap.containsKey(descr.getId())
              || privateImportsMap.containsKey(descr.getId())) {
        continue;
      }
      for (Iterator it2 = descr.getPrerequisites().iterator();
           it2.hasNext();) {
        PluginPrerequisite pre = (PluginPrerequisite) it2.next();
        if (!pre.getPluginId().equals(getPluginDescriptor().getId())
                || !pre.isReverseLookup()) {
          continue;
        }
        if (!pre.matches()) {
          continue;
        }
        reverseLookupsMap.put(descr.getId(), descr);
        break;
      }
    }

    reverseLookups =
            (PluginDescriptor[]) reverseLookupsMap.values().toArray(
                    new PluginDescriptor[reverseLookupsMap.size()]);
  }

  private static PluginResourceLoader getResourceLoader(final PluginManager manager, final Set<PluginDescriptor> descriptors) {
    List urls = new LinkedList();
    for (PluginDescriptor descriptor : descriptors) {
      for (Iterator it = descriptor.getLibraries().iterator(); it.hasNext();) {
        Library lib = (Library) it.next();
        if (lib.isCodeLibrary()) {
          continue;
        }
        urls.add(manager.getPathResolver().resolvePath(lib, lib.getPath()));
      }
    }
    if (urls.isEmpty()) {
      return null;
    }
    return new PluginResourceLoader((URL[]) urls.toArray(new URL[urls.size()]));
  }
}
