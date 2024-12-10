import java.util.NoSuchElementException;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.FindPath;
import edu.uwm.cs351.util.DefaultWeightedEdge;
import edu.uwm.cs351.util.DirectedGraph;
import edu.uwm.cs351.util.SimpleHashGraph;
import edu.uwm.cs351.util.WeightedEdge;
import edu.uwm.cs351.util.WeightedPath;

public class TestFindPath extends LockedTestCase {
	protected void assertException(Class<?> exc, Runnable r) {
		try {
			r.run();
			assertFalse("should have thrown an exception.",true);
		} catch (RuntimeException e) {
			if (exc == null) return;
			assertTrue("threw wrong exception type.",exc.isInstance(e));
		}
	}

	private DirectedGraph<String,WeightedEdge<String>> graph;
	private FindPath<String> find;
	
	public void setUp() {
		graph = new SimpleHashGraph<>();
		find = new FindPath<>(graph);
	}
	
	private static WeightedEdge<String> e(String v1, String v2, double w) {
		return new DefaultWeightedEdge<>(v1, v2, w);
	}

	/**
	 * Return weight of this path
	 * @param p weighted path
	 * @return weight of this path
	 */
	static double weight(WeightedPath<?> p) {
		return WeightedPath.weight(p);
	}

	public void test() {
		makeCompassGraph();
		test1(graph);
	}

	public void makeCompassGraph() {
		graph.addEdge(e("W", "N", 2));
		graph.addEdge(e("W", "S", 4));
		graph.addEdge(e("N", "S", 1));
		graph.addEdge(e("N", "E", 5));
		graph.addEdge(e("S", "E", 3));
	}
	
	private void test1(DirectedGraph<String, WeightedEdge<String>> g) {
		// Here's the graph:
		//        N
		//      2 | \
		//     /  |  5
		//    W   |   E
		//     \  |  3
		//      4 1 /
		//        S
		// directed edge starts from node with line, ends with number
		// There is an edge W-2.0->N, but nothing from N to W.
		// Path size is number of edges in path (not like in HW #12)
		WeightedPath<String> w_w_path = find.shortestPath("W", "W");
		assertEquals(Ts(1247379915), ""+w_w_path);
		WeightedPath<String> w_e_path = find.shortestPath("W", "E");
		assertEquals(Ti(2106070703), w_e_path.size());
		assertEquals(Td(1633578716), weight(w_e_path));
		// The "get(1)" method returns the second edge in the path.
		// Then we convert it into a string of the form From-Weight->To.
		// Remember that Java prints 1.0 as "1.0".
		assertEquals(Ts(494796880),w_e_path.get(1).toString());
	}
	
	public void testNoPath() {
		makeCompassGraph();
		assertException(NoSuchElementException.class, () -> find.shortestPath("S","N"));
	}
	
	public void testNotInGraph() {
		makeCompassGraph();
		assertException(IllegalArgumentException.class, () -> find.shortestPath("A", "N"));
		assertException(IllegalArgumentException.class, () -> find.shortestPath("N", "B"));
		assertException(IllegalArgumentException.class, () -> find.shortestPath("A", "B"));
	}
	
	public void testAlreadyThere() {
		makeCompassGraph();
		assertNull(find.shortestPath("W", new String("W")));
	}
}
