package assignment3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

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
	private XStream xmlStream = new XStream(new StaxDriver());

	public EmployeeDBHTTPClient() throws FileNotFoundException {
		//Returns a concurrent hashmap :-)
		departmentToServerURLMap = Utility
				.getDepartmentToServerURLMapping(filePath);
		// You need to initiate HTTPClient here
	}

	@Override
	public void addEmployee(Employee emp) throws DepartmentNotFoundException {
		String xmlString = xmlStream.toXML(emp);
		
		ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL(getServerURLForDepartment(emp.getDepartment()));
		Buffer buffer = new ByteArrayBuffer(xmlString);
		exchange.setRequestContent(buffer);
		try {
			client.send(exchange);
			exchange.waitForDone();
			exchange.getResponseContent();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Employee> listEmployeesInDept(List<Integer> departmentIds) {
		for (Integer depId : departmentIds) {
			ContentExchange exchange = new ContentExchange();
			exchange.setMethod("POST");
			try {
				exchange.setURL(getServerURLForDepartment(depId));
				client.send(exchange);
				exchange.waitForDone();
				exchange.getResponseContent();
			} catch (DepartmentNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
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
