/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repast.simphony.relogo.ide.code;

import java.util.LinkedList;

/**
 *
 * @author CBURKE
 */
public class Block {

    protected String type;
    protected LinkedList contents;
    protected LinkedList<Attribute> localVariables;
    protected boolean isMethodDefinition = false;
    
    public Block() {
        this(null, null);
    }
    
    public Block(LinkedList c) {
        this(null, c);
    }

    public Block(String t, LinkedList c) {
        type = t;
        contents = c;
    }
    
    public void setType(String t) {
        type = t;
    }
    
    public String getType(){
    	return type;
    }
    
    public void setContents(LinkedList c) {
        contents = c;
    }

    public LinkedList getContents() {
        return contents;
    }
    
    public Attribute findSymbol(String name) {
        if (localVariables != null) {
            for (Attribute attr : localVariables) {
                if (attr.name.equals(name)) {
                    return attr;
                }
            }
        }
        return null;
    }
    
    public void defineLocalVariable(Attribute attr) {
        if (localVariables == null) {
            localVariables = new LinkedList<Attribute>();
        }
        localVariables.add(attr);
    }
    
    public String toString() {
    	
        return toString(4);
    }

    public String toStringAsList() {
    	StringBuffer buf = new StringBuffer();
    	buf.append("[");
    	if (contents != null && !contents.isEmpty()) {
    		boolean isNotFirst = false;
            for (Object o : contents) {
            	if (isNotFirst){
            		buf.append(",");
            	}
                if (o instanceof Block) {
                    buf.append(((Block) o).toStringAsList());
                } 
                else {
                    buf.append(o.toString());
                }
                isNotFirst = true;
            }
        }
    	buf.append("]");
    	return buf.toString();
    }
    
    public String toString(int indent, boolean isMethodDefinition){
    	this.isMethodDefinition = isMethodDefinition;
    	return toString(indent);
    }
    public String toString(int indent) {
    	if (getType() != null && getType().equals("list")){
    		return toStringAsList();
    	}
        return toStringAsClosure(indent, null);
    }

    public String toStringAsClosure(int indent, String lambda) {
        StringBuffer buf = new StringBuffer();
        buf.append("{");
        if (lambda != null) {
            buf.append(lambda);
            buf.append(" ->");
        }
        if (contents != null && !contents.isEmpty()) {
        	if (contents.size() != 1 || isMethodDefinition){
        		buf.append("\n");
        	}
            for (Object o : contents) {
            	if (contents.size() != 1 || isMethodDefinition){
	                for (int i = 0; i < indent; i++) {
	                    buf.append(" ");
	                }
            	}
            	else {
            		buf.append(" ");
            	}
                if (o instanceof Block) {
                    buf.append(((Block) o).toString(indent + 4));
                } else if (o instanceof ProcedureInvocation) {
                    buf.append(((ProcedureInvocation) o).toString(indent + 4));
                } else {
                    buf.append(o.toString());
                }
                if (contents.size() != 1 || isMethodDefinition){
            		buf.append("\n");
            	}
            }
            if (contents.size() != 1 || isMethodDefinition) {
				// indent the closing bracket appropriately.
				// FIXME: use parameter instead of constant for indent size
				for (int i = 0; i < (indent - 4); i++) {
					buf.append(" ");
				}
			}
            else {
            	buf.append(" ");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    public Object execute(Object context) {
        Object result = null;
        if (contents != null) {
            for (Object next : contents) {
                if (next instanceof ProcedureInvocation) {
                    result = ((ProcedureInvocation)next).execute(context);
                } else if (next instanceof Block) {
                    result = ((Block)next).execute(context);
                } else if (next instanceof Attribute) {
                    result = ((Attribute)next).get(context);
                } else {
                    result = next;
                }
            }
        }
        return result;
    }
}
