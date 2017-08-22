package repast.simphony.ws;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@WebListener
public class RequestListener implements ServletRequestListener {

  @Override
  public void requestDestroyed(ServletRequestEvent sre) {
  }

  @Override
  public void requestInitialized(ServletRequestEvent sre) {
    // get the session so that it exists when ServletAwareConfig tries to get it
    ((HttpServletRequest) sre.getServletRequest()).getSession();
  }
}