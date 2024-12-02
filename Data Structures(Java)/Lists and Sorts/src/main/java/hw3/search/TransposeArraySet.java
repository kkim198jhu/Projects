package hw3.search;

/**
 * Set implemented using plain Java arrays and transpose-sequential-search heuristic.
 *
 * @param <T> Element type.
 */
public class TransposeArraySet<T> extends ArraySet<T> {

  @Override
  protected int find(T t) {
    if (t == null) {
      return -1;
    }

    int index = super.find(t);

    // Checks if it is a valid input
    if (index == -1) {
      return index;
    }

    // Switches it up one value
    if (index != 0) {
      T temp = data[index];
      data[index] = data[index - 1];
      data[index - 1] = temp;
      return index - 1;
    }
    return 0;
  }

  /**
   * Main Method.
   * @param args normal public static void main argument
   */
  public static void main(String[] args) {
    TransposeArraySet<String> myList = new TransposeArraySet<>(); //Creates list
    myList.insert("one"); // Inserts a bunch of new elements
    myList.insert("two");
    myList.insert("three");
    myList.insert("four");
    System.out.println(myList.has("four"));
    for (String elements: myList) { //prints out them in order
      System.out.println(elements);
    }
    System.out.println();
    myList.remove("two"); //Remove
    myList.has("three"); // Uses has and should point first
    for (String elements: myList) { //prints out them in order
      System.out.println(elements);
    }
  }

}
