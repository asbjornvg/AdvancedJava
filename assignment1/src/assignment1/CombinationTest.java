package assignment1;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import assignment1.AddSalary;
import assignment1.MinAge;

public class CombinationTest {
	
	Random r = new Random();
	
	private Employee genEmployee(){
		int age = r.nextInt(41)+20;
		int salary = r.nextInt(2001)+3000;
		Employee emp = new Employee("John Doe", age, salary);
		return emp;
	}

	@Test
	// Test that "AddSalary" is associative.
	public void addSalaryAssoTest() {
		AddSalary s = new AddSalary();
		for (int i = 0; i < 2000; i++) {
			Employee emp = genEmployee();
			int x = r.nextInt(5000);
			int y = r.nextInt(5000);
			int z = emp.getSalary();
			assertEquals(s.combine(s.combine(x,y),z), s.combine(x,s.combine(y,z)));
		}
	}
	
	@Test
	public void addSalaryNeutralTest(){
		AddSalary s = new AddSalary();
		for (int i = 0; i < 2000; i++) {
			Employee emp = genEmployee();
			int x = emp.getSalary();
			assertEquals(x, s.combine(x, s.neutral()));
			assertEquals(x, s.combine(s.neutral(), x));
		}
	}
	
	@Test
	// Test that "AddSalary" is associative.
	public void minAgeAssoTest() {
		MinAge s = new MinAge();
		for (int i = 0; i < 2000; i++) {
			Employee emp = genEmployee();
			int x = r.nextInt(20);
			int y = r.nextInt(100)+20;
			int z = emp.getAge();
			assertEquals(x, s.combine(s.combine(x,y),z));
			assertEquals(x, s.combine(x, s.combine(y,z)));
		}
	}
	
	@Test
	public void minAgeNeutralTest(){
		MinAge s = new MinAge();
		for (int i = 0; i < 2000; i++) {
			Employee emp = genEmployee();
			int x = emp.getAge();
			assertEquals(x, s.combine(x, s.neutral()));
			assertEquals(x, s.combine(s.neutral(), x));
		}
	}
}
