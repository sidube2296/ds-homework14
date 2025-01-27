package edu.uwm.cs351;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

import edu.uwm.cs351.util.DirectedGraph;
import edu.uwm.cs351.util.WeightedEdge;
import edu.uwm.cs351.util.WeightedPath;

/**
 * A class to find shortest paths in a graph.
 * @param T graph element type
 */
public class FindPath<T> {
	private DirectedGraph<T,? extends WeightedEdge<T>> graph;
	
	/**
	 * Set up the algorithm to work on the given graph.
	 * @param g graph to use, must not be null
	 */
	public FindPath(DirectedGraph<T,? extends WeightedEdge<T>> g) {
		graph = g;
	}

	/**
	 * Return a shortest instance path between two vertices in the graph.
	 * This algorithm works if there are no negative distances in the graph.
	 * @param v1 starting vertex
	 * @param v2 ending vertex
	 * @return path from the first to the second as a weighted path, or null
	 * if the two vertices are the same (the empty path)
	 * @throws NoSuchElementException if no path can be found
	 * @throws IllegalArgumentException if v1 or v2 is not in the graph
	 */
	public WeightedPath<T> shortestPath(T v1, T v2) throws NoSuchElementException {
		// TODO: Find shortest distance path from v1 to v2.  
		// (This is different than Homework #12, where we found
		// the shortest length path.  This time we're using weights! )
		// You are welcome to write and use new helper methods, but not to add fields.
		if (!graph.containsVertex(v1) || !graph.containsVertex(v2)) throw new IllegalArgumentException("no vertices found");
		if (v1 == null || v2 == null) throw new IllegalArgumentException("Vertices are null");
		if (v1.equals(v2)) return null; 
		PathHeapQueue<T> w = new PathHeapQueue<>();
		Set<T> v = new HashSet<>();
		for (WeightedEdge<T> e : graph.getConnected(v1)) w.offer(new WeightedPath<>(e));		
		while (!w.isEmpty()) {
			WeightedPath<T> cp = w.poll();
			if (cp.getEnd().equals(v2)) return cp;
			if (!v.add(cp.getEnd())) continue;
			for (WeightedEdge<T> e : graph.getConnected(cp.getEnd())) w.offer(new WeightedPath<>(cp, e));
		}
		throw new NoSuchElementException("no path found"); // no path found
	}
}
