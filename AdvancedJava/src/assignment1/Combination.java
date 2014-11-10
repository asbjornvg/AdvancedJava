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
