/*
 * AUTHOR: Stacy Sealky Lee
 * TravelingSalesperson
 *
 * PURPOSE:
 * Through this assignment,
 * I will implement the data structure of graphs.
 * The first part of this assignment will be reading in a .mtx file
 * and converting the file into a code representation of a graph.
 * By completing missing methods in the DGraph.java,
 * I aim to solve the traveling salesperson problem.
 *
 * DGraph represents a directed graph. The nodes are represented as
 * integers ranging from 1 to num_nodes inclusive.
 * The weights are assumed to be >= zero.
 *
 * Usage instructions:
 *
 * Construct a DGraph
 * DGraph graph = new DGraph(numNodes);
 *
 * Add an edge
 * graph.addEdge(v, w, weight);
 *
 * Other useful methods:
 * graph.getWeight(v,w)
 * graph.getNumNodes()
 * List<Integer> list = graph.getNeighbors(v);
 *
 * Additionally,
 * according to the TA's answer,
 * getNeighbor() returns empty list instead of null
 * if the node passed in doesn't exist
 *
 * (---some of the verbiages in the comments are directly referenced from the provided specs---)
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DGraph {

    private HashMap<Integer, List<Edge>> graph;
    private List<Edge> edges;
    private int size;

    /*
     * Constructs an instance of the DGraph class with # nodes numNodes.
     */
    public DGraph(int numNodes) {
        graph = new HashMap<Integer, List<Edge>>();
        edges = new ArrayList<Edge>();
        size = 0;
    }

    /**
     * Adds the directed edge (v,w) to the graph including updating the node
     * count appropriately.
     *
     * @param v
     * @param w
     */
    public void addEdge(int v, int w, double distance) {
        if (graph.get(v) == null) {
            graph.put(v, new ArrayList<Edge>());
            size++;
        }
        if (graph.get(w) == null) {
            graph.put(w, new ArrayList<Edge>());
            size++;
        }
        Edge edge = new Edge(v, w, distance);
        graph.get(v).add(edge);
        edges.add(edge);
    }

    /*
     * Returns the number of nodes in this graph.
     */
    public int getNumNodes() {
        return size;
    }

    // Returns the weight for the given edge.
    // Returns -1 if there is no edge between the nodes v and w.
    public double getWeight(int v, int w) {
        if (graph.get(v) != null && graph.get(w) != null) {
            List<Edge> neighbors = graph.get(v); // returns the list of edges
                                                 // from v
            for (Edge edge : neighbors) {
                if (edge.node2 == w) {
                    return edge.weight;
                }
            }
        }
        return -1;
    }

    /**
     * For a given node returns a sorted list of all its neighbors.
     *
     * @param v
     *            Node identifier
     * @return A sorted list of v's neighbors.
     */
    public List<Integer> getNeighbors(int v) {
        List<Edge> neighborEdges = graph.get(v);
        List<Integer> neighbors = new ArrayList<Integer>();

        if (neighborEdges != null) {
            for (Edge edge : neighborEdges) {
                neighbors.add(edge.node2);
            }
            Collections.sort(neighbors);
            return neighbors;
        } else {
            return neighbors;
            // return empty list instead of null according to TA's answer
        }
    }

    /* --------------------------------------- */
    /*
     * You should not need to touch anything below this line,
     * except for maybe the name edges in the for each loop just below
     * in the toDotString method if you named your collection of edges
     * differently.
     */
    // Create a dot representation of the graph.
    public String toDotString() {
        String dot_str = "digraph {\n";
        // Iterate over the edges in order.
        for (Edge e : edges) {
            dot_str += e.toDotString() + "\n";
        }
        return dot_str + "}\n";
    }

    /**
     * Immutable undirected edges.
     */
    public class Edge implements Comparable<Edge> {

        // Nodes in edge and weight on edge
        private final int node1;
        private final int node2;
        private final double weight;

        /**
         * Stores the given nodes with smaller id first.
         *
         * @param node1
         * @param node2
         */
        public Edge(int node1, int node2, double weight) {
            assert weight >= 0.0;
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weight;
        }

        /**
         * @return an directed edge string in dot format
         */
        public String toDotString() {
            return "" + node1 + " -> " + node2 + " [label=\"" + weight + "\"];";
        }

        /**
         * Lexicographical ordering on edges (node1,node2).
         */
        public int compareTo(Edge other) {
            if (this.equals(other)) {
                return 0; // this and other are equal
            } else if ((node1 < other.node1)
                    || (node1 == other.node1 && node2 < other.node2)) {
                return -1; // this is less than other
            } else {
                return 1; // this is greater than other
            }
        }

        /**
         * Lexicographical ordering on edges (node1,node2).
         */
        public boolean equals(Object o) {
            if (!(o instanceof Edge)) {
                return false;
            }
            Edge other = (Edge) o;
            return (node1 == other.node1) && (node2 == other.node2);
        }

        /**
         * Know number of nodes when read in input file, so can give each edge a
         * unique hash code.
         */
        public int hashCode() {
            return getNumNodes() * node1 + node2;
        }
    }

}
