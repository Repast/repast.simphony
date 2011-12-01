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
public class ProcedureDefinition {

    protected Profile profile;
    protected Block code;
    protected String name;
    protected LinkedList<String> formals;
    
    public ProcedureDefinition(Profile p, LinkedList<String> f, Block b) {
        profile = p;
        formals = f;
        code = b;
        if (profile == null) {
            name = "__anonymous_"+anonymousProcedureIndex;
            anonymousProcedureIndex++;
        } else {
            name = profile.getJavaName();
        }
    }
    
    
    
    /**
	 * @return the code
	 */
	public Block getCode() {
		return code;
	}



	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public void setName(String newName){
		name = newName;
	}



	public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("def ");
        buf.append(name);
        buf.append("(");
        if (formals != null) {
            boolean notFirst = false;
            for (String f:formals) {
                if (notFirst) {
                    buf.append(", ");
                } else {
                    notFirst = true;
                }
                buf.append(f);
            }
        }
        buf.append(") ");
        buf.append(code.toString(4,true));
        return buf.toString();
    }
    
    static protected int anonymousProcedureIndex = 0;
}
