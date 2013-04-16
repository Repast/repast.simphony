package repast.simphony.systemdynamics.translator;

public class GraphicObject {
    
    public final static long mask0 = 1 << 0;
    public final static long mask1 = 1 << 1;
    public final static long mask2 = 1 << 2;
    public final static long mask3 = 1 << 3;
    public final static long mask4 = 1 << 4;
    public final static long mask5 = 1 << 5;
    public final static long mask6 = 1 << 6;
    public final static long mask7 = 1 << 7;
    public final static long mask8 = 1 << 8;
    
    public static final String ARROW = "1";
    public static final String VARIABLE = "10";
    public static final String VALVE = "11";
    public static final String COMMENT = "12";
    public static final String BITMAP = "30";
    public static final String METAFILE = "31";
    public static final String RATE = "99";
    
    public static final String CLOUD = "48";

    private View view;

    private String rawObject;

    private String type;
    private String id;

    // Arrows
    private String from;
    private String to;
    private String shape; // Variables
    private String hidden; // Variables
    private String polarity;
    private String thickness;
    private String hasFont; // Variables
    private String delayType;
    private String reserved;
    private String color;
    private String font; // Variables
    private String numPoints;
    private String pointList;

    // Variables
    private String name;
    private String x;
    private String y;
    private String width;
    private String height;
    //    private String shape;
    private String bits;
    //    private String hidden;
    //    private String hasFont;
    private String textPos;
    private String boxWidth;
    private String nav1;
    private String nav2;
    private String boxColor;
    private String fillColor;
    //    private String font;
    
    private String additionalText;
    private SystemDynamicsObjectManager sdObjectManager;
    
    private GraphicObject associatedVariable;


    public GraphicObject(SystemDynamicsObjectManager sdObjectManager, View view, String rawObject) {
	this.rawObject = new String(rawObject);
	this.view = view;
	this.sdObjectManager = sdObjectManager;
	parse();
    }

    public void parse() {
    	String[] fields = Parser.splitQuoted(rawObject, ",");
    	if (fields.length < 10)
    		System.out.println("WTF!");
    	type = fields[0];
    	id = fields[1];
    	if (type.equals("1"))
    		parseAsArrow(fields);
    	else if (type.equals("10"))
    		parseAsVariable(fields);
    	else if (type.equals("11"))
    		parseAsValve(fields);
    	else if (type.equals("12"))
    		parseAsComment(fields);
    	else if (type.equals("30"))
    		parseAsBitmap(fields);
    	else if (type.equals("31"))
    		parseAsMetafile(fields);
    }
    
    public boolean isValve() {
    	return type.equals("11");
    }
    
    public boolean isVariable() {
    	return type.equals("10");
    }
    
    public boolean isRate() {
    	return type.equals("99");
    }
    
    public boolean isArrow() {
    	return type.equals("1");
    }
    
    public boolean isComment() {
    	return type.equals("12");
    }
    
    public boolean isBitmap() {
    	return type.equals("30");
    }
    
    public boolean isMetafile() {
    	return type.equals("31");
    }
    
    public boolean isCloud() {
    	return type.equals("48");
    }

    private void parseAsArrow(String[] fields) {

	// Arrows
	from = fields[2];
	to = fields[3];
	shape = fields[4];
	hidden = fields[5];
	polarity = fields[6];
	thickness = fields[7];
	hasFont = fields[8];
	delayType = fields[9];
	reserved = fields[10];
	color = fields[11];
	font = fields[12];
	// Need to handle the various formats that are possible

	
	numPoints = reconstructNumPoints(fields, 13);
	pointList = reconstructPointlist(fields, 13);;
    }
    
    private String reconstructNumPoints(String [] fields, int index) {
	String[] result = fields[index].split("\\|");
	return result[0];
    }
    
    private String reconstructPointlist(String [] fields, int index) {
	StringBuffer sb = new StringBuffer();
	
	// first reconstruct original string
	for (int i = index; i < fields.length; i++) {
	    if (i != index)
		sb.append(",");
	    sb.append(fields[i]);
	}
	return sb.toString().split("\\|")[1];
    }

