package hw3;

import hw3.sort.GnomeSort;
import hw3.sort.SortingAlgorithm;

public class GnomeSortTest extends SortingAlgorithmTest {
  @Override
  public SortingAlgorithm<Integer> createSortingAlgorithm() {
    return new GnomeSort<>();
  }
}
