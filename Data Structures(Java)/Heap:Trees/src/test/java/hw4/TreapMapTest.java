package hw4;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * In addition to the tests in BinarySearchTreeMapTest (and in OrderedMapTest & MapTest),
 * we add tests specific to Treap.
 */
@SuppressWarnings("All")
public class TreapMapTest extends BinarySearchTreeMapTest {

  @Override
  protected Map<String, String> createMap() {
    return new TreapMap<>(new Random(55));
  }

  @Test
  @DisplayName("Get the values of the seed")
  public void getSeed() {
    Random rand = new Random(55);
    for (int i = 0; i < 15; i++){
      System.out.println(rand.nextInt()/10000000);
    }
    // [2, 4, 14, 3, 15, 7, 12, 11, 1, 13, 6, 9, 10, 5, 8]
  }

  @Test
  @DisplayName("Size is 0 before insertion")
  public void sizeBeforeInsertion() {
    assertEquals(map.size(), 0);
  }

  @Test
  @DisplayName("Insertion increases size by one")
  public void sizeWithInsertion() {
    map.insert("1", "a");
    map.insert("2", "b");
    map.insert("3", "c");
    map.insert("4", "d");
    map.insert("5", "e");
    assertEquals(map.size(), 5);
  }

  @Test
  @DisplayName("Remove decreases size by one")
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
  @DisplayName("Has works under assumed conditions")
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
  @DisplayName("Has is false if key is null")
  public void testHasWithNull() {
    map.insert("1", "one");
    map.insert("2", "two");
    assertEquals(false, map.has(null));
  }

  @Test
  @DisplayName("Test Get works")
  public void testGet() {
    map.insert("1", "one");
    map.insert("2", "two");
    assertEquals("one", map.get("1"));
    map.insert("3", "three");
    map.insert("4", "four");
    assertEquals("three", map.get("3"));
  }

  @Test
  @DisplayName("Get throws error if key does not exist")
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
  @DisplayName("Get throws error if key is null")
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
  @DisplayName("Put gets value can be null")
  public void testGetValueNull() {
    map.insert("1", null);
    map.insert("2", "two");
    assertEquals(null, map.get("1"));
  }

  @Test
  @DisplayName("Put basic case works")
  public void testPut(){
    map.insert("1", "one");
    map.insert("2", "two");
    map.insert("3", "three");
    map.insert("4", "four");

    map.put("1", "ONEx2");
    assertEquals("ONEx2", map.get("1"));
  }

  @Test
  @DisplayName("Put throws error if the key doesn't exists")
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
  @DisplayName("Put doesn't throw error if value is null")
  public void testPutValueNull() {
    map.insert("1", "one");
    map.insert("2", "two");
    map.put("1", null);
    assertEquals(null, map.get("1"));
  }

