#TravelingSalesperson

Recorded are the timing experiments that compare my improved algorithm to the required algorithms for the input file big11.mtx and it describes why the algorithms have relative performance differences.

<1st run>

heuristic: cost = 3.3969307170000005, 4 milliseconds mine: cost = 1.3566775349999998, 95 milliseconds backtrack: cost = 1.3566775349999998, 125 milliseconds

<2nd run>

heuristic: cost = 3.3969307170000005, 3 milliseconds mine: cost = 1.3566775349999998, 123 milliseconds backtrack: cost = 1.3566775349999998, 162 milliseconds

<3rd run>

heuristic: cost = 3.3969307170000005, 3 milliseconds mine: cost = 1.3566775349999998, 210 milliseconds backtrack: cost = 1.3566775349999998, 185 milliseconds

<4th run>

heuristic: cost = 3.3969307170000005, 4 milliseconds mine: cost = 1.3566775349999998, 259 milliseconds backtrack: cost = 1.3566775349999998, 243 milliseconds

<5th run>

heuristic: cost = 3.3969307170000005, 7 milliseconds mine: cost = 1.3566775349999998, 385 milliseconds backtrack: cost = 1.3566775349999998, 405 milliseconds

<6th run> heuristic: cost = 3.3969307170000005, 1 milliseconds mine: cost = 1.3566775349999998, 100 milliseconds backtrack: cost = 1.3566775349999998, 111 milliseconds

In general, the heuristic approach was the fastest. My approach was using the recursive backtracking approach except I attempted to improve the runtime by putting in more pruning.

How is mine different from the original backtracking approach?: instead of calling the backtracking recursion as a default after the first condition check (if current trip cost is less than the mintrip cost), I added an additional pruning so it only calls backtracking recursion conditionally, rather than each time.

I believe MINE was (on average) faster than the suggested recursive backtracking approach because on my machine, it cuts down the runtime consistently less than the original backtracking.

However, it may depend on the machines, as the relative performance of the algorithms depend on characteristics of the hardware.

Also, relative performance might depend on the details of the data set. For instance, some sorting algorithms run faster if the data are already partially sorted; other algorithms run slower in this case.

A common way to avoid this problem is to analyze the worst case scenario. It is sometimes useful to analyze average case performance, but that’s usually harder, and it might not be obvious what set of cases to average over. Relative performance also depends on the size of the problem. A sorting algorithm that is fast for small lists might be slow for long lists. The usual solution to this problem is to express number of operations as a function of problem size, and group functions into categories depending on how quickly they grow as problem size increases.
