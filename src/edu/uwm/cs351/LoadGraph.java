package edu.uwm.cs351;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import edu.uwm.cs351.util.DefaultWeightedEdge;
import edu.uwm.cs351.util.DirectedGraph;
import edu.uwm.cs351.util.SimpleHashGraph;
import edu.uwm.cs351.util.WeightedEdge;

public class LoadGraph {
	
	/**
	 * Read in a directed graph from the given reader.
	 * Each line (once trimmed of whitespace) names a vertex (a string),
	 * but before we trim, we check to see if the line begins with whitespace, 
	 * in which case this vertex is the second vertex of an edge that
	 * begins with the last vertex that was on a line not starting with whitespace.
	 * <p> For example, the following text:
	 * <pre>
	 * A
	 * B
	 *   A 10
	 *   C 15
	 * </pre>
	 * is a graph with three vertices (A, B and C) and two
	 * edges, B-10->A and B-15->C.
	 * @param r
	 * @return graph represented by text of reader.
	 * @throws IOException if error reading or if the first line is indented.
	 */
	public static DirectedGraph<String,WeightedEdge<String>> read(Reader r) throws IOException {
		DirectedGraph<String,WeightedEdge<String>> result = new SimpleHashGraph<>();
		BufferedReader br = new BufferedReader(r);
		String s;
		String source = null;
		while ((s = br.readLine()) != null) {
			if (s.startsWith("#")) continue; // comment!
			if (s.trim().isEmpty()) continue; // ignore blank lines
			if (s.startsWith(" ") || s.startsWith("\t")) {
				s = s.trim();
				String[] parts = s.split("\\s+");
				if (source == null) throw new IOException("found edge before any node");
				if (parts.length != 2) throw new IOException("Expected 'destination distance', but got '" + s + "'");
				WeightedEdge<String> edge = new DefaultWeightedEdge<String>(source,parts[0],Double.parseDouble(parts[1]));
				result.addVertex(parts[0]);
				result.addEdge(edge);
			} else {
				s = s.trim();
				result.addVertex(s);
				source = s;
			}
		}
		return result;
	}
}
