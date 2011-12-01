/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repast.simphony.relogo.ide.dynamics;

import java.util.HashMap;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;

/**
 *
 * @author CBURKE
 */
public class SystemDynamicsParser {

    protected HashMap<String, String[]> symbols;

    public HashMap<String, String[]> getSystemDynamicsSymbols() {
        return symbols;
    }
    
    public SystemDynamicsParser(StringBuffer sb) {
        this(sb.toString());
    }

    public SystemDynamicsParser(final String s) {
        symbols = new HashMap<String, String[]>();
        if (s.length() == 0) return;
        NetLogoSystemDynamicsLexer lex = new NetLogoSystemDynamicsLexer(new ANTLRStringStream(s));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        NetLogoSystemDynamicsParser g = new NetLogoSystemDynamicsParser(tokens) {

            public String getErrorMessage(RecognitionException e, String[] tokenNames) {
                System.err.println("Problem with parsing interface stream: " + s);
                return super.getErrorMessage(e, tokenNames);
            }

            public void reportError(RecognitionException e) {
                System.err.println("Problem with parsing interface stream: " + s);
                super.reportError(e);
            }
        };
        try {
            // parse the file
            CommonTree tree = (CommonTree) g.file().getTree();
            //dumpTree(tree);
            extractSymbols(tree);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }

    public void dumpTree(CommonTree tree) {
        dumpChildren(tree, 0);
    }

    public static void dumpChildren(CommonTree tree, int depth) {
        if (tree == null) {
            return;
        }
        for (int j = 0; j < depth * 4; j++) {
            System.out.print(" ");
        }
        if (tree.getToken() == null) {
            System.out.print("*nil*");
        } else {
            System.out.print(tree.getToken().getText());
        }
        int n = tree.getChildCount();
        if (n > 0) {
            System.out.println("[");
            for (int i = 0; i < n; i++) {
                dumpChildren((CommonTree) tree.getChild(i), depth + 1);
            }
            for (int j = 0; j < depth * 4; j++) {
                System.out.print(" ");
            }
            System.out.println("]");
        } else {
            System.out.println();
        }
    }

    protected void processEntry(CommonTree entry) {
        String declType = entry.getToken().getText();
        if (declType == null) {
            System.err.println("System Dynamics entry with null token!");
        } else if (declType.equals("ENTRY")) {
            //dumpTree(entry);
            CommonTree indent = ((CommonTree) entry.getChild(0));
            int indentation = indent.getToken().getText().length() / 4;
            String entityClass = ((CommonTree) entry.getChild(1)).getToken().getText();
            String entityName = null;
            String[] entityData = new String[2];
            if (entityClass.equals("org.nlogo.aggregate.gui.WrappedConverter")) {
                // global variable (constant? parameter?)
                entityData[0] = ((CommonTree) entry.getChild(2).getChild(0)).getToken().getText();
                entityData[1] = "Converter";
                entityName = ((CommonTree) entry.getChild(3).getChild(0)).getToken().getText();
            } else if (entityClass.equals("org.nlogo.aggregate.gui.WrappedRate")) {
                // dynamic reporter
                entityData[0] = ((CommonTree) entry.getChild(2).getChild(0)).getToken().getText();
                entityData[1] = "Rate";
                entityName = ((CommonTree) entry.getChild(3).getChild(0)).getToken().getText();
            } else if (entityClass.equals("org.nlogo.aggregate.gui.WrappedStock")) {
                // global variable (may have initial value)
                entityData[1] = "Stock";
                if (((CommonTree) entry.getChild(3)).getToken().getText().equals("STR")) {
                    entityData[0] = ((CommonTree) entry.getChild(3).getChild(0)).getToken().getText();
                }
                entityName = ((CommonTree) entry.getChild(2).getChild(0)).getToken().getText();
            }
            if (entityName != null) {
                symbols.put(entityName, entityData);
            }
        } else {
            System.err.println("Unrecognized System Dynamics declaration type '" + declType + "'");
        }
    }

    /**
     * Extract global symbols and references defined in the system dynamics
     * section of the model.
     * @param tree
     */
    protected void extractSymbols(CommonTree decl) {
        String declType = decl.getToken().getText();
        if (declType == null) {
            System.err.println("System Dynamics declaration with null token!");
        } else if (declType.equals("MODEL")) {
            //decl.getChildCount() >= 1) {
            String modelNumber = ((CommonTree) decl.getChild(0)).getToken().getText();
            for (int i = 1; i < decl.getChildCount(); i++) {
                processEntry((CommonTree) decl.getChild(i));
            }
        } else {
            System.err.println("Unrecognized System Dynamics declaration type '" + declType + "'");
        }
    }
}
