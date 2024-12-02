package hw3;

import hw3.search.MoveToFrontLinkedSet;
import hw3.search.Set;

/**
 * Instantiate the MoveToFrontLinkedSetTest to test.
 */
public class MoveToFrontLinkedSetTest extends SetTest {
  @Override
  protected Set<String> createUnit() {
    return new MoveToFrontLinkedSet<>();
  }
}