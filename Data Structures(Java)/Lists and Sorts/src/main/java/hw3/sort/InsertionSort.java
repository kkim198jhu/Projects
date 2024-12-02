package hw3.sort;


/**
 * The Insertion Sort algorithm, with minimizing swaps optimization.
 *
 * @param <T> Element type.
 */
public final class InsertionSort<T extends Comparable<T>>
    implements SortingAlgorithm<T> {

  @Override
  public void sort(IndexedList<T> indexedList) {
    int i = 1;
    // Goes through the entire list
    while (i < indexedList.length()) {
      T x = indexedList.get(i);
      int j = i;
      // Goes through each element and swaps them
      while (j > 0 && indexedList.get(j - 1).compareTo(x) > 0) {
        indexedList.put(j, indexedList.get(j - 1));
        j = j - 1;
      }
      indexedList.put(j, x);
      i = i + 1;
    }
  }

  @Override
  public String name() {
    return "Insertion Sort";
  }
}