    private void parseAsVariable(String[] fields) {
	name = fields[2].replace("\"", "");
	x = fields[3];
	y = fields[4];
	width = fields[5];
	height = fields[6];
	shape = fields[7];
	bits = fields[8];
	if (bitwiseAnd(bits, mask2))
	    additionalText = new String(view.getNextRawObject());
	hidden = fields[9];
	hasFont = fields[10];
	textPos = fields[11];
	boxWidth = fields[12];
	nav1 = fields[13];
	nav2 = fields[14];
	if (nav1.equals("253") && nav2.equals("253"))
	    additionalText = new String(view.getNextRawObject());
	if (!hasFont.equals("0")) {
	    boxColor = fields[15];
	    fillColor = fields[16];
	    font = fields[17];
	}
    }

    private void parseAsValve(String[] fields) {
	name = fields[2].replace("\"", "");
	x = fields[3];
	y = fields[4];
	width = fields[5];
	height = fields[6];
	shape = fields[7];
	bits = fields[8];
	hidden = fields[9];
	hasFont = fields[10];
	textPos = fields[11];
	boxWidth = fields[12];
	nav1 = fields[13];
	nav2 = fields[14];
	if (!hasFont.equals("0")) {
	    boxColor = fields[15];
	    fillColor = fields[16];
	    font = fields[17];
	}
	
	// is it always connect to a variable?
	additionalText = new String(view.getNextRawObject());
	GraphicObject go = new GraphicObject(sdObjectManager, view, additionalText);
	if (go.getType().equals(VARIABLE)) {
		System.out.println("ASSOCIATED Variable "+go.name);
	    associatedVariable = go;
	    go.setType(GraphicObject.RATE);
	    go.setAssociatedVariable(this);
//	    view.getNextRawObject();
	}
	
    }

    private void parseAsComment(String[] fields) {
	name = fields[2].replace("\"", "");
	if (name.equals(CLOUD)) {
		name = "CLOUD_"+id;
		type = CLOUD;
	}
	x = fields[3];
	y = fields[4];
	width = fields[5];
	height = fields[6];
	shape = fields[7];
	bits = fields[8];
	if (bitwiseAnd(bits, mask2))
	    additionalText = new String(view.getNextRawObject());	
	hidden = fields[9];
	hasFont = fields[10];
	textPos = fields[11];
	boxWidth = fields[12];
	nav1 = fields[13];
	nav2 = fields[14];
	if (!hasFont.equals("0")) {
	    boxColor = fields[15];
	    fillColor = fields[16];
	    font = fields[17];
	}
    }

    private void parseAsBitmap(String[] fields) {
	name = fields[2].replace("\"", "");
	x = fields[3];
	y = fields[4];
	width = fields[5];
	height = fields[6];
	shape = fields[7];
	bits = fields[8];
	hidden = fields[9];
	hasFont = fields[10];
	textPos = fields[11];
	boxWidth = fields[12];
	nav1 = fields[13];
	nav2 = fields[14];
	if (!hasFont.equals("0")) {
	    boxColor = fields[15];
	    fillColor = fields[16];
	    font = fields[17];
	}
	additionalText = new String(view.getNextRawObject());
    }

    private void parseAsMetafile(String[] fields) {
	name = fields[2].replace("\"", "");
	x = fields[3];
	y = fields[4];
	width = fields[5];
	height = fields[6];
	shape = fields[7];
	bits = fields[8];
	hidden = fields[9];
	hasFont = fields[10];
	textPos = fields[11];
	boxWidth = fields[12];
	nav1 = fields[13];
	nav2 = fields[14];
	if (!hasFont.equals("0")) {
	    boxColor = fields[15];
	    fillColor = fields[16];
	    font = fields[17];
	}
	additionalText = new String(view.getNextRawObject());
    }
    
