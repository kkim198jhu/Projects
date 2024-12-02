package hw3.search;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Set implemented using plain Java arrays.
 *
 * @param <T> Element type.
 */
public class ArraySet<T> implements Set<T> {
  protected int numElements;
  protected T[] data;

  /**
   * Make a set.
   */
  public ArraySet() {
    data = (T[]) new Object[1];
    numElements = 0;
  }

  @Override
  public void insert(T t) {
    if (full()) {
      grow();
    }
    if (!has(t)) {
      data[numElements++] = t;
    }
  }

  private boolean full() {
    return numElements >= data.length;
  }

  // Double the capacity of data array.
  private void grow() {
    T[] bigger = (T[]) new Object[2 * numElements];
    System.arraycopy(data, 0, bigger, 0, numElements);
    data = bigger;
  }

  /**
   * Find the index of t in data.
   * @param t Element to find.
   *          Pre: t != null
   * @return Index of t in data or -1 if it is not found.
   */
  protected int find(T t) {
    for (int i = 0; i < numElements; i++) {
      if (t.equals(data[i])) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public void remove(T t) {
    int i = find(t);
    if (i != -1) {
      remove(i);
    }
  }

  /**
   * Removes the element at the given index in O(1).
   * Post: has(t) == false where t = data[index] before remove(index) was called.
   * @param index Index of the element to remove.
   *              Pre: 0 <= index < size()
   */
  protected void remove(int index) {
    data[index] = data[numElements - 1];
    data[numElements - 1] = null;
    numElements--;
  }

  @Override
  public boolean has(T t) {
    return find(t) != -1;
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<T> iterator() {
    return new SetIterator();
  }

  // Iterate from element at position 0 to numElements
  private class SetIterator implements Iterator<T> {
    private int current;

    SetIterator() {
      current = 0;
    }

    @Override
    public boolean hasNext() {
      return current < numElements;
    }

    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return data[current++];
    }
  }
}
