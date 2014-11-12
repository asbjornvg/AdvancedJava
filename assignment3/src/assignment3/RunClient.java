package assignment3;

import java.util.ArrayList;
import java.util.List;

public class RunClient {

	public static void main(String[] args) throws Exception {
		try {
			EmployeeDBHTTPClient client = new EmployeeDBHTTPClient();
			Employee emp = new Employee();
			
			System.out.println("== Adding employee ==");
			emp.setDepartment(2);
			emp.setName("Bo");
			emp.setSalary(43.0f);
			client.addEmployee(emp);
			System.out.println("");
			System.out.println("== Adding employee ==");
			emp.setDepartment(1);
			emp.setName("BoJack");
			client.addEmployee(emp);
			System.out.println("");
			System.out.println("== Listing employees ==");
			List<Integer> depIds = new ArrayList<Integer>();
			depIds.add(2);
			depIds.add(1);
			client.listEmployeesInDept(depIds);
			
			System.out.println("");
			System.out.println("== Incrementing salaries ==");
			List<SalaryIncrement> salaryIncrements = new ArrayList<SalaryIncrement>();
			SalaryIncrement increment = new SalaryIncrement();
			increment.setDepartment(1);
			increment.setIncrementBy(100.0f);
			salaryIncrements.add(increment);
			client.incrementSalaryOfDepartment(salaryIncrements);
			
			System.out.println("");
			System.out.println("== Clearing database ==");
			client.cleanupDB();
			
			System.out.println("");
			System.out.println("== Listing employees ==");
			client.listEmployeesInDept(depIds);	
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}
}
