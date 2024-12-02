package hw6;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public abstract class GraphTest {

  protected Graph<String, String> graph;

  @BeforeEach
  public void setupGraph() {
    this.graph = createGraph();
  }

  protected abstract Graph<String, String> createGraph();

  @Test
  @DisplayName("insert(v) returns a vertex with given data")
  public void canGetVertexAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  @DisplayName("insert(U, V, e) returns an edge with given data")
  public void canGetEdgeAfterInsert() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
  }

  @Test
  @DisplayName("inserting a vertex data that already exist throws exception")
  public void InsertVertexSameElementInsertionException() {
    Vertex<String> v1 = graph.insert("v1");
    try {
      Vertex<String> v2 = graph.insert("v1");
      fail ("Exception not thrown");
    }catch (InsertionException e) {
      return;
    }
  }

  @Test
  @DisplayName("inserting a vertex data = null throws exception")
  public void InsertVertexNullElementInsertionException() {
    Vertex<String> v1 = graph.insert("v1");
    try {
      Vertex<String> v2 = graph.insert(null);
      fail ("Exception not thrown");
    }catch (InsertionException e) {
      return;
    }
  }

  @Test
  @DisplayName("insert(V, null, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenSecondVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(v, null, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert(null, V, e) throws exception")
  public void insertEdgeThrowsPositionExceptionWhenFirstVertexIsNull() {
    try {
      Vertex<String> v = graph.insert("v");
      graph.insert(null, v, "e");
      fail("The expected exception was not thrown");
    } catch (PositionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert edge throws exception when it's the same edge")
  public void insertEdgeInsertionExceptionRepeatedEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    graph.insert(v1, v2, "e");
    try {
      graph.insert(v1, v2, "e");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("insert edge throws exception when it's forms a self loop")
  public void insertEdgeInsertionExceptionSelfLoop() {
    Vertex<String> v1 = graph.insert("v1");
    try {
      graph.insert(v1, v1, "e");
      fail("The expected exception was not thrown");
    } catch (InsertionException ex) {
      return;
    }
  }

  @Test
  @DisplayName("remove vertex works normally")
  public void removeVertex() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    assertEquals("v1", graph.remove(v1));
    assertEquals("v2", graph.remove(v2));
    assertEquals("v3", graph.remove(v3));
    assertEquals("v4", graph.remove(v4));
  }

  @Test
  @DisplayName("remove vertex when vertex is null")
  public void removeVertexPositionExceptionNull() {
    Vertex<String> v4 = null;

    try {
      graph.remove(v4);
      fail("Exception not thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("remove vertex when vertex is already removed")
  public void removeVertexPositionAlreadyRemoved() {
    Vertex<String> v4 = graph.insert("v1");
    graph.remove(v4);

    try {
      graph.remove(v4);
      fail("Exception not thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("remove vertex when it has outgoing throws error")
  public void removeRemovalExceptionHasOutgoing() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");

    try {
      graph.remove(v1);
      fail("Exception not thrown");
    } catch (RemovalException e) {
      return;
    }
  }

  @Test
  @DisplayName("remove vertex when it has Incoming throws error")
  public void removeRemovalExceptionHasIncoming() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");

    try {
      graph.remove(v2);
      fail("Exception not thrown");
    } catch (RemovalException e) {
      return;
    }
  }

  @Test
  @DisplayName("remove edge")
  public void removeEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals("v1-v2", graph.remove(e1));
  }

  @Test
  @DisplayName("remove edge when it's null throws error")
  public void removeEdgePositionExceptionWhenNull() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = null;
    try {
      graph.remove(e1);
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("remove edge when it's repeatedly removed throws error")
  public void removeEdgePositionExceptionWhenRepeated() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e1);
    try {
      graph.remove(e1);
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("Complex Case of Inserts and Removes (Insert a Remove Node and then remove)")
  public void insertAndRemovedNode() {
    Vertex<String> v1 = graph.insert("v1");
    graph.remove(v1);
    Vertex<String> v2 = graph.insert("v1");
    graph.remove(v2);
    Vertex<String> v3 = graph.insert("v1");
    graph.remove(v3);
    Vertex<String> v4 = graph.insert("v1");
    graph.remove(v4);

    try {
      graph.remove(v1);
      fail("Exception not thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("Complex Case of Inserts and Removes (Insert a Remove Edge and then remove)")
  public void insertARemovedEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e1);
    Edge<String> e2 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e2);
    Edge<String> e3 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e3);

    try {
      graph.remove(e3);
      fail("Exception not thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("iterate Over Vertices Without Remove")
  public void iterateOverVerticesWithoutRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    int count = 0;
    for (Vertex<String> ele: graph.vertices()){
      count++;
    }
    assertEquals(4, count);
  }

  @Test
  @DisplayName("iterate Over Vertices With Remove")
  public void iterateOverVerticesWithRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    graph.remove(v2);

    int count = 0;
    for (Vertex<String> ele: graph.vertices()){
      count++;
    }
    assertEquals(3, count);
  }

  @Test
  @DisplayName("iterate Over Vertices In Empty Set")
  public void iterateOverVerticesEmptySet() {
    int count = 0;
    for (Vertex<String> ele: graph.vertices()){
      count++;
    }
    assertEquals(0, count);
  }

  @Test
  @DisplayName("iterate Over Vertices With try to modify while running")
  public void iterateOverVerticesModifyWhileRunning() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");
    int count = 0;
    for (Vertex<String> ele : graph.vertices()) {
      if (ele == v1){
        graph.remove(v4);
        graph.remove(v2);
        graph.remove(v1);
      }
      count++;
    }
    assertEquals(4, count);
  }

  @Test
  @DisplayName("iterate Over Edges Without Remove")
  public void iterateOverEdgesWithoutRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v2, v3, "v2-v3");
    Edge<String> e3 = graph.insert(v3, v4, "v3-v4");
    Edge<String> e4 = graph.insert(v4, v1, "v4-v1");

    int count = 0;
    for (Edge<String> ele: graph.edges()){
      count++;
    }
    assertEquals(4, count);
  }

  @Test
  @DisplayName("iterate Over Edges With Remove")
  public void iterateOverEdgesWithRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v2, v3, "v2-v3");
    Edge<String> e3 = graph.insert(v3, v4, "v3-v4");
    Edge<String> e4 = graph.insert(v4, v1, "v4-v1");

    graph.remove(e2);

    int count = 0;
    for (Edge<String> ele: graph.edges()){
      count++;
    }
    assertEquals(3, count);
  }

  @Test
  @DisplayName("iterate Over Edges In Empty Set")
  public void iterateOverEdgesEmptySet() {
    int count = 0;
    for (Vertex<String> ele: graph.vertices()){
      count++;
    }
    assertEquals(0, count);
  }

  @Test
  @DisplayName("iterate Over Edges  With try to modify while running")
  public void iterateOverEdgesModifyWhileRunning() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v2, v3, "v2-v3");
    Edge<String> e3 = graph.insert(v3, v4, "v3-v4");
    Edge<String> e4 = graph.insert(v4, v1, "v4-v1");

    int count = 0;
    for (Edge<String> ele: graph.edges()){
      count++;
      if (ele == e1){
        graph.remove(e3);
        graph.remove(e2);
        graph.remove(e1);
        graph.remove(e4);
      }
    }
    assertEquals(4, count);
  }

  @Test
  @DisplayName("iterate Outgoing Without Remove")
  public void iterateOutgoingWithoutRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v1, v3, "v1-v3");
    Edge<String> e3 = graph.insert(v1, v4, "v1-v4");

    int count = 0;
    for (Edge<String> ele: graph.outgoing(v1)){
      count ++;
    }
    assertEquals(3, count);
  }

  @Test
  @DisplayName("iterate Outgoing With Remove Edge")
  public void iterateOutgoingWithRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v1, v3, "v1-v3");
    Edge<String> e3 = graph.insert(v1, v4, "v1-v4");

    graph.remove(e2);

    int count = 0;
    for (Edge<String> ele: graph.outgoing(v1)){
      count ++;
    }
    assertEquals(2, count);
  }

  @Test
  @DisplayName("iterate Outgoing With null node")
  public void iterateOutgoingWithNull() {
    int count = 0;
    try {
      for (Edge<String> ele : graph.outgoing(null)) {
        count++;
      }
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("iterate Outgoing With removed node")
  public void iterateOutgoingWithRemovedNode() {
    Vertex<String> v1 = graph.insert("v1");
    graph.remove(v1);
    int count = 0;
    try {
      for (Edge<String> ele : graph.outgoing(v1)) {
        count++;
      }
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("iterate Over Outgoing With try to modify while running")
  public void iterateOutgoingModifyWhileRunning() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v1, v3, "v1-v3");
    Edge<String> e3 = graph.insert(v1, v4, "v1-v4");

    int count = 0;
    for (Edge<String> ele: graph.outgoing(v1)){
      if (ele == e1){
        graph.remove(e3);
        graph.remove(e2);
        graph.remove(e1);
      }
      count++;
    }
    assertEquals(3, count);
  }

  @Test
  @DisplayName("iterate Incoming Without Remove")
  public void iterateIncomingWithoutRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v2, v1, "v2-v1");
    Edge<String> e2 = graph.insert(v3, v1, "v3-v1");
    Edge<String> e3 = graph.insert(v4, v1, "v4-v1");

    int count = 0;
    for (Edge<String> ele: graph.incoming(v1)){
      count ++;
    }
    assertEquals(3, count);
  }

  @Test
  @DisplayName("iterate Incoming With Remove Edge")
  public void iterateIncomingWithRemove() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v2, v1, "v2-v1");
    Edge<String> e2 = graph.insert(v3, v1, "v3-v1");
    Edge<String> e3 = graph.insert(v4, v1, "v4-v1");

    graph.remove(e2);

    int count = 0;
    for (Edge<String> ele: graph.incoming(v1)){
      count ++;
    }
    assertEquals(2, count);
  }

  @Test
  @DisplayName("iterate Incoming With null node")
  public void iterateIncomingWithNull() {
    int count = 0;
    try {
      for (Edge<String> ele : graph.incoming(null)) {
        count++;
      }
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("iterate Incoming With removed node")
  public void iterateIncomingWithRemovedNode() {
    Vertex<String> v1 = graph.insert("v1");
    graph.remove(v1);
    int count = 0;
    try {
      for (Edge<String> ele : graph.incoming(v1)) {
        count++;
      }
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("iterate Over Incoming With try to modify while running")
  public void iterateIncomingModifyWhileRunning() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Vertex<String> v4 = graph.insert("v4");

    Edge<String> e1 = graph.insert(v2, v1, "v2-v1");
    Edge<String> e2 = graph.insert(v3, v1, "v3-v1");
    Edge<String> e3 = graph.insert(v4, v1, "v4-v1");

    int count = 0;
    for (Edge<String> ele: graph.incoming(v1)){
      if (ele == e1){
        graph.remove(e3);
        graph.remove(e2);
        graph.remove(e1);
      }
      count++;
    }
    assertEquals(3, count);
  }

  @Test
  @DisplayName("from() Basic Case")
  public void fromBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(v1, graph.from(e1));
  }

  @Test
  @DisplayName("from() Edge Is Null")
  public void fromEdgeIsNull() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    try{
      graph.from(null);
      fail("Exception should have been thrown");
    } catch (PositionException e){
      return;
    }
  }

  @Test
  @DisplayName("from() Edge Is Removed")
  public void fromEdgeIsRemoved() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e1);
    try{
      graph.from(e1);
      fail("Exception should have been thrown");
    } catch (PositionException e){
      return;
    }
  }

  @Test
  @DisplayName("to() Basic Case")
  public void toBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(v2, graph.to(e1));
  }

  @Test
  @DisplayName("to() Edge Is Null")
  public void toEdgeIsNull() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    try{
      graph.to(null);
      fail("Exception should have been thrown");
    } catch (PositionException e){
      return;
    }
  }

  @Test
  @DisplayName("from() Edge Is Removed")
  public void toEdgeIsRemoved() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e1);
    try{
      graph.to(e1);
      fail("Exception should have been thrown");
    } catch (PositionException e){
      return;
    }
  }

  @Test
  @DisplayName("labeling Vertex Basic Case")
  public void labelingVertexBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    graph.label(v1, "This is labeled");
    Vertex<String> v2 = graph.insert("v2");
    graph.label(v2, "This is also labeled");
  }

  @Test
  @DisplayName("labeling Vertex Null Case")
  public void labelingVertexNullCase() {
    Vertex<String> v1 = null;
    try {
      graph.label(v1, "This is an error");
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("labeling Vertex Removed Node Case")
  public void labelingVertexRemovedNodeCase() {
    Vertex<String> v1 = graph.insert("v1");
    graph.remove(v1);
    try {
      graph.label(v1, "This is an error");
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("labeling Edge Basic Case")
  public void labelingEdgeBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, "This is labeled");
  }

  @Test
  @DisplayName("labeling Edge Null Case")
  public void labelingEdgeNullCase() {
    Edge<String> e1 = null;
    try {
      graph.label(e1, "This is an error");
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("labeling Vertex Removed Node Case")
  public void labelingEdgeRemovedNodeCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.remove(e1);
    try {
      graph.label(e1, "This is an error");
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("label Vertex Basic Case")
  public void labelVertexBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    graph.label(v1, "This is labeled");
    Vertex<String> v2 = graph.insert("v2");
    graph.label(v2, "This is also labeled");
    assertEquals(graph.label(v1), "This is labeled");
    assertEquals(graph.label(v2), "This is also labeled");
  }

  @Test
  @DisplayName("label Vertex No Label Case")
  public void labelVertexNoLabelCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    assertNull(graph.label(v1));
    assertNull(graph.label(v2));
  }

  @Test
  @DisplayName("label Vertex Null Case")
  public void labelVertexNullCase() {
    Vertex<String> v1 = null;
    try {
      graph.label(v1);
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("label Vertex Removed Node Case")
  public void labelVertexRemovedNodeCase() {
    Vertex<String> v1 = graph.insert("v1");
    graph.label(v1, "This is labeled");
    graph.remove(v1);
    try {
      graph.label(v1);
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("label Edge Basic Case")
  public void labelEdgeBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v2, v1, "v2-v1");
    assertEquals(graph.label(e1), null);
    assertEquals(graph.label(e2), null);
  }

  @Test
  @DisplayName("label Edge No Label Case")
  public void labelEdgeNoLabelCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertNull(graph.label(e1));
  }

  @Test
  @DisplayName("label Edge Null Case")
  public void labelEdgeNullCase() {
    Edge<String> e1 = null;
    try {
      graph.label(e1);
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("label Edge Removed Node Case")
  public void labelEdgeRemovedEdgeCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, "Now labeled");
    graph.remove(e1);
    try {
      graph.label(e1);
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("clear label Basic Case")
  public void clearLabelBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, "Now labeled");
    graph.label(v1, "Now labeled");
    graph.label(v2, "Now labeled");

    graph.clearLabels();

    assertNull(graph.label(v1));
    assertNull(graph.label(v2));
    assertNull(graph.label(e1));
  }

  @Test
  @DisplayName("clear label No Labels Case")
  public void clearLabelNoLabels() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");

    graph.clearLabels();

    assertNull(graph.label(v1));
    assertNull(graph.label(v2));
    assertNull(graph.label(e1));
  }

  @Test
  @DisplayName("clear label removed Node Case")
  public void clearLabelRemovedNodeCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, "Now labeled");
    graph.label(v1, "Now labeled");
    graph.label(v2, "Now labeled");
    graph.label(v3, "Now labeled");
    graph.remove(v3);

    graph.clearLabels();

    assertNull(graph.label(v1));
    assertNull(graph.label(v2));
    assertNull(graph.label(e1));

    try {
      graph.label(v3);
      fail("Exception should have been thrown");
    } catch (PositionException e) {
      return;
    }
  }

  @Test
  @DisplayName("testing toString()")
  public void toStringBasicCase() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Vertex<String> v3 = graph.insert("v3");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    graph.label(e1, "Now labeled");
    graph.label(v1, "Now labeled");
    graph.label(v2, "Now labeled");
    graph.label(v3, "Now labeled");
    graph.remove(v3);


    String[] s = new String[]{
            "digraph {",
            "  \"v2\"",
            "  \"v1\"",
            "  \"v1\" -> \"v2\" [label=\"v1-v2\"];",
            "}"
    };

    assertEquals(graph.toString(), (String.join("\n", s)));
  }

  @Test
  @DisplayName("testing To String")
  public void toStringEmptyCase() {
    String[] s = new String[]{
            "digraph {",
            "}"
    };

    assertEquals(graph.toString(), (String.join("\n", s)));
  }


}
