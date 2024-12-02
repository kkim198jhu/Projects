package hw4;

import exceptions.EmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public abstract class PriorityQueueTest {

  private PriorityQueue<Integer> pq;

  @BeforeEach
  void setUp() {
    pq = this.createQueue();
  }

  abstract protected PriorityQueue<Integer> createQueue();

  @Test
  @DisplayName("PriorityQueue is empty after construction")
  void newQueueEmpty() {
    assertTrue(pq.empty());
  }

  @Test
  @DisplayName("PriorityQueue is not empty after insertion")
  void queueNotEmpty() {
    pq.insert(1);
    assertFalse(pq.empty());
  }

  @Test
  @DisplayName("PriorityQueue is empty after removal")
  void queueEmptyAfterRemove() {
    pq.insert(1);
    assertFalse(pq.empty());
    pq.remove();
    assertTrue(pq.empty());
  }

  @Test
  @DisplayName("Best when there is nothing in the list")
  void bestEmpty() {
    try{
      pq.best();
      fail("This was supposed to throw an exception");
    } catch (EmptyException e) {
      return;
    }
  }

  @Test
  @DisplayName("Test Best Case Changes")
  void bestCaseWithRemovalAndInsertion() {
    pq.insert(1);
    assertEquals(1, pq.best());
    pq.insert(2);
    assertEquals(2, pq.best());
    pq.insert(3);
    assertEquals(3, pq.best());
    pq.remove();
    assertEquals(2, pq.best());
  }

  @Test
  @DisplayName("Insert With No Swim Ups")
  void insertNormal() {
    pq.insert(7);
    pq.insert(6);
    pq.insert(5);
    pq.insert(4);
    pq.insert(3);
    pq.insert(2);
    pq.insert(1);
  }

  @Test
  @DisplayName("Insert And Swims Up to Root")
  void insertSwimToRoot() {
    pq.insert(7);
    pq.insert(6);
    pq.insert(5);
    pq.insert(4);
    pq.insert(3);
    pq.insert(2);
    pq.insert(8);
    assertEquals(8, pq.best());
  }

  @Test
  @DisplayName("Insert And Swims Up to Middle")
  void insertSwimToMiddle() {
    pq.insert(7);
    pq.insert(5);
    pq.insert(4);
    pq.insert(3);
    pq.insert(2);
    pq.insert(6);
    assertEquals(7, pq.best());
    pq.remove();
    assertEquals(6, pq.best());
  }

  @Test
  @DisplayName("Insert And Swims Up to Top as Child")
  void insertTwo() {
    pq.insert(1);
    pq.insert(2);
    assertEquals(2, pq.best());
  }

  @Test
  @DisplayName("Insert Same")
  void insertSame() {
    pq.insert(1);
    pq.insert(1);
    assertEquals(1, pq.best());
    pq.remove();
    assertEquals(1, pq.best());
  }

  @Test
  @DisplayName("Remove when there is nothing in the list")
  void removeEmpty() {
    try{
      pq.remove();
      fail("This was supposed to throw an exception");
    } catch (EmptyException e) {
      return;
    }
  }

  @Test
  @DisplayName("Remove the root (general function)")
  void removeGeneral() {
    pq.insert(7);
    pq.insert(6);
    pq.insert(5);
    pq.insert(4);
    pq.insert(3);
    pq.insert(2);
    pq.insert(1);

    pq.remove();
    assertEquals(6, pq.best());
  }

  @Test
  @DisplayName("Remove the root (general function)")
  void removeGeneralPart2() {
    pq.insert(21);
    pq.insert(6);
    pq.insert(20);
    pq.insert(4);
    pq.insert(3);
    pq.insert(2);
    pq.insert(1);

    pq.remove();
    assertEquals(20, pq.best());
    pq.remove();
    assertEquals(6, pq.best());
    pq.remove();
    assertEquals(4, pq.best());
  }

  @Test
  @DisplayName("Test the enhanced loop after construction")
  void testEnhancedLoopAfterConstruction() {
    pq.insert(1);
    for(int element: pq) {
      assertEquals(1, element);
    }
  }

  @Test
  @DisplayName("Test the amount of elements that loop goes through")
  void testEnhancedLoopWithCounting() {
    pq.insert(1);
    pq.insert(1);
    pq.insert(1);
    pq.insert(1);
    pq.insert(1);
    pq.insert(1);
    pq.insert(1);
    pq.insert(1);
    pq.insert(1);
    int count = 0;

    for(int element: pq) {
      assertEquals(1, element);
      count++;
    }
    assertEquals(9, count);
  }

  @Test
  @DisplayName("Test each object through hasNext")
  void testIterateObjectUsageNext() {
    pq.insert(1);
    pq.insert(2);
    int counter = 0;
    // Obtaining the iterator directly
    Iterator<Integer> iterator = pq.iterator();
    // Use the iterator directly
    while (iterator.hasNext()) {
      int value = iterator.next(); // Manually retrieving each element
      // Checks the next methods and the values
      if (counter == 0){
        assertEquals(2, value);
      }else if (counter == 1){
        assertEquals(1, value);
      }
      counter++;
    }
    assertEquals(2, counter);
  }
}