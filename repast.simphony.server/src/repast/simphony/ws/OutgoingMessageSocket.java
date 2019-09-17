package repast.simphony.ws;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMsg;
import org.zeromq.ZMQ.Socket;

/**
 * Wraps a zeromq PUSH socket for ease of use. 
 * 
 * @author nick
 */
public class OutgoingMessageSocket {
    
    private Socket outgoing = null;
    private ZContext ctx = null;
    
    public OutgoingMessageSocket(String outgoingAddr) {
        ctx = new ZContext();
        outgoing = ctx.createSocket(SocketType.PUSH);
        outgoing.connect(outgoingAddr);
        ctx.setLinger(-1);
    }
    
    public void send(byte[] data) {
        ZMsg zmsg = new ZMsg();
        zmsg.add("binary");
        zmsg.add(data);
        zmsg.send(outgoing);
        // outgoing.send(data);
    }
    
    public void send(String data) {
        ZMsg.newStringMsg("json", data).send(outgoing);
    }
    
   
    public void close() {
        if (outgoing != null) {
            outgoing.close();
        }
        
        if (ctx != null) {
            ctx.close();
            ctx.destroy();
        }
    }

}
