package hw5;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OpenAddressingHashMap<K, V> implements Map<K, V> {
  private HashNode<K, V> tombstone;
  private HashNode<K, V>[] map;
  private int numElements;
  private int totalElements;
  private final int[] primes;
  private int currentHash;


  /**
   * Constructor of a OpenAddressingHashMap.
   */
  public OpenAddressingHashMap() {
    map = (HashNode<K, V>[]) new HashNode[5];
    numElements = 0;
    primes = new int[] {3, 5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717, 51437, 102877, 205759,
        411527, 823117, 1646237,3292489, 6584983, 13169977};
    currentHash = 1;
    totalElements = 0;
    tombstone = new HashNode(null,null);
  }

  /**
   * Gets a new hashcode for double hashing.
   *
   * @param k the key value provided
   *          pre: Not null
   * @return A new hash code
   *          post: Can't be 0
   */
  private int hash(K k) {
    int newHashFactor = primes[currentHash - 1] - 1;
    return newHashFactor - ((k.hashCode()) % newHashFactor);
  }

  @Override
  public void insert(K k, V v) throws IllegalArgumentException {
    if (k == null || has(k)) {
      throw new IllegalArgumentException();
    }

    //Finds the new index
    int index = findOpen(k);

    //Can't find index
    if (index == -1) {
      throw new IllegalArgumentException();
    }

    //Increases only if it isn't tombstone
    if (map[index] != tombstone) {
      totalElements++;
    }

    //This sets the new value
    map[index] = new HashNode<>(k, v);
    numElements++;


    // Use totalElements to decide rehashing
    if ((double) totalElements / map.length > 0.75) {
      rehash();
    }
  }

  /**
   * Insert a new node to the map.
   *
   * @param n The new Hash Node
   *          pre: n isn't null or tombstone
   * @param newMap The new hashMap.
   */
  private void insert(HashNode<K, V> n, HashNode<K, V>[] newMap) {
    //Gets the new index
    int index;
    int originalIndex = Math.abs(n.key.hashCode()) % newMap.length;
    int newHashFactor = hash(n.key);
    //Loops through until it finds an open spot
    for (int i = 0; i < newMap.length; i++) {
      index = (originalIndex + newHashFactor * i) % newMap.length;
      if (newMap[index] == null) {
        newMap[index] = n;
        break;
      }
    }
  }


  /**
   * Finds the open spot.
   * Pre: index starting position is already checked
   *
   * @param k is the key to make sure it isn't already mapped
   *           pre: k != null
   * @return The index of an open spot and -1 if it can't be mapped
   */
  private int findOpen(K k) throws IllegalArgumentException {
    int index;
    int newHashFactor = hash(k);
    int originalIndex = Math.abs(k.hashCode()) % map.length;
    //Runs through the entire list with double hashing
    for (int i = 0; i < map.length; i++) {
      //Tries to find index with double hashing
      index = (originalIndex + newHashFactor * i) % map.length;

      //If found, returns the index
      if (map[index] == null || map[index] == tombstone) {
        return index;
      }
    }
    return -1;
  }

  /**
   * Rehash the size of the table.
   */
  private void rehash() {
    //Gets the new Hash Size
    currentHash++;
    int nextPrime = primes[currentHash];
    int count = 0;

    //Creates a new Hash
    HashNode<K,V>[] newMap = (HashNode<K, V>[]) new HashNode[nextPrime];

    //Copies the array over
    for (int i = 0; i < map.length; i++) {
      if (map[i] != null && map[i] != tombstone) {
        insert(map[i], newMap);
        count++;
      }

      if (count == numElements) {
        break;
      }
    }

    //Sets the map to equal new Map
    map = newMap;

    totalElements = numElements;
  }


  @Override
  public V remove(K k) throws IllegalArgumentException {
    //If it is null, then throw error
    if (k == null) {
      throw new IllegalArgumentException();
    }

    //Get/Find the key associated with k
    int index = Math.abs(k.hashCode()) % map.length;
    int key = find(k, index);

    //Throws error if it isn't mapped
    if (key == -1) {
      throw new IllegalArgumentException();
    }

    //Get value and set tombstone
    V data = map[key].value;
    map[key] = tombstone;
    //Decrease numElements and return data
    numElements--;
    return data;
  }

  @Override
  public void put(K k, V v) throws IllegalArgumentException {
    //If null, throws exception
    if (k == null) {
      throw new IllegalArgumentException();
    }

    int index = Math.abs(k.hashCode()) % map.length;
    //If the map is either null or it is a tombstone, then throw exception
    if (map[index] == null || map[index] == tombstone) {
      throw new IllegalArgumentException();
    }

    //Returns the value associated with index
    int key = find(k, index);
    if (key == -1) {
      throw new IllegalArgumentException();
    } else {
      map[key].value = v;
    }
  }

  @Override
  public V get(K k) throws IllegalArgumentException {
    //If null, throws exception
    if (k == null) {
      throw new IllegalArgumentException();
    }

    int index = Math.abs(k.hashCode()) % map.length;
    //If the map is either null or it is a tombstone, then throw exception
    if (map[index] == null || map[index] == tombstone) {
      throw new IllegalArgumentException();
    }

    //Returns the value associated with index
    int key = find(k, index);
    // If it isn't mapped, throws error
    if (key == -1) {
      throw new IllegalArgumentException();
    } else {
      return map[key].value;
    }

  }

  /**
   * Get the index associated with a key.
   *
   * @param k The key.
   * @param index The current index
   * @return The index that was associated with k or -1 if it doesn't exist.
   */
  private int find(K k, int index) {
    int increaseIndex = 0;
    int originalIndex = index;
    int newHashFactor = hash(k);

    //Runs through the entire list with double hashing
    for (int i = 0; i < map.length; i++) {
      //Tries to find index with double hashing
      index = (originalIndex + newHashFactor * i) % map.length;

      //If at empty, is at empty return -1
      if (map[index] == null) {
        return -1;
      }

      //If found, return the index
      if (map[index] != tombstone && k.equals(map[index].key)) {
        return index;
      }

    }
    //Return -1 if not mapped
    return -1;
  }

  @Override
  public boolean has(K k) {
    //If null, throws exception
    if (k == null) {
      return false;
    }

    int index = Math.abs(k.hashCode()) % map.length;
    //If the map is either null or it is a tombstone, then throw exception
    if (map[index] == null || map[index] == tombstone) {
      return false;
    }

    //Returns the value associated with index
    int key = find(k, index);

    // If it isn't mapped, throws error
    return key != -1;
  }

  @Override
  public int size() {
    return numElements;
  }

  @Override
  public Iterator<K> iterator() {
    return new OpenIterator();
  }

  // Iterative in-order traversal over the keys
  private class OpenIterator implements Iterator<K> {
    int index;
    int count;

    OpenIterator() {
      index = 0;
      count = 0;
    }

    @Override
    public boolean hasNext() {
      return count < numElements;
    }

    @Override
    public K next() throws NoSuchElementException {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      K data = null;

      //Runs through until finds it
      for (int i = index; i < map.length; i++) {
        if (map[index] != null && map[index] != tombstone) {
          data = map[index].key;
          count++;
          break;
        }
        index++;
      }

      index++;
      return data;
    }
  }

  /**
   * Inner node class, each holds a key as well as a value.
   * @param <K> = key value
   * @param <V> = Value value
   **/
  private static class HashNode<K, V> {
    K key;
    V value;

    /**
     * Constructor of a HashNode.
     *
     * @param k The key.
     * @param v The value
     */
    private HashNode(K k, V v) {
      // Assign Values
      key = k;
      value = v;
    }

  }
}