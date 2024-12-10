import edu.uwm.cs351.PathHeapQueue;
import edu.uwm.cs351.util.DefaultWeightedEdge;
import edu.uwm.cs351.util.WeightedPath;
import junit.framework.TestCase;

public class TestEfficiency extends TestCase {

	private static final int POWER = 20;
	private PathHeapQueue<String> pq;

	static WeightedPath<String> p(double w) {
		return new WeightedPath<>(new DefaultWeightedEdge<>("start","end",w));
	}

	@Override
	protected void setUp() {
		try {
			assert pq.size() == 42 : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		pq = new PathHeapQueue<String>();
		int max = (1 << POWER);
		for (int i = 0; i < max; ++i) {
			pq.offer(p(max-i));
		}
	}

	public void testPoll() {
		int max = (1 << POWER);
		for (int i=0; i < max; ++i) {
			assertEquals(i+1.0, WeightedPath.weight(pq.poll()));
		}
	}
}
