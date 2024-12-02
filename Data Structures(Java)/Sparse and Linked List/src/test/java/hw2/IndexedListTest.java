package hw2;

import exceptions.IndexException;
import exceptions.LengthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Iterator;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit Tests for any class implementing the IndexedList interface.
 */
public abstract class IndexedListTest {
  protected static final int LENGTH = 10;
  protected static final int INITIAL = 7;
  protected static final char charINITIAL = 'a';
  protected static final ArrayIndexedList<Integer> arrayINITIAL = new ArrayIndexedList<>(10, 1);
  private IndexedList<Integer> indexedList;
  private IndexedList<Character> indexedCharacterList;
  private IndexedList<ArrayIndexedList<Integer>> indexedArrayList;
  private IndexedList<Integer> uninstantiatedList;

  public abstract IndexedList<Integer> createArray();
  public abstract IndexedList<ArrayIndexedList<Integer>> createListArray();
  public abstract IndexedList<Character> createCharArray();
  public abstract IndexedList<Integer> createInputArray(int newLength);


  @BeforeEach
  public void setup() {
    indexedList = createArray();
    indexedCharacterList = createCharArray();
    indexedArrayList = createListArray();
  }

  @Test
  @DisplayName("get() returns the default value after IndexedList is instantiated.")
  void testGetAfterConstruction() {
    for (int index = 0; index < indexedList.length(); index++) {
      assertEquals(INITIAL, indexedList.get(index));
    }
  }

  @Test
  @DisplayName("get() returns the default value after IndexedList is instantiated with arraylist.")
  void testGetAfterConstructionList() {
    for (int index = 0; index < indexedArrayList.length(); index++) {
      assertEquals(arrayINITIAL, indexedArrayList.get(index));
    }
  }

