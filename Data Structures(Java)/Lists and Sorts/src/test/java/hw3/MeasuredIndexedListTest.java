package hw3;

import exceptions.IndexException;
import exceptions.LengthException;
import hw3.sort.ArrayIndexedList;
import hw3.sort.MeasuredIndexedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MeasuredIndexedListTest {

  private static final int LENGTH = 15;
  private static final int DEFAULT_VALUE = 3;

  private MeasuredIndexedList<Integer> measuredIndexedList;
  private ArrayIndexedList<Integer> arrayIndexedList;
  private MeasuredIndexedList<Integer> newMeasuredIndexedList;

  @BeforeEach
  void setup() {
    measuredIndexedList = new MeasuredIndexedList<>(LENGTH, DEFAULT_VALUE);
    arrayIndexedList = new MeasuredIndexedList<>(LENGTH, DEFAULT_VALUE);
  }

  @Test
  @DisplayName("MeasuredIndexedList starts with zero reads")
  void zeroReadsStart() {
    assertEquals(0, measuredIndexedList.accesses());
  }

  @Test
  @DisplayName("MeasuredIndexedList starts with zero writes")
  void zeroWritesStart() {
    assertEquals(0, measuredIndexedList.mutations());
  }

  @Test
  @DisplayName("get() returns the default value after IndexedList is instantiated with polymorphism.")
  void testGetAfterConstructionWithPolymorphism() {
    for (int index = 0; index < measuredIndexedList.length(); index++) {
      assertEquals(DEFAULT_VALUE, arrayIndexedList.get(index));
    }
  }
  @Test
  @DisplayName("count() returns the size of the list without any puts.")
  void testCountWithoutPut() {
    assertEquals(LENGTH, measuredIndexedList.count(DEFAULT_VALUE));
  }

  @Test
  @DisplayName("count() returns the size of the list with any puts.")
  void testCountWithPut() {
    assertEquals(LENGTH, measuredIndexedList.count(DEFAULT_VALUE));
    measuredIndexedList.put(2, 3);
    measuredIndexedList.put(3, 7);
    measuredIndexedList.put(4, 4);
    measuredIndexedList.put(5, 7);
    measuredIndexedList.put(6, 3);
    assertEquals(12, measuredIndexedList.count(DEFAULT_VALUE));
  }

  @Test
  @DisplayName("get() returns the default value after IndexedList is instantiated.")
  void testGetAfterConstruction() {
    for (int index = 0; index < measuredIndexedList.length(); index++) {
      assertEquals(DEFAULT_VALUE, measuredIndexedList.get(index));
    }
  }

  @Test
  @DisplayName("length() returns the length.")
  void testLength() {
    assertEquals(LENGTH, measuredIndexedList.length());
  }

  @Test
  @DisplayName("Test constructor if given a size that is below 0")
  void testConstructorWithIndexBelowRangeThrowsException() {
    try {
      newMeasuredIndexedList =  new MeasuredIndexedList<>(-1, DEFAULT_VALUE);;
      fail("IndexException was not thrown for index < 0");
    } catch (LengthException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Test Get before constructor is called")
  void testGetsBeforeInstantiation() {
    try {
      newMeasuredIndexedList.get(1);
      fail("IndexException was not thrown for index < 0");
    } catch (NullPointerException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Test Put before constructor is called")
  void testPutBeforeInstantiation() {
    try {
      newMeasuredIndexedList.put(1, 1);
      fail("IndexException was not thrown for index < 0");
    } catch (NullPointerException ex) {
      return;
    }
  }

  @Test
  @DisplayName("get() throws exception if index is above the valid range.")
  void testGetWithIndexAboveRangeThrowsException() {
    try {
      measuredIndexedList.get(16);
      fail("IndexException was not thrown for index >= LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("get() throws exception if index is below the valid range.")
  void testGetWithIndexBelowRangeThrowsException() {
    try {
      measuredIndexedList.get(-1);
      fail("IndexException was not thrown for index < 0");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() throws exception if index is above the valid range.")
  void testPutWithIndexAboveRangeThrowsException() {
    try {
      measuredIndexedList.put(16, 6);
      fail("IndexException was not thrown for index >= LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() throws exception if index is below the valid range.")
  void testPutWithIndexBelowRangeThrowsException() {
    try {
      measuredIndexedList.put(-1, 6);
      fail("IndexException was not thrown for index < LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value")
  void testPutRepeat() {
    measuredIndexedList.put(2, 3);
    measuredIndexedList.put(2, 7);
    measuredIndexedList.put(2, 4);
    measuredIndexedList.put(2, 7);
    measuredIndexedList.put(2, 3);
    assertEquals(3, measuredIndexedList.get(2));
    measuredIndexedList.put(2, 3);
    assertEquals(DEFAULT_VALUE, measuredIndexedList.get(2));
  }

  @Test
  @DisplayName("mutations() works by removing and adding an inputted value and then the initial value")
  void testMutationRepeat() {
    measuredIndexedList.put(2, 3);
    measuredIndexedList.put(2, 7);
    measuredIndexedList.put(2, 4);
    measuredIndexedList.put(2, 7);
    measuredIndexedList.put(2, 3);
    assertEquals(5, measuredIndexedList.mutations());
    measuredIndexedList.put(2, 3);
    assertEquals(6, measuredIndexedList.mutations());
  }

  @Test
  @DisplayName("accesses() works by using a lot of get() ")
  void testAccessesRepeat() {
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    assertEquals(5, measuredIndexedList.accesses());
    measuredIndexedList.get(2);
    assertEquals(6, measuredIndexedList.accesses());
  }

  @Test
  @DisplayName("accesses() and mutate() by checking with put() and gets() to access and modify the list")
  void testAccessesAndMutateTogetherRepeat() {
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 7);
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 3);
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 4);
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 2000);
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 7);
    assertEquals(5, measuredIndexedList.accesses());
    assertEquals(5, measuredIndexedList.mutations());
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 7);
    assertEquals(6, measuredIndexedList.accesses());
    assertEquals(6, measuredIndexedList.mutations());
  }

  @Test
  @DisplayName("mutations() works with modifying the list with put() and then test reset()")
  void testMutationRepeatWithReset() {
    measuredIndexedList.put(2, 3);
    measuredIndexedList.put(2, 7);
    measuredIndexedList.put(2, 4);
    measuredIndexedList.put(2, 7);
    measuredIndexedList.put(2, 3);
    assertEquals(5, measuredIndexedList.mutations());
    measuredIndexedList.put(2, 3);
    assertEquals(6, measuredIndexedList.mutations());
    measuredIndexedList.reset();
    assertEquals(0, measuredIndexedList.mutations());
    measuredIndexedList.put(2, 3);
    measuredIndexedList.put(2, 7);
    assertEquals(2, measuredIndexedList.mutations());
    measuredIndexedList.put(2, 3);
    assertEquals(3, measuredIndexedList.mutations());
    measuredIndexedList.reset();
    assertEquals(0, measuredIndexedList.mutations());
  }

  @Test
  @DisplayName("accesses() works by using a lot of get() to access then test resets()")
  void testAccessesRepeatWithReset() {
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    assertEquals(5, measuredIndexedList.accesses());
    measuredIndexedList.get(2);
    assertEquals(6, measuredIndexedList.accesses());
    measuredIndexedList.reset();
    assertEquals(0, measuredIndexedList.accesses());
    measuredIndexedList.get(2);
    measuredIndexedList.get(2);
    assertEquals(2, measuredIndexedList.accesses());
    measuredIndexedList.get(2);
    assertEquals(3, measuredIndexedList.accesses());
  }

  @Test
  @DisplayName("accesses() and mutate() by checking with put() and gets() to access and modify the list with reset()")
  void testAccessesAndMutateTogetherRepeatWithReset() {
    measuredIndexedList.put(2, 7);
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 3);
    measuredIndexedList.get(2);
    assertEquals(2, measuredIndexedList.accesses());
    assertEquals(2, measuredIndexedList.mutations());
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 7);
    assertEquals(3, measuredIndexedList.accesses());
    assertEquals(3, measuredIndexedList.mutations());
    measuredIndexedList.reset();
    assertEquals(0, measuredIndexedList.accesses());
    assertEquals(0, measuredIndexedList.mutations());
    measuredIndexedList.get(2);
    measuredIndexedList.put(2, 7);
    assertEquals(1, measuredIndexedList.accesses());
    assertEquals(1, measuredIndexedList.mutations());
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value")
  void testPutMultiple() {
    measuredIndexedList.put(2, 3);
    measuredIndexedList.put(3, 2);
    measuredIndexedList.put(4, 8);
    measuredIndexedList.put(9, 10010);
    measuredIndexedList.put(5, 18203);
    assertEquals(3, measuredIndexedList.get(2));
    assertEquals(2, measuredIndexedList.get(3));
    assertEquals(8, measuredIndexedList.get(4));
    assertEquals(10010, measuredIndexedList.get(9));
    assertEquals(18203, measuredIndexedList.get(5));
  }




  @Test
  @DisplayName("Test constructor if given a size that is below 0 with polymorphism")
  void testConstructorWithIndexBelowRangeThrowsExceptionWithPolymorphism() {
    try {
      arrayIndexedList =  new MeasuredIndexedList<>(-1, DEFAULT_VALUE);;
      fail("IndexException was not thrown for index < 0");
    } catch (LengthException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Test Get before constructor is called with polymorphism")
  void testGetsBeforeInstantiationWithPolymorphism() {
    try {
      newMeasuredIndexedList.get(1);
      fail("IndexException was not thrown for index < 0");
    } catch (NullPointerException ex) {
      return;
    }
  }

  @Test
  @DisplayName("get() throws exception if index is above the valid range with polymorphism.")
  void testGetWithIndexAboveRangeThrowsExceptionWithPolymorphism() {
    try {
      arrayIndexedList.get(16);
      fail("IndexException was not thrown for index >= LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("get() throws exception if index is below the valid range with polymorphism.")
  void testGetWithIndexBelowRangeThrowsExceptionWithPolymorphism() {
    try {
      arrayIndexedList.get(-1);
      fail("IndexException was not thrown for index < 0");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() throws exception if index is above the valid range with polymorphism.")
  void testPutWithIndexAboveRangeThrowsExceptionWithPolymorphism() {
    try {
      arrayIndexedList.put(16, 6);
      fail("IndexException was not thrown for index >= LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() throws exception if index is below the valid range with polymorphism.")
  void testPutWithIndexBelowRangeThrowsExceptionWithPolymorphism() {
    try {
      arrayIndexedList.put(-1, 6);
      fail("IndexException was not thrown for index < LENGTH");
    } catch (IndexException ex) {
      return;
    }
  }

  @Test
  @DisplayName("put() works by removing and adding an inputted value and then the initial value with polymorphism")
  void testPutMultipleWithPolymorphism() {
    arrayIndexedList.put(2, 3);
    arrayIndexedList.put(3, 2);
    arrayIndexedList.put(4, 8);
    arrayIndexedList.put(9, 10010);
    arrayIndexedList.put(5, 18203);
    assertEquals(3, arrayIndexedList.get(2));
    assertEquals(2, arrayIndexedList.get(3));
    assertEquals(8, arrayIndexedList.get(4));
    assertEquals(10010, arrayIndexedList.get(9));
    assertEquals(18203, arrayIndexedList.get(5));
  }

  @Test
  @DisplayName("Test iterator by checking hasNext will always be true except the last function")
  void testIterateObjectHasNext() {
    int counter = 0;
    // Obtaining the iterator directly
    Iterator<Integer> iterator = measuredIndexedList.iterator();
    // Use the iterator directly
    while (iterator.hasNext()) {
      int value = iterator.next();
      if (counter == LENGTH - 1){
        assertEquals(false, iterator.hasNext());
      } else {
        assertEquals(true, iterator.hasNext());
      }
      counter++;
    }
    assertEquals(false, iterator.hasNext());
  }

  @Test
  @DisplayName("Test iterator after it goes pass all known values with Integers")
  void testIterateObjectBounds() {
    int counter = 0;
    // Obtaining the iterator directly
    Iterator<Integer> iterator = measuredIndexedList.iterator();
    // Use the iterator directly
    while (iterator.hasNext()) {
      int value = iterator.next();
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
  @DisplayName("test iterator after IndexedCharacterList is instantiated")
  void testEnhancedLoopAfterConstruction() {
    int counter = 0;
    for(int element: measuredIndexedList) {
      assertEquals(DEFAULT_VALUE, element);
      counter++;
    }
    assertEquals(LENGTH, counter);
  }

  @Test
  @DisplayName("Checks that gets doesn't change the length with integers")
  void testLengthWithGets() {
    measuredIndexedList.get(2);
    assertEquals(10, measuredIndexedList.length());
    measuredIndexedList.put(2, 2);
    assertEquals(10, measuredIndexedList.length());
    assertEquals(10, measuredIndexedList.length());
  }

  @Test
  @DisplayName("Tests Puts with null values")
  void testPutsWithNull() {
    measuredIndexedList.put(2, null);
    assertEquals(null, measuredIndexedList.get(2));
    measuredIndexedList.put(2, 7);
    assertEquals(7, measuredIndexedList.get(2));
    measuredIndexedList.put(3, null);
    assertEquals(null, measuredIndexedList.get(3));
  }
}
