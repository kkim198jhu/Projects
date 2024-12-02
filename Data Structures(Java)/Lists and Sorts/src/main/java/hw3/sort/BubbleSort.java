package hw3.sort;


/**
 * The Bubble Sort algorithm with the optimized "quick" break to exit
 * if the array is sorted.
 *
 * @param <T> The type being sorted.
 */
public final class BubbleSort<T extends Comparable<T>>
    implements SortingAlgorithm<T> {

  // is a less than b?
  private boolean less(T a, T b) {
    return a.compareTo(b) < 0;
  }

  @Override
  public void sort(IndexedList<T> indexedList) {
    boolean swapped;
    for (int i = indexedList.length() - 1; i > 0; i--) {
      swapped = false;
      for (int j = 0; j < i; j++) {
        if (less(indexedList.get(j + 1), indexedList.get(j))) {
          swap(indexedList, j, j + 1);
          swapped = true;
        }
      }
      if (!swapped) {
        return;
      }
    }
  }

  // Pre: i & j are valid indices.
  // Post: elements at i & j are swapped.
  private void swap(IndexedList<T> indexedList, int i, int j) {
    T t = indexedList.get(i);
    indexedList.put(i, indexedList.get(j));
    indexedList.put(j, t);
  }

  @Override
  public String name() {
    return "Bubble Sort";
  }
}
