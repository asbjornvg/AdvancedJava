package assignment1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AggregationTest {
	
	// Same dataset as MapTest
	private List<Employee> dataSet(){
		List<Employee> l = new ArrayList<Employee>();
		l.add(new Employee("Bo", 26, 100));
		l.add(new Employee("Rasmus", 21, 200));
		l.add(new Employee("Niels", 55, 300));
		l.add(new Employee("Henrik", 38, 400));
		l.add(new Employee("Martin", 29, 500));
		l.add(new Employee("Jeppe", 24, 600));
		l.add(new Employee("Asbjørn", 27, 700));
		l.add(new Employee("Rolf", 18, 800));
		l.add(new Employee("Susan", 45, 900));
		l.add(new Employee("Ivana", 27, 1000));
		return l;
		// expected sum = 5500
	}

	private void testAggregation(Aggregation<Employee> a) {
		List <Employee> l = dataSet();
		AddSalary as = new AddSalary();
		int sum = a.aggregate(as, l);
		assertEquals(5500, sum);
	}
	
	@Test
	public void testAggregationSequential(){
		AggregationSequential a = new AggregationSequential();
		this.testAggregation(a);
	}
	
	@Test
	public void testAggregationParallel(){
		AggregationParallel a = new AggregationParallel();
		this.testAggregation(a);
	}

}
