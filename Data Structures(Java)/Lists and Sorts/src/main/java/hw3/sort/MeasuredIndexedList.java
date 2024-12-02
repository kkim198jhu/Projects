package hw3.sort;

import exceptions.IndexException;

/**
 * An ArrayIndexedList that is able to report the number of
 * accesses and mutations, as well as reset those statistics.
 *
 * @param <T> The type of the array.
 */
public class MeasuredIndexedList<T> extends ArrayIndexedList<T>
    implements Measured<T> {
  int access;
  int mutate;

  /**
   * Constructor for a MeasuredIndexedList.
   *
   * @param size         The size of the array.
   * @param defaultValue The initial value to set every object to in the array..
   */
  public MeasuredIndexedList(int size, T defaultValue) {
    super(size, defaultValue);
    //Declares our counting variables
    access = 0;
    mutate = 0;
  }

  @Override
  public int length() {
    return super.length();
  }

  @Override
  public T get(int index) throws IndexException {
    //Updates access and returns the get method
    T data = super.get(index);
    access++;
    return data;
  }

  @Override
  public void put(int index, T value) throws IndexException {
    //Updates mutate and returns the get method
    super.put(index, value);
    mutate++;
  }

  @Override
  public void reset() {
    //Resets the counting fields back to 0
    access = 0;
    mutate = 0;
  }

  @Override
  public int accesses() {
    return access;
  }

  @Override
  public int mutations() {
    return mutate;
  }

  @Override
  public int count(T value) {
    int count = 0;
    //Runs through each element and gets the value
    for (int i = 0; i < this.length(); i++) {
      //Checks if the value is equal to the element at the position
      if (this.get(i).equals(value)) {
        count++;
      }
    }
    return count;
  }

}
