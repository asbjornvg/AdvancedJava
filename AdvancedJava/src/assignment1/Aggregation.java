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

class AggregationSequential implements Aggregation<Employee>{

	@Override
	public int aggregate(Combination<Employee> c, List<Employee> l) {
		int sum = 0;
		for (Employee employee : l) {
			sum = c.combine(sum, employee.getSalary());
		}
		return sum;
	}
	
}

class AggregationParallel implements Aggregation<Employee>{

	@Override
	public int aggregate(Combination<Employee> c, List<Employee> l) {
		if(l.size() == 1){
			// return the only element in the list
			return l.get(0).getSalary();
		}
		else{
			List<Employee> l1 = l.subList(0, l.size()/2);
			List<Employee> l2 = l.subList(l.size()/2, l.size());
			int sumLeft = this.aggregate(c, l1);
			int sumRight = this.aggregate(c, l2);
			Thread t = new Thread(new CombineThread(sumLeft, sumRight));
		}
		
		
	}
	
	class CombineThread extends AddSalary implements Runnable {
		
		int x;
		int y;
		
		public CombineThread(int x, int y){
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			this.combine(this.x, this.y);
		}
		
	}
}