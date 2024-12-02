# Discussion
## Part 1: Benchmarking
    hotel_california:
    Benchmark                  Mode  Cnt  Score   Error  Units
    JmhRuntimeTest.arrayMap    avgt    2  0.169          ms/op
    JmhRuntimeTest.avlTreeMap  avgt    2  0.120          ms/op
    JmhRuntimeTest.bstMap      avgt    2  0.132          ms/op
    JmhRuntimeTest.treapMap    avgt    2  0.157          ms/op

    federalist01:
    Benchmark                  Mode  Cnt  Score   Error  Units
    JmhRuntimeTest.arrayMap    avgt    2  1.514          ms/op
    JmhRuntimeTest.avlTreeMap  avgt    2  0.681          ms/op
    JmhRuntimeTest.bstMap      avgt    2  0.623          ms/op
    JmhRuntimeTest.treapMap    avgt    2  0.820          ms/op

    moby_dick:
    Benchmark                  Mode  Cnt  Score   Error  Units
    JmhRuntimeTest.arrayMap    avgt    2  1.465          ms/op
    JmhRuntimeTest.avlTreeMap  avgt    2  0.635          ms/op
    JmhRuntimeTest.bstMap      avgt    2  0.614          ms/op
    JmhRuntimeTest.treapMap    avgt    2  0.840          ms/op

    pride_and_prejudice:
    Benchmark                  Mode  Cnt  Score   Error  Units
    JmhRuntimeTest.arrayMap    avgt    2  1.540          ms/op
    JmhRuntimeTest.avlTreeMap  avgt    2  0.654          ms/op
    JmhRuntimeTest.bstMap      avgt    2  0.623          ms/op
    JmhRuntimeTest.treapMap    avgt    2  0.817          ms/op


    From my observation, I noticed that the avlTreeMaps and BST
    are the fatest, which makes sense as it is the most balanced tree
    as it constantly balancing itself, which is an O(1) time 
    complexity. The time it takes to add therefore is O(lg(n)).
    The next up is the bstMap, which makes sense that it's roughly 
    O(lg(n)) especially with larger data sets as we are using the 
    entire english language, and the starting letter and the next
    letter is mostly random from word to word. This would mean
    that our set is very close if not balanced especially with a
    larger data set. Since we don't preform rotations, it makes sense
    that it is slightly faster than AVL trees; however, this is only
    for large data sets. If we take note of hotel california, it has a
    faster AVL tree, and I believe that since there are such few words,
    and the song is very repetitive, it makes sense that the avl tree would
    be faster. However, in general, the BST and AVL tree are comparable
    in terms of speed. The second to last score/time is treapMap.

    This treamp is only based on observations that it has seen, 
    but there is no guarentee that it would be either O(lg(n)).
    In worst case, it could very well be O(n). This makes it 
    significantly slower than BST and AVL, while also faster than
    array Map. This also makes sense as common words like "the" could
    be placed near the bottom, which makes the tree significantly most
    costly when it comes to time as it has to searh to the bottom of 
    the tree for words that much be more common. Once again, if the data
    set is smaller like in hotel_california, the running time becomes 
    comparable, but when comparing it to a big set, it becomes a lot worse.

    The last one is the array map, which makes sense as we have 
    a large sample size and modifying this array takes O(n) times,
    which only becomes more apparent with a larger data set (i.e, 
    not hotel california). It does make sense that it would take 
    the most time. It is important to note that the most common
    words could be near the front due to the frequency of certain
    common words like "the" or "a", which may increase the access
    of these words compare to Treap, but maintain this is still O(n),
    which means that it is significantly slower.

## Part 2: Selection Problem
    Note: Worst case n = k
    Strategy 1
        This is correct. I believe this is correct because it is in a binary max heap, which means that 
        the first value will always be the "best" or highest in value in any given time. This means that
        when we removfe the best value, it will swap with the last value, and that value will sink/swap
        until we have a next "best" or highest on the top. This means that when we remove k elements, we 
        remove the kths best element before the heap was changed.

        Time Complexity:
            Building the heap - O(n)
            Removing 1 elements - O(lg(n))
            Removing k (nth in worst case) elements - O(nlg(n))
            Overall - O(n * lg(n))
        Space Complexity:
            Store all elements - O(n)
            Overall - O(n)

    Strategy 2
        This is correct. I believe this will work as we are replacing the worst value
        n-k times. By removing n-k times of the "best" or lowest values. Therefore, after m-k
        operations, we have a heap the size of k with kth value being the top value. Therefore, 
        kth value should be at the top or in the "best" position. So we efficiently jsut removed until,
        we reach the kth value.

        Time Complexity:
            Building the heap - O(k), but worst case k <= n, so O(n)
            Removing 1 elements - O(lg(n))
            Removing n-k (worst case k = 0, so n) elements - O(n * lg(k))
            Overall - O(n * lg(n))
        Space Complexity:
            Store all elements - O(k), but since k <= n, it's O(n) in worst case
            Overall - O(n)
    
    


