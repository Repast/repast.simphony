package repast.simphony.ws;

import java.net.URI;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class Client {
  public static void main(String[] args) {
    URI uri = URI.create("ws://localhost:8080/events/");
    
    WebSocketClient client = new WebSocketClient();
    SimpleSimFrontEnd socket = new SimpleSimFrontEnd();
    try
    {
        client.start();
        
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        client.connect(socket,uri,request);
        System.out.printf("Connecting to : %s%n",uri);

        // wait for closed socket connection.
        socket.awaitClose(); //5,TimeUnit.SECONDS);
    }
    catch (Throwable t)
    {
        t.printStackTrace();
    }
    finally
    {
        try
        {
            client.stop();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
  }
}
