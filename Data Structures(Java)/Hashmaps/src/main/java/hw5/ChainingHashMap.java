package hw5;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ChainingHashMap<K, V> implements Map<K, V> {
  private HashLinkedList<K, V>[] map;
  private int numElements;
  private final int[] primes;
  private int filled;
  private int currentHash;


  /**
   * Constructor of a OpenAddressingHashMap.
   */
  public ChainingHashMap() {
    map = (HashLinkedList<K, V>[]) new HashLinkedList[5];
    numElements = 0;
    primes = new int[]{5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717, 51437, 102877, 205759,
        411527, 823117, 1646237,3292489, 6584983, 13169977};
    filled = 0;
    currentHash = 0;
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }
    //Adds the value to the list
    int index = Math.abs(k.hashCode()) % map.length;
    if (map[index] == null) {
      map[index] = new HashLinkedList<>(k, v);
      filled++;
    } else {
      map[index].add(k, v);
    }

    numElements++;
    //Rehash it
    if ((float) filled / map.length > 0.75) {
      rehash();
    }
  }

  /**
   * Helper function and inserts a linked list into new spots.
   *
   * @param oldList The old list provided
   *                 pre: Not null
   * @param newMap The new map
   * @param newFilled The fill of the new map
   * @return The amount filled
   */
  private int insertList(HashLinkedList<K, V> oldList, HashLinkedList<K,V>[] newMap, int newFilled) {
    HashNode<K, V> cur = oldList.head;
    HashNode<K, V> nextNode;

    //Traverse though the entire list
    while (cur != null) {
      nextNode = cur.next;
      int index = Math.abs(cur.key.hashCode()) % newMap.length;

      //Checks if it is a new part of the list
      if (newMap[index] == null) {
        newMap[index] = new HashLinkedList<>(cur);
        newFilled++;
      } else {
        newMap[index].add(cur);
      }
      cur = nextNode;
    }
    return newFilled;
  }

  /**
   * Rehash the size of the table.
   */
  private void rehash() {
    //Gets the new Hash Size
    currentHash++;
    int nextPrime = primes[currentHash];
    int count = 0;
    int newFilled = 0;

    //Creates a new Hash
    HashLinkedList<K,V>[] newMap = (HashLinkedList<K, V>[]) new HashLinkedList[nextPrime];

    //Copies the array over
    for (int i = 0; i < map.length; i++) {
      if (map[i] != null) {
        newFilled = insertList(map[i], newMap, newFilled);
        count++;
      }

      if (count == filled) {
        break;
      }
    }

    //Sets the map to equal new Map
    map = newMap;
    filled = newFilled;
  }

  @Override
  public V remove(K k) throws IllegalArgumentException {
    if (k == null) {
      throw new IllegalArgumentException();
    }

    //Finds and removes the data
    int index = Math.abs(k.hashCode()) % map.length;

    if (map[index] == null) {
      throw new IllegalArgumentException();
    }

    V data = map[index].remove(k);

    //Removes it if it is 0 elements
    if (map[index].size == 0) {
      map[index] = null;
      filled--;
    }

    //Returns the data
    numElements--;
    return data;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    //Checks if k is null
    if (k == null) {
      throw new IllegalArgumentException();
    }

    //Get and checks that the index is valid
    int index = Math.abs(k.hashCode()) % map.length;

    if (map[index] == null) {
      throw new IllegalArgumentException();
    }

    HashNode<K,V> node = map[index].find(k);
    //Checks that the node is in the list
    if (node == null) {
      throw new IllegalArgumentException();
    }

    node.value = v;
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    //Checks if k is null
    if (k == null) {
      throw new IllegalArgumentException();
    }

    //Get and checks that the index is valid
    int index = Math.abs(k.hashCode()) % map.length;

    if (map[index] == null) {
      throw new IllegalArgumentException();
    }

    HashNode<K,V> node = map[index].find(k);
    //Checks that the node is in the list
    if (node == null) {
      throw new IllegalArgumentException();
    }

    return node.value;
  }

  @Override
  public boolean has(K k) {
    //Checks if k is null
    if (k == null) {
      return false;
    }

    //Get and checks that the index is valid
    int index = Math.abs(k.hashCode()) % map.length;

    if (map[index] == null) {
      return false;
    }

    HashNode<K,V> node = map[index].find(k);
    //Checks that the node is in the list
    return node != null;
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<K> iterator() {
    return new ChainIterator();
  }

  private int[] getIndex() {
    int[] myList = new int[filled];
    int count = 0;
    for (int i = 0; i < map.length; i++) {
      if (map[i] != null) {
        myList[count] = i;
        count++;
      }
      if (count == filled) {
        break;
      }
    }
    return myList;
  }


  // Iterative in-order traversal over the keys
  private class ChainIterator implements Iterator<K> {
    int index;
    int[] keyList;
    HashNode<K,V> current;
    int total;

    ChainIterator() {
      index = 0;
      keyList = getIndex();
      current = null;
      total = 0;
    }

    @Override
    public boolean hasNext() {
      return total < numElements;
    }

    @Override
    public K next() throws NoSuchElementException {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      if (current == null) {
        current = map[keyList[index]].head;
        index++;
      }

      K data = current.key;
      current = current.next;
      total++;
      return data;
    }
  }

  /**
   * Inner node class, each holds a key as well as a value.
   * @param <K> = key value
   * @param <V> = Value value
   **/
  private static class HashNode<K, V> {
    final K key;
    V value;
    HashNode<K, V> next;

    /**
     * Constructor of a HashNode.
     *
     * @param k The key.
     * @param v The value
     */
    protected HashNode(K k, V v) {
      // Assign Values
      key = k;
      value = v;
      next = null;
    }
  }

  /**
   * Inner HashLinkedList class, each holds the linked list of each key value.
   * @param <K> = key value
   * @param <V> = Value value
   **/
  private static class HashLinkedList<K,V> {
    private HashNode<K, V> head;
    private int size;

    /**
     * Constructor of a HashLinkedList.
     *
     * @param k The key value
     * @param v The value
     */
    protected HashLinkedList(K k, V v) {
      // Assign Values
      head = new HashNode<K, V>(k, v);
      size = 1;
    }

    /**
     * Constructor of a HashLinkedList.
     *
     * @param newHead This is now the new head
     */
    protected HashLinkedList(HashNode<K, V> newHead) {
      // Assign Values
      head = newHead;
      newHead.next = null;
      size = 1;
    }


    /**
     * Insert a first node in the linked list.
     *
     * @param k Where K is the key data wanting to insert
     * @param v Where V is the value corresponding to key
     */
    private void addFirst(K k, V v) {
      //Adds it first
      HashNode<K, V> n = new HashNode<K,V>(k, v);
      n.next = head;
      head = n;
    }

    /**
     * Insert a node in the sorted linked list.
     *
     * @param k Where K is the key data wanting to insert
     * @param v Where V is the value corresponding to key
     */
    protected void add(K k, V v) throws IllegalArgumentException {
      if (head == null) { // Checks edge cases
        addFirst(k, v);
      } else {
        HashNode<K,V> cur = head;
        HashNode<K,V> prev = null;

        while (cur != null) { // Runs until cur == null
          if (cur.key.equals(k)) {
            throw new IllegalArgumentException();
          }

          prev = cur;
          cur = cur.next;
        }
        //Adds the node last
        HashNode<K,V> n = new HashNode<K,V>(k, v);
        n.next = cur;
        prev.next = n;
      }
      size++;
    }

    /**
     * Insert add node in the sorted linked list.
     *
     * @param newNode Where newNode is the node wanting to insert
     *                pre: It's not first & doesn't exist in list
     */
    protected void add(HashNode<K, V> newNode) {
      HashNode<K,V> cur = head;
      HashNode<K,V> prev = null;
      while (cur != null) { // Runs until cur == null\
        prev = cur;
        cur = cur.next;
      }
      //Adds the node last
      newNode.next = cur;
      prev.next = newNode;

      size++;
    }

    /**
     * Find a node for a given key.
     *
     * @param key Where K is the key data wanting to found
     * @return The value found or null if not found
     */
    protected HashNode<K, V> find(K key) {
      HashNode<K, V> cur = head;

      while (cur != null) {
        if (cur.key.equals(key)) {
          return cur;
        }
        cur = cur.next;
      }
      return null;
    }

    /**
     * Removes a node given the node that is wanted to be removed.
     *
     * @param k What the key of the node to be removed
     * @return The value removed or error if not found
     */
    private V remove(K k) throws IllegalArgumentException {
      HashNode<K, V> cur = head;
      HashNode<K, V> previous = null;

      // If the node to be removed is the head
      if (head.key.equals(k)) {
        return removeFirst();
      }

      // Iterates through all the nodes until finds the node to be removed
      while (cur != null && !(cur.key.equals(k))) {
        previous = cur;
        cur = cur.next;
      }

      //If it's not mapped, throws error
      if (cur == null) {
        throw new IllegalArgumentException();
      }

      //Sets the new value
      V data = cur.value;
      previous.next = cur.next;
      size--;
      return data;
    }

    /**
     * Removes a node at the first value.
     * @return The value of the remove
     */
    private V removeFirst() {
      V data = head.value;
      head = head.next; // Move head to the next node
      size--;
      return data;
    }
  }

}