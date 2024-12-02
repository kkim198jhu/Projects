package hw4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to AVL Tree.
 */
@SuppressWarnings("All")
public class AvlTreeMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new AvlTreeMap<>();
  }

  @Test
  @DisplayName("Makes sure that the size is 0 before insertion")
  public void sizeBeforeInsertion() {
    assertEquals(map.size(), 0);
  }

  @Test
  @DisplayName("Makes sure that the size updated with insertion")
  public void sizeWithInsertion() {
    map.insert("1", "a");
    map.insert("2", "b");
    map.insert("3", "c");
    map.insert("4", "d");
    map.insert("5", "e");
    assertEquals(map.size(), 5);
  }

  @Test
  @DisplayName("Makes sure that the size updated with remove")
  public void testSizeWithRemove() {
    assertEquals(0, map.size());

    map.insert("1", "one");
    map.insert("2", "two");

    assertEquals(2, map.size());

    map.remove("1");
    assertEquals(1, map.size());

    map.remove("2");
    assertEquals(0, map.size());
  }

  @Test
  @DisplayName("Has basic case works i.e. it has key")
  public void testHas() {
    map.insert("1", "one");
    map.insert("2", "two");
    assertEquals(true, map.has("1"));

    map.remove("1");
    assertEquals(false, map.has("1"));

    map.remove("2");
    assertEquals(false, map.has("2"));
  }

  @Test
  @DisplayName("Has null will return false")
  public void testHasWithNull() {
    map.insert("1", "one");
    map.insert("2", "two");
    assertEquals(false, map.has(null));
  }

  @Test
  @DisplayName("Get base case works normally")
  public void testGet() {
    map.insert("1", "one");
    map.insert("2", "two");
    assertEquals("one", map.get("1"));
    map.insert("3", "three");
    map.insert("4", "four");
    assertEquals("three", map.get("3"));
  }

  @Test
  @DisplayName("Get throws error if there is no valid value")
  public void testGetWithoutValidValue() {
    map.insert("1", "one");
    map.insert("2", "two");
    try{
      map.get("3");
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Get throws error if tries with null")
  public void testGetWithNull() {
    map.insert("1", "one");
    map.insert("2", "two");
    try{
      map.get(null);
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Get throws error if the key is null")
  public void testGetValueNull() {
    map.insert("1", null);
    map.insert("2", "two");
    assertEquals(null, map.get("1"));
  }

  @Test
  @DisplayName("Put works normally and expected with base case")
  public void testPut(){
    map.insert("1", "one");
    map.insert("2", "two");
    map.insert("3", "three");
    map.insert("4", "four");

    map.put("1", "ONEx2");
    assertEquals("ONEx2", map.get("1"));
  }

  @Test
  @DisplayName("Put throws error if key isn't valid")
  public void testPutFail(){
    map.insert("1", "one");
    map.insert("2", "two");
    map.insert("3", "three");
    map.insert("4", "four");

    try{
      map.put("5", "DNE");
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Put throws error if key is null")
  public void testPutNull(){
    map.insert("1", "one");
    map.insert("2", "two");
    map.insert("3", "three");
    map.insert("4", "four");

    try{
      map.put(null, "DNE");
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Put doesn't throws error if value is null")
  public void testPutValueNull() {
    map.insert("1", "one");
    map.insert("2", "two");
    map.put("1", null);
    assertEquals(null, map.get("1"));
  }

  @Test
  @DisplayName("Insert works normally and expected")
  public void testInsertBasic() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("3", "c"); // it must do a left rotation here!
    System.out.println(map.toString());

    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert throws error with null")
  public void testInsertNull() {
    map.insert("2", "b");
    map.insert("1", "a");
    try{
      map.insert(null, "DNE");
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Insert throws error if key already exist")
  public void testInsertAlreadyExistingValue() {
    map.insert("2", "b");
    map.insert("1", "a");
    try{
      map.insert("2", "DNE");
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Insert causes a left rotation on the root")
  public void insertLeftRotation() {
    map.insert("1", "a");
    map.insert("2", "b");
    map.insert("3", "c"); // it must do a left rotation here!
    System.out.println(map.toString());
    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert causes a left rotation not on the root")
  public void insertLeftRotationOnSeperateBranch() {
    map.insert("2", "b");
    map.insert("3", "c");
    map.insert("1", "a");
    map.insert("4", "d");
    map.insert("5", "e"); // it must do a left rotation here!
    System.out.println(map.toString());

    String[] expected = new String[]{
            "2:b",
            "1:a 4:d",
            "null null 3:c 5:e"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert causes a right-left rotation on the root")
  public void insertRightLeftRotation() {
    map.insert("3", "a");
    map.insert("7", "b");
    map.insert("5", "c");

    String[] expected = new String[]{
            "5:c",
            "3:a 7:b"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert causes a right-left rotation on the root")
  public void insertRightLeftRotationOnSeperateBranch() {
    map.insert("2", "b");
    map.insert("3", "c");
    map.insert("1", "a");
    map.insert("5", "e");
    map.insert("4", "d"); // it must do a left rotation here!
    System.out.println(map.toString());

    String[] expected = new String[]{
            "2:b",
            "1:a 4:d",
            "null null 3:c 5:e"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert causes a right rotation on the root")
  public void insertRightRotation() {
    map.insert("3", "c"); // it must do a left rotation here!
    map.insert("2", "b");
    map.insert("1", "a");

    String[] expected = new String[]{
            "2:b",
            "1:a 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert causes a right rotation not on the root")
  public void insertRightRotationOnSeperateBranch() {
    map.insert("4", "d");
    map.insert("5", "e");
    map.insert("3", "c");
    map.insert("2", "b");
    map.insert("1", "a"); // This must do rotation

    String[] expected = new String[]{
            "4:d",
            "2:b 5:e",
            "1:a 3:c null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert causes a left-right rotation on the root")
  public void insertLeftRightRotation() {
    map.insert("7", "a");
    map.insert("3", "b");
    map.insert("5", "c"); // it must do a left rotation here!
    System.out.println(map.toString());

    String[] expected = new String[]{
            "5:c",
            "3:b 7:a"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insert causes a left-right rotation not on the root")
  public void insertLeftRightRotationOnSeperateBranch() {
    map.insert("4", "d");
    map.insert("5", "e");
    map.insert("3", "c");
    map.insert("1", "a");
    map.insert("2", "b"); // This must do rotation

    String[] expected = new String[]{
            "4:d",
            "2:b 5:e",
            "1:a 3:c null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove works normally when it has no children")
  public void removeNoChildren() {
    map.insert("1", "one");
    map.insert("2", "two");
    map.remove("2");
    try{
      assertEquals(null, map.get("2"));
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }

    String[] expected = new String[]{
            "1:one"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove with no children causes left rotation")
  public void removeNoChildrenLeftRotation() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("3", "c");
    map.insert("4", "d");
    map.remove("1");

    String[] expected = new String[]{
            "3:c",
            "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove with no children causes right-left rotation")
  public void removeNoChildrenRightLeftRotation() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("4", "d");
    map.insert("3", "c");
    map.remove("1");

    String[] expected = new String[]{
            "3:c",
            "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove with no children causes right rotation")
  public void removeNoChildrenRightRotation() {
    map.insert("4", "d");
    map.insert("2", "b");
    map.insert("5", "e");
    map.insert("1", "a");
    map.remove("5");

    String[] expected = new String[]{
            "2:b",
            "1:a 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove with no children causes left-right rotation")
  public void removeNoChildrenLeftRightRotation() {
    map.insert("4", "d");
    map.insert("2", "b");
    map.insert("5", "e");
    map.insert("3", "c");
    map.remove("5");

    String[] expected = new String[]{
            "3:c",
            "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove with key null throws error")
  public void removeNull() {
    map.insert("1", "one");
    map.insert("2", "two");
    try{
      map.remove(null);
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Remove with invalid key throws error")
  public void removeInvalidKey() {
    map.insert("1", "one");
    map.insert("2", "two");
    try{
      map.remove("3");
      fail("Did not throw exception");
    } catch (IllegalArgumentException ex) {
      return;
    }
  }

  @Test
  @DisplayName("Testing remove with one child on the left")
  public void removeOneChildrenLeft() {
    map.insert("2", "two");
    map.insert("1", "one");
    map.remove("2");

    String[] expected = new String[]{
            "1:one"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with one child on the right")
  public void removeOneChildrenRight() {
    map.insert("1", "one");
    map.insert("2", "two");
    map.remove("1");

    String[] expected = new String[]{
            "2:two"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with one child on a subtree")
  public void removeOneChildrenComplex() {
    map.insert("3", "c");
    map.insert("4", "d");
    map.insert("1", "a");
    map.insert("2", "b");

    map.remove("1");

    String[] expected = new String[]{
            "3:c",
            "2:b 4:d"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with one child causing left rotation")
  public void removeOneChildrenLeftRotation() {
    map.insert("5", "e");
    map.insert("3", "c");
    map.insert("7", "g");
    map.insert("2", "b");
    map.insert("6", "f");
    map.insert("8", "h");
    map.insert("9", "i");
    map.remove("3");

    String[] expected = new String[]{
            "7:g",
            "5:e 8:h",
            "2:b 6:f null 9:i"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with one child causing right-left rotation")
  public void removeOneChildrenRightLeftRotation() {
    map.insert("5", "e");
    map.insert("3", "c");
    map.insert("8", "h");
    map.insert("2", "b");
    map.insert("7", "g");
    map.insert("9", "i");
    map.insert("6", "f");
    map.remove("3");

    String[] expected = new String[]{
            "7:g",
            "5:e 8:h",
            "2:b 6:f null 9:i"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with one child causing a right rotation")
  public void removeOneChildrenRightRotation() {
    map.insert("5", "e");
    map.insert("3", "c");
    map.insert("8", "h");
    map.insert("2", "b");
    map.insert("4", "d");
    map.insert("9", "i");
    map.insert("1", "a");
    map.remove("8");

    String[] expected = new String[]{
            "3:c",
            "2:b 5:e",
            "1:a null 4:d 9:i"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with one child causing a left-right rotation")
  public void removeOneChildrenLeftRightRotation() {
    map.insert("6", "f");
    map.insert("3", "c");
    map.insert("8", "h");
    map.insert("2", "b");
    map.insert("4", "d");
    map.insert("9", "i");
    map.insert("5", "e");
    map.remove("8");

    String[] expected = new String[]{
            "4:d",
            "3:c 6:f",
            "2:b null 5:e 9:i"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with two children")
  public void removeTwoChildren() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("3", "c");
    map.remove("2");

    String[] expected = new String[]{
            "1:a",
            "null 3:c"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with two children on a subtree")
  public void removeTwoChildrenOnSubtree() {
    map.insert("2", "b");
    map.insert("1", "a");
    map.insert("4", "d");
    map.insert("3", "c");
    map.insert("5", "e");
    map.remove("4");

    String[] expected = new String[]{
            "2:b",
            "1:a 3:c",
            "null null null 5:e",
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing remove with two children with a complex case")
  public void removeTwoChildrenComplex() {
    map.insert("6", "6");
    map.insert("3", "3");
    map.insert("8", "8");
    map.insert("1", "1");
    map.insert("5", "5");
    map.insert("7", "7");
    map.insert("9", "9");
    map.insert("4", "4");
    map.remove("6");

    String[] expected = new String[]{
            "5:5",
            "3:3 8:8",
            "1:1 4:4 7:7 9:9"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing removing the root node")
  public void removeIntial(){
    map.insert("5", "5");
    String data = map.remove("5");
    assertEquals(data, "5");
  }

  @Test
  @DisplayName("Test iterator after object is instantiated.")
  void testEnhancedLoopAfterConstruction() {
    map.insert("1", "1");
    for(String element: map) {
      assertEquals("1", element);
    }
  }

  @Test
  @DisplayName("test iterator through direct object usage after it is instantiated")
  void testIterateObjectUsageNext() {
    map.insert("1", "1");
    map.insert("2", "1");
    int counter = 0;
    // Obtaining the iterator directly
    Iterator<String> iterator = map.iterator();
    // Use the iterator directly
    while (iterator.hasNext()) {
      String value = iterator.next(); // Manually retrieving each element
      // Checks the next methods and the values
      if (counter == 0){
        assertEquals("1", value);
      }else if (counter == 1){
        assertEquals("2", value);
      }
      counter++;
    }
    assertEquals(map.size(), counter);
  }
}
