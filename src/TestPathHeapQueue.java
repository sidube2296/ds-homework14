import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.PathHeapQueue;
import edu.uwm.cs351.util.DefaultWeightedEdge;
import edu.uwm.cs351.util.WeightedPath;

public class TestPathHeapQueue extends LockedTestCase {
	protected void assertException(Class<?> exc, Runnable r) {
		try {
			r.run();
			assertFalse("should have thrown an exception.",true);
		} catch (RuntimeException e) {
			if (exc == null) return;
			assertTrue("threw wrong exception type.",exc.isInstance(e));
		}
	}

	private PathHeapQueue<String> pq;

	@Override
	protected void setUp() {
		try {
			assert pq.poll() == null;
			throw new IllegalStateException("assertions must be enabled to run this test");
		} catch (NullPointerException ex) {
			// OK!
		}
		pq = new PathHeapQueue<>();
	}
	
	/**
	 * Create a trivial path with this weight
	 * @param w weight to use
	 * @return single edge path with this weight
	 */
	static WeightedPath<String> p(double w) {
		return new WeightedPath<>(new DefaultWeightedEdge<>("0","1",w));
	}
	
	/**
	 * Return weight of this path
	 * @param p weighted path
	 * @return weight of this path
	 */
	static double weight(WeightedPath<?> p) {
		return WeightedPath.weight(p);
	}
	
	
	/// Locked tests
	
	public void test() {
		// pq is empty
		assertEquals(Ti(533240306), pq.size());
		assertEquals(Td(1126284317), weight(pq.poll()));
		pq.offer(p(4));
		pq.offer(p(1));
		pq.offer(p(2));
		assertEquals(Td(14292346), weight(pq.peek()));
		assertEquals(Td(1868237592), weight(pq.poll()));
		assertEquals(Td(1357407423), weight(pq.poll()));
	}
	
	
	public void test0() {
		pq.offer(p(3));
		assertEquals(1, pq.size());
		assertEquals(3.0, weight(pq.peek()));
		assertEquals(3.0, weight(pq.poll()));
		assertNull(pq.poll());
	}
	
	public void test1() {
		pq.offer(p(1));
		pq.offer(p(2));
		assertEquals(2, pq.size());
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertNull(pq.poll());
	}
	
	public void test2() {
		pq.offer(p(2));
		pq.offer(p(1));
		assertEquals(2, pq.size());
		assertEquals(1.0, weight(pq.poll()));
		pq.offer(p(0));
		assertEquals(0.0, weight(pq.peek()));
		assertEquals(2, pq.size());
	}
	
	public void test3() {
		pq.offer(p(3));
		pq.offer(p(2));
		pq.offer(p(1));
		assertEquals(3, pq.size());
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertEquals(3.0, weight(pq.poll()));
		assertNull( pq.peek());
	}
	
	public void test4() {
		pq.offer(p(1));
		pq.offer(p(3));
		pq.offer(p(2));
		assertEquals(3, pq.size());
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertEquals(3.0, weight(pq.poll()));
		assertNull( pq.peek());		
	}
	
	public void test5() {
		pq.offer(p(4));
		pq.offer(p(3));
		pq.offer(p(2));
		pq.offer(p(1));
		assertEquals(4, pq.size());
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertEquals(3.0, weight(pq.poll()));
		assertEquals(4.0, weight(pq.poll()));
		assertNull( pq.peek());		
	}
	
	public void test6() {
		pq.offer(p(2));
		pq.offer(p(2));
		pq.offer(p(1));
		pq.offer(p(1));
		assertEquals(4, pq.size());
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertNull( pq.peek());		
	}
		
	public void test7() {
		pq.offer(p(7));
		pq.offer(p(6));
		pq.offer(p(5));
		pq.offer(p(4));
		pq.offer(p(3));
		pq.offer(p(2));
		pq.offer(p(1));
		assertEquals(7, pq.size());
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertEquals(3.0, weight(pq.poll()));
		assertEquals(4.0, weight(pq.poll()));
		assertEquals(5.0, weight(pq.poll()));
		assertEquals(6.0, weight(pq.poll()));
		assertEquals(7.0, weight(pq.poll()));
		assertNull( pq.peek());
	}
		
	public void test8() {
		pq.offer(p(2));
		pq.offer(p(6));
		pq.offer(p(5));
		pq.offer(p(4));
		pq.offer(p(3));
		pq.offer(p(7));
		pq.offer(p(1));
		pq.offer(p(0));
		assertEquals(8, pq.size());
		assertEquals(0.0, weight(pq.poll()));
		assertEquals(1.0, weight(pq.poll()));
		assertEquals(2.0, weight(pq.poll()));
		assertEquals(3.0, weight(pq.poll()));
		assertEquals(4.0, weight(pq.poll()));
		assertEquals(5.0, weight(pq.poll()));
		assertEquals(6.0, weight(pq.poll()));
		assertEquals(7.0, weight(pq.poll()));
		assertNull( pq.peek());
	}
	
	// for more tests, use random testing
}
