/**
 * 
 */
package repast.simphony.ws;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.websocket.Session;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

import com.google.gson.Gson;

/**
 * Server for clients each of which runs a model.
 * 
 * @author Nick Collier
 */
public class ModelServer implements Runnable {

  private static class Command {
    Session session;
    String cmd;
    Map<String, Object> json;

    public Command(String cmd, Session session,  Map<String, Object> json) {
      this.session = session;
      this.cmd = cmd;
      this.json = json;
    }
  }

  private int identityKey = 1000;
  private ConcurrentLinkedQueue<Command> cmdQueue = new ConcurrentLinkedQueue<>();
  private Map<String, Session> identity2Session = new ConcurrentHashMap<>();
  private Map<String, String> session2Identity = new ConcurrentHashMap<>();
  private Socket server;
  private Gson gson = new Gson();
  
  private boolean processCommand(Command cmd) {
    boolean retval = true;
    if (cmd != null) {
      if (cmd.cmd.equals(WSConstants.START)) {
        try {
          start(cmd.session, cmd.json);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      } else if (cmd.cmd.equals(WSConstants.PAUSE)) {
        pause(cmd.session);
      } else if (cmd.cmd.equals(WSConstants.RESUME)) {
        resume(cmd.session);
      } else if (cmd.cmd.equals(WSConstants.STOP)) {
        stop(cmd.session);
      } else if (cmd.cmd.equals(WSConstants.EXIT)) {
        retval = false;
      }
    }
    return retval;
  }
  
  private void stop(Session session) {
    String identity = session2Identity.get(session.getId());
    sendMessageToClient(new ZFrame(identity), WSConstants.STOP);
  }
  
  private void pause(Session session) {
    String identity = session2Identity.get(session.getId());
    sendMessageToClient(new ZFrame(identity), WSConstants.PAUSE);
  }

  private void resume(Session session) {
    String identity = session2Identity.get(session.getId());
    sendMessageToClient(new ZFrame(identity), WSConstants.START);
  }

  private void start(Session session,  Map<String, Object> json) throws IOException {
    Properties props = new Properties();
    Path scenario = Paths.get(json.get("scenario").toString());
    
    props.put(WSConstants.WORKING_DIRECTORY, scenario.getParent().toString());
    props.put(WSConstants.SCENARIO_DIRECTORY, scenario.toString());
    props.put(WSConstants.REPAST_LIB_DIRECTORY, json.get("repast_jars"));
    props.put(WSConstants.VM_ARGS, "");

    String identity = String.format("%04X-%04X", identityKey, identityKey);
    ++identityKey;
    props.put(WSConstants.IDENTITY, identity);
    
    for (Object key : props.keySet()) {
      System.out.println(key + ": " + props.getProperty(key.toString()));
    }
    
    // TODO get host from a config file or lookup
    //String hostname = System.getenv("HOSTNAME");
    //if (hostname.startsWith("ip-")) {
    String hostname = "localhost";
    //}
    props.put(WSConstants.HOST, hostname + ":5570");
    identity2Session.put(identity, session);
    session2Identity.put(session.getId(), identity);
    
    ModelLauncher launcher = new ModelLauncher();
    launcher.run(props);
  }

  public void queueCommand(String cmd, Session session,  Map<String, Object> json) {
    cmdQueue.add(new Command(cmd, session, json));
  }
  
  private void sendMessageToClient(ZFrame address, String msg) {
    ZMsg outMsg = new ZMsg();
    outMsg.add(msg);
    outMsg.wrap(address);
    outMsg.send(server);
  }
  
  private void processStatusMessage(ZFrame address, Map<String, Object> json) {
    String status = json.get(WSConstants.VALUE).toString();
    if (status.equals(WSConstants.READY)) {
      sendMessageToClient(address, WSConstants.START);
    } 
  }
  
  private void processDataMessage(ZFrame address, Map<String, Object> json, String msg) {
    String type = json.get(WSConstants.TYPE).toString();
    if (type.equals(WSConstants.ROW)) {
      Session session = identity2Session.get(new String(address.getData()));
      session.getAsyncRemote().sendText(msg);
    }
  }
  
  private void processMsg(ZMsg msg) {
    //System.out.println("Server received message");
    ZFrame address = msg.unwrap();
    //System.out.println(new String(address.getData()));
    String msgContent = msg.pop().toString();
    System.out.println("Server msg content: " + msgContent);
    msg.destroy();

    Map<String, Object> map = gson.fromJson(msgContent, Map.class);
    String id = map.get(WSConstants.ID).toString();
    if (id.equals(WSConstants.STATUS)) {
      processStatusMessage(address, map);
    } else if (id.equals(WSConstants.DATA)) {
      processDataMessage(address, map, msgContent);
    }
  }

  public void run() {
    
    ZContext ctx = null;
    System.out.println("RUNNING");
    try {

      ctx = new ZContext();

      // Frontend socket talks to clients over TCP
      server = ctx.createSocket(ZMQ.ROUTER);
      server.setRouterMandatory(true);
      server.bind("tcp://*:5570");

      Poller poller = ctx.createPoller(1);
      poller.register(server, Poller.POLLIN);

      boolean go = true;
      while (go) {
        poller.poll(100);

        if (poller.pollin(0)) {
          ZMsg msg = ZMsg.recvMsg(server);
          processMsg(msg);
        }
        
        Command cmd = cmdQueue.poll();
        go = processCommand(cmd);
      }

    } finally {
      if (ctx != null) {
        ctx.close();
        ctx.destroy();
      }
    }
    System.out.println("SERVER STOPPED");
  }
}
