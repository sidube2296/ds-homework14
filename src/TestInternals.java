import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.PathHeapQueue;
import edu.uwm.cs351.util.DefaultWeightedEdge;
import edu.uwm.cs351.util.WeightedEdge;
import edu.uwm.cs351.util.WeightedPath;


public class TestInternals extends LockedTestCase {
	protected PathHeapQueue.Spy spy;
	protected int reports;
	protected PathHeapQueue<Integer> self;
	protected List<WeightedPath<Integer>> heap;
	
	protected void assertReporting(boolean expected, Supplier<Boolean> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (expected) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get().booleanValue());
			if (!expected) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
	
	protected void assertWellFormed(boolean expected) {
		if (self == null) makeInstance();
		assertReporting(expected, () -> spy.wellFormed(self));
	}

	protected PathHeapQueue<Integer> makeInstance() {
		return self = spy.newInstance(heap);
	}
	
	
	/// Locked tests
	
	
	protected WeightedPath<Integer> p(double... weights) {
		WeightedPath<Integer> p = null;
		int v = 0;
		for (double w : weights) {
			WeightedEdge<Integer> e = new DefaultWeightedEdge<>(v,v+1,w);
			++v;
			if (p == null) p = new WeightedPath<>(e);
			else p = new WeightedPath<>(p,e);
		}
		return p;
	}
	
	@Override
	protected void setUp() {
		spy = new PathHeapQueue.Spy();
		heap = new ArrayList<>();
	}
	
	// Locked test
	public void test() {
		//      0
		//    /   \
		//   1     2
		//  / \   / \
		// 3   4 5   6
		assertEquals(Ti(312675606),spy.parent(5));
		assertEquals(Ti(1149670805),spy.parent(4));
		assertEquals(Ti(145810953),spy.parent(2));
		assertEquals(Ti(245880880),spy.parent(0));
		// "child" returns the left child of the argument
		assertEquals(Ti(1654112578),spy.child(1));
		assertEquals(Ti(122264606),spy.child(0));
		assertEquals(Ti(1998421738),spy.child(2));
	}
	
	public void testA() {
		assertEquals(20,spy.parent(42));
	}

	public void testB() {
		assertEquals(20,spy.parent(41));
	}

	public void testC() {
		assertEquals(41,spy.child(20));
	}
	
	public void testD() {
		assertEquals(43,spy.child(21));
	}

	public void testE() {
		assertWellFormed(true); // empty is OK
	}
	
	public void testF() {
		heap.add(null);
		assertWellFormed(false); // null not OK
	}
	
	public void testG() {
		heap.add(p(-3));
		assertWellFormed(true); // negative OK
	}
	
	public void testH() {
		heap.add(p(0,1));
		assertWellFormed(true);
	}
	
	public void testI() {
		heap.add(p(0,1,1));
		heap.add(p(2));
		assertWellFormed(true); // equal is OK
	}
	
	public void testJ() {
		heap.add(p(0,3));
		heap.add(p(2));
		assertWellFormed(false);
	}
	
	public void testK() {
		heap.add(p(0,1,1));
		heap.add(p(2));
		heap.add(p(1,1));
		assertWellFormed(true);
	}
	
	public void testL() {
		heap.add(p(3));
		heap.add(p(2,2));
		heap.add(p(2));
		assertWellFormed(false);
	}
	
	private void makeTree() {
		heap.add(p(0,1,1));
		heap.add(p(2));
		heap.add(p(5));
		heap.add(p(3));
		heap.add(p(2,2));
		heap.add(p(6));
		heap.add(p(3,2));
	}
	
	public void testM() {
		makeTree();
		assertWellFormed(true);
	}
	
	public void testN() {
		makeTree();
		heap.set(2, p(3,1));
		assertWellFormed(true);
	}
	
	public void testO() {
		makeTree();
		heap.set(1, p(3,1));
		assertWellFormed(false);
	}
	
	public void testP() {
		makeTree();
		heap.set(4, p(3,1));
		assertWellFormed(true);
	}
	
	public void testQ() {
		makeTree();
		heap.set(5, p(3,1));
		assertWellFormed(false);
	}

}
