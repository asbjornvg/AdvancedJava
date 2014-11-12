package assignment3;

import java.util.List;

public class SimpleEmployeeDB implements EmployeeDB {

	public SimpleEmployeeDB(List<Integer> departmentIds) {

	}

	@Override
	public synchronized void addEmployee(Employee emp)
			throws DepartmentNotFoundException {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized List<Employee> listEmployeesInDept(
			List<Integer> departmentIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized void cleanupDB() {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized void incrementSalaryOfDepartment(
			List<SalaryIncrement> salaryIncrements)
			throws DepartmentNotFoundException,
			NegativeSalaryIncrementException {
		// TODO Auto-generated method stub
	}

}
