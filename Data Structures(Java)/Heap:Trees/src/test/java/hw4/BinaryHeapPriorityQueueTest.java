package hw4;

public class BinaryHeapPriorityQueueTest extends PriorityQueueTest {

  @Override
  protected PriorityQueue<Integer> createQueue() {
    return new BinaryHeapPriorityQueue<>();
  }
}
