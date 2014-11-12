package assignment3;

import org.eclipse.jetty.server.Server;
import java.util.List;
import java.util.Map;

/**
 * EmployeeDBHTTPServer is the EmployeeDB server which receives client requests
 * 
 * @author bonii
 * 
 */
public class EmployeeDBHTTPServer {
	private static final String filePath = "/home/bonii/advanced-java/src/assignment3/departmentservermapping.xml";

	public static void main(String[] args) throws Exception {

		Map<String, List<Integer>> serverURLToDepartmentMap = Utility
				.getServerURLToDepartmentMapping(filePath);

		Server myServer = new Server(8080);
		// Pass the list of departments that this server should handle in the
		// constructor
		SimpleEmployeeDB db = new SimpleEmployeeDB(
				serverURLToDepartmentMap.get("http://localhost:8080/"));

		EmployeeDBHTTPHandler handler = new EmployeeDBHTTPHandler(db); // This
																		// handler
																		// will
																		// handle
																		// all
																		// incoming
																		// HTTP
																		// requests
		myServer.setHandler(handler);
		myServer.start();
		myServer.join();
	}
}
