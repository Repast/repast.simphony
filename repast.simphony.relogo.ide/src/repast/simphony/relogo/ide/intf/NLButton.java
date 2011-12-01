/*
 * NLButton.java
 *
 * Created on October 3, 2007, 3:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package repast.simphony.relogo.ide.intf;

import java.awt.Rectangle;

/**
 *
 * @author CBURKE
 */
public class NLButton extends NLControl {
    String text;
    String commands;
    Object commandProcedure;
    boolean doForever;
    int unknownInt;
    String unknownString;
    String agentName;
    boolean updateEachRun;
    String actionKey;
    
    /** Creates a new instance of NLButton */
    public NLButton(Rectangle bb, String t, String c, String dof, int u1, String u2, String n, String u, String k) {
        super(bb);
        text = (t.trim().equals("NIL") ? null : t.trim());
        commands = (c.trim().equals("NIL") ? null : c.trim());
        doForever = (dof == null ? false : (dof.trim().equals("T") ? true : false));
        unknownInt = u1;
        unknownString = u2;
        if (unknownInt != 1) {
            System.out.println("unknownInt == "+unknownInt);
        }
        if (!unknownString.trim().equals("T")) {
            System.out.println("unknownString == "+unknownString);
        }
        agentName = n;
        updateEachRun = (u == null ? false : (u.trim().equals("T") ? true : false));
        actionKey = (k == null ? null : (k.trim().equals("NIL") ? null : k.trim()));
//        System.out.println("u1 = " + u1);
//        System.out.println("u2 = " + u2);
//        System.out.println("n = " + n);
//        System.out.println("k = " + k);
    }
    
    /**
	 * @return the doForever
	 */
	public boolean isDoForever() {
		return doForever;
	}

	/**
	 * @return the agentName
	 */
	public String getAgentName() {
		return agentName;
	}

	public String getText() {
        return text;
    }
    
    public String getCommands() {
        return commands;
    }
    
    public Object getCommandProcedure() {
        return commandProcedure;
    }
    
    public void setCommandProcedure(Object proc) {
        commandProcedure = proc;
    }
    
    public String toString() {
        return "Button("+boundingBox.x+", "+boundingBox.y+", "+boundingBox.width+", "+boundingBox.height+", "+
                "text=\""+text+"\", commands=\""+commands+"\", doForever="+doForever+", agentName=\""+agentName+"\", updateEachRun="+updateEachRun+", actionKey="+actionKey+")";
    }
}
