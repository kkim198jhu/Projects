package hw3;

import hw3.sort.ArrayIndexedList;
import hw3.sort.IndexedList;
import hw3.sort.SortingAlgorithm;
import hw3.sort.SortingAlgorithmDriver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public abstract class SortingAlgorithmTest {

  private SortingAlgorithm<Integer> unit;

  private static IndexedList<Integer> sample() {
    IndexedList<Integer> indexedList = new ArrayIndexedList<>(8, 0);
    indexedList.put(0, 14);
    indexedList.put(1, 10);
    indexedList.put(2, 23);
    indexedList.put(3, 34);
    indexedList.put(4, 6);
    indexedList.put(5, 17);
    indexedList.put(6, 50);
    indexedList.put(7, 14);
    return indexedList;
  }

  protected abstract SortingAlgorithm<Integer> createSortingAlgorithm();

  @BeforeEach
  public void initSortingAlgorithm() {
    this.unit = createSortingAlgorithm();
  }

  @Test
  @DisplayName("The sorting algorithm sorted the input array sample")
  public void sortSample() {
    IndexedList<Integer> indexedList = sample();
    unit.sort(indexedList);
    assertTrue(SortingAlgorithmDriver.isSorted(indexedList));
  }

  /*  You are NOT required to add more tests here BUT encouraged to do so! */
}
