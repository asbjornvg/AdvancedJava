package assignment3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
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
	private static final String filePath = "/home/jeppe/Git/AdvancedJava/assignment3/src/assignment3/departmentservermapping.xml";
	private Map<Integer, String> departmentToServerURLMap = null;
	private XStream xmlStream = new XStream(new StaxDriver());

	public EmployeeDBHTTPClient() throws FileNotFoundException {
		//Returns a concurrent hashmap :-)
		departmentToServerURLMap = Utility
				.getDepartmentToServerURLMapping(filePath);
		// You need to initiate HTTPClient here
		client = new HttpClient();
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addEmployee(Employee emp) throws DepartmentNotFoundException {
		String xmlString = xmlStream.toXML(emp);
		
		ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		
		try {
			 String temp = getServerURLForDepartment(emp.getDepartment());
			 exchange.setURL(temp + "addemployee");
			
		} catch (DepartmentNotFoundException e) {
			System.out.println("Department not found!");
			return;
		}
		Buffer buffer = new ByteArrayBuffer(xmlString);
		exchange.setRequestContent(buffer);
		try {
			client.send(exchange);
			int exchangeState = exchange.waitForDone();
			if (exchangeState == HttpExchange.STATUS_COMPLETED) {
				System.out.println("Response received");
				if (exchange.getResponseContent() != null){
					DepartmentNotFoundException exp = (DepartmentNotFoundException) xmlStream.fromXML(exchange.getResponseContent());
					exp.printStackTrace();
				}
				
				System.out.println("Added employee to department: " + emp.getDepartment());
				
		
			} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
				System.out.println("Error occured");
			} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
				System.out.println("Request timed out");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Employee> listEmployeesInDept(List<Integer> departmentIds) {
		List<Employee> emps = new ArrayList<Employee>();
			ContentExchange exchange = new ContentExchange();
			exchange.setMethod("POST");
			String xmlString = xmlStream.toXML(departmentIds);
			Buffer buffer = new ByteArrayBuffer(xmlString);
			exchange.setRequestContent(buffer);
			try {
				exchange.setURL("http://localhost:8080/" + "listemployees");
				client.send(exchange);
				int exchangeState = exchange.waitForDone();
				if (exchangeState == HttpExchange.STATUS_COMPLETED) {
					System.out.println("Response received");
					emps.addAll((List<Employee>) xmlStream.fromXML(exchange.getResponseContent()));
					
					for (Employee employee : emps) {
						System.out.println("Name: " + employee.getName() + ",  Department: " + employee.getDepartment());
					}
					
					
				} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
					System.out.println("Error occured");
				} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
					System.out.println("Request timed out");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		
		return emps;
	}

	@Override
	public void incrementSalaryOfDepartment(
			List<SalaryIncrement> salaryIncrements)
			throws DepartmentNotFoundException,
			NegativeSalaryIncrementException {
			ContentExchange exchange = new ContentExchange();
			exchange.setMethod("POST");
			exchange.setURL("http://localhost:8080/" + "incrementsalary");
			String xmlString = xmlStream.toXML(salaryIncrements);
			Buffer postData = new ByteArrayBuffer(xmlString);
			exchange.setRequestContent(postData);
			try {
				client.send(exchange);
				int exchangeState = exchange.waitForDone();
				if (exchangeState == HttpExchange.STATUS_COMPLETED) {
					System.out.println("Response received");
					
					System.out.println("Incremented all salaries for departments");
					
					if (exchange.getResponseContent() != null){
				   
						 Exception e = (Exception) xmlStream.fromXML(exchange.getResponseContent());
						
						if(e instanceof DepartmentNotFoundException)
						{
							DepartmentNotFoundException exp = (DepartmentNotFoundException) xmlStream.fromXML(exchange.getResponseContent());
							exp.printStackTrace();
						}
						else if(e instanceof NegativeSalaryIncrementException)
						{
							NegativeSalaryIncrementException exp = (NegativeSalaryIncrementException) xmlStream.fromXML(exchange.getResponseContent());							
							exp.printStackTrace();
						}
						
					}
					
					
				} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
					System.out.println("Error occured");
				} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
					System.out.println("Request timed out");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}

	@Override
	public void cleanupDB() {
		ContentExchange exchange = new ContentExchange();
		exchange.setMethod("POST");
		exchange.setURL("http://localhost:8080/" + "cleandb");
		try {
			client.send(exchange);
			int exchangeState = exchange.waitForDone();
			if (exchangeState == HttpExchange.STATUS_COMPLETED) {
				System.out.println("Response received");
				System.out.println("Database has been cleared");
			} else if (exchangeState == HttpExchange.STATUS_EXCEPTED) {
				System.out.println("Error occured");
			} else if (exchangeState == HttpExchange.STATUS_EXPIRED) {
				System.out.println("Request timed out");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		

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