    public void print() {
	
	System.out.println(">>>>");
	System.out.println("    ID: "+id);
	
	if (type.equals("1")) {
	    System.out.println("    Type: "+"Arrow");
	    System.out.println("    Name: "+name);
	    System.out.println("    From: "+from);
	    System.out.println("    To: "+to);
	    System.out.println("    NumPoints: "+numPoints);
	    System.out.println("    PointList: "+pointList);
	    System.out.println("    Shape: "+shape);
	} else if (type.equals("10")) {
	    System.out.println("    Type: "+"Variable");
	    System.out.println("    Name: "+name);
	    System.out.println("    X: "+x);
	    System.out.println("    Y: "+y);
	    System.out.println("    Width: "+width);
	    System.out.println("    Height: "+height);
	    System.out.println("    Shape: "+shape);
	} else if (type.equals("11")) {
	    System.out.println("    Type: "+"Valve");
	    System.out.println("    Name: "+name);
	    System.out.println("    X: "+x);
	    System.out.println("    Y: "+y);
	    System.out.println("    Width: "+width);
	    System.out.println("    Height: "+height);
	    System.out.println("    Shape: "+shape);
	} else if (type.equals("12")) {
	    System.out.println("    Type: "+"Comment");
	    System.out.println("    Name: "+name);
	    System.out.println("    X: "+x);
	    System.out.println("    Y: "+y);
	    System.out.println("    Width: "+width);
	    System.out.println("    Height: "+height);
	    System.out.println("    Comment: "+additionalText);
	    System.out.println("    Shape: "+shape);
	}   else if (type.equals("48")) {
		System.out.println("    Type: "+"Cloud");
		System.out.println("    Name: "+name);
		System.out.println("    X: "+x);
		System.out.println("    Y: "+y);
		System.out.println("    Width: "+width);
		System.out.println("    Height: "+height);
		System.out.println("    Comment: "+additionalText);
		System.out.println("    Shape: "+shape);
	} else if (type.equals("30")) {
	    System.out.println("    Type: "+"BitMap");
	    System.out.println("    Name: "+name);
	    System.out.println("    X: "+x);
	    System.out.println("    Y: "+y);
	    System.out.println("    Width: "+width);
	    System.out.println("    Height: "+height);
	    System.out.println("    Shape: "+shape);
	} else if (type.equals("31")) {
	    System.out.println("    Type: "+"Metafile");
	    System.out.println("    Name: "+name);
	    System.out.println("    X: "+x);
	    System.out.println("    Y: "+y);
	    System.out.println("    Width: "+width);
	    System.out.println("    Height: "+height);
	    System.out.println("    Shape: "+shape);
	} else if (type.equals("99")) {
	    System.out.println("    Type: "+"Rate");
	    System.out.println("    Name: "+name);
	    System.out.println("    X: "+x);
	    System.out.println("    Y: "+y);
	    System.out.println("    Width: "+width);
	    System.out.println("    Height: "+height);
	    System.out.println("    Shape: "+shape);
	}
    }
    
    public boolean isInfluenceArrow() {
    	return type.equals(ARROW) && (shape.equals("0") ||shape.equals("1"));
    }
    
    public boolean isFlowArrow() {
    	return type.equals(ARROW) && (shape.equals("4") ||shape.equals("100") ||shape.equals("68"));
    }
    
    public boolean isInFlowArrow() {
    	return isFlowArrow() && shape.equals("100");
    }
    
    public boolean isOutFlowArrow() {
    	return isFlowArrow() && (shape.equals("4")  ||shape.equals("68"));
    }
    
    public boolean isInputOutput() {
	if (nav1 == null || nav2 == null)
	    return false;
	return (nav1.equals("253") && nav2.equals("253"));
    }
    
    private boolean bitwiseAnd(String value, long mask) {
	long val = Long.parseLong(value);
	long result = val & mask;
	return result > 0;
    }

    public View getView() {
        return view;
    }

    public String getRawObject() {
        return rawObject;
    }

    public String getType() {
        return type;
    }
    
    public String getTypeAsString() {
        return view.translateCodeToString(type);
    }

    public String getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getShape() {
        return shape;
    }

    public String getHidden() {
        return hidden;
    }

    public String getPolarity() {
        return polarity;
    }

    public String getThickness() {
        return thickness;
    }

    public String getHasFont() {
        return hasFont;
    }

    public String getDelayType() {
        return delayType;
    }

    public String getReserved() {
        return reserved;
    }

    public String getColor() {
        return color;
    }

    public String getFont() {
        return font;
    }

    public String getNumPoints() {
        return numPoints;
    }

    public String getPointList() {
        return pointList;
    }

    public String getName() {
        return name;
    }

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getBits() {
        return bits;
    }

    public String getTextPos() {
        return textPos;
    }

    public String getBoxWidth() {
        return boxWidth;
    }

    public String getNav1() {
        return nav1;
    }

    public String getNav2() {
        return nav2;
    }

    public String getBoxColor() {
        return boxColor;
    }

    public String getFillColor() {
        return fillColor;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public GraphicObject getAssociatedVariable() {
        return associatedVariable;
    }

	public void setType(String type) {
		this.type = type;
	}

	public void setAssociatedVariable(GraphicObject associatedVariable) {
		this.associatedVariable = associatedVariable;
	}
}
