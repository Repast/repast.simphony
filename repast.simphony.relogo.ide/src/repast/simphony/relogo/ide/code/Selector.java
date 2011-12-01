/*
 * Encapsulates an agentset selection criterion.
 */
package repast.simphony.relogo.ide.code;

import java.util.LinkedList;

/**
 *
 * @author CBURKE
 */
public class Selector {

    protected Object criteria;

    public Selector(Object c) {
        criteria = c;
    }

    public String toString() {
        return criteria.toString();
    }
    
    public String toString(int indent) {
        Object criterion = criteria;
        if (criterion instanceof LinkedList) {
            LinkedList critlist = (LinkedList)criterion;
            if (critlist.size() != 1) {
                System.err.println("Selector has more than one child!");
            }
            if (!critlist.isEmpty()) {
                criterion = critlist.get(0);
            }
        }
        StringBuffer buf = new StringBuffer();
        if (criterion instanceof Block) {
            buf.append(((Block) criterion).toStringAsClosure(indent + 4, null));
        } else if (criterion instanceof ProcedureInvocation) {
            buf.append(((ProcedureInvocation) criterion).toString(indent + 4));
        } else {
            buf.append(criterion.toString());
        }
        return buf.toString();
    }
}
