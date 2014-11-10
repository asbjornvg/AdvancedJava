package assignment1;

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
