package hw2;

import java.util.ArrayList;

public class ArrayIndexedListTest extends IndexedListTest {

  @Override
  public IndexedList<Integer> createArray() {
    return new ArrayIndexedList<>(LENGTH, INITIAL);
  }
  @Override
  public IndexedList<Integer> createInputArray(int newLength) {
    return new ArrayIndexedList<>(newLength, INITIAL);
  }
  @Override
  public IndexedList<Character> createCharArray() {return new ArrayIndexedList<>(LENGTH, charINITIAL);}
  @Override
  public IndexedList<ArrayIndexedList<Integer>> createListArray() {return new ArrayIndexedList<>(LENGTH, arrayINITIAL);};

}
