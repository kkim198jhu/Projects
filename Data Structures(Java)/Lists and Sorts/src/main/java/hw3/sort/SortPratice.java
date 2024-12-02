package hw3.sort;

public class SortPratice<T extends Comparable<T>>{


  public void sort(IndexedList<T> indexedList) {
    selectionSortR(indexedList, 0);
  }

  // is a less than b?
  public boolean less(T item, T item2) {
    return item.compareTo(item2) > 0;
  }


  public void BubbleSortI(IndexedList<T> indexedList) {
    boolean swapped = false;
    for (int i = 0; i < indexedList.length(); i++) {
      swapped = false;
      for (int j = 0; j < indexedList.length(); j++) {
        if (less(indexedList.get(j),indexedList.get(j + 1))) {
          T temp = indexedList.get(j);
          indexedList.put(j, indexedList.get(j + 1));
          indexedList.put(j + 1, temp);
          swapped = true;
        }
      }
      if (!swapped) {
        break;
      }
    }
  }

  public void BubbleSortR(IndexedList<T> indexedList, int position) {
    if (position == indexedList.length()) {
      return;
    }

    boolean swapped = false;
    for (int j = 0; j < indexedList.length() - position - 1; j++) {
      if (less(indexedList.get(j),indexedList.get(j + 1))) {
        T temp = indexedList.get(j);
        indexedList.put(j, indexedList.get(j + 1));
        indexedList.put(j + 1, temp);
        swapped = true;
      }
    }

    if (!swapped) {
      return;
    } else {
      BubbleSortR(indexedList, position + 1);
    }
  }

  public void InsertionSortI(IndexedList<T> indexedList) {
    int index;
    for (int i = 0; i < indexedList.length(); i++) {
      T max = indexedList.get(i);
      index = i;
      for (int j = i + 1; j < indexedList.length(); j++) {
        if (less(max, indexedList.get(j))) {
          index = j;
          max = indexedList.get(j);
        }
      }
      indexedList.put(index, indexedList.get(i));
      indexedList.put(i, max);
    }
  }

  public void InsertionSortR(IndexedList<T> indexedList, int position) {
    if (position == indexedList.length()) {
      return;
    }
    T max = indexedList.get(position);
    int index = position;
    for (int j = position + 1; j < indexedList.length(); j++) {
      if (less(max, indexedList.get(j))) {
        max = indexedList.get(j);
        index = j;
      }
    }

    indexedList.put(index, indexedList.get(position));
    indexedList.put(position, max);
    InsertionSortR(indexedList, position + 1);
  }

  public void selectionSortI (IndexedList<T> indexedList) {
    for (int i = 0; i < indexedList.length(); i++) {
      for (int j = i; j > 0; j--) {
        if (less(indexedList.get(j - 1), indexedList.get(j))) {
          T temp = indexedList.get(j);
          indexedList.put(j, indexedList.get(j - 1));
          indexedList.put(j - 1, temp);
        }
      }
    }
  }

  public void selectionSortR (IndexedList<T> indexedList, int position) {
    if (position == indexedList.length()) {
      return;
    }
    for (int j = position; j > 0; j--) {
      if (less(indexedList.get(j - 1), indexedList.get(j))) {
        T temp = indexedList.get(j);
        indexedList.put(j, indexedList.get(j - 1));
        indexedList.put(j - 1, temp);
      }
    }
    selectionSortR(indexedList, position + 1);
  }

  /**
   * Main Test for a sortPractice.*/
  public static void main(String[] args) {
    int size = 20;
    IndexedList<String> list = new ArrayIndexedList<>(size, "A");
    list.put(1, "E");
    list.put(2,"D");
    list.put(3,"C");
    list.put(4,"B");
    list.put(5,"A");
    list.put(6,"C");
    list.put(7,"E");
    list.put(8,"G");
    list.put(9,"Z");
    list.put(10,"K");
    list.put(11,"L");
    for (String ele: list) {
      System.out.print(ele + " ");
    }
    System.out.println();

    SortPratice<String> sorter = new SortPratice<String>();
    sorter.sort(list);
    for (String ele: list) {
      System.out.print(ele + " ");
    }
  }
}
