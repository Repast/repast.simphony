package repast.simphony.ws;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Starts the server by creating a classloader that contains all the repast related jars etc.,
 * and the websocket etc. code. This should not be run with any of that on the classpath. 
 * 
 * @author Nick Collier
 */
public class ServerMain {
  
  private void createURLS(String directory, List<URL> urls) {
    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
      for (Path path : directoryStream) {
        path = path.toAbsolutePath().normalize();
        urls.add(path.toUri().toURL());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private void run(String root) {
    List<URL> urls = new ArrayList<>();
    createURLS(root + "/lib.ws", urls);
    createURLS(root+ "/lib", urls);
    try {
      urls.add(Paths.get(root + "/bin/").toAbsolutePath().normalize().toUri().toURL());
    } catch (MalformedURLException e1) {
      e1.printStackTrace();
    }
    
    SimClassLoader classLoader = new SimClassLoader(urls.toArray(new URL[0]));
    try {
      Class<?> clazz = Class.forName("repast.simphony.ws.SimServer", true, classLoader);
      Object server = (Object) clazz.newInstance();
      clazz.getMethod("run").invoke(server);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | 
        NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new ServerMain().run(args[0]);
  }
}
