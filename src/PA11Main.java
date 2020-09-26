/*
 * AUTHOR: Stacy Sealky Lee
 * HW: TravelingSalesperson
 * PURPOSE:
 * Through this assignment,
 * I will be solving the traveling salesperson problem
 * using three different ways: recursive backtracking,
 * heuristic, and my own approach.
 *
 * The classes that are used for this assignment are:
 * DGraph, CreateBig, and Trip
 *
 * PA11Main class reads in a .mtx file
 * and store it properly into the DGraph class.
 * It builds the dot file as a string
 * and performs three different algorithms to solve the
 * traveling salesman problem.
 * It calculates what is the shortest trip through a sequence of
 * locations and back to the beginning while only visiting each location once.
 * The distances between locations are the input to the problem.
 * These distances can be represented as weights on edges in a graph.
 *
 * Part1 - DGraph.java
 * Part2 - Heuristic
 * Part3 - Recursive Backtracking
 * Part4 - Mine (my approach for improving the recursive backtracking approach)
 * Part5 - Timing all 3 approaches (main method has this but also see README.md)
 *
 * DGraph represents a directed graph. The nodes are represented as
 * integers ranging from 1 to num_nodes inclusive.
 * The weights are assumed to be >= zero.
 *
 * Methods used in this class:
 * createGraph(), heuristic(),
 * recursive(), backtrackingFunction(),
 * mine(), altBacktrackingFunction(), and main()
 *
 * Additionally, there are two other goals for this assignment, which are
 * >> Decomposition: using multiple interacting classes to achieve a larger goal
 * >> Continue the discussion of performance analysis that we have started this semester
 *
 * command line usage:
 * PathTo/infile.mtx [HEURISTIC, BACKTRACK, MINE, TIME]
 *
 * (---some of the verbiages in the comments are directly referenced from the provided specs---)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PA11Main {

    /*
     * Purpose: Read the .mtx file and convert into string by
     * using the buffered reader. Ignore the comment lines,
     * store the int values and double values to create graph
     * and throws IOException for a checked exception handling.
     *
     * @param String filename (in this PA, we use .mtx)
     *
     * @return DGraph graph
     */
    public static DGraph createGraph(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));

        // read type code initial line
        String line = br.readLine();

        // read comment lines if any
        boolean comment = true;
        while (comment) {
            line = br.readLine();
            comment = line.startsWith("%");
        }
        // line now contains the size information which needs to be parsed
        String[] str = line.split("( )+");
        int nRows = (Integer.valueOf(str[0].trim())).intValue();
        DGraph graph = new DGraph(nRows); // added

        // into the data section
        while (true) {
            line = br.readLine();
            if (line == null)
                break;
            str = line.split("( )+");
            int i = (Integer.valueOf(str[0].trim())).intValue();
            int j = (Integer.valueOf(str[1].trim())).intValue();
            double x = (Double.valueOf(str[2].trim())).doubleValue();
            graph.addEdge(i, j, x);
            ;
        }
        br.close(); // let's close the file!
        return graph;
    }

    /*
     * Purpose: this is the Part2 - Heuristic
     *
     * using the heuristic approach to traverse through
     * the graph then store data in List<Integer>
     * this method also uses Trip.java object and methods,
     * chooses the closest city that is available for the trip,
     * calls that closest city the current city
     * And by comparing weight < minDistance
     * it assigns the minimum distance and minimum neighbor
     *
     * @param DGraph graph
     *
     * @return Trip trip
     */
    public static Trip heuristic(DGraph graph) {
        Trip trip = new Trip(graph.getNumNodes());

        // choose city 1 first, call it the current city
        trip.chooseNextCity(1);
        int currentCity = 1;

        for (int i = 1; i < graph.getNumNodes(); i++) {
            List<Integer> neighbors = graph.getNeighbors(currentCity);
            List<Integer> curlist = trip.citiesLeft();
            List<Integer> newneighbors = new ArrayList<Integer>();
            for (Integer neighbor : neighbors) {
                if (curlist.contains(neighbor)) {
                    newneighbors.add(neighbor);
                }
            }
            double minDistance = Integer.MAX_VALUE;
            int minNeighbor = Integer.MAX_VALUE;
            for (Integer neighbor : newneighbors) {
                double weight = graph.getWeight(currentCity, neighbor);
                if (weight < minDistance) {
                    minDistance = weight;
                    minNeighbor = neighbor;
                }
            }
            trip.chooseNextCity(minNeighbor);
            currentCity = minNeighbor;
        }
        return trip;
    }

    /*
     * Purpose: this is the Part3 - Recursive Backtracking
     *
     * backtrackingFunction() is called within this method as a helper
     *
     * using the recursive backtracking approach to
     * see if the current trip has less cost than minimum trip
     *
     * @param DGraph graph
     *
     * @return Trip trip
     */
    public static Trip recursive(DGraph graph) {
        Trip curtrip = new Trip(graph.getNumNodes());
        Trip mintrip = new Trip(graph.getNumNodes());

        curtrip.chooseNextCity(1);
        backtrackingFunction(graph, curtrip, mintrip);
        return mintrip;
    }

    /*
     * Purpose: Helper method for Part3 - Recursive Backtracking
     *
     * starting with node 1 and continuing through the nodes in order.
     * Every time a node is chosen, that choice will be checked
     * before recursing to do some pruning.
     * We cannot stop at the first node we find,
     * because it is possible that paths through other nodes will cost less.
     *
     * @param DGraph graph, Trip curtrip, Trip mintrip
     * (graph data structure, current trip so far, min trip previously found)
     *
     * @return void
     */
    public static void backtrackingFunction(DGraph graph, Trip curtrip,
            Trip mintrip) {
        if (curtrip.citiesLeft().size() == 0) {
            double curTripcost = curtrip.tripCost(graph);
            double minTripcost = mintrip.tripCost(graph);

            if (curTripcost < minTripcost) {
                mintrip.copyOtherIntoSelf(curtrip);
                return;
            }
        }
        if (curtrip.tripCost(graph) < mintrip.tripCost(graph)) {
            List<Integer> curlist = curtrip.citiesLeft();
            for (int i = 0; i < curlist.size(); i++) {
                curtrip.chooseNextCity(curlist.get(i));
                backtrackingFunction(graph, curtrip, mintrip);
                curtrip.unchooseLastCity();
            }
        }
    }

    /*
     * Purpose: Part4-Mine(my approach for improving recursive backtracking
     * approach)
     * this part is pretty much the same as Part3 but
     * I made edits to the alternative backtracking function
     *
     * @param DGraph graph (graph data structure)
     *
     * @return Trip mintrip
     */
    public static Trip mine(DGraph graph) {
        Trip curtrip = new Trip(graph.getNumNodes());
        Trip mintrip = new Trip(graph.getNumNodes());

        curtrip.chooseNextCity(1);
        altBacktrackingFunction(graph, curtrip, mintrip);
        return mintrip;
    }

    /*
     * Purpose: Helper Method for Part4 - Mine (my approach for improving
     * recursive backtracking approach)
     * this is faster than the suggested recursive backtracking approach
     *
     * What is different from the original backtracking approach?:
     * instead of calling the backtracking recursion as a default
     * after the first condition check
     * (if current trip cost is less than the mintrip cost),
     * I added an additional pruning
     * so it only calls backtracking recursion
     * inside the additional if condition.
     * Then it will unchoose the last city.
     *
     *
     * on my machine, it cuts down the runtime consistently less than the
     * original backtracking
     *
     * @param DGraph graph, Trip curtrip, Trip mintrip
     *
     * @return void
     */
    public static void altBacktrackingFunction(DGraph graph, Trip curtrip,
            Trip mintrip) {
        if (curtrip.citiesLeft().size() == 0) {
            double curTripcost = curtrip.tripCost(graph);
            double minTripcost = mintrip.tripCost(graph);
            if (curTripcost < minTripcost) {
                mintrip.copyOtherIntoSelf(curtrip);
                return;
            }
        }
        if (curtrip.tripCost(graph) < mintrip.tripCost(graph)) {
            List<Integer> curlist = curtrip.citiesLeft();
            for (int i = 0; i < curlist.size(); i++) {
                curtrip.chooseNextCity(curlist.get(i));
                if (curtrip.tripCost(graph) < mintrip.tripCost(graph)) {
                    altBacktrackingFunction(graph, curtrip, mintrip);
                }
                curtrip.unchooseLastCity();
            }
        }
    }

    /*
     * Purpose: Driver/Main to call the methods and test and create outputs
     * Args[0] is the file name that we are reading from
     * Args[1] can be one of the 4 commands from HEURISTIC, BACKTRACK, MINE,
     * TIME
     *
     * command line usage:
     * PathTo/infile.mtx [HEURISTIC, BACKTRACK, MINE, TIME]
     *
     * for this assignment, we assume that the input args are valid and legal
     * and thus, no additional error handling for the inputs
     */
    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        DGraph graph = createGraph(fileName);

        if (args[1].equals("HEURISTIC")) {
            Trip trip = heuristic(graph);
            System.out.println(trip.toString(graph));
        } else if (args[1].equals("BACKTRACK")) {
            Trip trip = recursive(graph);
            System.out.println(trip.toString(graph));
        } else if (args[1].equals("MINE")) {
            Trip trip = mine(graph);
            System.out.println(trip.toString(graph));
        } else if (args[1].equals("TIME")) {
            long startTime = System.nanoTime();
            Trip trip = heuristic(graph);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println("heuristic: cost = " + trip.tripCost(graph)
                    + ", " + duration + " milliseconds");
            // resetting the time measurements and trip to MINE TIME
            startTime = System.nanoTime();
            trip = mine(graph);
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000;
            System.out.println("mine: cost = " + trip.tripCost(graph)
                    + ", "
                    + duration + " milliseconds");
            // resetting the time measurements and trip to BACKTRACK TIME
            startTime = System.nanoTime();
            trip = recursive(graph);
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000;
            System.out.println("backtrack: cost = "
                    + trip.tripCost(graph)
                    + ", " + duration + " milliseconds");
        } else {
            System.out.println(
                    "choose 1 arg from: HEURISTIC, BACKTRACK, MINE, TIME");
        }
    }
}
