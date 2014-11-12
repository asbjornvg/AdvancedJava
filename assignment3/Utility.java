/**
 * 
 */
package assignment3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper class to provide utility methods of reading the department and server
 * url mappings
 * 
 * @author bonii
 * 
 */
public final class Utility {

	/**
	 * Construct a map from department to server url mappings
	 */
	public static Map<Integer, String> getDepartmentToServerURLMapping(
			String filePath) throws FileNotFoundException {
		Map<String, List<Integer>> serverURLToDepartmentMap = getServerURLToDepartmentMapping(filePath);

		// Now construct a reverse map
		Map<Integer, String> departmentToServerURLMap = new ConcurrentHashMap<Integer, String>();
		for (String serverURL : serverURLToDepartmentMap.keySet()) {
			for (Integer departmentId : serverURLToDepartmentMap.get(serverURL)) {
				departmentToServerURLMap.put(departmentId, serverURL);
			}
		}
		return departmentToServerURLMap;
	}

	/**
	 * Constructs a map from server url to departments by looking up the xml
	 * mapping file
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<Integer>> getServerURLToDepartmentMapping(
			String filePath) throws FileNotFoundException {
		XStream xstream = new XStream(new StaxDriver());
		Map<String, List<Integer>> serverURLToDepartmentMap = (Map<String, List<Integer>>) xstream
				.fromXML(new FileInputStream(filePath));
		return serverURLToDepartmentMap;
	}
}
