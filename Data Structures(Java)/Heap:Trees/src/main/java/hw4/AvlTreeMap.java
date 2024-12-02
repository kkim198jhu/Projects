package hw4;

import java.util.Iterator;
import java.util.Stack;

/**
 * Map implemented as an AVL Tree.
 *
 * @param <K> Type for keys.
 * @param <V> Type for values.
 */
public class AvlTreeMap<K extends Comparable<K>, V> implements OrderedMap<K, V> {
  private int numElements;
  /*** Do not change variable name of 'root'. ***/
  private Node<K, V> root;

  /**
   * Construct an empty AvlTreeMap.
   */
  public AvlTreeMap() {
    root = null;
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
    if (cur == null) { /* If the subtree is empty, return a new node */
      return new Node<>(k, v);
    }
    /* Otherwise, recur down the tree */
    if (cur.key.compareTo(k) > 0) {
      cur.left = insert(cur.left, k, v);
    } else if (cur.key.compareTo(k) < 0) {
      cur.right = insert(cur.right, k, v);
    } else { // The node already exist
      throw new IllegalArgumentException();
    }

    updateHeight(cur);
    int balanceFactor = isBalanced(cur);
    // Checks it if is balanced, and if not it will shift the tree
    if (balanceFactor > 1 || balanceFactor < -1) {
      return rotateTree(cur, balanceFactor);
    }
    return cur; // return the (unchanged) node pointer
  }

  @Override
  public V remove(K k) {
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

    root = remove(root, k);
    numElements--;
    return data;
  }

  /**
   * The remove the node.
   *
   * @param cur The subtree root/current node
   * @param k The current value
   * @return The Node Removed
   */
  private Node<K, V> remove(Node<K, V> cur, K k) {
    if (cur == null) {
      return null;
    }

    int cmp = cur.key.compareTo(k);
    if (cmp > 0) {
      cur.left = remove(cur.left, k);
    } else if (cmp < 0) {
      cur.right = remove(cur.right, k);
    } else {
      // If the node has 2 children
      if (cur.right != null && cur.left != null) {
        remove(cur);
      } else if (cur.right == null) {
        return cur.left;
      } else {
        return cur.right;
      }
    }

    return trueBalance(cur);
  }

  /**
   * The remove the node.
   *
   * @param node The current node
   */
  private void remove(Node<K, V> node) {
    // If it has two children, find the predecessor (max in left subtree),
    Node<K, V> maxNode = max(node.left);

    // Copy its data to the given node (value change),
    node.key = maxNode.key;
    node.value = maxNode.value;
    node.height = maxNode.height;

    // then remove the predecessor node (structural change).
    node.left = remove(node.left, maxNode.key);


  }

  private Node<K,V> trueBalance(Node<K, V> cur) {
    updateHeight(cur);
    int balanceFactor = isBalanced(cur);
    // Checks it if is balanced, and if not it will shift the tree
    if (balanceFactor > 1 || balanceFactor < -1) {
      return rotateTree(cur, balanceFactor);
    }
    return cur;
  }

  /**
   * The maximum node.
   *
   * @param node The current node
   * @return The maximum node
   */
  private Node<K, V> max(Node<K, V> node) {
    Node<K, V> curr = node;
    while (curr.right != null) {
      curr = curr.right;
    }
    return curr;
  }


  /**
   * Updates the height field in the node.
   *
   * @param cur The current node
   */
  private void updateHeight(Node<K, V> cur) {
    int leftHeight = -1;
    if (cur.left != null) {
      leftHeight = cur.left.height;
    }

    int rightHeight = -1;
    if (cur.right != null) {
      rightHeight = cur.right.height;
    }

    if (leftHeight > rightHeight) {
      cur.height = 1 + leftHeight;
    } else {
      cur.height = 1 + rightHeight;
    }

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
    if (dataPoint == null) {
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

  /**
   * Get the value associated with a key with recursion.
   *
   * @param k The key.
   * @param cur The current node
   * @return The value that was associated with k.
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
  public boolean has(K k) {
    // Null Case (The key can never be null)
    if (k == null) {
      return false;
    }
    // Finds the value
    return find(k,root) != null;
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<K> iterator() {
    return new InorderIterator();
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
   * Rotates the tree.
   *
   * @param cur The parent node and the top node
   *            Pre: Has a child and a grandchild on the same side
   * @param balanceFactor The balance of cur
   *                      Pre: is > 1 or < -1
   * @return The child node
   */
  private Node<K,V> rotateTree(Node<K, V> cur, int balanceFactor) {
    //Rotates the tree right
    Node<K, V> returnNode;
    if (balanceFactor > 1) {
      Node<K, V> left = cur.left;
      if (isBalanced(left) >= 0) {
        returnNode = rightRotation(cur);
      } else {
        returnNode = leftRightRotation(cur);
      }
    } else { // Rotates the tree left
      Node<K, V> right = cur.right;
      if (isBalanced(right) <= 0) {
        returnNode = leftRotation(cur);
      } else {
        returnNode = rightLeftRotation(cur);
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

    //Updates heights
    updateHeight(cur);
    updateHeight(child);
    return child;
  }


  /**
   * Swaps the rotates child and grandchild and the rotates the tree left.
   *
   * @param cur The parent node and the top node
   *            pre: It is never null
   *            pre: It has a grandchild to the left of the child
   * @return The new highest node
   */
  private Node<K,V> rightLeftRotation(Node<K, V> cur) {
    //Swaps the positions, so it is set up for leftRotation
    cur.right = rightRotation(cur.right);

    //Rotates left
    return leftRotation(cur);
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

    //Updates heights
    updateHeight(cur);
    updateHeight(child);
    return child;
  }

  /**
   * Swaps the rotates child and grandchild and the rotates the tree right.
   *
   * @param cur The parent node and the top node
   *            pre: It is never null
   *            pre: It has a grandchild to the right of the child
   * @return The new highest node
   */
  private Node<K,V> leftRightRotation(Node<K, V> cur) {
    //Swaps the positions, so it is set up for rightRotation
    cur.left = leftRotation(cur.left);

    //Rotates right
    return rightRotation(cur);
  }


  /**
   * Get the balance factor associated with a node.
   *
   * @param cur The current node
   *            pre: It is never null
   * @return The balance factor
   */
  private int isBalanced(Node<K, V> cur) {
    //Gets the height of the left side
    int leftHeight;
    if (cur.left == null) {
      leftHeight = -1;
    } else {
      leftHeight = cur.left.height;
    }

    //Gets the height of the right side
    int rightHeight;
    if (cur.right == null) {
      rightHeight = -1;
    } else {
      rightHeight = cur.right.height;
    }
    return leftHeight - rightHeight;
  }


  /*** Do not change this function's name or modify its code. ***/
  @Override
  public String toString() {
    return BinaryTreePrinter.printBinaryTree(root);
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

  /**
   * Inner node class, each holds a key (which is what we sort the
   * BST by) as well as a value. We don't need a parent pointer as
   * long as we use recursive insert/remove helpers.
   *
   * @param <K> Type for keys.
   * @param <V> Type for values.
   **/
  private static class Node<K, V> implements BinaryTreeNode {
    Node<K, V> left;
    Node<K, V> right;
    K key;
    V value;
    int height;

    // Constructor to make node creation easier to read.
    Node(K k, V v) {
      // left and right default to null
      key = k;
      value = v;
      height = 0;
    }



    @Override
    public String toString() {
      return key + ":" + value;
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
}
