package hw3;

import hw3.sort.SelectionSort;
import hw3.sort.SortingAlgorithm;

public class SelectionSortTest extends SortingAlgorithmTest {
  @Override
  public SortingAlgorithm<Integer> createSortingAlgorithm() {
    return new SelectionSort<>();
  }
}
