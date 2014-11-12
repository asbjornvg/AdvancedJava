package assignment3;

import org.junit.BeforeClass;
import org.junit.AfterClass;

/**
 * Generated JUnit test class to test the EmployeeDB interface. It can be used
 * to test both the client and the server implementation.
 * 
 * @author bonii
 * 
 */
public class EmployeeDBTest {
	private static EmployeeDB employeeDB = null;

	/**
	 * The method runs just once before the test cases run. If you want to
	 * change the behavior look at @Before and other annotations in JUnit
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// employeeDB = new SimpleEmployeeDB();By setting the employeeDB
		// to either the server implementation first, you can write the server
		// implementation and test it. You dont even have to run the Jetty
		// HTTP server, you can just implement SimpleEmployeeDB and test it
		//
		// employeeDB = new EmployeeDBHTTPClient(); Once you have written the
		// client you can test the full system for the same test cases and it
		// should work, welcome to Unit testing :-)
		//
	}

	/**
	 * This method is run just once after the test cases finish to cleanup. If
	 * you want to change the behavior look at @After and other annotations in
	 * JUnit
	 */
	@AfterClass
	public static void tearDownAfterClass() {

	}
}