  @Test
  @DisplayName("Test constructor if given a size that is below 0")
  void testConstructorWithIndexBelowRangeThrowsException() {
    try {
      indexedList =  createInputArray(-1);
      fail("IndexException was not thrown for index < 0");
    } catch (LengthException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Test Get before constructor is called")
  void testGetsBeforeInstantiation() {
    try {
      uninstantiatedList.get(1);
      fail("IndexException was not thrown for index < 0");
    } catch (NullPointerException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Test Put before constructor is called")
  void testPutBeforeInstantiation() {
    try {
      uninstantiatedList.put(1, 1);
      fail("IndexException was not thrown for index < 0");
    } catch (NullPointerException ex) {
      return;
    }
  }

  @Test
  @DisplayName("get() throws exception if index is below the valid range.")
  void testGetWithIndexBelowRangeThrowsException() {
    try {
      indexedList.get(-1);
      fail("IndexException was not thrown for index < 0");
    } catch (IndexException ex) {
      return;
    }
  }


  @Test
  @DisplayName("get() throws exception if index is above the valid range.")
  void testGetWithIndexAboveRangeThrowsException() {
    try {
      indexedList.get(10);
      fail("IndexException was not thrown for index >= LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() throws exception if index is above the valid range.")
  void testPutWithIndexAboveRangeThrowsException() {
    try {
      indexedList.put(10, 9);
      fail("IndexException was not thrown for index >= LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() throws exception if index is below the valid range.")
  void testPutWithIndexBelowRangeThrowsException() {
    try {
      indexedList.put(-1, 9);
      fail("IndexException was not thrown for index < 0");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() works and returns the inputted value")
  void testPutWorksGeneral() {
    indexedList.put(2, 3);
    assertEquals(3, indexedList.get(2));
  }

  @Test
  @DisplayName("put() works and doesn't change other value")
  void testPutDoesNotChangeOtherValues() {
    indexedList.put(0, 3);
    for (int index = 1; index < indexedList.length(); index++) {
      assertEquals(INITIAL, indexedList.get(index));
    }
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value")
  void testPutRepeat() {
    indexedList.put(2, 3);
    indexedList.put(2, 7);
    indexedList.put(2, 4);
    indexedList.put(2, 7);
    indexedList.put(2, 3);
    assertEquals(3, indexedList.get(2));
    indexedList.put(2, 7);
    assertEquals(INITIAL, indexedList.get(2));
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value with array")
  void testPutRepeatArray() {
    ArrayIndexedList<Integer> my_array = new ArrayIndexedList<>(20, 3);
    indexedArrayList.put(2, my_array);
    indexedArrayList.put(2, arrayINITIAL);
    assertEquals(arrayINITIAL, indexedArrayList.get(2));
    indexedArrayList.put(2, my_array);
    assertEquals(my_array, indexedArrayList.get(2));
  }

  @Test
  @DisplayName("put() tested by trying to compare two data types that are the same but differ in memory")
  void testPutRepeatArrayMemory() {
    ArrayIndexedList<Integer> my_array = new ArrayIndexedList<>(20, 3);
    ArrayIndexedList<Integer> my_array1 = new ArrayIndexedList<>(20, 3);

    indexedArrayList.put(2, my_array);
    assertEquals(my_array, indexedArrayList.get(2));
    indexedArrayList.put(2, my_array1);
    assertEquals(my_array1, indexedArrayList.get(2));
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value")
  void testPutMultiple() {
    indexedList.put(2, 3);
    indexedList.put(3, 2);
    indexedList.put(4, 8);
    indexedList.put(9, 10010);
    indexedList.put(5, 18203);
    assertEquals(3, indexedList.get(2));
    assertEquals(2, indexedList.get(3));
    assertEquals(8, indexedList.get(4));
    assertEquals(10010, indexedList.get(9));
    assertEquals(18203, indexedList.get(5));
    assertEquals(INITIAL, indexedList.get(8));
  }

  @Test
  @DisplayName("put() works by putting in initial value then a non-initial value")
  void testPutInitialValue() {
    indexedList.put(2, 7);
    indexedList.put(2, 8);
    assertEquals(8, indexedList.get(2));
    assertEquals(INITIAL, indexedList.get(8));
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value using characters")
  void testPutCharacterMultiple() {
    indexedCharacterList.put(2, 'f');
    indexedCharacterList.put(3, 'b');
    indexedCharacterList.put(4, 'c');
    indexedCharacterList.put(9, 'd');
    indexedCharacterList.put(5, 'e');
    assertEquals('f', indexedCharacterList.get(2));
    assertEquals('b', indexedCharacterList.get(3));
    assertEquals('c', indexedCharacterList.get(4));
    assertEquals('d', indexedCharacterList.get(9));
    assertEquals('e', indexedCharacterList.get(5));
    assertEquals(charINITIAL, indexedCharacterList.get(8));
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value with character")
  void testPutRepeatCharacter() {
    indexedCharacterList.put(2, 'b');
    indexedCharacterList.put(2, 'a');
    indexedCharacterList.put(2, 'b');
    indexedCharacterList.put(2, 'a');
    indexedCharacterList.put(2, 'c');
    assertEquals('c', indexedCharacterList.get(2));
    indexedCharacterList.put(2, 'a');
    assertEquals(charINITIAL, indexedCharacterList.get(2));
  }

  @Test
  @DisplayName("Checks that puts doesn't change the length with characters")
  void testLengthWithPutsCharacters() {
    indexedCharacterList.put(2, 'b');
    assertEquals(10, indexedCharacterList.length());
    indexedCharacterList.put(2, 'a');
    assertEquals(10, indexedCharacterList.length());
  }

  @Test
  @DisplayName("Checks that puts doesn't change the length with integers")
  void testLengthWithPuts() {
    indexedList.put(2, 1);
    assertEquals(10, indexedList.length());
    indexedList.put(2, 2);
    assertEquals(10, indexedList.length());
  }

  @Test
  @DisplayName("Checks that gets doesn't change the length with characters")
  void testLengthWithGetsCharacters() {
    indexedCharacterList.get(2);
    assertEquals(10, indexedCharacterList.length());
    indexedCharacterList.get(8);
    assertEquals(10, indexedCharacterList.length());
  }

  @Test
  @DisplayName("Checks that gets doesn't change the length with integers")
  void testLengthWithGets() {
    indexedList.get(2);
    assertEquals(10, indexedList.length());
    indexedList.put(2, 2);
    assertEquals(10, indexedList.length());
  }

  @Test
  @DisplayName("Tests Puts with null values")
  void testPutsWithNull() {
    indexedList.put(2, null);
    assertEquals(null, indexedList.get(2));
    indexedList.put(2, 7);
    assertEquals(7, indexedList.get(2));
    indexedList.put(3, null);
    assertEquals(null, indexedList.get(3));
  }

  @Test
  @DisplayName("test iterator after IndexedList is instantiated.")
  void testEnhancedLoopAfterConstruction() {
    int counter = 0;
    for(int element: indexedList) {
      assertEquals(INITIAL, element);
      counter++;
    }
    assertEquals(LENGTH, counter);
  }

  @Test
  @DisplayName("test iterator after IndexedCharacterList is instantiated with Characters")
  void testEnhancedLoopAfterConstructionWithCharacter() {
    int counter = 0;
    for(int element: indexedCharacterList) {
      assertEquals(charINITIAL, element);
      counter++;
    }
    assertEquals(LENGTH, counter);
  }

  @Test
  @DisplayName("test iterator through direct object usage after it is instantiated with Characters")
  void testIterateObjectUsageNext() {
    indexedCharacterList.put(0, 'b');
    indexedCharacterList.put(1, 'c');
    int counter = 0;
    // Obtaining the iterator directly
    Iterator<Character> iterator = indexedCharacterList.iterator();
    // Use the iterator directly
    while (iterator.hasNext()) {
      char value = iterator.next(); // Manually retrieving each element
      // Checks the next methods and the values
      if (counter == 0){
        assertEquals('b', value);
      }else if (counter == 1){
        assertEquals('c', value);
      } else {
        assertEquals('a', value);
      }
      counter++;
    }
    assertEquals(LENGTH, counter);
  }

  @Test
  @DisplayName("Test iterator after it goes pass all known values with Characters")
  void testIterateObjectBounds() {
    int counter = 0;
    // Obtaining the iterator directly
    Iterator<Character> iterator = indexedCharacterList.iterator();
    // Use the iterator directly
    while (iterator.hasNext()) {
      char value = iterator.next();
      counter++;
    }
    assertEquals(LENGTH, counter);
    try {
      iterator.next();
      fail("It went pass the bounds of the iterator");
    } catch (NoSuchElementException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Test iterator by checking hasNext will always be true except the last function")
  void testIterateObjectHasNext() {
    int counter = 0;
    // Obtaining the iterator directly
    Iterator<Character> iterator = indexedCharacterList.iterator();
    // Use the iterator directly
    while (iterator.hasNext()) {
      char value = iterator.next();
      if (counter == LENGTH - 1){
        assertEquals(false, iterator.hasNext());
      } else {
        assertEquals(true, iterator.hasNext());
      }
      counter++;
    }
    assertEquals(false, iterator.hasNext());
  }



}
