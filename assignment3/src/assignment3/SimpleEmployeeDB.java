	package assignment3;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class SimpleEmployeeDB implements EmployeeDB {

	private static Hashtable<Integer, List<Employee>> employees = new Hashtable<Integer, List<Employee>>();
	
	public SimpleEmployeeDB(List<Integer> departmentIds) {
		for (Integer depId : departmentIds) {
			employees.put(depId, new ArrayList<Employee>());
		}

	}

	@Override
	public synchronized void addEmployee(Employee emp)
			throws DepartmentNotFoundException {
		if(!employees.containsKey(emp.getDepartment())){
			throw new DepartmentNotFoundException("Department not found!");
		}
		else{ 
			employees.get(emp.getDepartment()).add(emp);
		}
	}

	@Override
	public synchronized List<Employee> listEmployeesInDept(List<Integer> departmentIds) {
		List<Employee> result = new ArrayList<Employee>();
		
		if(employees != null)
			{
			for (Integer depId : departmentIds) {
				
				if(employees.containsKey(depId))
				{
					result.addAll(employees.get(depId));
				}
			}
		}
		return result;
	}

	@Override
	public synchronized void cleanupDB() {
		employees.clear();
	}

	@Override
	public synchronized void incrementSalaryOfDepartment(
			List<SalaryIncrement> salaryIncrements)
			throws DepartmentNotFoundException,
			NegativeSalaryIncrementException {
		for (SalaryIncrement salaryIncrement : salaryIncrements) {
			
			if(salaryIncrement.getIncrementBy() < 0){				
				throw new NegativeSalaryIncrementException("Found a negative salary!");
			}
			if(!employees.containsKey(salaryIncrement.getDepartment())){
				throw new DepartmentNotFoundException("Department not found!");
			}		
		}
		for (SalaryIncrement salaryIncrement : salaryIncrements) {
			float increaseAmount = salaryIncrement.getIncrementBy();
			for (Employee emp : employees.get(salaryIncrement.getDepartment())) {
				emp.setSalary(emp.getSalary() + increaseAmount);
			}
		}	
	}
}
