package assignment3;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;

/**
 * EmployeeDBHTTPClient implements the client side methods of EmployeeDB
 * interface using HTTP protocol. The methods must send HTTP requests to the
 * EmployeeDBHTTPServer
 * 
 * @author bonii
 * 
 */
public class EmployeeDBHTTPClient implements EmployeeDBClient, EmployeeDB {
	private HttpClient client = null;
	private static final String filePath = "/home/bonii/advanced-java/src/assignment3/departmentservermapping.xml";
	private Map<Integer, String> departmentToServerURLMap = null;

	public EmployeeDBHTTPClient() throws FileNotFoundException {
		//Returns a concurrent hashmap :-)
		departmentToServerURLMap = Utility
				.getDepartmentToServerURLMapping(filePath);
		// You need to initiate HTTPClient here
	}

	@Override
	public void addEmployee(Employee emp) throws DepartmentNotFoundException {
		// TODO Auto-generated method stub
	}

	@Override
	public List<Employee> listEmployeesInDept(List<Integer> departmentIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incrementSalaryOfDepartment(
			List<SalaryIncrement> salaryIncrements)
			throws DepartmentNotFoundException,
			NegativeSalaryIncrementException {
		// TODO Auto-generated method stub
	}

	@Override
	public void cleanupDB() {
		// TODO Auto-generated method stub

	}

	/**
	 * Returns the server URL (starting with http:// and ending with /) to
	 * contact for a department
	 */
	public String getServerURLForDepartment(int departmentId)
			throws DepartmentNotFoundException {
		if (!departmentToServerURLMap.containsKey(departmentId)) {
			throw new DepartmentNotFoundException("department " + departmentId
					+ " does not exist in mapping");
		}
		return departmentToServerURLMap.get(departmentId);
	}

}
