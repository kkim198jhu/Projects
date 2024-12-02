package hw3.sort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A driver program to run SortingAlgorithm(s) on various benchmark data sets.
 * MeasuredArray must have been correctly implemented.
 */
@SuppressWarnings({"Checkstyle", "checkstyle:ClassFanOutComplexity"})
public class SortingAlgorithmDriver {

  public static int SIZE = 22;     // Number of data values for experiment.
  private static final double NANOS = 1e9;    // How many nanoseconds in a second.
  private StringBuilder report;

  // List of all benchmark data.
  // Add to this list when you make a new benchmark data set.
  // Pre: data files must be stored in the "data" folder in "source resources"
  private List<File> getDataFiles() {
    URL url = Thread.currentThread().getContextClassLoader().getResource("");
    String path = url.getPath().replace("%20", " ")
        .replace("classes", "resources");
    List<File> dataFiles = new ArrayList<>();
    dataFiles.add(new File(path + "ascending.data"));
    dataFiles.add(new File(path + "descending.data"));
    dataFiles.add(new File(path + "random.data"));
    return dataFiles;
  }

  /**
   * Get the sorting algorithms to be used in the experiment.
   * @return a list of sorting algorithms.
   */
  protected List<SortingAlgorithm<String>> getSortingAlgorithms() {
    // Add to this list when you make a new sorting algorithm.
    // Your sorting algorithm must implement SortingAlgorithm.
    List<SortingAlgorithm<String>> sorts = new ArrayList<>();
    sorts.add(new NullSort<>());
    sorts.add(new GnomeSort<>());
    sorts.add(new SelectionSort<>());
    sorts.add(new BubbleSort<>());
    sorts.add(new InsertionSort<>());
    return sorts;
  }

  // Read data from dataFile and store it in a List.
  // Pre: each line in dataFile contains one data value.
  // Post: each element in the returned list contains one data value.
  private List<String> readData(File dataFile) throws IOException {
    FileReader fr = new FileReader(dataFile);
    BufferedReader br = new BufferedReader(fr);
    List<String> data = new ArrayList<>();
    String line = br.readLine();
    while (line != null) {
      data.add(line);
      line = br.readLine();
    }
    br.close();
    return data;
  }

  /**
   * Convert a List to a (Measured) IndexedList.
   * @param list list to convert
   * @param toCopy number of elements to copy
   *               Pre: toCopy <= list.size().
   * @return a Measured IndexedList
   */
  protected Measured<String> toArray(List<String> list, int toCopy) {
    Measured<String> measured = new MeasuredIndexedList<>(toCopy, null);
    IndexedList<String> indexedList = (IndexedList<String>) measured;
    for (int i = 0; i < toCopy; i++) {
      indexedList.put(i, list.get(i));
    }
    return measured;
  }

  // Update report by adding runtime and operation statistics to it.
  private void updateReport(String dataFile, String algo,
                            Measured<String> array,
                            double time) {
    if (report == null) {
      initReport();
    }
    report.append(
        String.format("%-17s %-16s %-8b %,-12d %,-12d %,-12f\n",
            dataFile, algo, isSorted((IndexedList<String>) array),
            array.accesses(), array.mutations(), time));
  }

  // Initialize the report by adding a header to it.
  private void initReport() {
    report = new StringBuilder();
    report.append(
        String.format("%-17s %-16s %-8s %-12s %-12s %-12s\n\n",
            "Data file", "Algorithm", "Sorted?",
            "Accesses", "Mutations", "Seconds"));
  }

  // Run sortAlgo on data in dataFile.
  // Update report with runtime and operation statistics.
  private void runSortAlgoOnData(SortingAlgorithm<String> sortAlgo,
                                 File dataFile) throws IOException {
    Measured<String> data = toArray(readData(dataFile), SIZE);
    data.reset();

    long before = System.nanoTime();
    sortAlgo.sort((IndexedList<String>) data);
    long after = System.nanoTime();

    double seconds = (after - before) / NANOS;
    updateReport(dataFile.getName(), sortAlgo.name(), data, seconds);
  }

  /**
   * Checks the elements in arr are sorted in ascending order.
   *
   * @param arr input array to check if it is sorted.
   * @param <T> base type of array (must be Comparable)
   * @return true if arr is sorted.
   */
  public static <T extends Comparable<T>> boolean isSorted(IndexedList<T> arr) {
    // Use iterator so to not affect the operation count in arr.
    Iterator<T> it = arr.iterator();
    T prev = it.hasNext() ? it.next() : null;
    while (it.hasNext()) {
      T current = it.next();
      if (prev.compareTo(current) > 0) {
        return false;
      }
      prev = current;
    }
    return true;
  }

  /**
   * Experiment with sorting algorithms.
   */
  public void experiment() {
    try {
      List<File> dataFiles = getDataFiles();
      List<SortingAlgorithm<String>> algorithms = getSortingAlgorithms();

      for (File dataFile : dataFiles) {
        for (SortingAlgorithm<String> algo : algorithms) {
          runSortAlgoOnData(algo, dataFile);
        }
        report.append("\n");
      }

      System.out.println(report.toString());

    } catch (FileNotFoundException e) {
      System.out.println("Unable to find a data file");
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("Unable to read from a data file");
      e.printStackTrace();
    }
  }

  /**
   * Execution starts here.
   *
   * @param args command-line arguments (not used here!).
   */
  public static void main(String[] args) {
    SortingAlgorithmDriver driver = new SortingAlgorithmDriver();
    driver.experiment();
  }

}