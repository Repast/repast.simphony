package repast.simphony.ws;

import java.io.IOException;
import java.util.Properties;

import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class ModelLauncherTest {

  public void run() {
    Properties props = new Properties();
    props.put(WSConstants.WORKING_DIRECTORY,
        "/Users/nick/Documents/workspace/RepastSimphony2/jzombies");
    props.put(WSConstants.SCENARIO_DIRECTORY,
        "/Users/nick/Documents/workspace/RepastSimphony2/jzombies/jzombies.rs");
    props.put(WSConstants.REPAST_LIB_DIRECTORY,
        "/Users/nick/Documents/repos/repast.simphony/repast.simphony.ws/lib.ws");
    props.put(WSConstants.VM_ARGS, "");

    String identity = String.format("%04X-%04X", 1000, 1000);
    props.put(WSConstants.IDENTITY, identity);
    props.put(WSConstants.HOST, "localhost:5570");

    ZContext ctx = null;
    boolean started = false;
    try {
      ModelLauncher launcher = new ModelLauncher();
      launcher.run(props);

      ctx = new ZContext();

      // Frontend socket talks to clients over TCP
      Socket server = ctx.createSocket(ZMQ.ROUTER);
      server.bind("tcp://*:5570");

      Poller poller = ctx.createPoller(1);
      poller.register(server, Poller.POLLIN);

      while (!Thread.currentThread().isInterrupted()) {
        poller.poll(100);

        if (poller.pollin(0)) {
          ZMsg msg = ZMsg.recvMsg(server);
          ZFrame address = msg.unwrap();
          String msgContent = msg.pop().toString();
          System.out.println("Server msg content: " + msgContent);
          msg.dump();
          System.out.println(address);
          msg.destroy();

          if (msgContent.equals("READY")) {
            System.out.println("SENDING MSG");
            ZMsg outMsg = new ZMsg();
            outMsg.add("START");
            outMsg.wrap(address);
            outMsg.send(server);
            started = true;
          }
        }

        if (started) {
          Thread.sleep(5000);
          ZMsg stopMsg = new ZMsg();
          stopMsg.add("STOP");
          stopMsg.wrap(new ZFrame(identity));
          stopMsg.send(server);
          break;
        }
      }

    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      if (ctx != null) {
        ctx.destroy();
      }
    }
  }

  public static void main(String[] args) {
    new ModelLauncherTest().run();
  }
}
