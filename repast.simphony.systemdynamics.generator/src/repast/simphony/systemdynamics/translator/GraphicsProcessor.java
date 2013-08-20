package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsProcessor {
    
    private SystemDynamicsObjectManager sdObjectManager;

    public GraphicsProcessor() {
	
    }
    
    public Map<String, View> processGraphics(SystemDynamicsObjectManager sdObjectManager, List<String> mdlContents) {

	Map<String, View> viewMap = new HashMap<String, View>();
	
	this.sdObjectManager = sdObjectManager;

	// first skip to the beginning of the 
	int graphRecord = 0;

	for (String aLine : mdlContents) {
	    if (aLine.contains("---///"))
		break;
	    graphRecord++;
	}

	int viewNumber = 0;
	
	String aLine = "";
	String graphicsMarker = mdlContents.get(graphRecord++);
//	System.out.println("GP: graphics marker = "+graphicsMarker);
//	System.out.println("GP: graphrecord = "+graphRecord+" mdl size = "+mdlContents.size());

	while(true) {
//		System.out.println("GP: <"+aLine+">");
		// if there are no graphics, we are pointing to EOF marker checkfor it
		String next = mdlContents.get(graphRecord);
	    if (aLine.endsWith("---\\\\\\") || next.endsWith("---\\\\\\") || graphRecord >= mdlContents.size())
		break;
	    
	    String versionCode = mdlContents.get(graphRecord++);
	    String viewName = mdlContents.get(graphRecord++).substring(1);
	    String viewDefaultFont = mdlContents.get(graphRecord++);
	    
	    View aView = new View(viewName, versionCode, viewDefaultFont);
	    viewMap.put(viewName, aView);
	    viewNumber++;
	    aLine = mdlContents.get(graphRecord++);
	    List<String> objects = new ArrayList<String>();
	    while(!aLine.startsWith("\\\\\\---///") && !aLine.startsWith("///---\\\\\\")) {
		objects.add(aLine);
		aLine = mdlContents.get(graphRecord++);
		if (graphRecord > mdlContents.size()) {
		    System.out.println("Graphics processor bad data read");
		}
	    }
	    
//	    System.out.println("### VIEW ### "+viewName);
	    aView.setRawObjects(objects);
	    aView.parse(sdObjectManager);
	    
	}



	return viewMap;
    }
}
