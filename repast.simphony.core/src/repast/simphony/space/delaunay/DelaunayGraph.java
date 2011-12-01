package repast.simphony.space.delaunay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DelaunayGraph {
    private static final long serialVersionUID = 3258416131596695094L;
    private Map<Object, TriangulationPoint> nodeMap;
    List<QuadEdge> edges = new ArrayList<QuadEdge>();
    
    public DelaunayGraph() {
        init();
    }        
    
    public void init() {
        nodeMap = new HashMap<Object, TriangulationPoint>();
        QuadEdge e1 = makeQEdge();
        QuadEdge e2 = makeQEdge();
        QuadEdge e3 = makeQEdge();
        
        // Three points at infinity
        e1.setOrg(new TriangulationPoint(0, -8192, null));
        e2.setOrg(new TriangulationPoint(-16384, 8192, null));
        e3.setOrg(new TriangulationPoint(16384, 8192, null));
        
        e1.setDest(e2.getOrg());
        e1.getOrg().isInf = true;
        e2.setDest(e3.getOrg());
        e2.getOrg().isInf = true;
        e3.setDest(e1.getOrg());
        e3.getOrg().isInf = true;
        
        e1.setONext(e3.getSym());
        e1.getRot().setONext(e2.getRot());
        e1.getSym().setONext(e2);
        e1.getRot3().setONext(e2.getRot3());
        
        e2.setONext(e1.getSym());
        e2.getRot().setONext(e3.getRot());
        e2.getSym().setONext(e3);
        e2.getRot3().setONext(e3.getRot3());
        
        e3.setONext(e2.getSym());
        e3.getRot().setONext(e1.getRot());
        e3.getSym().setONext(e1);
        e3.getRot3().setONext(e1.getRot3());
    }
    
    public QuadEdge connect(QuadEdge a, QuadEdge b) {
        QuadEdge e = makeQEdge();
        e.setOrg(a.getDest());
        e.setDest(b.getOrg());
        splice(e, a.getLNext());
        splice(e.getSym(), b);
        return e;
    }
    
    public void deleteEdge(QuadEdge e) {
        splice(e, e.getOPrev());
        splice(e.getSym(), e.getSym().getOPrev());
        
        QuadEdge eRot;
        for (int i = 0; i < 3; i++) {
            eRot = e.getRot();
            edges.remove(e);
            e = eRot;
        }
        edges.remove(e);
    }
    
    public void swap(QuadEdge e) {
        QuadEdge a = e.getOPrev();
        QuadEdge b = e.getSym().getOPrev();
        splice(e, a);
        splice(e.getSym(), b);
        splice(e, a.getLNext());
        splice(e.getSym(), b.getLNext());
        e.setOrg(a.getDest());
        e.setDest(b.getDest());
    }
    
    public TriangulationPoint getPoint(Object node){
        return nodeMap.get(node);
    }
    
    public void insertSite(TriangulationPoint X, Object node) {
        if(node != null){
            nodeMap.put(node, X);
        }
        QuadEdge e = locate(X); // where is X?
        
        if (X.isOn(e)) { // on a edge
            QuadEdge t = e.getOPrev();
            deleteEdge(e);
            e = t;
        }
        
        // connects X to existing points
        QuadEdge newEdge = makeQEdge();
        TriangulationPoint StartPnt = e.getOrg();
        newEdge.setOrg(StartPnt);
        newEdge.setDest(X);
        splice(newEdge, e);
        do {
            newEdge = connect(e, newEdge.getSym());
            e = newEdge.getOPrev();
        } while (e.getDest() != StartPnt);
        
        // makes necessary changes in the graph
        while (true) {
            QuadEdge t = e.getOPrev();
            boolean passTest = X.isInCircle(e.getOrg(), t.getDest(), e
                    .getDest());
            if (t.getDest().isRightOf(e) && passTest) {
                // X is in the circle. Should delete an old edge and add a
                // new one.
                swap(e);
                e = e.getOPrev();
            } else if (e.getOrg() == StartPnt)
                return;
            else
                e = e.getONext().getLPrev();
        }
    }
    
    public QuadEdge locate(TriangulationPoint X) {
        QuadEdge e = edges.get(0);
        
        while (true) {
            
            if ((X == e.getOrg()) || (X == e.getDest())) {
                return e;
            } else if (X.isRightOf(e))
                e = e.getSym();
            else if (!(X.isRightOf(e.oNext)))
                e = e.oNext;
            else if (!(X.isRightOf(e.getDPrev())))
                e = e.getDPrev();
            else
                return e;
        }
    }
    
    public void getVoronoiDiagram() {
        TriangulationPoint pl, pr;
        
        for (int i = 12; i < edges.size(); i += 4) {
            QuadEdge e = (QuadEdge) edges.get(i);
            
            if (e.isValidEdge()) {
                pl = getVoronoiVertex(e, e.getONext());
                pr = getVoronoiVertex(e, e.getOPrev());
                
                e.rot.setOrg(pr);
                e.rot.setDest(pl);
            }
        }
    }
    
    // Computes the Voronoi vertices, which are the intersecting points of
    // the dual edges of Delaunay Triangulation.
    private TriangulationPoint getVoronoiVertex(QuadEdge e1, QuadEdge e2) {
        TriangulationPoint pnt = new TriangulationPoint(0, 0, null);
        long x2 = (long) e1.getOrg().x;
        long y2 = (long) e1.getOrg().y;
        long x1 = (long) e1.getDest().x;
        long y1 = (long) e1.getDest().y;
        long x3 = (long) e2.getDest().x;
        long y3 = (long) e2.getDest().y;
        
        long det = (y2 - y3) * (x2 - x1) - (y2 - y1) * (x2 - x3);
        long c1 = (x1 + x2) * (x2 - x1) / 2 + (y2 - y1) * (y1 + y2) / 2;
        long c2 = (x2 + x3) * (x2 - x3) / 2 + (y2 - y3) * (y2 + y3) / 2;
        pnt.x = (int) ((c1 * (y2 - y3) - c2 * (y2 - y1)) / det);
        pnt.y = (int) ((c2 * (x2 - x1) - c1 * (x2 - x3)) / det);
        pnt.isInf = false;
        
        return pnt;
    }
    
    public QuadEdge makeQEdge() {
        QuadEdge e[] = new QuadEdge[4];
        for (int i = 0; i < 4; i++)
            e[i] = new QuadEdge();
        
        // set initial relationships
        e[0].setRot(e[1]);
        e[1].setRot(e[2]);
        e[2].setRot(e[3]);
        e[3].setRot(e[0]);
        e[0].setONext(e[0]);
        e[1].setONext(e[3]);
        e[2].setONext(e[2]);
        e[3].setONext(e[1]);
        
        for (int i = 0; i < 4; i++)
            edges.add(e[i]);
        
        return e[0];
    }
    
    public void splice(QuadEdge a, QuadEdge b) {
        QuadEdge aa = a.getONext().getRot();
        QuadEdge bb = b.getONext().getRot();
        
        QuadEdge tmp = a.getONext();
        a.setONext(b.getONext());
        b.setONext(tmp);
        
        tmp = aa.getONext();
        aa.setONext(bb.getONext());
        bb.setONext(tmp);
    }
}
