package repast.simphony.ws;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WSContextListener implements ServletContextListener {

  @Override
  public void contextDestroyed(ServletContextEvent evt) {
    ModelServer server = (ModelServer)evt.getServletContext().getAttribute("SERVER");
    server.queueCommand(WSConstants.EXIT, null, null);
  }

  @Override
  public void contextInitialized(ServletContextEvent evt) {
    ModelServer server = new ModelServer();
    new Thread(server).start();
    evt.getServletContext().setAttribute("SERVER", server);
  }
}