  @Test
  @DisplayName("Insertion with basic case")
  public void testInsertBasic() {
    map.insert("2", "b");
    System.out.println(map.toString());

    String[] expected = new String[]{
            "2:b:-1162794806"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insertion with a very complex case and lots of rotations")
  public void testInsertComplex() {
    map.insert("5", "d");
    map.insert("3", "a");
    map.insert("7", "f");
    map.insert("6", "f");
    map.insert("8", "8");
    map.insert("9", "e");
    map.insert("4", "d");
    map.insert("2", "f");


    String[] expected = new String[]{
            "5:d:-1162794806",
            "3:a:-244584937 6:f:-742418317",
            "2:f:1771066680 4:d:1777075997 null 9:e:539284930",
            "null null null null null null 7:f:1983759278 null",
            "null null null null null null null null null null null null null 8:8:2031813485 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insertion with a basic case")
  public void insertBasicWithoutRotationComplicated() {
    map.insert("7", "a");
    map.insert("3", "b");
    map.insert("5", "c"); // it must do a left rotation here!
    System.out.println(map.toString());

    String[] expected = new String[]{
            "7:a:-1162794806",
            "3:b:-244584937 null",
            "null 5:c:1983759278 null null"
    };

    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Insertion throws error if a value is null")
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
  @DisplayName("Insertion throws an error if value already exists")
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
  @DisplayName("Testing the insertion that causes right rotation")
  public void insertRightRotation() {
    map.insert("5", "c");
    map.insert("3", "b");
    map.insert("14", "a");
    map.insert("4", "a");

    String[] expected = new String[]{
            "5:c:-1162794806",
            "4:a:-742418317 null",
            "3:b:-244584937 null null null",
            "14:a:1983759278 null null null null null null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Testing the insertion that causes left rotation")
  public void insertLeftRotation() {
    map.insert("5", "d");
    map.insert("3", "a");
    map.insert("7", "f");
    map.insert("6", "f");


    String[] expected = new String[]{
            "5:d:-1162794806",
            "3:a:-244584937 6:f:-742418317",
            "null null null 7:f:1983759278"
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
  @DisplayName("Remove the left leaf")
  public void removeLeftLeaf() {
    map.insert("2", "two");
    map.insert("1", "one");
    map.insert("3", "two");
    map.remove("1");
    String[] expected = new String[]{
            "2:two:-1162794806",
            "null 3:two:1983759278"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove the right leaf")
  public void removeRightLeaf() {
    map.insert("2", "two");
    map.insert("1", "one");
    map.insert("3", "two");
    map.remove("3");
    String[] expected = new String[]{
            "2:two:-1162794806",
            "1:one:-244584937 null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove the root")
  public void removeRoot() {
    map.insert("1", "one");
    map.insert("2", "two");
    map.remove("1");

    String[] expected = new String[]{
            "2:two:-244584937"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());
  }

  @Test
  @DisplayName("Remove the root in a very complex case with a lot of rotations")
  public void removeRootComplex() {
    map.insert("5", "d");
    map.insert("3", "a");
    map.insert("7", "f");
    map.insert("6", "f");
    map.insert("8", "8");
    map.insert("9", "e");
    map.insert("4", "d");
    map.insert("2", "f");


    String[] expected = new String[]{
            "5:d:-1162794806",
            "3:a:-244584937 6:f:-742418317",
            "2:f:1771066680 4:d:1777075997 null 9:e:539284930",
            "null null null null null null 7:f:1983759278 null",
            "null null null null null null null null null null null null null 8:8:2031813485 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());

    map.remove("5");
    String[] expected2 = new String[]{
            "6:f:-742418317",
            "3:a:-244584937 9:e:539284930",
            "2:f:1771066680 4:d:1777075997 7:f:1983759278 null",
            "null null null null null 8:8:2031813485 null null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());

  }

  @Test
  @DisplayName("Remove the a node that connects to a subtree")
  public void removeSubtree() {
    map.insert("3", "three");
    map.insert("1", "one");
    map.insert("2", "two");
    map.insert("4", "four");
    map.insert("5", "five");

    String[] expected = new String[]{
            "3:three:-1162794806",
            "1:one:-244584937 4:four:-742418317",
            "null 2:two:1983759278 null 5:five:2031813485"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());

    map.remove("1");

    String[] expected2 = new String[]{
            "3:three:-1162794806",
            "2:two:1983759278 4:four:-742418317",
            "null null null 5:five:2031813485"
    };

    assertEquals((String.join("\n", expected2) + "\n"), map.toString());

  }

  @Test
  @DisplayName("Remove the subtree in a very complex case with a lot of rotations")
  public void removeSubtreeComplex() {
    map.insert("5", "d");
    map.insert("3", "a");
    map.insert("7", "f");
    map.insert("6", "f");
    map.insert("8", "8");
    map.insert("9", "e");
    map.insert("4", "d");
    map.insert("2", "f");


    String[] expected = new String[]{
            "5:d:-1162794806",
            "3:a:-244584937 6:f:-742418317",
            "2:f:1771066680 4:d:1777075997 null 9:e:539284930",
            "null null null null null null 7:f:1983759278 null",
            "null null null null null null null null null null null null null 8:8:2031813485 null null"
    };
    assertEquals((String.join("\n", expected) + "\n"), map.toString());

    map.remove("6");

    String[] expected2 = new String[]{
            "5:d:-1162794806",
            "3:a:-244584937 9:e:539284930",
            "2:f:1771066680 4:d:1777075997 7:f:1983759278 null",
            "null null null null null 8:8:2031813485 null null"
    };
    assertEquals((String.join("\n", expected2) + "\n"), map.toString());

  }
}