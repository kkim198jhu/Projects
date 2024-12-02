package hw2;

import exceptions.IndexException;
import exceptions.LengthException;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * An implementation of an IndexedList designed for cases where
 * only a few positions have distinct values from the initial value.
 *
 * @param <T> Element type.
 */
public class SparseIndexedList<T> implements IndexedList<T> {
  private final int length;
  private final T theDefaultValue;
  private Node<T> head;

  /**
   * Constructs a new SparseIndexedList of length size
   * with default value of defaultValue.
   * @param size Length of list, expected: size > 0.
   * @param defaultValue Default value to store in each slot.
   * @throws LengthException if size <= 0.
   */
  public SparseIndexedList(int size, T defaultValue) throws LengthException {
    //Makes sure that the size is greater than 0
    if (size <= 0) {
      throw new LengthException();
    }

    //Initialize size and default values, but not plug into
    //linked list as we are trying to keep memory allocation down
    length = size;
    theDefaultValue = defaultValue;
  }

  /**
   * Checks if the index abides by the length size.
   *
   * @param index Where the index is, expected: length > index > 0.
   * @return A boolean if it within the length <= index or index <= 0.
   * @throws LengthException if length <= index or index <= 0.
   */
  private boolean isValid(int index) {
    return index >= 0 && index < length();
  }

  /**
   * Insert a node at the beginning of the linked list.
   *
   * @param t Where t is the data wanting to insert
   * @param i Where i is the index (value) wanting to insert into node
   */
  private void addFirst(T t, int i) {
    //Creates a new node
    Node<T> n = new Node<T>();
    n.data = t;
    n.index = i;
    //points it towards head
    n.next = head;
    //Sets head at node
    head = n;
  }


  /**
   * Insert a node in the linked list.
   *
   * @param t Where t is the data wanting to insert
   * @param i Where i is the index (value) wanting to insert into node
   */
  private void add(T t, int i) {
    if (head == null || head.index > i) { // Checks edge cases
      addFirst(t, i);
    } else {
      Node<T> cur = head;
      Node<T> prev = null;
      while (cur != null && cur.index < i) { // Runs until cur == null
        prev = cur;
        cur = cur.next;
      }
      Node<T> n = new Node<T>();
      n.data = t;
      n.index = i;
      n.next = cur;
      prev.next = n;
    }
  }

  @Override
  public int length() {
    return length;
  }

  /**
   * Find the node for a given index.
   *
   * @param index Where the index is, given: length > index > 0
   * @return returnValue Returns the node at the given index, or null if there isn't one
   */
  private Node<T> find(int index) throws IndexException {
    // Initialize the variable for the search
    // process such as current node and the return node
    Node<T> cur = head;
    Node<T> returnValue = null;

    while (cur != null) {
      if (index == (cur.index)) {
        returnValue = cur;
      }
      cur = cur.next;
    }

    return returnValue;
  }

  /**
   * Removes a node given the node that is wanted to be removed.
   *
   * @param index Where the node is, given: node exist
   */
  private void remove(int index) {
    Node<T> cur = head;
    Node<T> previous = null;

    int count = 0;
    //If the node is the first node wanted removed
    if (index == 0) {
      head = head.next;
    }
    // If the node to be removed is the head
    if (head.index == (index)) {
      head = head.next; // Move head to the next node
      return;
    }
    // Iterates through all the nodes until finds the node to be removed
    while (cur.index != index) {
      previous = cur;
      cur = cur.next;
    }

    previous.next = cur.next;
  }

  @Override
  public T get(int index) throws IndexException {
    if (!isValid(index)) {
      throw new IndexException();
    }

    Node<T> node = find(index);
    if (node != null) {
      return node.data;
    } else {
      return theDefaultValue;
    }
  }

  @Override
  public void put(int index, T value) throws IndexException {
    if (!isValid(index)) {
      throw new IndexException();
    }
    T thereValue = get(index);
    if (value != null && thereValue != null) {
      // Adds if there is no node add
      if (!(value.equals(theDefaultValue)) && (thereValue.equals(theDefaultValue))) {
        add(value, index);
        // Adds if there is a node change data
      } else if (!(value.equals(theDefaultValue))) {
        Node<T> myNode = find(index);
        myNode.data = value;
        // Remove node if the value is default value
      } else if (value.equals(theDefaultValue) && !thereValue.equals(theDefaultValue)) {
        remove(index);
      }
    } else {
      putNull(index, value, thereValue);
    }
  }

  /**
   * Different method for null value edge cases and uses == instead of .equals
   *
   * @param index , given that index is within range
   * @param value , value is can be null
   * @param thereValue , value is can be null
   */
  public void putNull(int index, T value, T thereValue) {
    // Adds if there is no node add
    if (value != theDefaultValue && thereValue == theDefaultValue) {
      add(value, index);
      // Adds if there is a node change data
    } else if (value != theDefaultValue) {
      Node<T> myNode = find(index);
      myNode.data = value;
      // Remove node if the value is default value
    } else if (thereValue != theDefaultValue) {
      remove(index);
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new SparseIndexedListIterator();
  }

  // Node does not have access to members of SparseIndexedList
  // because it is static
  private static class Node<T> {
    T data;
    int index;
    Node<T> next;
  }

  private class SparseIndexedListIterator implements Iterator<T> {
    private int nextIndex;
    private Node<T> cur;

    private SparseIndexedListIterator() {
      nextIndex = 0;
      cur = head;
    }

    @Override
    public boolean hasNext() {
      return nextIndex < length();
    }

    @Override
    public T next() throws NoSuchElementException {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      T data;
      if (cur != null && cur.index == nextIndex) {
        data = cur.data;
        cur = cur.next;
      } else {
        data = theDefaultValue;
      }

      nextIndex++;
      return data;
    }
  }
}
