package repast.simphony.relogo.ide.intf;

import java.io.*;
import java.util.List;
import org.antlr.runtime.*;
import org.antlr.runtime.debug.DebugEventSocketProxy;


public class ControlSectionParser {
    private boolean success;
    private List<NLControl> controls;
    protected int controlErrors;
    
    public ControlSectionParser(final StringBuffer buf) {
        NetLogoInterfaceLexer lex = new NetLogoInterfaceLexer(new ANTLRStringStream(buf.toString()));
        final CommonTokenStream tokens = new CommonTokenStream(lex);

        NetLogoInterfaceParser g = new NetLogoInterfaceParser(tokens) {
        	public String getErrorMessage(RecognitionException e, String[] tokenNames) { 
        		System.err.println("Problem with parsing interface stream: "+buf);
        		return super.getErrorMessage(e, tokenNames);
        	}  
        	public void reportError(RecognitionException e) {
        		System.err.println("Problem with parsing interface stream: "+buf);
        		super.reportError(e);
        	}

        	public void recoverFromMismatchedToken(IntStream input, RecognitionException e, int ttype, BitSet follow) {
        		try {
        			String problemToken = (tokens.LT(1).getText());
        			controlErrors++;
        			super.recoverFromMismatchedToken(input, e, ttype, follow);
        		} catch (Exception exc) {
        			exc.printStackTrace();
        		}
        		//System.exit(1);
        	}
        };
        try {
        	controls = g.control_section();
        	success = true;
        } catch (RecognitionException e) {
        	success = false;
        	e.printStackTrace();
        }
    }

    public boolean succeeded() {
        return success;
    }
    
    public int errorCount() {
        return controlErrors;
    }
    
    public List<NLControl> getControls() {
        return controls;
    }
    
    public static void main(String args[]) throws Exception {
        NetLogoInterfaceLexer lex = new NetLogoInterfaceLexer(new ANTLRFileStream("C:\\Projects\\netlogo\\ui_example.txt"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        NetLogoInterfaceParser g = new NetLogoInterfaceParser(tokens);
        try {
            System.out.println(g.control_section());
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }
}