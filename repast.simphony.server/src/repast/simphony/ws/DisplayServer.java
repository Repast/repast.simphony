package repast.simphony.ws;

import java.nio.file.Path;
import java.util.List;

import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.engine.DisplayDescriptor;
import simphony.util.messages.MessageCenter;

public abstract class DisplayServer implements ProjectionListener {

    protected static final MessageCenter LOG = MessageCenter.getMessageCenter(DisplayServer.class);

    private static final int UPDATE_INTERVAL = 34;

    protected String outgoingAddr;
    protected DisplayData<?> displayData;
    protected DisplayDescriptor descriptor;
    protected OutgoingMessageSocket outgoing;
    protected Layout layout;
    protected int idCounter = 0;
    private long lastUpdateTS = 0;
    protected int id;

    public DisplayServer(String outgoingAddr, DisplayData<?> data, DisplayDescriptor descriptor, Layout<?, ?> layout, int id) {
        this.outgoingAddr = outgoingAddr;
        this.displayData = data;
        this.descriptor = descriptor;
        this.layout = layout;
        this.id = id;
    }

    public void projectionEventOccurred(ProjectionEvent evt) {
        if (evt.getType() == ProjectionEvent.OBJECT_ADDED) {
            Object obj = evt.getSubject();
            addObject(obj);
        } else if (evt.getType() == ProjectionEvent.OBJECT_REMOVED) {
            Object obj = evt.getSubject();
            removeObject(obj);
        } else if (evt.getType() == ProjectionEvent.OBJECT_MOVED) {
            Object obj = evt.getSubject();
            moveObject(obj);
        }
    }

    protected abstract void addObject(Object o);

    protected abstract void removeObject(Object o);

    protected abstract void moveObject(Object o);

    public abstract void init();
    
    /**
     * Gets the agents with the specified viz ids.
     * 
     * @param ids
     * @return the list of agents with the specified ids.
     */
    public abstract List<Pair<Integer,Object>> getAgents(List<Integer> ids);

    protected abstract void doUpdate();

    public void update(boolean force) {
        long ts = System.currentTimeMillis();
        if (force || ts - lastUpdateTS > UPDATE_INTERVAL) {
            lastUpdateTS = ts;
            doUpdate();
        }
    }

    public void destroy() {
        System.out.println(this);
        LOG.info("Display Server Destroy");
        outgoing.close();
    }
    
    public int getId() {
        return id;
    }
}

