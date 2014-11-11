package assignment1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.Test;

import assignment1.IncreaseSalary;

public class MapTest {
	
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
		// expected sum after increased salary = 5549.
	}

	private void testMap(Map<Employee> m) {
		List <Employee> l = dataSet();
		IncreaseSalary is = new IncreaseSalary();
		m.map(is, l);
		int sum = 0;
		for (Employee employee : l) {
			sum += employee.getSalary();
		}
		
		assertEquals(5549, sum);
	}
	
	@Test
	public void testMapSequential(){
		MapSequential m = new MapSequential();
		testMap(m);
	}
	
	@Test
	public void testMapParallel(){
		MapParallel m = new MapParallel();
		testMap(m);	
	}
	
	@Test
	public void testMapChunked(){
		MapChunked m = new MapChunked();
		testMap(m);	
	}
}
