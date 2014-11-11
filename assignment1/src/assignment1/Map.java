package assignment1;

import java.util.ArrayList;
import java.util.List;

public interface Map<T> {

	/**
	 * A mutation map. Mutates each element in a list by a given mutation.
	 * 
	 * @param m
	 *            The mutations.
	 * @param l
	 *            The list to mutate.
	 */
	public void map(Mutation<T> m, List<T> l);

}
	class MapSequential implements Map<Employee> {

		@Override
		public void map(Mutation<Employee> m, List<Employee> l) {
			// Traverse the list sequentially.
			for (Employee employee : l) {
				m.mutate(employee);
			}
		}
		
	}
	
	class MutationThread extends IncreaseSalary implements Runnable {
		
		private Mutation<Employee> m;
		private Employee emp;
		
		public MutationThread(Mutation<Employee> m, Employee emp){
			this.m = m;
			this.emp = emp;
		}

		@Override
		public void run() {
			// Mutate the employee.
			this.m.mutate(this.emp);
		}
		
	}
	
	class MapParallel implements Map<Employee> {
		@Override
		public void map(Mutation<Employee> m, List<Employee> l) {
			// Keep a list of threads.
			List<Thread> threads = new ArrayList<Thread>();
			
			// For each employee in the list, start a new mutation thread.
			for (Employee employee : l) {
				Thread mt = new Thread(new MutationThread(m, employee));
				mt.start();
				threads.add(mt);
			}
			
			// Wait for all the threads to finish.
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class MapChunked implements Map<Employee> {
		
		MapParallel mp = new MapParallel();

		@Override
		public void map(Mutation<Employee> m, List<Employee> l) {
			if(l.size() <= 3){
				// Three or less elements in the list, just map
				// them in parallel.
				mp.map(m,l);
			}
			else{
				// Otherwise, map the first three in parallel (which
				// includes waiting for them to finish)
				mp.map(m, l.subList(0, 3));
				// And then call recursively on the rest of the list.
				this.map(m, l.subList(3, l.size()));
			}
			
		}
	}
