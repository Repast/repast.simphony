package repast.simphony.ws;

import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.poi.util.SystemOutLogger;

import com.google.gson.Gson;
import com.sun.swing.internal.plaf.synth.resources.synth_sv;

@ServerEndpoint(value = "/simsocket", configurator=repast.simphony.ws.ServletAwareConfig.class)
public class SimSocket {
  
  private Gson gson = new Gson();
  private Session session;
  //private String webInfPath;
  private ModelServer server;
 
  @OnOpen
  public void onWebSocketConnect(Session sess, EndpointConfig config) {
    this.session = sess;
    this.session.setMaxIdleTimeout(0);
    //this.webInfPath = config.getUserProperties().get("WEB_INF").toString();
    this.server = (ModelServer)config.getUserProperties().get("SERVER");
    System.out.println("Socket Connected: " + session.getId());
  }
  

  @OnMessage
  public void onWebSocketText(String message, Session session) {
    System.out.println(message);
    @SuppressWarnings("unchecked")
    Map<String, Object> map = gson.fromJson(message, Map.class);
    String cmd = map.get("command").toString();
    server.queueCommand(cmd, session, map);
  }

  @OnClose
  public void onWebSocketClose(Session session, CloseReason reason) {
    // TODO stop the model associated with this socket
    System.out.println("Socket Closed: " + reason);
  }

  @OnError
  public void onWebSocketError(Throwable cause) {
    cause.printStackTrace(System.err);
  }
}