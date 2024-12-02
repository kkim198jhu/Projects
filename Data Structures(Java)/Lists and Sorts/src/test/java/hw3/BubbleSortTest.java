package hw3;

import hw3.sort.BubbleSort;
import hw3.sort.SortingAlgorithm;

public class BubbleSortTest extends SortingAlgorithmTest {
  @Override
  public SortingAlgorithm<Integer> createSortingAlgorithm() {
    return new BubbleSort<>();
  }
}
