package assignment1;

public interface Mutation<T> {

	/**
	 * A mutation. This method changes the internal state of the given object.
	 * 
	 * @param x
	 *            The object to mutate.
	 */
	public void mutate(T x);

}
	
	class IncreaseSalary implements Mutation<Employee> {

		@Override
		public void mutate(Employee x) {
			if(x.getAge() > 40){
				x.setSalary(x.getSalary() + (x.getAge()/2));
			}
			
		}
	}
	
	class LowerCaseName implements Mutation<Employee> {

		@Override
		public void mutate(Employee x) {
			x.setName(x.getName().toLowerCase());
			
		}
		
	}