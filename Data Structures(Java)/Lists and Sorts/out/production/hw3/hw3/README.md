# Discussion

## PART I: MeasuredIndexedList Iterator
    
I believe that iterating over the MeasuredIndexedList should count one once for access, but not any for
mutations as we traverse the list once, but we would not be able to mutate this list. From a design perspective,
the point of having a MeasuredIndexedList is to see the amount of times that we access or mutate this list
with an option to reset; therefore, if we traverse over the entire list, I would argue that we are accessing
this list in one continuous session, but not modifying it. We cannot directly override the next() and 
hasNext() as they are both within a private iterator class, which means that this class is inherited, but
we cannot access it due to it being private. If we wanted these methods, we would have to create a separate
iterator class.

## PART II: Profiling Sorting Algorithms

There are discrepancy within the main test file. At first, the data one is that within descending data seems
wrong, it goes from 9980 to 998 or like 9990 to 999. This is weird at first, but looking at the data it behaves 
as if there weren't that those special integers, while ascending (which has "correct" data) has wrong data. This 
lead me to seeing that the data is parsed into strings, which would make the data for descending correct in its 
sorting, while ascending wrong as 999 > 9989 in strings. This also means that 20 > 100, which will mess up the 
ascending. However, if we change it to integers, this would cause descending to be incorrect.

## PART III: Analysis of Selection Sort

    Total:
    public static void selectionSort(int[] a) {
        int max, temp;
        for (int i = 0; i < a.length - 1; i++) { // 1 + (N) + (N) + (N - 1) = 3N
            max = i; // 1 * (N - 1) = N - 1
            for (int j = i + 1; j < a.length; j++) { // (2 + (N/2) + N/2) * (N - 1) = (N + 2) * (N - 1)
                if (a[j] > a[max]) { // (1) * (N/2) * (N - 1) = N^2/2 - N /2
                    max = j; // (1) * (N/2) * (N - 1) = N^2/2 - N /2
                }
            }
            temp = a[i]; // (1) * (N - 1) = (N - 1)
            a[i] = a[max]; // (1) * (N - 1) = (N - 1)
            a[max] = temp; // (1) * (N - 1) = (N - 1)
        }
    }
    
    Comparisons C(N) (Only a[j] > a[max] and the for loops):
    
    Line 3
    #(i < a.length - 1) = N as it runs until it reaches N - 1, so i will be 0, 1, 2 ... N - 2 (before break)
                        = N - 1 (0 is a single time too) + 1 (for final check to break out of loop)
                        = N
    
     Line 5
    #(j < a.length) = Inner loop runs N/2 + 1 as i is incrementing so (N(max) + 0 (min))/2 = N/2 + 1 (for final check)
                    = Multiply this by the times this is run which is N - 1
                    = (N/2 + 1) * (N - 1)

    Line 6
    #(a[j] > a[max]) = inner loop times * out loop times
                     = (N/2) * (N - 1) = N^2/2 - N/2

    No other comparisons made on any other lines

    C(N) = #(i < a.length - 1) + #(j < a.length) + #(a[j] > a[max])
         = (N) + ((N/2 + 1) * (N - 1)) + (N^2/2 - N/2)
         = (N) + (N^2/2 + N/2 - 1) + (N^2/2 - N/2)
         = N^2 + N - 1
    
    Assignments A(N) (In each for loop int i/j = variable and i/j++. Also general assignments):

    Line 3
    #(int i = 0) = This runs 1 time
    #(i++) = This gets incremented until i < a.length - 1, so i = 0, 1, 2 ... N - 1
           = This means i++ is equal to N-1
    #(Line 3) = #(int i = 0) + #(i++) = 1 + (N - 1) = N

    Line 4
    #(max = i) = # of times run through outer loop = N - 1

    Line 5
    #(int j = i + 1) = This assigns 1 thing, so it's 1
    #(j++) = This gets incremented until j < a.length, so j = 1, 2, 3 ... N
           = Inner loop runs N/2 + 1 as i is incrementing so (N(max) + 0 (min))/2 = N/2 + 1 (for final check)
           = This means j++ is equal to N/2
    #(Line 5) = (#(int j = i + 1) + #(j++)) * (#Outer Loop)
              = (1 + N/2) * (N - 1)

    Line 7
    #(max = j) = inner loop times * outer loop times
               = (N/2) * (N - 1) = N^2/2 - N/2

    Line 10
    #(temp = a[i]) = Times of outer loop
                   = N - 1
    
    Line 11
    #(a[i] = a[max]) = Times of outer loop
                     = N - 1

    Line 12
    #(a[max] = temp) = Times of outer loop
                     = N - 1

    Note: For shorthand, #(swap) = #(Line 10-12(inclusive)) = 3(N-1)
    
    No other assignments made on any other lines

    A(N) = #(int i = 0 and i++) + #(max = i) + #(int j = i + 1 and j++) + #(max = j) + #(swap)
         = (N) + (N - 1) + ((1 + N/2) * (N - 1)) + (N^2/2 - N/2) + 3(N - 1)
         = (2N - 1) + (N^2/2 + N/2 - 1) + (N^2/2 - N/2) + (3N - 3)
         = (N^2 + 2N - 1) + (3N - 3)
         = N^2 + 5N - 5


## PART IV: Reflecting on Search Heuristics
    
The removal of an object help achieve the goals of these heuristic as the order is maintain after the
removal of the object. Technically, it enhances the performance of the has operation (in many, but not all cases)
as there are fewer elements to search from. For example, once we remove and object, it becomes easier to find any other object
within the list. It also helps that if that object was searched for before, it can remove it faster as
it can locate it much faster. This also allows the data structure to be maintain and thus be better at searching
for these objects. 

## PART V: Profiling Search Heuristics

I did find the result that I want comparing measurable differences of the move-to-front heuristic 
and transpose-array-set heuristic compared with linkedSet and ArraySet respectfully. I found, in fact,
that the heuristics score was lower/took shorter amount of time compared to that of its counterparts. 
I used a large data set and a large amount of "has"/search function on the random smaller number to the point 
where it would make a noticeable difference as the number should be moved more in front by this point, and 
therefore easier to call. I used random number to mimic an actual user, but it could be just a single number.
(i.e. I have tested it with single number and it was correct)