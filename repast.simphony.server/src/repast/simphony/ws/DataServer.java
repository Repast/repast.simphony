package repast.simphony.ws;

import java.util.List;

import org.zeromq.ZMsg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import repast.simphony.data2.DataSink;
import simphony.util.messages.MessageCenter;

public class DataServer implements DataSink {

    private static final int UPDATE_INTERVAL = 250;
    private static final MessageCenter LOG = MessageCenter.getMessageCenter(DataServer.class);

//    private static class Row {
//        // these are used when serialized to json
//        @SuppressWarnings("unused")
//        public String id = "data";
//        @SuppressWarnings("unused")
//        public String type = "row";
//        public Map<String, Object> value = new HashMap<>();
//    }

    private StringBuilder rowBuilder;
    private ObjectMapper mapper = new ObjectMapper();

    private String outgoingAddr, dataSet;
    private OutgoingMessageSocket outgoing;
    private long lastUpdateTS = 0;
    private boolean firstRowItem, firstRow;
    private boolean updatesPending = false;

    public DataServer(String outgoingAddr, String dataSet) {
        this.outgoingAddr = outgoingAddr;
        this.dataSet = dataSet;
    }

    /*
     * @see repast.simphony.data2.DataSink#open(java.util.List)
     */
    @Override
    public void open(List<String> sourceIds) {
    	LOG.info("Remote Data Sink Opened");
        outgoing = new OutgoingMessageSocket(outgoingAddr);
        resetRowBuilder();
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.data2.DataSink#rowStarted()
     */
    @Override
    public void rowStarted() {
        firstRowItem = true;
        if (!firstRow) {
            rowBuilder.append(",");
        }
        rowBuilder.append("{");
        firstRow = false;
    }

    private void resetRowBuilder() {
        rowBuilder = new StringBuilder("{\"id\" : \"data\", \"type\" : \"update\", \"dataset\" : \"");
        rowBuilder.append(dataSet);
        rowBuilder.append("\", \"value\" : [");
        firstRow = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.data2.DataSink#append(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public void append(String key, Object value) {
        // TODO use a formatter to format the object correctly
        if ((value instanceof Double) && Double.isNaN((Double) value)) {
            value = 0.0;
        }

        if (!firstRowItem) {
            rowBuilder.append(",");
        }
        rowBuilder.append(String.format("\"%s\" : %s", key, value.toString()));
        firstRowItem = false;
        updatesPending = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.data2.DataSink#rowEnded()
     */
    @Override
    public void rowEnded() {
        rowBuilder.append("}");
        sendUpdate(false);
    }

    public void sendUpdate(boolean force) {
        long ts = System.currentTimeMillis();
        if (force || ts - lastUpdateTS > UPDATE_INTERVAL) {
            rowBuilder.append("]}");
            outgoing.send(rowBuilder.toString());
            lastUpdateTS = ts;
            resetRowBuilder();
            updatesPending = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.data2.DataSink#recordEnded()
     */
    @Override
    public void recordEnded() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.data2.DataSink#flush()
     */
    @Override
    public void flush() {
    	if (outgoing != null) {
    		sendUpdate(true);
    	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see repast.simphony.data2.DataSink#close()
     */
    @Override
    public void close() {
        // this can get called twice
        if (updatesPending) {
            flush();
        }
        if (outgoing != null) {
            outgoing.close();
        }
        outgoing = null;
        LOG.info("Remote Data Sink Closed");
    }
}
