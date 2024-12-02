package hw6;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;




/**
 * An implementation of Graph ADT using incidence lists
 * for sparse graphs where most nodes aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {

  ArrayList<VertexNode<V>> labelVert;
  ArrayList<EdgeNode<E>> labelEdge;
  int numberOfVertices;
  int numberOfEdges;

  HashSet<EdgeNode<E>> edges;
  HashMap<VertexNode<V>, ArrayList<EdgeNode<E>>> incidenceList;
  HashMap<VertexNode<V>, ArrayList<EdgeNode<E>>> vertexIncoming;
  HashMap<VertexNode<V>, ArrayList<EdgeNode<E>>> vertexOutgoing;
  Graph<V, E> trueValue = this;

  /**
   * This is the default constructor of Sparse Graph.
   */
  public SparseGraph() {
    //Initializes the lists
    labelVert = new ArrayList<>();
    labelEdge = new ArrayList<>();

    //Initializes the maps
    vertexOutgoing = new HashMap<>();
    incidenceList = new HashMap<>();
    vertexIncoming = new HashMap<>();
    edges = new HashSet<>();
    numberOfVertices = 0;
    numberOfEdges = 0;
  }

  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      if (gv.owner != this) {
        throw new PositionException();
      }
      return gv;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      if (ge.owner != this) {
        throw new PositionException();
      }
      return ge;
    } catch (NullPointerException | ClassCastException ex) {
      throw new PositionException();
    }
  }



  @Override
  public Vertex<V> insert(V v) throws InsertionException {
    //Checks for null case
    if (v == null) {
      throw new InsertionException();
    }

    //Checks to make sure that we don't already contain the list
    if (containsVertexData(incidenceList, v)) {
      throw new InsertionException();
    }
    VertexNode<V> tempNode = new VertexNode<>(v);

    //Adds to the list
    ArrayList<EdgeNode<E>> newList = new ArrayList<>();
    incidenceList.put(tempNode, newList);
    ArrayList<EdgeNode<E>> newList2 = new ArrayList<>();
    vertexOutgoing.put(tempNode, newList2);
    ArrayList<EdgeNode<E>> newList3 = new ArrayList<>();
    vertexIncoming.put(tempNode, newList3);
    numberOfVertices++;
    return tempNode;
  }

  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
      throws PositionException, InsertionException {

    //Checks if the positions are valid
    if (insertPositionError(convert(from), convert(to))) {
      throw new PositionException();
    }

    //Self Loop Exception
    if (from == to) {
      throw new InsertionException();
    }

    //If the vertex is in the list, then throw an error
    if (containsEdgeData(edges, from, to)) {
      throw new InsertionException();
    }

    EdgeNode<E> temp = new EdgeNode<E>(convert(from), convert(to), e);
    incidenceList.get(convert(from)).add(temp);
    incidenceList.get(convert(to)).add(temp);

    addIncomingOutgoing(convert(from), convert(to), temp);
    edges.add(temp);
    numberOfEdges++;

    return temp;
  }

  /**
   *Checks if the value has already been inserted.
   * @param mySet the set that we are checking
   * @param from where the edge is from
   * @param to where the edge is
   *           pre: to != from and positions are valid
   * @return true if it does contain the edge
   */
  private boolean containsEdgeData(HashSet<EdgeNode<E>> mySet, Vertex<V> from, Vertex<V> to) {
    //Iterates through all the keys
    for (EdgeNode<E> tempEdge : mySet) {
      //If it finds the data, then it returns true
      if (tempEdge.from == from && tempEdge.to == to) {
        return true;
      }
    }
    //Returns false otherwise
    return false;
  }

  /**
   *Checks if the value has already been inserted.
   * @param myMap the map that we are checking
   * @param v the data we are comparing to
   *          pre: v != null
   * @return true if it does contain the data
   */
  private boolean containsVertexData(HashMap<VertexNode<V>, ArrayList<EdgeNode<E>>> myMap, V v) {
    //Iterates through all the keys
    for (VertexNode<V> tempNode : myMap.keySet()) {
      //If it finds the data, then it returns true
      if (tempNode.data.equals(v)) {
        return true;
      }
    }
    //Returns false otherwise
    return false;
  }

  /**
   * This is checks is there is a position error.
   * @param from the first vertex
   * @param to the second vertex
   * @return True if there is an error and false if not
   */
  private boolean insertPositionError(VertexNode<V> from, VertexNode<V> to) {
    // If the keys are null
    if (from == null || to == null) {
      return true;
    }

    //Checks to make sure that we don't already contain the list
    if (!(incidenceList.containsKey(from))) {
      return true;
    }

    //Checks to make sure that we don't already contain the list
    if (!incidenceList.containsKey(to)) {
      return true;
    }

    return false;
  }

  /**
   * Adds the incoming and outgoing edge.
   * @param from where the edge is from
   * @param to where the edge is going towards
   * @param myEdge the edge added
   */
  private void addIncomingOutgoing(VertexNode<V> from, VertexNode<V> to, EdgeNode<E> myEdge) {
    // Adds the new outgoing
    ArrayList<EdgeNode<E>> tempOutgoingList = vertexOutgoing.get(from);
    tempOutgoingList.add(myEdge);

    // Adds the new incoming
    ArrayList<EdgeNode<E>> tempIncomingList = vertexIncoming.get(to);
    tempIncomingList.add(myEdge);
  }

  @Override
  public V remove(Vertex<V> v) throws PositionException, RemovalException {
    //Null case
    if (v == null) {
      throw new PositionException();
    }

    VertexNode<V> newNode = convert(v);

    //Makes sure that it exists within the graph
    if (!incidenceList.containsKey(newNode)) {
      throw new PositionException();
    }

    ArrayList<EdgeNode<E>> tempList = incidenceList.get(newNode);

    //If this still has edges
    if (!tempList.isEmpty()) {
      throw new RemovalException();
    }

    //Saves the data
    V data = v.get();

    //Removes from the list
    incidenceList.remove(newNode);
    numberOfVertices--;

    return data;
  }

  @Override
  public E remove(Edge<E> e) throws PositionException {
    //Null case
    if (e == null) {
      throw new PositionException();
    }

    EdgeNode<E> tempE = convert(e);
    VertexNode<V> dataFrom = tempE.from;

    ArrayList<EdgeNode<E>> tempList = incidenceList.get(dataFrom);

    //Isn't in the list error
    if (!(tempList.contains(tempE))) {
      throw new PositionException();
    }

    //Removes it from all the lists
    tempList.remove(tempE);

    //This point both of these should contain tempE
    vertexOutgoing.get(dataFrom).remove(tempE);
    vertexIncoming.get(tempE.to).remove(tempE);
    edges.remove(tempE);
    numberOfEdges--;

    // Returns the value
    return e.get();
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    return new VertexIteratorClass();
  }

  @Override
  public Iterable<Edge<E>> edges() {
    return new EdgeIteratorClass();
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    //If the vertex is null, then throw an error
    if (v == null) {
      throw new PositionException();
    }

    //Makes the converted temp node
    VertexNode<V> temp = convert(v);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.containsKey(temp)) {
      throw new PositionException();
    }

    ArrayList<EdgeNode<E>> tempList = vertexOutgoing.get(convert(v));
    return new OutgoingIteratorClass(tempList);
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    //If the vertex is null, then throw an error
    if (v == null) {
      throw new PositionException();
    }

    //Makes the converted temp node
    VertexNode<V> temp = convert(v);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.containsKey(temp)) {
      throw new PositionException();
    }

    ArrayList<EdgeNode<E>> tempList = vertexIncoming.get(convert(v));
    return new IncomingIteratorClass(tempList);
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    //If the vertex is null, then throw an error
    if (e == null) {
      throw new PositionException();
    }

    EdgeNode<E> temp = convert(e);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.get(temp.from).contains(temp)) {
      throw new PositionException();
    }

    return temp.from;
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    //If the vertex is null, then throw an error
    if (e == null) {
      throw new PositionException();
    }

    EdgeNode<E> temp = convert(e);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.get(temp.from).contains(temp)) {
      throw new PositionException();
    }

    return temp.to;
  }

  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    //If the vertex is null, then throw an error
    if (v == null) {
      throw new PositionException();
    }

    //Makes the converted temp node
    VertexNode<V> temp = convert(v);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.containsKey(temp)) {
      throw new PositionException();
    }

    //Adds the label
    temp.label = l;

    labelVert.add(temp);
  }

  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    //If the vertex is null, then throw an error
    if (e == null) {
      throw new PositionException();
    }

    EdgeNode<E> temp = convert(e);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.get(temp.from).contains(temp)) {
      throw new PositionException();
    }

    //Add the label
    temp.label = l;

    labelEdge.add(temp);
  }

  @Override
  public Object label(Vertex<V> v) throws PositionException {
    //If the vertex is null, then throw an error
    if (v == null) {
      throw new PositionException();
    }
    VertexNode<V> temp = convert(v);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.containsKey(temp)) {
      throw new PositionException();
    }

    return temp.label;
  }

  @Override
  public Object label(Edge<E> e) throws PositionException {
    //If the vertex is null, then throw an error
    if (e == null) {
      throw new PositionException();
    }

    EdgeNode<E> temp = convert(e);

    //If the vertex is not in the list, then throw an error
    if (!incidenceList.get(temp.from).contains(temp)) {
      throw new PositionException();
    }

    return temp.label;
  }

  @Override
  public void clearLabels() {
    //Removes the vertexes labels
    for (VertexNode<V> vertex: labelVert) {
      vertex.label = null;
    }

    //Removes the edges labels
    for (EdgeNode<E> edge: labelEdge) {
      edge.label = null;
    }

    //Clears the labels containers
    labelVert.clear();
    labelEdge.clear();
  }

  @Override
  public String toString() {
    GraphPrinter<V, E> gp = new GraphPrinter<>(this);
    return gp.toString();
  }

  // Class for a vertex of type V
  private final class VertexNode<V> implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    Object label;

    VertexNode(V v) {
      this.data = v;
      this.label = null;
      this.owner = (Graph<V, E>) trueValue;
    }

    @Override
    public V get() {
      return this.data;
    }

  }

  //Class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;
    VertexNode<V> to;
    Object label;

    // Constructor for a new edge
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
      this.owner = (Graph<V, E>) trueValue;
    }

    @Override
    public E get() {
      return this.data;
    }
  }

  // Iterative vertex
  private class VertexIteratorClass implements Iterable<Vertex<V>> {

    @Override
    public Iterator<Vertex<V>> iterator() {
      return new VertexIterator();
    }

    private class VertexIterator implements Iterator<Vertex<V>> {
      int count;
      List<VertexNode<V>> vertices;
      int sizeOfVertex;

      VertexIterator() {
        count = 0;
        sizeOfVertex = numberOfVertices;
        vertices = new ArrayList<>(incidenceList.keySet());
      }

      @Override
      public boolean hasNext() {
        //Makes sure that the count is lower
        return count < sizeOfVertex;
      }

      @Override
      public Vertex<V> next() throws NoSuchElementException {
        //Throws error if there is too many
        if (!hasNext()) {
          throw new NoSuchElementException();
        }

        //Returns the next count
        return vertices.get(count++);
      }
    }
  }

  // Iterative edge
  private class EdgeIteratorClass implements Iterable<Edge<E>> {

    @Override
    public Iterator<Edge<E>> iterator() {
      return new EdgeIterator();
    }

    private class EdgeIterator implements Iterator<Edge<E>> {
      int count;
      List<Edge<E>> myEdges;
      int sizeOfEdge;

      EdgeIterator() {
        count = 0;
        sizeOfEdge = numberOfEdges;
        myEdges = new ArrayList<>(edges);
      }

      @Override
      public boolean hasNext() {
        //Makes sure that the count is lower
        return count < sizeOfEdge;
      }

      @Override
      public Edge<E> next() throws NoSuchElementException {
        //Throws error if there is too many
        if (!hasNext()) {
          throw new NoSuchElementException();
        }

        //Returns the next count
        return myEdges.get(count++);
      }
    }
  }

  // Iterative outgoing
  private class OutgoingIteratorClass implements Iterable<Edge<E>> {
    ArrayList<EdgeNode<E>> outgoingEdges;
    int numOutgoing;

    OutgoingIteratorClass(ArrayList<EdgeNode<E>> theEdges) {
      outgoingEdges = new ArrayList<>(theEdges);
      numOutgoing = outgoingEdges.size();
    }

    @Override
    public Iterator<Edge<E>> iterator() {
      return new OutgoingIterator();
    }

    private class OutgoingIterator implements Iterator<Edge<E>> {
      int count;

      OutgoingIterator() {
        count = 0;
      }

      @Override
      public boolean hasNext() {
        //Makes sure that the count is lower
        return count < numOutgoing;
      }

      @Override
      public Edge<E> next() throws NoSuchElementException {
        //Throws error if there is too many
        if (!hasNext()) {
          throw new NoSuchElementException();
        }

        //Returns the next count
        return outgoingEdges.get(count++);
      }
    }
  }

  // Iterative incoming
  private class IncomingIteratorClass implements Iterable<Edge<E>> {
    ArrayList<EdgeNode<E>> incomingEdges;
    int numIncoming;

    IncomingIteratorClass(ArrayList<EdgeNode<E>> theEdges) {
      incomingEdges = new ArrayList<>(theEdges);
      numIncoming = incomingEdges.size();
    }

    @Override
    public Iterator<Edge<E>> iterator() {
      return new IncomingIterator();
    }

    private class IncomingIterator implements Iterator<Edge<E>> {
      int count;

      IncomingIterator() {
        count = 0;
      }

      @Override
      public boolean hasNext() {
        //Makes sure that the count is lower
        return count < numIncoming;
      }

      @Override
      public Edge<E> next() throws NoSuchElementException {
        //Throws error if there is too many
        if (!hasNext()) {
          throw new NoSuchElementException();
        }

        //Returns the next count
        return incomingEdges.get(count++);
      }
    }
  }
}
