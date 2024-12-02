package hw5;

@SuppressWarnings("All")
public class JdkHashMapTest extends MapTest {
  @Override
  protected Map<String, String> createMap() {
    return new JdkHashMap<>();
  }
}