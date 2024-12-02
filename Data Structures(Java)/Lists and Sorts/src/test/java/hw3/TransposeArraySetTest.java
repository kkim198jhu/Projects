package hw3;

import hw3.search.Set;
import hw3.search.TransposeArraySet;

/**
 * Instantiate the TransposeArraySetTest to test.
 */
public class TransposeArraySetTest extends SetTest {

  @Override
  protected Set<String> createUnit() {
    return new TransposeArraySet<>();
  }
}