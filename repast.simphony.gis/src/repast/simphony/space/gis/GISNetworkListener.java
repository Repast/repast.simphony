/* Copyright (c) 2017 Argonne National Laboratory
   All rights reserved.
  
   Redistribution and use in source and binary forms, with 
   or without modification, are permitted provided that the following 
   conditions are met:
  
  	 Redistributions of source code must retain the above copyright notice,
  	 this list of conditions and the following disclaimer.
  
  	 Redistributions in binary form must reproduce the above copyright notice,
  	 this list of conditions and the following disclaimer in the documentation
  	 and/or other materials provided with the distribution.
  
  	 Neither the name of the Argonne National Laboratory nor the names of its
     contributors may be used to endorse or promote products derived from
     this software without specific prior written permission.
  
   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
   ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
   LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
   PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
   CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
   EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
   PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
   PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
   LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
   NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
   EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package repast.simphony.space.gis;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import repast.simphony.context.Context;
import repast.simphony.space.gis.Geography;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.ProjectionEvent;
import repast.simphony.space.projection.ProjectionListener;

/**
 * A ProjectionListener implementation for managing Repast network edges in a
 * Repast geography projection.  This listener responds to both geography event
 * and network events.
 * 
 * @author Eric Tatara
 *
 */
public class GISNetworkListener implements ProjectionListener {

	Context context;
	Network network;
	Geography geography;
	GeometryFactory fac = new GeometryFactory();
	
	public GISNetworkListener(Context c, Geography g, Network n) {
		context = c;
		network = n;
		geography = g;
		
		network.addProjectionListener(this);
		geography.addProjectionListener(this);
	}
	
	@Override
	public void projectionEventOccurred(ProjectionEvent evt) {
	
		// When an object is moved in the geography, its network edges positions 
		// should be updated if the object has edges.
		if (evt.getType() == ProjectionEvent.OBJECT_MOVED){
			Iterable<RepastEdge> edges = network.getEdges(evt.getSubject());

			if (edges != null){
				for (RepastEdge e : edges){
					// Get the existing geometry for this edge
					MultiLineString lineFeature = (MultiLineString)geography.getGeometry(e);
					
					Coordinate sourceCoord = geography.getGeometry(e.getSource()).getCoordinate();
					Coordinate targetCoord = geography.getGeometry(e.getTarget()).getCoordinate();

					Coordinate coords[] = lineFeature.getCoordinates();
					
					// Update the edge coordinates based on the source and target object 
					// (agent) coordinates.
					coords[0].setCoordinate(sourceCoord);
					coords[1].setCoordinate(targetCoord);
				}
			}
		}
			
		// When a Repast network edge is added, create a new MultiLineString geometry
		// to represent the edge in the geography.
		else if (evt.getType() == ProjectionEvent.EDGE_ADDED){	
			RepastEdge e = (RepastEdge)evt.getSubject();
			
			Coordinate sourceCoord = geography.getGeometry(e.getSource()).getCoordinate();
			Coordinate targetCoord = geography.getGeometry(e.getTarget()).getCoordinate();
			
			LineString lineString = fac.createLineString(new Coordinate[]{sourceCoord, 
					targetCoord});
			
			MultiLineString mls = fac.createMultiLineString(new LineString[]{lineString});
			
			context.add(e);
			geography.move(e, mls);
		}
		
		// When a Repast edge remove event occurs, remove the edge geometry from the 
		// geography and the context.  This should also occur automatically when agents
		// are removed from a context or network.
		else if (evt.getType() == ProjectionEvent.EDGE_REMOVED){
			RepastEdge e = (RepastEdge)evt.getSubject();
			
			geography.move(e, null);
			context.remove(e);			
		}
	}
}