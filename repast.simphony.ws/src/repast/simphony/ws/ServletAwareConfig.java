package repast.simphony.ws;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ServletAwareConfig extends ServerEndpointConfig.Configurator {

  /*
  private void createURLs(String directory, List<URL> urls) {

    try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
      for (Path path : directoryStream) {
        path = path.toAbsolutePath().normalize();
        urls.add(path.toUri().toURL());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  */
  
  private String getPath(URL url) {
    String path = null;
    try {
      path = URLDecoder.decode(url.getPath(), "UTF-8");
      if (System.getProperty("os.name").toLowerCase().contains("windows") && path.startsWith("/")) {
        path = path.substring(1);
      }
    } catch (UnsupportedEncodingException ex) {
      ex.printStackTrace();
    }
    return path;
  }

  @Override
  public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request,
      HandshakeResponse response) {
    try {
      HttpSession httpSession = (HttpSession) request.getHttpSession();
      ServletContext context = httpSession.getServletContext();
      URL root = context.getResource("/WEB-INF");
      String path = getPath(root);
      config.getUserProperties().put("WEB_INF", path);
      config.getUserProperties().put("SERVER", context.getAttribute("SERVER"));
    } catch (Exception ex) {
      // TODO handle
      ex.printStackTrace();
    }
  }

}
