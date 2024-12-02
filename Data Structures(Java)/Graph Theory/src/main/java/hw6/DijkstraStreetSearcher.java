package hw6;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


public class DijkstraStreetSearcher extends StreetSearcher {
  PriorityQueue<VertexSearchNode> vertexQueue;
  Map<Vertex<String>, VertexSearchNode> convertNodes;

  /**
   * Creates a StreetSearcher object.
   *
   * @param graph an implementation of Graph ADT.
   */
  public DijkstraStreetSearcher(Graph<String, String> graph) {
    super(graph);
  }

  @Override
  public void findShortestPath(String startName, String endName) {
    //Makes sure that the endpoints are valid
    if (invalidEndpointError(startName, endName)) {
      List<Edge<String>> path;
      double totalDist;
      vertexQueue = new PriorityQueue<>(Comparator.comparing(VertexSearchNode::getDistance));
      convertNodes = new HashMap<>();

      //The start and end are the same error
      if (startName.equals(endName)) {
        path = null;
        totalDist = 0.0;
      } else {
        Vertex<String> start = vertices.get(startName);
        Vertex<String> end = vertices.get(endName);

        // These method calls will create and print the path for you
        path = findShortestPathNoError(start, end);
        totalDist = convertNodes.get(end).getDistance();
      }

      if (VERBOSE) {
        printPath(path, totalDist);
      }
    }
  }

  /**
   * Runs find the shortest path assuming that there is no errors.
   * @param start The starting node
   * @param end The ending node
   * @return The path that it takes
   * */
  private List<Edge<String>> findShortestPathNoError(Vertex<String> start, Vertex<String> end) {
    //Establishes the priority queues.
    setDefault(start);
    //Runs through a loop of everything
    loopThroughAllNodes();

    return getPath(end, start);
  }

  /**
   * Checks to make sure that the endpoints are valid.
   * @param startName The starting node's name
   * @param endName The ending node's name
   * @return True if there is no error and false otherwise
   * */
  private boolean invalidEndpointError(String startName, String endName) {
    boolean isWorking = true;
    try { // Checks to see if they are valid endpoints
      checkValidEndpoint(startName);
      checkValidEndpoint(endName);
    } catch (IllegalArgumentException e) { // Prints out the message
      System.out.println(e.getMessage());
      isWorking = false;
    }
    return isWorking;
  }

  private void loopThroughAllNodes() {
    while (!vertexQueue.isEmpty()) {
      VertexSearchNode u = vertexQueue.poll();


      for (Edge<String> neighborEdge: graph.outgoing(vertices.get(u.get()))) {
        readjustDistance(u, neighborEdge);
      }
    }
  }

  /**
   * See if the distance is less and update if it is.
   * @param u The current node
   * @param neighborEdge The current edge that we are looking at
   * */
  private void readjustDistance(VertexSearchNode u, Edge<String> neighborEdge) {
    //Gets values
    Vertex<String> neighbor = graph.to(neighborEdge);
    VertexSearchNode neighborNode = convertNodes.get(neighbor);
    double alt = u.getDistance() + (double) graph.label(neighborEdge);
    if (alt < neighborNode.getDistance()) {
      //Set new values
      neighborNode.setDistance(alt);
      neighborNode.setPrevious(u);

      //Resets the priority
      vertexQueue.remove(neighborNode);
      vertexQueue.add(neighborNode);
      graph.label(neighbor, neighborEdge);
    }
  }

  /**
   * This sets all but the start node to default.
   * @param start The starting node
   * */
  private void setDefault(Vertex<String> start) {
    //Add start to the first in the queue (doesn't matter as it compares through distance)
    VertexSearchNode convertedStart = new VertexSearchNode(start.get(), 0, null);
    convertNodes.put(start, convertedStart);
    vertexQueue.add(convertedStart);

    double tempDistance;
    for (Vertex<String> vertex : graph.vertices()) {
      //Only runs this is it isn't start
      if (vertex != start) {
        tempDistance = Double.POSITIVE_INFINITY;
        VertexSearchNode node = new VertexSearchNode(vertex.get(), tempDistance, null);
        convertNodes.put(vertex, node);
        vertexQueue.add(node);
      }
    }
  }

  // Class for a vertex of type V
  private static final class VertexSearchNode implements Vertex<String>, Comparable<VertexSearchNode> {
    private double distance;
    private Vertex<String> previous;
    private String data;

    VertexSearchNode(String da, double d, Vertex<String> p) {
      data = da;
      distance = d;
      previous = p;
    }

    @Override
    public int compareTo(VertexSearchNode other) {
      return Double.compare(this.distance, other.distance);
    }

    public void setDistance(double d) {
      distance = d;
    }

    public void setPrevious(Vertex<String> p) {
      previous = p;
    }

    public double getDistance() {
      return distance;
    }

    public Vertex<String> getPrevious() {
      return previous;
    }

    @Override
    public String get() {
      return data;
    }

  }


}
