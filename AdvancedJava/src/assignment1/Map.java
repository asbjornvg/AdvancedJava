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
	class MapSequential implements Map<Employee>{

		@Override
		public void map(Mutation<Employee> m, List<Employee> l) {
			for (Employee employee : l) {
				m.mutate(employee);
			}
		}
		
	}
	
	class MutationThread extends IncreaseSalary implements Runnable {
		
		Employee emp;
		
		public MutationThread(Employee emp){
			this.emp = emp;
		}

		@Override
		public void run() {
			this.mutate(this.emp);
		}
		
	}
	
	class MapParallel implements Map<Employee>{
		@Override
		public void map(Mutation<Employee> m, List<Employee> l) {
			List<Thread> threads = new ArrayList<Thread>(); 
			for (Employee employee : l) {
				Thread mt = new Thread(new MutationThread(employee));
				mt.start();
				threads.add(mt);
			}
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class MapChunked implements Map<Employee>{
		
		MapParallel mp = new MapParallel();

		@Override
		public void map(Mutation<Employee> m, List<Employee> l) {
			if(l.size() <= 3){
				mp.map(m,l);
			}
			else{
				mp.map(m, l.subList(0, 3));
				// Recursive call
				this.map(m, l.subList(3, l.size()));
			}
			
		}
	}
