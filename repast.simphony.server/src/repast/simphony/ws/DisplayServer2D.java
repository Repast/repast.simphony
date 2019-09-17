package repast.simphony.ws;

import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.space.graph.Network;
import repast.simphony.space.projection.Projection;
import repast.simphony.util.collections.Pair;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.DisplayData;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.editedStyle.EditedStyleData;
import repast.simphony.visualization.editedStyle.EditedStyleUtils;
import repast.simphony.visualization.engine.DisplayDescriptor;
import repast.simphony.visualizationOGL2D.EdgeStyleOGL2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;

public class DisplayServer2D extends DisplayServer {

    private Map<Class<?>, StyledLayer> classStyleMap = new HashMap<>();
    private Map<Network<?>, StyledLayer> networkStyleMap = new HashMap<>();
    private int layeridx = 0;

    public DisplayServer2D(String outgoingAddr, DisplayData<?> data, DisplayDescriptor descriptor,
            Layout layout, int id) {
        super(outgoingAddr, data, descriptor, layout, id);
        
    }
    

    @Override
    public void init() {
        LOG.info("DisplayServer2D.init()");
        outgoing = new OutgoingMessageSocket(outgoingAddr);
             
        StringBuilder builder = new StringBuilder("{\"id\": \"display_init\", \"name\":\"");
        builder.append(descriptor.getName());
        builder.append("\", \"display_id\" : ");
        builder.append(id);
        builder.append(", \"type\":\"");
        builder.append(descriptor.getDisplayType());
        builder.append("\", \"background_color\": [");
        float[] color = descriptor.getBackgroundColor().getRGBColorComponents(null);
        builder.append(color[0]);
        builder.append(",");
        builder.append(color[1]);
        builder.append(",");
        builder.append(color[2]);
        builder.append("], \"bbox\": [");
        Box box = layout.getBoundingBox();
        builder.append(box.getWidth());
        builder.append(",");
        builder.append(box.getHeight());
        builder.append("], \"layers\": [");
        boolean first = true;
        for (StyledLayer layer : classStyleMap.values()) {
            if (!first) {
                builder.append(",");
            }
            builder.append("{\"name\":\"");
            builder.append(layer.getName());
            builder.append("\", \"layer_id\" : ");
            builder.append(layer.getLayerId());
            
            
            Map<String, String> styles = descriptor.getEditedStyles();
            EditedStyleData<Object> data = EditedStyleUtils.getStyle(styles.get(layer.getName()));
            String iconFile = data.getIconFile2D();
            if (iconFile != null && iconFile.length() > 0) {
                outgoing.send("{\"id\": \"req\", \"value\": \"copy_icon\", \"icon\": \"" + data.getIconFile2D() + "\"}");
            }
            
            builder.append(", \"icon\":\"");
            if (iconFile != null && iconFile.length() > 0) {
                builder.append(Paths.get(iconFile).getFileName());
            }
            color = data.getColor();
            builder.append("\",\"color\":[");
            builder.append(color[0]);
            builder.append(",");
            builder.append(color[1]);
            builder.append(",");
            builder.append(color[2]);
            builder.append("], \"shape_wkt\":\"");
            builder.append(data.getShapeWkt());
            builder.append("\", \"size\":");
            builder.append(data.getSize());
            builder.append(", \"scale\":");
            builder.append(data.getSizeScale());
          
            builder.append("}");
            
            
            first = false;
        }
        
        builder.append("]}");
        outgoing.send(builder.toString());
        
        
        for (Projection<?> proj : displayData.getProjections()) {
            // TOOD this prevents network projection firing add events if the
            // layout is 2D cont space -- but maybe need the network projection 
            // for edge events???
            if (proj.getName().equals(descriptor.getLayoutProjection())) {
                proj.addProjectionListener(this); 
            }
        }

        for (Object obj : displayData.objects()) {
            addObject(obj);
        }
        
        doUpdate();
    }
    
    public void registerStyle(Class<?> clazz, StyleOGL2D<?> style) {
        // style.init(canvas.getShapeFactory());
        StyledLayer layer = classStyleMap.get(clazz);
        if (layer == null) {
            layer = new StyledLayer(style, clazz.getName(), layeridx);
            ++layeridx;
            classStyleMap.put(clazz, layer);
        } else {
            layer.setStyle(style);
        }
    }
    
    public void registerNetworkStyle(Network<?> network, EdgeStyleOGL2D style) {
    	StyledLayer layer = networkStyleMap.get(network);
      if (layer == null) {
       
//        layer = new StyledLayer(style, network.getName());
        
//        layer = new NetworkLayerOGL2D(network, style, vLayer, this);
        networkStyleMap.put(network, layer);
      } else {
//        layer.setStyle(style);
      }
    }

    protected StyledLayer findLayer(Object obj) {
        Class<?> objClass = obj.getClass();
        StyledLayer layer = classStyleMap.get(objClass);
        if (layer == null) {
            // find a parent class or interface
            for (Class<?> clazz : classStyleMap.keySet()) {
                if (clazz.isAssignableFrom(objClass)) {
                    layer = classStyleMap.get(clazz);
                    break;
                }
            }
        }
        return layer;
    }

    @Override
    protected void addObject(Object o) {
        StyledLayer layer = findLayer(o);
        layer.addObject(o, idCounter);
        ++idCounter;
    }

    @Override
    protected void removeObject(Object o) {
        StyledLayer layer = findLayer(o);
        layer.removeObject(o);
    }

    @Override
    protected void moveObject(Object o) {}

    //@Override
//    public void doUpdate1() {
//        StringBuilder builder = new StringBuilder("{\"id\": \"display_update\", \"name\":\"");
//        builder.append(descriptor.getName());
//        builder.append("\", \"display_id\" : ");
//        builder.append(id);
//        builder.append(", \"layers\": [");
//        boolean first = true;
//        for (StyledLayer layer : classStyleMap.values()) {
//            if (!first) {
//                builder.append(",");
//            }
//            layer.update(builder, layout);
//            first = false;
//            
//        }
//        builder.append("]}");
//        outgoing.send(builder.toString());
//    }
    
    public void doUpdate() {
        int bytes = 0;
        for (StyledLayer layer : classStyleMap.values()) {
            bytes += layer.getSizeInBytes();
        }
        
        // 1 for message type id, 1 for display id, 1 for layer count
        bytes += Integer.BYTES * 3;
        
        ByteBuffer bb = ByteBuffer.allocate(bytes);
        bb.putInt(MessageConstants.TWO_D_DISPLAY_UPDATE);
        bb.putInt(id);
        bb.putInt(classStyleMap.size());
        
        for (StyledLayer layer : classStyleMap.values()) {
            layer.update(bb, layout);
        }
        bb.rewind();
        outgoing.send(bb.array());
    }
    
    public List<Pair<Integer,Object>> getAgents(List<Integer> ids) {
        List<Pair<Integer,Object>> agents = new ArrayList<>();
        for (StyledLayer layer : classStyleMap.values()) {
            layer.getAgents(ids, agents);
            
        }
        return agents;
    }
}


