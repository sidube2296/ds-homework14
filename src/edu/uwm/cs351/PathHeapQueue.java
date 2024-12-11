package edu.uwm.cs351;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import edu.uwm.cs351.util.WeightedPath;

/**
 * A priority queue of weighted paths in a graph,
 * where the minimum weight path is the first to come out.
 */
public class PathHeapQueue<V> extends AbstractQueue<WeightedPath<V>> {
	private final List<WeightedPath<V>> heap = new ArrayList<>(); // The only field!

	/**
	 * Return the parent index of this index, or -1 if there is no parent
	 * @param x index in the heap
	 * @return index of parent in heap
	 */
	private static int parent(int x) {
		return (x <= 0) ? -1 : (x - 1) / 2; // TODO: one line, no if's or loops
	}

	/**
	 * Return the index of the first (left) child of this node in the heap.
	 * The second (right) child index will be one greater.
	 * @param x index of node
	 * @return index of left child.
	 */
	private static int child(int x) {
		return 2 * x + 1; // TODO: one line, no if's or loops
	}

	
	/// Invariant checks:
	
	
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	/**
	 * Used to report an error found when checking the invariant.
	 * By providing a string, this will help debugging the class if the invariant should fail.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	private boolean wellFormed() {
		// (1) The heap cannot contain any nulls
	    for (WeightedPath<V> p : heap) if (p == null) return report("Null elements in Heap");
		// (2) Every path in the heap must have weight no less than
		//     the weight of the path as its parent (if any)
	    for (int i = 0; i < heap.size(); i++) {
	        if (child(i) < heap.size() && WeightedPath.weight(heap.get(i)) > WeightedPath.weight(heap.get(child(i)))) return report("Heap property violated at left child.");
	        if ((child(i) + 1) < heap.size() && WeightedPath.weight(heap.get(i)) > WeightedPath.weight(heap.get(child(i) + 1))) return report("Heap property violated at right child.");
	    }
	    return true;
	}
	
	private PathHeapQueue(boolean ignored) {} // do not change this construct -- used by Spy
	
	public PathHeapQueue() {
		//nothing to do
		assert wellFormed() : "invariant failed at end of constructor";
	}
	
	@Override
	public int size() {
		assert wellFormed() : "invariant failed in size()";
		return heap.size(); // TODO (very easy) 
	}

	@Override
	public boolean offer(WeightedPath<V> e) {
		assert wellFormed() : "invariant failed at start of offer()";
		// TODO
		if (e == null) throw new NullPointerException("Weighted path is null");
	    heap.add(e);
	    shiftUp(heap.size() - 1);
		assert wellFormed() : "invariant failed at end of offer()";
		return true;
	}

	@Override
	public WeightedPath<V> poll() {
		assert wellFormed() : "invariant failed at start of poll()";
		// TODO
		if (heap.isEmpty()) return null;
		WeightedPath<V> m = heap.get(0);
		WeightedPath<V> l = heap.remove(heap.size() - 1);
		if (!heap.isEmpty()) {
			heap.set(0, l); 
			shiftDown(0);
		}
		assert wellFormed() : "invariant failed at end of poll()";
		return m; // TODO
	}

	@Override
	public WeightedPath<V> peek() {
		assert wellFormed() : "invariant failed in peek()";
		return heap.isEmpty() ? null : heap.get(0); // TODO (no changes, so no assertion at end)
	}

	@Override
	public Iterator<WeightedPath<V>> iterator() {
		return Collections.unmodifiableCollection(heap).iterator();
	}
	
	// TODO: Our solution has helper methods for sifting up and down
	
	private void shiftUp(int i) {
	    while (i > 0) {
	        if (WeightedPath.weight(heap.get(i)) >= WeightedPath.weight(heap.get(parent(i)))) break; 
	        Collections.swap(heap, i, parent(i));
	        i = parent(i);
	    }
	}
	
	private void shiftDown(int i) {
	    while (true) {
	        int s = i;
	        if (child(i) < heap.size() && WeightedPath.weight(heap.get(child(i))) < WeightedPath.weight(heap.get(s))) s = child(i);	        
	        if (child(i)+ 1  < heap.size() && WeightedPath.weight(heap.get(child(i) + 1)) < WeightedPath.weight(heap.get(s))) s = child(i) + 1;
	        if (s == i) break;
	        Collections.swap(heap, i, s);
	        i = s;
	    }
	}

	public static class Spy {
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}
		
		/**
		 * What does the ADT think is the parent of this index?
		 * @param i index to check
		 * @return parent index
		 */
		public int parent(int i) {
			return PathHeapQueue.parent(i);
		}

		/**
		 * What does the ADT think is the first (left) child of this index?
		 * @param i index to check
		 * @return first child index
		 */
		public int child(int i) {
			return PathHeapQueue.child(i);
		}

		/**
		 * Create a path heap queue with a copy of the contents provided
		 * @param E element type
		 * @param l contents to use
		 * @return new debugging instead of path heap queue
		 */
		public <E> PathHeapQueue<E> newInstance(List<WeightedPath<E>> l) {
			PathHeapQueue<E> result = new PathHeapQueue<>(false);
			result.heap.addAll(l);
			return result;
		}

		/**
		 * Return whether debugging instance meets the 
		 * requirements on the invariant.
		 * @param d instance of to use, must not be null
		 * @return whether it passes the check
		 */
		public boolean wellFormed(PathHeapQueue<?> d) {
			return d.wellFormed();
		}
	}
}