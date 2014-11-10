package assignment1;

public interface Combination<T> {

	/**
	 * combine(x, neutral()) = x = combine(neutral(), x).
	 * 
	 * @return The neutral element of the combination.
	 */
	public int neutral();

	/**
	 * 
	 * combine(combine(x,y),z) = combine(x,combine(y,z))
	 * 
	 * @return The combination of x and y.
	 */
	public int combine(int x, int y);

	/**
	 * @param x An element
	 * @return The projection of the property that this combination is defined
	 *         on (e.g. if T = Employee, the salary of an employee).
	 */
	public int projectInt(T x);
}
	
	class AddSalary implements Combination<Employee>{

		@Override
		public int neutral() {
			// Anything combined with neutral returns 0
			return 0;
		}

		@Override
		public int combine(int x, int y) {
			// Returns the combined value of x and y
			return x + y;
		}

		@Override
		public int projectInt(Employee x) {
			// Returns the salary of an employee 'x'
			return x.getSalary();
		}
		
	}
	
	class MinAge implements Combination<Employee>{

		@Override
		public int neutral() {
			return Integer.MAX_VALUE;
		}

		@Override
		public int combine(int x, int y) {
			return Math.min(x, y);
		}

		@Override
		public int projectInt(Employee x) {
			return x.getSalary();
		}
		
	}
