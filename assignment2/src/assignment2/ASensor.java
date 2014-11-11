package assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ASensor implements Sensor, Runnable {
	
	//A sensor can push readings to one or many monitors
	private ThreadLocal<List<Monitor>> monitors = new ThreadLocal<List<Monitor>>();
	
	public ASensor() {
		List<Monitor> ms = new ArrayList<Monitor>();
		this.monitors.set(ms);
	}
	
	public SensorReading generateSensorReading() {
		SensorReading sr = new SensorReading();
		Random r = new Random();
		float t = r.nextFloat() * 60.0f; // From 0.0 to 60.0
		float h = r.nextFloat() * 60.0f + 40.0f; // From 40.0 to 100.0
		sr.setHumidity(h);
		sr.setTemperature(t);
		return sr;
	}

	public void run() {
		SensorReading reading = null;
		while(true) {
			reading = this.generateSensorReading();
			List<Monitor> ms = this.monitors.get();
			for (Monitor monitor : ms) {
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
		List<Monitor> ms = this.monitors.get();
		for (Monitor monitor : sm) {
			ms.add(monitor);
		}
		this.monitors.set(ms);
	}

}
