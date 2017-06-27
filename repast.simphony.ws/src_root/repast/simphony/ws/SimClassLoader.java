package repast.simphony.ws;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

public class SimClassLoader extends URLClassLoader {

  public SimClassLoader(URL[] urls, ClassLoader parent) {
    super(urls, 
        parent);
  }
  
  public SimClassLoader(URL[] urls) {
    super(urls);
  }
  
  public void addClassPath(String path) {
    String[] paths = path.split(":");
    for (String p : paths) {
      addURL(p);
    }
  }
  
  protected void addURL(String path) {
    try {
      super.addURL(Paths.get(path).toUri().toURL());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }
  
  /*
  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    //System.out.println("loading class");
    return super.loadClass(name);
  }

  @Override
  protected Class<?> findClass(final String name) throws ClassNotFoundException {
    //System.out.println("finding class");
    return super.findClass(name);
  }
  */
}