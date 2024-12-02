package hw3.sort;

/**
 * An object that can provide measured statistics about itself.
 *
 * <p> We count operations that access or mutate the object in question.
 * Access doesn't modify the object itself, a mutation does.
 * For example, if we make an IndexedList measured, get() would be an
 * access whereas put() would be a mutation. </p>
 *
 * @param <T> The type the object holds
 */
public interface Measured<T> {
  /**
   * Reset the statistics collected so far.
   * Post: accesses() == mutations() == 0.
   */
  void reset();

  /**
   * Accesses so far.
   *
   * @return Number of accesses so far.
   *          Inv: accesses() >= 0.
   */
  int accesses();

  /**
   * Mutations so far.
   *
   * @return Number of mutations so far.
   *         Inv: mutations() >= 0.
   */
  int mutations();

  /**
   * Get the count of occurrences of value in the data.
   *
   * @param value The value to count.
   * @return The count of occurrences of value in the data.
   */
  int count(T value);
}