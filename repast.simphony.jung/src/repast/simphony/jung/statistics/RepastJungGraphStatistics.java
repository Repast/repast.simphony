package repast.simphony.jung.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.Graph;

/**
 * 
 * 
 * @author Eric Tatara
 *
 */
public class RepastJungGraphStatistics  {

	public static Map<Object,Double> averageDistances(Graph graph){
		return averageDistances(graph, new UnweightedShortestPath(graph));
	}

	public static Map<Object,Double> averageDistances(Graph graph, Distance<Object> d){
		Map<Object,Double> avg_dist = new HashMap<Object,Double>();

		for(Object outer : graph.getVertices()){
			double avgPathLength = 0;

			for(Object inner : graph.getVertices()){
				if (outer != inner){ 
					Number dist = d.getDistance(outer,inner);
					if (dist == null){
						avgPathLength = Double.POSITIVE_INFINITY;
						break;
					}
					avgPathLength += dist.doubleValue();
				}
			}
			avgPathLength /= (graph.getVertices().size() - 1);
			avg_dist.put(outer, new Double(avgPathLength));
		}

		return avg_dist;
	}

	public static Map<Object,Double> clusteringCoefficients(Graph graph){
		Map<Object,Double> coefficients = new HashMap<Object,Double>();

		for(Object vertex : graph.getVertices()){

			int numNeighbors = graph.getNeighborCount(vertex);

			if (numNeighbors == 0)
				coefficients.put(vertex, new Double(0));

			else if (numNeighbors == 1)
				coefficients.put(vertex, new Double(1));

			else{
//				ArrayList neighbors = (ArrayList)graph.getNeighbors(vertex);
				ArrayList neighbors = new ArrayList();
				
				for (Object o : graph.getNeighbors(vertex))
					neighbors.add(o);

				double edge_count = 0;

				for (int i = 0; i < neighbors.size(); i++){
					Object vertex1 = neighbors.get(i);

					for (int j = i+1; j < neighbors.size(); j++ )	{
						Object vertex2 = neighbors.get(j);

						edge_count +=  graph.isNeighbor(vertex1, vertex2) ? 1 : 0;
					}
				}

				double possible_edges = (numNeighbors * (numNeighbors - 1))/2.0;

				coefficients.put(vertex, new Double(edge_count / possible_edges));
			}
		}
		return coefficients;
	}
}