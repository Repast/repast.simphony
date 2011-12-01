package repast.simphony.jung.statistics;

import repast.simphony.space.graph.RepastEdge;
import cern.colt.list.DoubleArrayList;
import edu.uci.ics.jung.graph.Graph;

/**
 * Adaptation of DegreeDistributions class from JUNG 1.7.6 release since
 * this functionality is not available in the alpha 2 release
 * 
 * @author Eric Tatara
 *
 */
public class RepastJungDegreeDistributions {

	public static DoubleArrayList getDegreeValues(Graph<Object,RepastEdge<Object>> graph) {
		DoubleArrayList degreeValues = new DoubleArrayList();

		for (Object vertex : graph.getVertices())
			degreeValues.add(graph.degree(vertex));
		
		return degreeValues;
	}

	public static DoubleArrayList getOutdegreeValues(Graph<Object,RepastEdge<Object>> graph) {
		DoubleArrayList outDegreeValues = new DoubleArrayList();
		
		for (Object vertex : graph.getVertices())
			outDegreeValues.add(graph.outDegree(vertex));

		return outDegreeValues;
	}

	public static DoubleArrayList getIndegreeValues(Graph<Object,RepastEdge<Object>> graph) {
		DoubleArrayList inDegreeValues  = new DoubleArrayList();

		for (Object vertex : graph.getVertices())
			inDegreeValues.add(graph.inDegree(vertex));

		return inDegreeValues;
	}

//	public static Histogram getOutdegreeHistogram(Set vertices, double min, double max, int numBins) {
//		Histogram histogram = new Histogram(min,max,numBins);
//
//		for (Iterator i = vertices.iterator(); i.hasNext(); ) {
//			Vertex currentVertex = (Vertex) i.next();
//			int currentOutdegree = currentVertex.outDegree();
//			histogram.fill(currentOutdegree);
//		}
//		return histogram;
//	}
//	
//	public static Histogram getIndegreeHistogram(Set vertices, double min, double max, int numBins) {
//		Histogram histogram = new Histogram(min,max,numBins);
//
//		for (Iterator i = vertices.iterator(); i.hasNext(); ) {
//			Vertex currentVertex = (Vertex) i.next();
//			int currentIndegree = currentVertex.inDegree();
//			histogram.fill(currentIndegree);
//		}
//		return histogram;
//	}
//
//	
//	public static void saveDistribution(Histogram histogram, String file) {
//
//		try {
//			BufferedWriter degreeWriter = new BufferedWriter(new FileWriter(file));
//			for (int i = 0; i < histogram.size(); i++) {
//				int currentDegree = (int) (i + histogram.getMinimum());
//				degreeWriter.write(currentDegree + " " + histogram.yValueAt(i) + "\n");
//			}
//			degreeWriter.close();
//
//		} catch (Exception e) {
//			throw new FatalException("Error saving binned data to " + file,e);
//		}
//	}
}