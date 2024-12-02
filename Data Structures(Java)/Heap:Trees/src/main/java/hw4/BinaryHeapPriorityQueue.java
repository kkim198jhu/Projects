package hw4;

import exceptions.EmptyException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Priority queue implemented as a binary heap with a ranked array representation.
 *
 * @param <T> Element type.
 */
public class BinaryHeapPriorityQueue<T extends Comparable<T>> implements PriorityQueue<T> {
  private final List<T> heap;
  private Comparator<T> cmp;
  private int numElements;

  /**
   * Make a BinaryHeapPriorityQueue.
   */
  public BinaryHeapPriorityQueue() {
    this(new DefaultComparator<>());
  }

  /**
   * Make a BinaryHeapPriorityQueue with a custom comparator.
   *
   * @param cmp Comparator to use.
   */
  public BinaryHeapPriorityQueue(Comparator<T> cmp) {
    this.cmp = cmp;
    heap = new ArrayList<>();
    heap.add(null); // Add a dummy element at index 0 to simplify arithmetic
    numElements = 0;
  }

  @Override
  public void insert(T t) {
    heap.add(t);
    numElements++;
    swimUp(numElements);
  }

  private void swimUp(int i) {
    int j = i / 2;
    if (j < 1) {
      return;
    }
    if (cmp.compare(heap.get(i), heap.get(j)) > 0) {
      T temp = heap.get(i);
      heap.set(i, heap.get(j));
      heap.set(j, temp);
      swimUp(j);
    }
  }

  @Override
  public void remove() throws EmptyException {
    if (empty()) {
      throw new EmptyException();
    }

    heap.set(1, heap.get(numElements));
    heap.remove(numElements);
    numElements--;

    if (numElements > 0) {
      sink(1);
    }
  }

  private void sink(int i) {
    int left = i * 2;
    int right = i * 2 + 1;

    //If the values are too big, then stop
    if (left > numElements) {
      return;
    }

    //Finds the smallest node/value
    int smallest = left;
    if (right <= numElements && cmp.compare(heap.get(right), heap.get(left)) > 0) {
      smallest = right;
    }

    //If the swaps the two
    if (cmp.compare(heap.get(i), heap.get(smallest)) < 0) {
      T temp = heap.get(i);
      heap.set(i, heap.get(smallest));
      heap.set(smallest, temp);
      sink(smallest);
    }
  }

  @Override
  public T best() throws EmptyException {
    if (empty()) {
      throw new EmptyException();
    }
    //Gets the first index
    return heap.get(1);
  }

  @Override
  public boolean empty() {
    //Checks if it is empty
    return numElements == 0;
  }

  @Override
  public Iterator<T> iterator() {
    return new InorderIterator();
  }

  // Iterative in-order traversal over the keys
  private class InorderIterator implements Iterator<T> {
    int index;

    InorderIterator() {
      index = 1;
    }

    @Override
    public boolean hasNext() {
      return index <= numElements;
    }

    @Override
    public T next() throws NoSuchElementException {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      return heap.get(index++);
    }
  }

  // Default comparator is the natural order of elements that are Comparable.
  private static class DefaultComparator<T extends Comparable<T>> implements Comparator<T> {
    public int compare(T t1, T t2) {
      return t1.compareTo(t2);
    }
  }
}
