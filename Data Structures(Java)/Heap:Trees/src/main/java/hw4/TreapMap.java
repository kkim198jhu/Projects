package hw4;

import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Map implemented as a Treap.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class TreapMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {

  /*** Do not change variable name of 'rand'. ***/
  private static Random rand;
  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;
  private int numElements;

  /**
   * Make a TreapMap.
   */
  public TreapMap() {
    root = null;
    rand = new Random();
    numElements = 0;
  }

  /**
   * Helpful Treap Map Constructor in Order to test the treap.
   *
   * @param setRand a set of random values
   **/
  public TreapMap(Random setRand) {
    root = null;
    rand = setRand;
    numElements = 0;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }

    // Uses a recursive (private) helper insert
    root = insert(root, k, v);
    numElements++;
  }

  /**
   * Insert a new key/value pair recursively.
   *
   * @param k   The key.
   * @param v   The value to be associated with k.
   * @param cur The current node.
   * @return The next node in the in relation to k
   * @throws IllegalArgumentException If k is null or already mapped.
   */
  private Node<K, V> insert(Node<K, V> cur, K k, V v) throws IllegalArgumentException {
    if (cur == null) { // If the subtree is empty, return a new node
      return new Node<>(k, v);
    }

    boolean goesLeft = true;
    // Otherwise, recur down the tree
    if (cur.key.compareTo(k) > 0) {
      cur.left = insert(cur.left, k, v);
    } else if (cur.key.compareTo(k) < 0) {
      cur.right = insert(cur.right, k, v);
      goesLeft = false;
    } else { // The node already exist
      throw new IllegalArgumentException();
    }

    //Makes a rotation if necessary
    Node<K,V> newNode = rotateTree(cur, goesLeft);
    if (newNode != null) {
      return newNode;
    }

    return cur; // return the (unchanged) node pointer
  }

  /**
   * Rotates the tree.
   *
   * @param cur The parent node and the top node
   *            Pre: Has a child and a grandchild on the same side
   * @param goesLeft If the tree went left or not
   * @return The child node
   */
  private Node<K,V> rotateTree(Node<K, V> cur, boolean goesLeft) {
    //Rotates the tree right
    Node<K, V> returnNode = null;
    if (goesLeft) {
      Node<K, V> left = cur.left;
      if (left != null && left.priority < cur.priority) {
        returnNode = rightRotation(cur);
      }
    } else { // Rotates the tree left
      Node<K, V> right = cur.right;
      if (right != null && right.priority < cur.priority) {
        returnNode = leftRotation(cur);
      }
    }

    return returnNode;
  }

  /**
   * Rotates the tree left.
   *
   * @param cur The parent node and the top node
   *            pre: It is never null
   *            pre: Has a child right and a grandchild right of the child
   * @return The new highest node
   */
  private Node<K, V> leftRotation(Node<K, V> cur) {
    //Gets the positions of the children
    Node<K, V> child = cur.right;
    Node<K, V> left = child.left;

    //Swaps the positions and updates them
    child.left = cur;
    cur.right = left;

    return child;
  }

  /**
   * Get the rotates the tree right.
   *
   * @param cur The parent node and the top node
   *            pre: It is never null
   *            pre: It has a grandchild to the left of the child
   *            pre: Has a child left of the cur
   * @return The new highest node
   */
  private Node<K,V> rightRotation(Node<K, V> cur) {
    //Gets the positions of the children
    Node<K, V> child = cur.left;
    Node<K, V> right = child.right;

    //Swaps the positions and updates them
    child.right = cur;
    cur.left = right;

    return child;
  }


  @Override
  public V remove(K k) throws IllegalArgumentException {
    //Edge case
    if (k == null) {
      throw new IllegalArgumentException();
    }

    Node<K, V> node = find(k, root);
    //If it doesn't exist, then ignore it
    if (node == null) {
      throw new IllegalArgumentException();
    }

    V data = node.value;

    root = remove(root, node);
    numElements--;

    return data;
  }

  /**
   * The remove the node.
   *
   * @param cur The subtree root/current node
   * @param removeNode The current node
   * @return The Node Removed
   */
  private Node<K, V> remove(Node<K, V> cur, Node<K, V> removeNode) {
    if (cur == null) {
      return null;
    }

    int cmp = cur.key.compareTo(removeNode.key);
    if (cmp > 0) {
      cur.left = remove(cur.left, removeNode);
    } else if (cmp < 0) {
      cur.right = remove(cur.right, removeNode);
    } else {
      cur.priority = Integer.MAX_VALUE;
      return sink(cur);
    }
    return cur;
  }

  /**
   * Sinks the node to the bottom.
   *
   * @param cur The subtree root/current node
   * @return The subtree with the node
   */
  private Node<K,V> sink(Node<K, V> cur) {
    boolean greatestPriorityLeft;
    // If it's at the bottom, stop
    if (cur.left == null && cur.right == null) {
      return null;
    // Set the priority
    } else {
      greatestPriorityLeft = getPriority(cur);
    }

    Node<K, V> returnNode;
    // Rotate if they have greater priority
    if (greatestPriorityLeft) {
      returnNode = sinkLeft(cur);
      returnNode.right = sink(cur);
    } else {
      returnNode = sinkRight(cur);
      returnNode.left = sink(cur);
    }

    return returnNode;
  }

  /**
   * Sinks to the left.
   *
   * @param cur The subtree root/current node
   * @return The rot of the new subtree
   */
  private Node<K,V> sinkLeft(Node<K, V> cur) {
    Node<K,V> left = cur.left;

    cur.left = left.right;
    left.right = cur;

    return left;
  }

  /**
   * Sinks to the right.
   *
   * @param cur The subtree root/current node
   * @return The rot of the new subtree
   */
  private Node<K,V> sinkRight(Node<K, V> cur) {
    Node<K,V> right = cur.right;

    cur.right = right.left;
    right.left = cur;

    return right;
  }

  /**
   * Gets whether it should go left or right.
   *
   * @param cur The subtree root/current node
   * @return The Node Removed
   */
  private boolean getPriority(Node<K, V> cur) {
    //Initialize and Declare variables
    Node<K, V> left = cur.left;
    Node<K, V> right = cur.right;
    boolean greatestPriorityLeft;
    if (left == null) {
      return false;
    } else if (right == null) {
      return true;
    }

    // Set the left has greater priority
    greatestPriorityLeft = left.priority < right.priority;

    return greatestPriorityLeft;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    //Null test fail
    if (k == null) {
      throw new IllegalArgumentException();
    }

    //Gets the node
    Node<K,V> dataPoint = find(k, root);

    //Throws error when data point is null/DNE
    if  (dataPoint == null) {
      throw new IllegalArgumentException();
    }

    dataPoint.value = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    //Null edge case
    if (k == null) {
      throw new IllegalArgumentException();
    }

    Node<K,V> dataPoint = find(k, root);

    //Throws error when data point is null/DNE
    if (dataPoint == null) {
      throw new IllegalArgumentException();
    }

    return dataPoint.value;
  }

  @Override
  public boolean has(K k) {
    // Null Case (The key can never be null)
    if (k == null) {
      return false;
    }

    // Finds the value
    return find(k,root) != null;
  }

  /**
   * Get the value associated with a key with recursion.
   *
   * @param k The key.
   * @param cur The current node
   * @return The value that was associated with k or null if it doesn't exist.
   */
  private Node<K,V> find(K k, Node<K,V> cur) {
    //Exception case i.e. can't find the node
    if (cur == null) {
      return null;
    }

    //Base case were it finds the node
    if (cur.key.equals(k)) {
      return cur;
    }

    //Uses recursion to go through each and every node
    if (!less(cur.key, k)) {
      return find(k, cur.left);
    } else {
      return find(k, cur.right);
    }
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<K> iterator() {
    return new InorderIterator();
  }

  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
  }

  /**
   * Finds if something is less than another.
   *
   * @param firstData The first data point to compare
   * @param secondData The second data point to compare
   * @return A boolean that signifies if it is less
   */
  private boolean less(K firstData, K secondData) {
    return firstData.compareTo(secondData) < 0;
  }

  /**
   * Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers. Since this is
   * a node class for a Treap we also include a priority field.
   *
   * @param <K> Type for keys.
   * @param <V> Type for values.
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int priority;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      priority = generateRandomInteger();
    }

    // Use this function to generate random values
    // to use as node priorities as you insert new
    // nodes into your TreapMap.
    private int generateRandomInteger() {
      // Note: do not change this function!
      return rand.nextInt();
    }

    @Override
    public String toString() {
      return key + ":" + value + ":" + priority;
    }

    @Override
    public BinaryTreeNode getLeftChild() {
      return left;
    }

    @Override
    public BinaryTreeNode getRightChild() {
      return right;
    }

    // Feel free to add whatever you want to the Node class (e.g. new fields).
    // Just avoid changing any existing names, deleting any existing variables, or modifying the overriding methods.
  }

  // Iterative in-order traversal over the keys
  private class InorderIterator implements Iterator<K> {
    private final Stack<Node<K, V>> stack;

    InorderIterator() {
      stack = new Stack<>();
      pushLeft(root);
    }

    private void pushLeft(Node<K, V> curr) {
      while (curr != null) {
        stack.push(curr);
        curr = curr.left;
      }
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public K next() {
      Node<K, V> top = stack.pop();
      pushLeft(top.right);
      return top.key;
    }
  }
}
