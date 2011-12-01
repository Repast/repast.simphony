package repast.simphony.context.space.graph;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.BidiMap;

import repast.simphony.space.graph.Network;
import edu.uci.ics.jung.algorithms.util.Indexer;

/**
 * Generator for n x n lattice where each vertex is
 * incident with its four neighbors except perhaps
 * for the edge vertices depending on whether the
 * lattice is toroidal.
 * <p/>
 * Based on code from the Jung project.
 *
 * @author Nick Collier
 * @author Jung Project
 */
public class Lattice2DGenerator<T> extends AbstractGenerator<T> {

  private boolean isToroidal;
  private int latticeSize;

  /**
   * Creates a Lattice2DGenerator.
   *
   * @param isToroidal whether lattice wraps or not.
   */
  public Lattice2DGenerator(boolean isToroidal) {
    this.isToroidal = isToroidal;
  }

  /**
   * Given an existing network, add edges to create a
   * 2D lattice. The lattice dimension n is the square root
   * of the number of nodes in the specified network. The resulting
   * lattice will be nxn.
   *
   * @param network the network to rewire
   * @return the created network
   */
  public Network<T> createNetwork(Network<T> network) {
    latticeSize = (int) Math.floor(Math.sqrt(network.size()));
    Set<T> set = new HashSet<T>();
    for (T node : network.getNodes()) {
      set.add(node);
    }

    int currentLatticeRow = 0, currentLatticeColumn = 0;
    int upIndex = 0, downIndex = 0, leftIndex = 0, rightIndex = 0;

    BidiMap<T, Integer> map = Indexer.create(set);

    int numNodes = network.size();
    boolean isDirected = network.isDirected();

    for (int i = 0; i < numNodes; i++) {
      currentLatticeRow = i / latticeSize;
      currentLatticeColumn = i % latticeSize;

      upIndex = upIndex(currentLatticeRow, currentLatticeColumn);
      leftIndex = leftIndex(currentLatticeRow, currentLatticeColumn);
      downIndex = downIndex(currentLatticeRow, currentLatticeColumn);
      rightIndex = rightIndex(currentLatticeRow, currentLatticeColumn);

      //Add short range connections
      if (currentLatticeRow != 0 || (currentLatticeRow == 0 && isToroidal)) {
        T source = map.getKey(i);
        T target = map.getKey(upIndex);
        if (isDirected)
          network.addEdge(source, target);
        else if (!network.isAdjacent(source, target))
          network.addEdge(source, target);
      }

      if (currentLatticeColumn != 0 || (currentLatticeColumn == 0 && isToroidal)) {
        T source = map.getKey(i);
        T target = map.getKey(leftIndex);
        if (isDirected)
          network.addEdge(source, target);
        else if (!network.isAdjacent(source, target))
          network.addEdge(source, target);
      }

      if (currentLatticeRow != latticeSize - 1 || (currentLatticeRow == latticeSize - 1 && isToroidal)) {
        T source = map.getKey(i);
        T target = map.getKey(downIndex);
        if (isDirected)
          network.addEdge(source, target);
        else if (!network.isAdjacent(source, target))
          network.addEdge(source, target);
      }

      if (currentLatticeColumn != latticeSize - 1 ||
              (currentLatticeColumn == latticeSize - 1 && isToroidal)) {
        T source = map.getKey(i);
        T target = map.getKey(rightIndex);
        if (isDirected)
          network.addEdge(source, target);
        else if (!network.isAdjacent(source, target))
          network.addEdge(source, target);
      }
    }

    return network;
  }

  protected int upIndex(int currentLatticeRow, int currentLatticeColumn) {
    if (currentLatticeRow == 0) {
      return latticeSize * (latticeSize - 1) + currentLatticeColumn;
    } else {
      return (currentLatticeRow - 1) * latticeSize
              + currentLatticeColumn;
    }
  }

  protected int downIndex(int currentLatticeRow, int currentLatticeColumn) {
    if (currentLatticeRow == latticeSize - 1) {
      return currentLatticeColumn;
    } else {
      return (currentLatticeRow + 1) * latticeSize
              + currentLatticeColumn;
    }
  }

  protected int leftIndex(int currentLatticeRow, int currentLatticeColumn) {
    if (currentLatticeColumn == 0) {
      return currentLatticeRow * latticeSize + latticeSize - 1;
    } else {
      return currentLatticeRow * latticeSize + currentLatticeColumn - 1;
    }
  }

  protected int rightIndex(int currentLatticeRow, int currentLatticeColumn) {
    if (currentLatticeColumn == latticeSize - 1) {
      return currentLatticeRow * latticeSize;
    } else {
      return currentLatticeRow * latticeSize + currentLatticeColumn + 1;
    }
  }
}
