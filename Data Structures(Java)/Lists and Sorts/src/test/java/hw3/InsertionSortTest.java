package hw3;

import hw3.sort.InsertionSort;
import hw3.sort.SortingAlgorithm;

public class InsertionSortTest extends SortingAlgorithmTest {
  @Override
  public SortingAlgorithm<Integer> createSortingAlgorithm() {
    return new InsertionSort<>();
  }
}
