package hw3.search;

/**
 * Set implemented using a doubly linked list and move-to-front heuristic.
 *
 * @param <T> Element type.
 */
public class MoveToFrontLinkedSet<T> extends LinkedSet<T> {

  @Override
  protected Node<T> find(T t) {
    Node<T> node = super.find(t); // Finds value
    if (node == null) { // Checks for null case
      return null;
    }

    if (head != node) { //Moves to front if it is not head
      super.remove(node);
      this.addFront(node);
    }
    return node;
  }

  /**
   * Adds to the end of this linked-list in O(1)
   * Post: has(t) == true && tail.data == t
   * @param node the node to be added at the front
   *          Pre: node != null && node does exist on list (meaning head != null)
   *            && head != node
   */
  private void addFront(Node<T> node) {
    node.next = head;
    node.prev = null;
    head.prev = node;
    head = node;
  }

  /**
   * Main Method.
   * @param args normal public static void main argument
   */
  public static void main(String[] args) {
    MoveToFrontLinkedSet<Integer> myList = new MoveToFrontLinkedSet<>(); //Creates list
    myList.insert(1); // Inserts a bunch of new nodes
    myList.insert(6);
    myList.insert(6);
    myList.insert(7);
    System.out.println(myList.has(7));
    System.out.println(myList.has(8));
    myList.insert(1);
    for (int elements: myList) { //prints out them in order
      System.out.println(elements);
    }
    System.out.println();
    myList.remove(7); //Remove
    for (int elements: myList) { //prints out them in order
      System.out.println(elements);
    }
  }


}
