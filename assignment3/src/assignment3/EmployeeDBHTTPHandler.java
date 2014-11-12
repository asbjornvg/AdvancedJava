package assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * EmployeeDBHTTPHandler is invoked when an HTTP request is received by the
 * EmployeeDBHTTPServer
 * 
 * @author bonii
 * 
 */
public class EmployeeDBHTTPHandler extends AbstractHandler {
	private SimpleEmployeeDB db = null;
	private XStream xmlStream = new XStream(new StaxDriver());

	private String getPostContent(HttpServletRequest req){
		try {
			int len = req.getContentLength();
			char[] cbuf = new char[len];
			BufferedReader reqReader = req.getReader();
			reqReader.read(cbuf);
			reqReader.close();
			return new String(cbuf);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public EmployeeDBHTTPHandler(SimpleEmployeeDB db) {
		this.db = db;
	}

	/**
	 * Although this method is thread-safe, what it invokes is not thread-safe
	 */
	public void handle(String target, Request baseRequest,
			HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {

		String uri = req.getRequestURI().trim().toUpperCase();	
		res.setContentType("text/html;charset=utf-8");
        res.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
		if (uri.equalsIgnoreCase("/addemployee")) {
			Employee emp = (Employee) xmlStream.fromXML(getPostContent(req));
			try {
				db.addEmployee(emp);
			} catch (DepartmentNotFoundException e) {
				res.getWriter().println(xmlStream.toXML(e));
			}
		}
		 if (uri.equalsIgnoreCase("/listemployees")){
			
			List<Integer> departments = (List<Integer>) xmlStream.fromXML(getPostContent(req));
			List<Employee> employees = db.listEmployeesInDept(departments);
									
			res.getWriter().println(xmlStream.toXML(employees));
		}
		 if (uri.equalsIgnoreCase("/incrementsalary")){
			
			List<SalaryIncrement> salaryIncrements = (List<SalaryIncrement>) xmlStream.fromXML(getPostContent(req));
			
			try {
				db.incrementSalaryOfDepartment(salaryIncrements);
			} catch (DepartmentNotFoundException e) {
				res.getWriter().println(xmlStream.toXML(e));
			}
			catch (NegativeSalaryIncrementException e) {
				res.getWriter().println(xmlStream.toXML(e));
			}
		}
	    if (uri.equalsIgnoreCase("/cleandb")){
	    		db.cleanupDB();
		}
		
		
	}
	

}
