package hw3.sort;


/**
 * A sorting algorithm that doesn't sort.
 * This is provided mostly as a sanity check: No other algorithm should be
 * faster than this one.
 *
 * @param <T> Element type.
 */
public final class NullSort<T extends Comparable<T>>
    implements SortingAlgorithm<T> {

  @Override
  public void sort(IndexedList<T> indexedList) {
    // nothing to do
  }

  @Override
  public String name() {
    return "Null Sort";
  }
}