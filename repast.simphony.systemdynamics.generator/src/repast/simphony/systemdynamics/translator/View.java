package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class View {

    public static final String ARROW = "ARROW";
    public static final String VARIABLE = "VARIABLE";
    public static final String VALVE = "VALVE";
    public static final String COMMENT = "COMMENT";
    public static final String BITMAP = "BITMAP";
    public static final String METAFILE = "METAFILE";
    public static final String RATE = "RATE";
    public static final String CLOUD = "CLOUD";

    private String name;
    private String versionCode;
    private String viewDefaultFont;
    private List<String> rawObjects;
    private List<GraphicObject> graphicObjects;
    private int currentPtr = 0;
    
    private Map<String, String> idNameMap;

    public View(String name, String versionCode, String viewDefaultFont) {
	this.name = name;
	this.versionCode = versionCode;
	this.viewDefaultFont = viewDefaultFont;
	graphicObjects = new ArrayList<GraphicObject>();
	idNameMap = new HashMap<String, String>();

    }

    public void parse(SystemDynamicsObjectManager sdObjectManager) {

    	while(currentPtr < rawObjects.size()) {
    		String raw = rawObjects.get(currentPtr++);
    		GraphicObject go = new GraphicObject(sdObjectManager, this, raw);
    		graphicObjects.add(go);
    		// if this go has an associated variable (can only be valve)
    		// need to add to graphic objects as this was processed together
    		// with valve
    		if (go.isValve() && go.getAssociatedVariable() != null) {
//    			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+go.getName());
    			
    			graphicObjects.add(go.getAssociatedVariable());
    		}
    	}

    	print();
    	sdObjectManager.extractStructure(this);
 
    }


    public String translateCodeToString(String numeric) {

	//	public static final String ARROW = "ARROW";
	//	public static final String VARIABLE = "VARIABLE";
	//	public static final String VALVE = "VALVE";
	//	public static final String COMMENT = "COMMENT";
	//	public static final String BITMAP = "BITMAP";
	//	public static final String METAFILE = "METAFILE";

	if (numeric.equals(GraphicObject.ARROW))
	    return ARROW;
	else if (numeric.equals(GraphicObject.VARIABLE))
	    return VARIABLE;
	else if (numeric.equals(GraphicObject.VALVE))
	    return VALVE;
	else if (numeric.equals(GraphicObject.COMMENT))
	    return COMMENT;
	else if (numeric.equals(GraphicObject.BITMAP))
	    return BITMAP;
	else if (numeric.equals(GraphicObject.METAFILE))
	    return METAFILE;
	else if (numeric.equals(GraphicObject.RATE))
	    return RATE;
	else if (numeric.equals(GraphicObject.CLOUD))
	    return CLOUD;
	else
	    return "UNKNOWN";

    }
    
    public String peekNextRawObject() {
	// error condition?
	String next = rawObjects.get(currentPtr);
//	currentPtr++;
	return next;

    }

    public String getNextRawObject() {
	// error condition?
	String next = rawObjects.get(currentPtr);
	currentPtr++;
	return next;

    }

    public void print() {
//	System.out.println("##########################");
//	System.out.println("View Name: "+name);
//	System.out.println("View Version: "+versionCode);
//	System.out.println("View Default Font: "+viewDefaultFont);
//	System.out.println("Graphic Objects:");
//	for (GraphicObject go : graphicObjects)
//	    go.print();
    }

    public List<String> getRawObjects() {
	return rawObjects;
    }

    public void setRawObjects(List<String> rawObjects) {
	this.rawObjects = rawObjects;
    }

    public String getName() {
	return name;
    }

    public List<GraphicObject> getGraphicObjects() {
	return graphicObjects;
    }

}
