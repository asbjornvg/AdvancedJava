package assignment2;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ASensor implements Sensor, Runnable {
	
	//A sensor can push readings to one or many monitors
	private ConcurrentLinkedQueue<Monitor> monitors = new ConcurrentLinkedQueue<Monitor>();
	
	public SensorReading generateSensorReading() {
		System.out.println("ASensor: Thread " + Thread.currentThread().getId() + " generating SensorReading.");
		SensorReading sr = new SensorReading();
		Random r = new Random();
		float t = r.nextFloat() * 60.0f; // From 0.0 to 60.0
		float h = r.nextFloat() * 60.0f + 40.0f; // From 40.0 to 100.0
		sr.setHumidity(h);
		sr.setTemperature(t);
		return sr;
	}

	public void run() {
		System.out.println("ASensor: Thread " + Thread.currentThread().getId() + " started.");
		SensorReading reading = null;
		while(true) {
			reading = this.generateSensorReading();
			System.out.println("ASensor: Thread " + Thread.currentThread().getId() + " going to notify " + this.monitors.size() + " monitor(s).");
			for (Monitor monitor : this.monitors) {
				monitor.pushReading(reading);
			}
			System.out.println("ASensor: Thread " + Thread.currentThread().getId() + " going to sleep.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
			System.out.println("ASensor: Thread " + Thread.currentThread().getId() + " woke up.");
		}
		
	}

	@Override
	public synchronized void registerMonitor(List<Monitor> sm) {
		System.out.println("ASensor: Thread " + Thread.currentThread().getId() + " invoked registerMonitor on ASensor "+ this + " with " + sm.size() + " monitor(s).");
		for (Monitor monitor : sm) {
			this.monitors.add(monitor);
		}
	}

}
