package repast.simphony.ws;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

@ServerEndpoint(value = "/SimSocket", configurator=repast.simphony.ws.ServletAwareConfig.class)
public class SimSocket {
  
  private Gson gson = new Gson();
  private Session session;
  private Set<String> addedClassPaths = new HashSet<String>();
  private BlockingQueue<SimCommand> cmdQueue;
  private BlockingQueue<String> statusQueue;
  private SimClassLoader classLoader;
  private String webInfPath;
  
  @OnOpen
  public void onWebSocketConnect(Session sess, EndpointConfig config) {
    this.session = sess;
    this.session.setMaxIdleTimeout(0);
    this.webInfPath = config.getUserProperties().get("WEB_INF").toString();
    List<URL> urls = (List<URL>)config.getUserProperties().get("SIMPHONY_JARS");
    classLoader = new SimClassLoader(urls.toArray(new URL[0]), this.getClass().getClassLoader());
    
    System.out.println("Socket Connected: " + session.getId());
    
    //HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
    //ServletContext servletContext = httpSession.getServletContext();
    //System.out.println(servletContext.getRealPath("/simphony"));
  }
  
  private void start(Session session, Map<String, Object> json) {
    try {
      String scenario = json.get("scenario").toString();
      scenario = scenario.replace("${root}", webInfPath);
      String classpath = json.get("classpath").toString();
      classpath = classpath.replace("${root}", webInfPath);
      
      System.out.println("scenario: " + scenario);
      System.out.println("classpath: " + classpath);
      
      
      if (!addedClassPaths.contains(classpath)) {
        classLoader.addClassPath(classpath);
        addedClassPaths.add(classpath);
      }
      
      cmdQueue = new LinkedBlockingQueue<>();
      statusQueue = new LinkedBlockingQueue<>();
      
      Class<?> clazz = Class.forName("repast.simphony.ws.SimStarter", true, classLoader);
      Object obj = clazz.newInstance();
      Method m = clazz.getMethod("run", String.class, BlockingQueue.class, BlockingQueue.class, Session.class);
      Thread t = (Thread) m.invoke(obj, scenario, cmdQueue, statusQueue, session);
      
      /*
      String parameters = scenario + "/parameters.xml";
      Class<?> clazz = Class.forName("repast.simphony.parameter.ParametersParser", true, classLoader);
      ParametersParser parser = (ParametersParser) clazz.getConstructor(File.class).newInstance(new File(parameters));
      //ParametersParser parser = new ParametersParser(new File(parameters));
      Parameters params = parser.getParameters();
      
      
      
     clazz = Class.forName("repast.simphony.ws.WSRunner", true, classLoader);
     WSRunner runner = (WSRunner) clazz.getConstructor(File.class, BlockingQueue.class, Parameters.class).newInstance(
         new File(scenario), cmdQueue, statusQueue, params, session);
     //WSRunner runner = new WSRunner(new File(scenario), cmdQueue, statusQueue, params, session);

      Thread t = new Thread(runner);
      */
      t.start();
      cmdQueue.put(SimCommand.START);
      
     new Thread(()-> {
       try {
        String status = statusQueue.take();
        if (status.equals("DONE")) {
          t.join();
          session.getBasicRemote().sendText("{\"id\" : \"status\", \"data\": \"DONE\"}");
        }
      } catch (InterruptedException | IOException e) {
        // TODO handle
        e.printStackTrace();
      }
     }).start();
      
    } catch (IllegalArgumentException |  SecurityException |  
        InterruptedException | ClassNotFoundException | InstantiationException | IllegalAccessException | 
        InvocationTargetException | NoSuchMethodException ex) {
      ex.printStackTrace();
    }
  }
  
  private void pause() {
    try {
      System.out.println("pausing");
      cmdQueue.put(SimCommand.PAUSE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  private void resume() {
    try {
      System.out.println("resuming");
      // runner starts from pause
      cmdQueue.put(SimCommand.START);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  private void stop() {
    try {
      System.out.println("stopping");
      // runner starts from pause
      cmdQueue.put(SimCommand.STOP);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  public void onWebSocketText(String message, Session session) {
    System.out.println("Received TEXT message: " + message);
    @SuppressWarnings("unchecked")
    Map<String, Object> map = gson.fromJson(message, Map.class);
    String cmd = map.get("command").toString();
    if (cmd.equals("start")) {
      start(session, map);
    } else if (cmd.equals("pause")) {
      pause();
    } else if (cmd.equals("resume")) {
      resume();
    } else if (cmd.equals("stop")) {
      stop();
    }
  }

  @OnClose
  public void onWebSocketClose(CloseReason reason) {
    System.out.println("Socket Closed: " + reason);
  }

  @OnError
  public void onWebSocketError(Throwable cause) {
    cause.printStackTrace(System.err);
  }
}