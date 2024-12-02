package hw2;

public class SparseIndexedListTest extends IndexedListTest {

  @Override
  public IndexedList<Integer> createArray() {
    return new SparseIndexedList<>(LENGTH, INITIAL);
  }
  @Override
  public IndexedList<Integer> createInputArray(int newLength) {
    return new SparseIndexedList<>(newLength, INITIAL);
  }
  @Override
  public IndexedList<Character> createCharArray() {return new SparseIndexedList<>(LENGTH, charINITIAL);}
  @Override
  public IndexedList<ArrayIndexedList<Integer>> createListArray() {return new ArrayIndexedList<>(LENGTH, arrayINITIAL);};

}