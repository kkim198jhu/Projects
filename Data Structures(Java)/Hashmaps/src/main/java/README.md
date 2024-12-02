# Discussion
### Open Addressing Hash Map    
    Linear Probing:
        Time:
            JmhRuntimeTest.buildSearchEngine                                       apache.txt  avgt    2        150.216           ms/op
            JmhRuntimeTest.buildSearchEngine                                          jhu.txt  avgt    2          0.157           ms/op
            JmhRuntimeTest.buildSearchEngine                                       joanne.txt  avgt    2          0.055           ms/op
            JmhRuntimeTest.buildSearchEngine                                       newegg.txt  avgt    2         73.555           ms/op
            JmhRuntimeTest.buildSearchEngine                                    random164.txt  avgt    2         509.241          ms/op
            JmhRuntimeTest.buildSearchEngine                                         urls.txt  avgt    2          0.020           ms/op
        
        Space:
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            apache.txt  avgt    2   87407920.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               jhu.txt  avgt    2   17625756.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            joanne.txt  avgt    2   17674920.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            newegg.txt  avgt    2   89668408.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc         random164.txt  avgt    2  1210534272.000          bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc              urls.txt  avgt    2   16680308.000           bytes
    
    Quadratic Probing: (Note: apache doesn't proceed as quadratic probing throws error due to not finding open position)
        Time:
            JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.137           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.057           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          70.417           ms/op
            JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         492.789           ms/op
            JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.019           ms/op

        Space:
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    17713652.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    17144204.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    93627264.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2  1243083800.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    16804728.000           bytes

    Double Hashing:
        Time:
            JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         158.961           ms/op
            JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.154           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.058           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          76.504           ms/op
            JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         599.148           ms/op
            JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.021           ms/op

        Space:
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2    89528888.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    17699644.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    17693388.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    88485188.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2  1118229632.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    16757396.000           bytes



### Chaining Hash Map:
    Without ReHashing (Linked List):
        Time: 
            JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2       5135.386           ms/op
            JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2          0.174           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2          0.076           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2       2932.312           ms/op
            JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2     769244.183           ms/op
            JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2          0.019           ms/op
        Space:
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2  120159064.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2   17523244.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2   17568944.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2   83436192.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2  183058828.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2   17135712.000           bytes

    ReHashing (Linked List):
        Time:
            JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         132.682           ms/op
            JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.148           ms/op
            JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.055           ms/op
            JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2          71.037           ms/op
            JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2         464.734           ms/op
            JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.020           ms/op
        Space:
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2    87991968.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    17042820.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    17295716.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2    95748408.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            random164.txt  avgt    2   513081048.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    17039748.000           bytes
    
    ReHashing at greater load factor capacity (load factor = 90%) (Linked List)
        Time:   
            JmhRuntimeTest.buildSearchEngine                                          apache.txt  avgt    2         156.653           ms/op
            JmhRuntimeTest.buildSearchEngine                                             jhu.txt  avgt    2           0.145           ms/op
            JmhRuntimeTest.buildSearchEngine                                          joanne.txt  avgt    2           0.056           ms/op
            JmhRuntimeTest.buildSearchEngine                                          newegg.txt  avgt    2          71.935           ms/op
            JmhRuntimeTest.buildSearchEngine                                       random164.txt  avgt    2         604.579           ms/op
            JmhRuntimeTest.buildSearchEngine                                            urls.txt  avgt    2           0.019           ms/op
        
        Space:
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               apache.txt  avgt    2    96873292.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                  jhu.txt  avgt    2    17210596.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               joanne.txt  avgt    2    17334336.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc               newegg.txt  avgt    2    88094628.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc            random164.txt  avgt    2   514853296.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                 urls.txt  avgt    2    16791200.000           bytes

    ReHashing at lower load factor capacity (load factor = 60%) (Linked List)
        Time:   
            JmhRuntimeTest.buildSearchEngine                                                         apache.txt  avgt    2         138.478           ms/op
            JmhRuntimeTest.buildSearchEngine                                                            jhu.txt  avgt    2           0.140           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         joanne.txt  avgt    2           0.057           ms/op
            JmhRuntimeTest.buildSearchEngine                                                         newegg.txt  avgt    2          74.494           ms/op
            JmhRuntimeTest.buildSearchEngine                                                      random164.txt  avgt    2         516.629           ms/op
            JmhRuntimeTest.buildSearchEngine                                                           urls.txt  avgt    2           0.019           ms/op

        Space:
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              apache.txt  avgt    2    93758092.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                 jhu.txt  avgt    2    17982968.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              joanne.txt  avgt    2    17258364.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                              newegg.txt  avgt    2    97878348.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                           random164.txt  avgt    2  1398168864.000           bytes
            JmhRuntimeTest.buildSearchEngine:+c2k.gc.maximumUsedAfterGc                                urls.txt  avgt    2    16911852.000           bytes

    
### Discussion:
    The chain hash map search is significantly better than the open addressing hash map in both memory and speed.
    I tried many strategies as we see here. For the Open Addressing Hash Map, I tried linear probing, quadratic probing,
    and double hasing probing. Overall, the linear probing was the fastest, which was interesting as I thought that due 
    to clustering, it would be the slowest, but it was actually the fastest. This could make sense as due to the nature
    of double hashing, there is a lot of wasted time on look ups and has a lower cache preformance compared to the linear
    probing. Double hashing also took up significantly less memory when comparing to linear probing. Quadratic probing was 
    a nice mixture of both memory and speed as it had roughly the same memory linear probing, but was significantly faster. 
    However, quadratic probing had a fatal flaw in that there is a chance in which the key won't map even if there is an 
    open space, which made it unvable comapared to linear search and double hashing, which seemed to have none/less of 
    this problem. Therefore, I chose double hashing when for Open Addressing Hash Map as it only slightly slower, but the
    memory that it saves is significantly more than linear probing (especially in larger data sets like random). I also
    chose double hasing because it will not have the effects of primary clustoring or secondary cluster. 

    However, these don't compare to linked list within a chain map. We can clearly see that it is both significantly faster, 
    and the memory that it takes is significantly less. I did try a strategy of without rehashing the table, but we can 
    clearly see that the memory is comparable; however, the speed it takes to access and modify the data is significantly 
    slower, which is why we won't use it. I also decided to use linked list due to the ease of modifying these list. It
    also is comparable to array list when inserting as we need to make sure that the element isn't currently in the list,
    which in turn, means that we have to traverse over most if not all of the list anyways. However, since linked list are
    easy to maintain and we don't need to increase constantly capacity of these buckets like in an array, I chose linked 
    list over array list. I also attempted to see how big of a factor is capacity, and I realized that if we increase the 
    capacity threshold for rehashing (i.e. load factor = 90%, when originally at 75%), the speed becomes significantly slower
    and the memory takes more space, which was expected as we are trying to assign variable to spaces that are already filled;
    therefore, we should maintain a lower load factor for rehashing, but not too low that we have to rehash every time and 
    therefore have worse space as clearly seen in rehashing with a load factor threshold of 60%. Not only does the lower load
    factor threshold increase memory, but it also increases the time complexity significantly. The load factor threshold is
    also applicable for the Open Addressing Hash Maps.
    
    Overall, what surprised me the most was the efficiency of the linear probing; however, I choose double hashing because 
    of the memory difference and the effects (or lack of) it has on clustering. It was expected that the quadratic probing 
    would fail as it is known to be able to skip empty/available spots. I was surprised about double hashing as I thought 
    that it would do much better, but in the end these modifications are just optimizations. In the end, we will choose 
    the chaining hash map as our search engine with rehashing of a load factor around 75%.
