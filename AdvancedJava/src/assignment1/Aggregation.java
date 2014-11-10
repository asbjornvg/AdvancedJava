package assignment1;

import java.util.List;

public interface Aggregation<T> {

	/**
	 * Compute the aggregate over a list by combining all elements with a given
	 * combination.
	 * 
	 * @param c
	 *            The combination to aggregate with (*).
	 * @param l
	 *            A list of values [e_1,...e_k].
	 * @return The aggregation e_1 * ... * e_k.
	 */
	public int aggregate(Combination<T> c, List<T> l);

}

class AggregationSequential implements Aggregation<Employee> {

	@Override
	public int aggregate(Combination<Employee> c, List<Employee> l) {
		int sum = 0;
		for (Employee employee : l) {
			sum = c.combine(sum, employee.getSalary());
		}
		return sum;
	}
	
}

class AggregationParallel implements Aggregation<Employee> {
	
	@Override
	public int aggregate(Combination<Employee> c, List<Employee> l) {
		// Start a thread that will compute the result.
		AggregationThread a = new AggregationThread(c, l);
		Thread t1 = new Thread(a);
		t1.start();
		try {
			// Wait for the thread to finish.
			t1.join();
		} catch (InterruptedException e) {
			// Do whatever, should not happen.
			e.printStackTrace();
		}
		// Return what the thread computed.
		return a.getResult();
		
	}
	
	private class AggregationThread implements Runnable {
		
		private Combination<Employee> c;
		private List<Employee> l;
		private int result;
		
		public AggregationThread(Combination<Employee> c, List<Employee> l){
			this.c = c;
			this.l = l;
		}
		
		public int getResult() {
			return this.result;
		}

		@Override
		public void run() {
			if(this.l.size() == 1) {
				// Return the salary of the only Employee in the list.
				this.result = l.get(0).getSalary();
				return;
			}
			else {
				// Split into two sublists.
				List<Employee> l1 = this.l.subList(0, this.l.size()/2);
				List<Employee> l2 = this.l.subList(this.l.size()/2, this.l.size());
				// Call recursively on both sublists (start one thread
				// for each sublist).
				AggregationThread a1 = new AggregationThread(this.c, l1);
				AggregationThread a2 = new AggregationThread(this.c, l2);
				Thread t1 = new Thread(a1);
				Thread t2 = new Thread(a2);
				t1.start();
				t2.start();
				try {
					// Wait for the threads to finish.
					t1.join();
					t2.join();
				} catch (InterruptedException e) {
					// Do whatever, should not happen.
					e.printStackTrace();
				}
				// Now, the two sub-results are available.
				int sumLeft = a1.getResult();
				int sumRight = a2.getResult();
				// Combine them.
				this.result = this.c.combine(sumLeft, sumRight);
				return;
			}
		}
		
	}
}