package repast.simphony.visualization.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.styling.Style;

import repast.simphony.gis.RepastMapLayer;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;
import repast.simphony.visualization.editor.gis.DisplayEditorGIS;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * **** IN PROGRESS NYI ****
 * 
 * Display Layer for networks in 2D GIS displays
 * 
 * @author Eric Tatara
 *
 */
public class NetworkDisplayLayerGIS extends RepastMapLayer implements ProjectionListener {

	private Geography geography;
	private Map <RepastEdge<?>,Feature> featureMap;
	Set<RepastEdge> edgesToAdd;
	private Network<?> net;
	private FeatureCollection fc;
	
	private Lock lock = new ReentrantLock();
	
	public NetworkDisplayLayerGIS(Network<?> net, Geography geography, Style style) {
		super(FeatureCollections.newCollection(), style);
		
		try {
			fc = this.getFeatureSource().getFeatures();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		net.addProjectionListener(this);
		setDynamic(true);
		this.geography = geography;
		this.net = net;
		
		edgesToAdd = new HashSet<RepastEdge>();
		featureMap = new HashMap<RepastEdge<?>,Feature>();
	}
	
	public void update() {
		try {
			lock.lock();

			addAddedEdges();
//			removeRemovedEdges();

		} finally {
			lock.unlock();
		}
	}
	
	public void addEdge(RepastEdge edge) {
		try {
			lock.lock();
			edgesToAdd.add(edge);
		} finally {
			lock.unlock();
		}
	}

	protected void addAddedEdges(){
		for (RepastEdge edge : edgesToAdd) {
			
//			System.out.println(" $ NetworkDisplayLayer -> adding edge: " + edge);
			
			List<Coordinate> coords = new ArrayList<Coordinate>();
			coords.add(geography.getGeometry(edge.getSource()).getCoordinate());
			coords.add(geography.getGeometry(edge.getTarget()).getCoordinate());

			Class<?> agentClass = net.getEdgeCreator().getEdgeType();
			
			EdgeFeature feature = new EdgeFeature(agentClass, 
					DisplayEditorGIS.createLineString(coords), geography.getCRS());
			featureMap.put(edge, feature);

			feature.setParent(fc);
		  fc.add(feature);

		}
	}

	public void removeEdge(RepastEdge edge) {
		
	}
	
	public void projectionEventOccurred(ProjectionEvent evt) {
		if (evt.getType() == ProjectionEvent.EDGE_ADDED) {
			addEdge((RepastEdge) evt.getSubject());
		} else if (evt.getType() == ProjectionEvent.EDGE_REMOVED) {
			removeEdge((RepastEdge) evt.getSubject());
		}
		
	}
	
	public void test(){
	}

}
