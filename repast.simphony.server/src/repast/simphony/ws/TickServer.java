package repast.simphony.ws;

import java.nio.ByteBuffer;

import repast.simphony.engine.controller.TickListener;

public class TickServer implements TickListener {
    
    private static final int UPDATE_INTERVAL = 34;

    private OutgoingMessageSocket outgoing;
    
    private long lastUpdateTS = 0;
    private double tick;
    
    public TickServer(String outgoingAddr) {
        outgoing = new OutgoingMessageSocket(outgoingAddr);
    }

    @Override
    public void tickCountUpdated(double newTick) {
        tick = newTick;
        update(false);
    }
    
    public void update(boolean force) {
        long ts = System.currentTimeMillis();
        
        if (force || ts - lastUpdateTS > UPDATE_INTERVAL) {
            ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES + Double.BYTES);
            bb.putInt(MessageConstants.TICK_COUNT_UPDATE);
            bb.putDouble(tick);
            bb.rewind();
            lastUpdateTS = ts;
            outgoing.send(bb.array());
        }
    }
    
    public void close() {
        outgoing.close();
    }

}
