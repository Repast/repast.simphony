/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repast.simphony.relogo.ide.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

import repast.simphony.relogo.ide.code.CodeSectionParser;
import repast.simphony.relogo.ide.code.MethodPartition;
import repast.simphony.relogo.ide.code.NetLogoRGWLexer;
import repast.simphony.relogo.ide.code.NetLogoRGWParser;
import repast.simphony.relogo.ide.code.Profile;
import repast.simphony.relogo.ide.dynamics.SystemDynamicsParser;
import repast.simphony.relogo.ide.image.ImageSectionParser;
import repast.simphony.relogo.ide.image.NLImage;
import repast.simphony.relogo.ide.intf.ControlSectionParser;
import repast.simphony.relogo.ide.intf.NLButton;
import repast.simphony.relogo.ide.intf.NLChooser;
import repast.simphony.relogo.ide.intf.NLControl;
import repast.simphony.relogo.ide.intf.NLInputBox;
import repast.simphony.relogo.ide.intf.NLMonitor;
import repast.simphony.relogo.ide.intf.NLPlot;
import repast.simphony.relogo.ide.intf.NLSlider;
import repast.simphony.relogo.ide.intf.NLSwitch;
import repast.simphony.relogo.ide.wizards.WizardUtilities;

/**
 *
 * @author CBURKE
 */
public class NetlogoSimulation {

    private String separator;
    private int maxSectionIndex;
    protected String sourceFile;
    
    protected CodeSectionParser codeParser;
    protected ControlSectionParser csp;
    protected ImageSectionParser turtleParser;
    protected SystemDynamicsParser systemDynamicsParser;

    /*
     * Some additional information is needed to actually generate code
     * in the correct location. This can be expected to be set in the
     * invoking wizard.
<?xml version="1.0" encoding="UTF-8"?>
<score:SContext xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:score="http://scoreabm.org/score" label="Simple Happy Model" ID="simpleHappyModel">
  <attributes label="A String Parameter" ID="aStringParameter" sType="STRING" defaultValue="Cormac"/>
  <attributes label="A FLoat Parameter" ID="aFLoatParameter" sType="FLOAT" defaultValue="3.5"/>
  <attributes label="An Int Parameter" ID="anIntParameter" sType="INTEGER" defaultValue="8"/>
  <implementation package="repast.simphony.demo.simple" className="SimpleHappyContextCreator" basePath="../repast.simphony.demos"/>
  <agents label="Simple Happy Agent" ID="simpleHappyAgent">
    <attributes label="Happiness" ID="happiness" sType="FLOAT" defaultValue=""/>
    <attributes label="Current Wealth" ID="currentWealth" defaultValue="" units=""/>
    <implementation className="SimpleHappyAgent" binDir=""/>
  </agents>
  <projections xsi:type="score:SNetwork" label="A Network" ID="network"/>
  <projections xsi:type="score:SContinuousSpace" label="Continuous Space" ID="continuousSpace"/>
  <projections xsi:type="score:SGrid" label="Simple Grid" ID="simpleGrid" dimensionality="3">
    <attributes label="X Extent" ID="xExtent" description="The horizontal extent of the space." sType="INTEGER"/>
    <attributes label="Y Extent" ID="yExtent" description="The vertical extent of the space." sType="INTEGER"/>
    <attributes label="Z Extent" ID="zExtent" description="The depth extent of the space." sType="INTEGER"/>
  </projections>
</score:SContext>
     */
    
    public NetlogoSimulation() {
        maxSectionIndex = 0;
        separator = System.getProperty("file.separator");
        codeParser = new CodeSectionParser();
        // other section parsers instantiated locally
    }

