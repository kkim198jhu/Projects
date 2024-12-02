package hw2;

public class LinkedIndexedListTest extends IndexedListTest {

  @Override
  public IndexedList<Integer> createArray() {
    return new LinkedIndexedList<>(LENGTH, INITIAL);
  }
  @Override
  public IndexedList<Integer> createInputArray(int newLength) {
    return new LinkedIndexedList<>(newLength, INITIAL);
  }
  @Override
  public IndexedList<Character> createCharArray() {return new LinkedIndexedList<>(LENGTH, charINITIAL);}
  @Override
  public IndexedList<ArrayIndexedList<Integer>> createListArray() {return new ArrayIndexedList<>(LENGTH, arrayINITIAL);};


}
