package hw3;

import hw3.search.*;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.Random;

@State(Scope.Benchmark)
public class JmhRuntimeTest {

  private List<Integer> data;
  // You may add more private fields in here.

  @Setup(Level.Invocation)
  public void setUp() {
    // You may update this method.
    data = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      data.add(i);
    }
    Collections.shuffle(data);
  }

  // Experiment: perform a sequence of operations on an implementation of Set ADT.
  private void experiment(Set<Integer> set) {
    // You may update this method.
    for (Integer num : data) {
      set.insert(num);
    }
    Random rand = new Random();
    for (int i = 0; i < 1000000; i++) {
      set.has(rand.nextInt(100));
    }

  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 1)
  @Measurement(iterations = 2)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void linkedSet() {
    // Do not change this method.
    Set<Integer> set = new LinkedSet<>();
    experiment(set);
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 1)
  @Measurement(iterations = 2)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void moveToFront() {
    // Do not change this method.
    Set<Integer> set = new MoveToFrontLinkedSet<>();
    experiment(set);
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 1)
  @Measurement(iterations = 2)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void arraySet() {
    // Do not change this method.
    Set<Integer> set = new ArraySet<>();
    experiment(set);
  }

  @Benchmark
  @Fork(value = 1, warmups = 1)
  @Warmup(iterations = 1)
  @Measurement(iterations = 2)
  @OutputTimeUnit(TimeUnit.MILLISECONDS)
  @BenchmarkMode(Mode.AverageTime)
  public void transposeSequence() {
    // Do not change this method.
    Set<Integer> set = new TransposeArraySet<>();
    experiment(set);
  }
}
