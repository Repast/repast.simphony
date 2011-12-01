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
public class Paren {

    protected String type;
    protected LinkedList contents;

    public Paren(LinkedList c) {
        this(null, c);
    }

    public Paren(String t, LinkedList c) {
        type = t;
        contents = c;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("(");
        if (!contents.isEmpty()) {
            boolean notFirst = false;
            for (Object o : contents) {
                if (notFirst) {
                    buf.append(", ");
                } else {
                    notFirst = true;
                }
                buf.append(o.toString());
            }
        }
        buf.append(")");
        return buf.toString();
    }
}