    protected String unquote(String s) {
        if (s.startsWith("\"")) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    protected String unescape(String s) {
        if (s == null) {
            return s;
        }
        StringBuffer buf = new StringBuffer(s);
        for (int i = 0; i < buf.length(); i++) {
            if (buf.charAt(i) == '\\') {
                // process an escape code
                //   \n	New line
                //   \t	Tab
                //   \b	Backspace
                //   \r	Carriage return
                //   \f	Formfeed
                //   \\	Backslash
                //   \'	Single quotation mark
                //   \"	Double quotation mark
                //   \d	Octal
                //   \xd	Hexadecimal
                //   \ ud	Unicode character
                char esc = buf.charAt(i + 1);
                switch (esc) {
                    case '0':
                    case '1':
                    case '2':
                    case '3': {
                        char esc2 = buf.charAt(i + 2);
                        char esc3 = buf.charAt(i + 3);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        int ch = 0;
                        int tmp = esc - '0';
                        int tmp2 = esc2 - '0';
                        int tmp3 = esc3 - '0';
                        ch = (tmp * 8 + tmp2) * 8 + tmp3;
                        if (tmp > 7 || tmp2 > 7 || tmp3 > 7 || tmp < 0 || tmp2 < 0 || tmp3 < 0) {
                            System.err.println("Unexpected character in Unicode escape.");
                        } else {
                            buf.setCharAt(i, (char) ch);
                        }
                        break;
                    }
                    case 'x': {
                        char esc2 = buf.charAt(i + 2);
                        char esc3 = buf.charAt(i + 3);
                        char esc4 = buf.charAt(i + 4);
                        char esc5 = buf.charAt(i + 5);
                        int tmp2 = "0123456789ABCDEF".indexOf(esc2);
                        int tmp3 = "0123456789ABCDEF".indexOf(esc3);
                        int tmp4 = "0123456789ABCDEF".indexOf(esc4);
                        int tmp5 = "0123456789ABCDEF".indexOf(esc5);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        int ch = 0;
                        ch = ((tmp2 * 16 + tmp3) * 16 + tmp4) * 16 + tmp5;
                        if (tmp2 > 15 || tmp3 > 15 || tmp4 > 15 || tmp5 > 15 || tmp2 < 0 || tmp3 < 0 || tmp4 < 0 || tmp5 < 0) {
                            System.err.println("Unexpected character in Unicode escape.");
                        } else {
                            buf.setCharAt(i, (char) ch);
                        }
                        break;
                    }
                    case 'u': {
                        char esc2 = buf.charAt(i + 2);
                        char esc3 = buf.charAt(i + 3);
                        char esc4 = buf.charAt(i + 4);
                        char esc5 = buf.charAt(i + 5);
                        int tmp2 = "0123456789ABCDEF".indexOf(esc2);
                        int tmp3 = "0123456789ABCDEF".indexOf(esc3);
                        int tmp4 = "0123456789ABCDEF".indexOf(esc4);
                        int tmp5 = "0123456789ABCDEF".indexOf(esc5);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        buf.deleteCharAt(i);
                        int ch = 0;
                        ch = ((tmp2 * 16 + tmp3) * 16 + tmp4) * 16 + tmp5;
                        if (tmp2 > 15 || tmp3 > 15 || tmp4 > 15 || tmp5 > 15 || tmp2 < 0 || tmp3 < 0 || tmp4 < 0 || tmp5 < 0) {
                            System.err.println("Unexpected character in Unicode escape.");
                        } else {
                            buf.setCharAt(i, (char) ch);
                        }
                        break;
                    }
                    case 'n':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\n');
                        break;
                    case 't':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\t');
                        break;
                    case 'b':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\b');
                        break;
                    case 'r':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\r');
                        break;
                    case 'f':
                        buf.deleteCharAt(i);
                        buf.setCharAt(i, '\f');
                        break;
                    case '\\':
                    case '\'':
                    case '\"':
                        buf.deleteCharAt(i);
                        break;
                }
            }
        }
        return buf.toString();
    }

    public boolean scan(String source) {
        try {
            sourceFile = source;
            Reader is = new FileReader(source);
            return scan(is);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean scan(Reader r) {
        BufferedReader rdr = new BufferedReader(r);
        int sectionIndex = 0;
        int linesInSection = 0;
        StringBuffer sectionBuffer = new StringBuffer();
        try {
            for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
                //line = line.trim();  // removed for system dynamics parser; verify other are still ok.
                if (line.equals("@#$#@#$#@")) {
                    // code_section windows_section doc_section image_section ver_section pre_section ser_section bsp_section?
                    switch (sectionIndex) {
                        case 0: // code section
                            codeParser.parse(sectionBuffer.toString());
                            processIncludeFiles(codeParser);
                            //codeParser.dumpTree();
                            break;
                        case 1: // control section
                            csp = new ControlSectionParser(sectionBuffer);
                            //System.out.println(csp.getControls());
                            break;
                        case 2: // documentation section
                            break;
                        case 3: // turtle vector graphics section
                            turtleParser = new ImageSectionParser(sectionBuffer);
                            //System.out.println(turtleParser.getModel());
                            break;
                        case 4: // NetLogo version number section
                            break;
                        case 5: // preview section
                            break;
                        case 6: // System Dynamics section
                            systemDynamicsParser = new SystemDynamicsParser(sectionBuffer);
                            break;
                        case 7: // Behavior Space section (XML-ish)
                            break;
                        case 8: // HubNet section (similar to controls section)
                            break;
                        case 9: // ??? section
                            break;
                        default: // all other sections
                            break;
                    }
                    //if (sectionIndex > 4 && linesInSection > 0) {
                    //    System.out.println("section "+sectionIndex+" lines "+linesInSection);
                    //}
                    sectionIndex++;
                    linesInSection = 0;
                    sectionBuffer.setLength(0);
                } else {
                    sectionBuffer.append(line);
                    sectionBuffer.append('\n');
                    linesInSection++;
                }
            }
            if (maxSectionIndex == 0) {
                maxSectionIndex = sectionIndex;
            } else if (sectionIndex != maxSectionIndex) {
                //System.out.println("Model section count variance.");
                if (sectionIndex > maxSectionIndex) {
                    maxSectionIndex = sectionIndex;
                }
            }
            // harvest additional declarations from the controls
            int buttonIndex = 0;
            List<NLControl> controls = csp.getControls();
            for (NLControl ctl : controls) {
                if (ctl instanceof NLButton) {
                    String label = ((NLButton) ctl).getText();
                    String cmd = ((NLButton) ctl).getCommands();
                    String agentName = ((NLButton) ctl).getAgentName();
                    boolean doForever = ((NLButton) ctl).isDoForever();
                    cmd = unescape(cmd);
                    if (label == null) {
                        label = cmd;
                    }
                    label = WizardUtilities.getJavaName(label);
                    String procName;
                    if (!doForever){
                    	procName = "button_method_" + label;
                    }
                    else {
                    	procName = "toggle_button_method_" + label;
                    }
                    if (agentName.equals("OBSERVER")){
                    	cmd = ";; " + label + "\nto " + procName + " " + cmd + "\n end";
                    }
                    else {
                    	String targetAgentSet;
                    	if (agentName.equals("TURTLE")){
                    		targetAgentSet = "turtles";
                    	}
                    	else if (agentName.equals("PATCH")){
                    		targetAgentSet = "patches";
                    	}
                    	else {
                    		targetAgentSet = "links";
                    	}
                    	cmd = ";; " + label + "\nto " + procName + "\nask " + targetAgentSet + " [\n" + cmd + "\n]\n end";
                    }
                    //System.out.println("Button command == " + cmd);
                    NetLogoRGWLexer lex = new NetLogoRGWLexer(new ANTLRStringStream(cmd));
                    CommonTokenStream tokens = new CommonTokenStream(lex);
                    final String savedCmd = cmd;
                    NetLogoRGWParser g = new NetLogoRGWParser(tokens) {

                        public String getErrorMessage(RecognitionException e, String[] tokenNames) {
                            System.err.println("Problem with parsing interface stream: " + savedCmd);
                            return super.getErrorMessage(e, tokenNames);
                        }
                    };
                    // parse the commands
                    CommonTree tree = (CommonTree) g.prog().getTree();
                    //Block codeBlock = new Block(codeParser.generateCode(tree));
                    Profile buttonProfile = new Profile(procName, "void=void/void");
                    //ProcedureDefinition pd = new ProcedureDefinition(buttonProfile, null, codeBlock);
                    buttonProfile.setCode(tree);
                    codeParser.declareSymbol("*PROCEDURE*", procName, buttonProfile);
                } else if (ctl instanceof NLChooser) {
                	// 'enum' not a supported type; make this INTEGER
                    String var = ((NLChooser) ctl).getVariable().trim();
                    String lab = ((NLChooser) ctl).getLabel();
                    Object val = ((NLChooser) ctl).getInitialValue();
                    codeParser.declareAttribute("*GLOBAL*", var, lab, "INTEGER", val,false);
                } else if (ctl instanceof NLInputBox) {
                	// could be any type; try to infer from default value
                    String var = ((NLInputBox) ctl).getVariable();
                    String lab = ((NLInputBox) ctl).getLabel();
                    String type = "STRING";  // placeholder
                    Object val = ((NLInputBox) ctl).getInitialValue();
                    codeParser.declareAttribute("*GLOBAL*", var, lab, type, val, false);
                } else if (ctl instanceof NLMonitor) {
                	// how should these be handled? they are not input attributes
                    String var = ((NLMonitor) ctl).getVariable();
                    // remove newlines
                    String tempVar = WizardUtilities.stripNewLines(var);
                    var = tempVar;
                    String label = ((NLMonitor) ctl).getLabel();
                    if (label != null && label.length() > 0) {
                        label = codeParser.getJavaName(label);
                    } else {
                        label = codeParser.getJavaName(var);
                    }
                    label = "monitor_reporter_"+label;
                    Profile buttonProfile = new Profile(label, "*=void/void");
                    var = "to-report " + label + "\n report ( " + var + " )\n end";
                    //System.out.println("Button command == " + cmd);
                    NetLogoRGWLexer lex = new NetLogoRGWLexer(new ANTLRStringStream(var));
                    CommonTokenStream tokens = new CommonTokenStream(lex);
                    final String savedCmd = var;
                    NetLogoRGWParser g = new NetLogoRGWParser(tokens) {

                        public String getErrorMessage(RecognitionException e, String[] tokenNames) {
                            System.err.println("Problem with parsing interface stream: " + savedCmd);
                            return super.getErrorMessage(e, tokenNames);
                        }
                    };
                    // parse the commands
                    CommonTree tree = (CommonTree) g.prog().getTree();
                    buttonProfile.setCode(tree);
                    codeParser.declareSymbol("*PROCEDURE*", label, buttonProfile);
                } else if (ctl instanceof NLSlider) {
                	// typically FLOAT, although may be INTEGER
                    String var = ((NLSlider) ctl).getVariable();
                    String lab = ((NLSlider) ctl).getLabel();
                    Object val = ((NLSlider) ctl).getInitialValue();
                    String type = ((NLSlider) ctl).getScoreType();
                    codeParser.declareAttribute("*GLOBAL*", var, lab, type, val, false);
                } else if (ctl instanceof NLSwitch) {
                	// BOOLEAN
                    String var = ((NLSwitch) ctl).getVariable();
                    String lab = ((NLSwitch) ctl).getLabel();
                    String type = "BOOLEAN";  // placeholder
                    Object val = ((NLSwitch) ctl).getInitialValue();
                    if (val != null) {
                    	String value = val.toString();
                    	val = value.equalsIgnoreCase("t") ? "true" : value.equalsIgnoreCase("true") ? "true" : "false";
                    }
                    codeParser.declareAttribute("*GLOBAL*", var, lab, type, val, false);
                }
            }
            // harvest declarations from the System Dynamics component, if any
            if (systemDynamicsParser != null) {
                HashMap<String, String[]> sdSymbols =
                        systemDynamicsParser.getSystemDynamicsSymbols();
                for (Map.Entry<String, String[]> me : sdSymbols.entrySet()) {
                    String newReporterName = null;
                    String newReporter = null;
                    if (me.getValue()[1].equals("Converter")) {
                        // global variable (constant? parameter?)
                        codeParser.declareAttribute("*GLOBAL*", me.getKey());
                        newReporterName = "__INIT_" + codeParser.getJavaName(unquote(me.getKey()));
                        newReporter = "to-report " + newReporterName + " " + unquote(unescape(me.getValue()[0])) + "\n end";
                    } else if (me.getValue()[1].equals("Rate")) {
                        // dynamic reporter, presumably also stored per step
                        codeParser.declareAttribute("*GLOBAL*", me.getKey());
                        newReporterName = "__UPDATE_" + codeParser.getJavaName(unquote(me.getKey()));
                        newReporter = "to-report " + newReporterName + " " + unquote(unescape(me.getValue()[0])) + "\n end";
                    } else if (me.getValue()[1].equals("Stock")) {
                        // global variable (may have initial value)
                        codeParser.declareAttribute("*GLOBAL*", me.getKey());
                        if (me.getValue()[0] != null) {
                            newReporterName = "__INIT_" + codeParser.getJavaName(unquote(me.getKey()));
                            newReporter = "to-report " + newReporterName + " " + unquote(unescape(me.getValue()[0])) + "\n end";
                        }
                    }
                    if (newReporter != null) {
                        NetLogoRGWLexer lex = new NetLogoRGWLexer(new ANTLRStringStream(newReporter));
                        CommonTokenStream tokens = new CommonTokenStream(lex);
                        final String savedCmd = newReporter;
                        NetLogoRGWParser g = new NetLogoRGWParser(tokens) {

                            public String getErrorMessage(RecognitionException e, String[] tokenNames) {
                                System.err.println("Problem with parsing system dynamics stream: " + savedCmd);
                                return super.getErrorMessage(e, tokenNames);
                            }
                        };
                        // parse the commands
                        CommonTree tree = (CommonTree) g.prog().getTree();
                        Profile sdProfile = new Profile(newReporterName, "*=void/void");
                        sdProfile.setCode(tree);
                        codeParser.declareSymbol("*PROCEDURE*", newReporterName, sdProfile);
                    }
                }
            }
//            codeParser.declareExtensionSymbols();
            codeParser.generateCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public MethodPartition getMethodPartition() {
    	return codeParser.getMethodPartition();
    }
    
    public NLPlot[] getPlots() {
    	if (csp == null) {
    		return new NLPlot[0];
    	}
    	int plotCount = 0;
    	for (NLControl control : csp.getControls()) {
    		if (control instanceof NLPlot) {
    			plotCount++;
    		}
    	}
    	NLPlot[] plots = new NLPlot[plotCount];
    	plotCount = 0;
    	for (NLControl control : csp.getControls()) {
    		if (control instanceof NLPlot) {
    			plots[plotCount++] = (NLPlot)control;
    		}
    	}
    	return plots;
    }
    
    public List<NLControl> getNLControls(){
    	return csp.getControls();
    }
    
    public List<NLImage> getNLImages(){
    	return turtleParser.getModel();
    }
    
    protected boolean scanIncludeFile(CodeSectionParser codeParser, Reader r)
            throws IOException {
        BufferedReader rdr = new BufferedReader(r);
        StringBuffer includeBuffer = new StringBuffer();
        for (String line = rdr.readLine(); line != null; line = rdr.readLine()) {
            includeBuffer.append(line);
            includeBuffer.append("\n");
        }
        return codeParser.parse(includeBuffer.toString());
    }

    protected boolean processIncludeFiles(CodeSectionParser codeParser) {
        LinkedList<String> includeFiles = codeParser.getIncludeFiles();
        if (includeFiles == null) {
            return true;
        }
        if (sourceFile == null) {
            return false;
        }  // don't know where to get the files!
        try {
            File sourceRef = new File(sourceFile);
            String includePath = sourceRef.getParent() + separator;
            boolean ok = true;
            for (String inc : includeFiles) {
                System.err.println("--> including "+inc);
                File incRef = new File(unquote(inc));
                if (!incRef.exists()) {
                    incRef = new File(includePath + unquote(inc));
                    if (!incRef.exists()) {
                        System.err.println("Can't find required include file " + inc);
                        return false;
                    }
                }
                ok = ok && scanIncludeFile(codeParser, new FileReader(incRef));
            }
        } catch (Exception e) {
            // think about what to do here...
            e.printStackTrace();
        }
        return true;
    }
}
