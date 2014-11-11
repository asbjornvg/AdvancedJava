package assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SensorNetworkHarness {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService exec = Executors.newCachedThreadPool();
		List<Sensor> sensors = new ArrayList<Sensor>();
		for (int i = 0; i < 3; i++) {
			ASensor s = new ASensor();
			sensors.add(s);
			exec.submit(s);
		}
		List<Monitor> monitors = new ArrayList<Monitor>();
		for (int i = 0; i < 2; i++) {
			AMonitor m = new AMonitor();
			monitors.add(m);
			exec.submit(m);
		}
		for (int i = 0; i < 2; i++) {
			
		}
	}

}
