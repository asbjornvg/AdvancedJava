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