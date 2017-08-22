package repast.simphony.ws;

import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class SimpleSimFrontEnd {

  private Session session;
  // used to pause the thread that started this, until the
  // connection closes
  private CountDownLatch closeLatch;
  
  public SimpleSimFrontEnd() {
    closeLatch = new CountDownLatch(1);
  }

  public void awaitClose() throws InterruptedException {
    closeLatch.await();
  }

  @OnWebSocketClose
  public void onClose(int statusCode, String reason) {
    System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
    this.session = null;
    this.closeLatch.countDown(); // trigger latch
  }

  @OnWebSocketConnect
  public void onConnect(Session session) {
    System.out.printf("Got connect: %s%n", session);
    this.session = session;
    try {

      String json = "{'command' :'start', 'scenario' : '/Users/nick/Documents/workspace/RepastSimphony2/jzombies/jzombies.rs',"
          + "'classpath': '/Users/nick/Documents/workspace/RepastSimphony2/jzombies/bin'}";
      session.getRemote().sendString(json);

      /*
       * Future<Void> fut; fut =
       * session.getRemote().sendStringByFuture("Hello");
       * fut.get(2,TimeUnit.SECONDS); // wait for send to complete.
       * 
       * fut =
       * session.getRemote().sendStringByFuture("Thanks for the conversation.");
       * fut.get(2,TimeUnit.SECONDS); // wait for send to complete.
       * 
       * session.close(StatusCode.NORMAL,"I'm done");
       */
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  @OnWebSocketMessage
  public void onMessage(String msg) {
    System.out.printf("Got msg: %s%n", msg);
    if (msg.equals("DONE")) {
      this.session.close();
    }
  }
}
