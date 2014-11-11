package assignment2;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ASensor implements Sensor, Runnable {
	
	//A sensor can push readings to one or many monitors
	private ConcurrentLinkedQueue<Monitor> monitors = new ConcurrentLinkedQueue<Monitor>();
	private Random r = new Random(System.currentTimeMillis());
	
	public SensorReading generateSensorReading() {
		SensorReading sr = new SensorReading();
		//Random r = new Random();
		float r1 = r.nextFloat();
		float r2 = r.nextFloat();
		float t = r1 * 60.0f; // From 0.0 to 60.0
		float h = r2 * 60.0f + 40.0f; // From 40.0 to 100.0
		sr.setHumidity(h);
		sr.setTemperature(t);
		return sr;
	}

	public void run() {
		System.out.println("ASensor: Thread " + Thread.currentThread().getId() + " started.");
		SensorReading reading = null;
		while(true) {
			reading = this.generateSensorReading();
			for (Monitor monitor : this.monitors) {
				monitor.pushReading(reading);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
		
	}

	@Override
	public void registerMonitor(List<Monitor> sm) {
		for (Monitor monitor : sm) {
			this.monitors.add(monitor);
		}
	}

}
