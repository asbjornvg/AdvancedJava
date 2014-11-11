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
		System.out.println("Main thread (thread " + Thread.currentThread().getId() + ") started.");
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
		sensors.get(0).registerMonitor(monitors.subList(0, 1));
		sensors.get(1).registerMonitor(monitors.subList(0, 2));
		sensors.get(2).registerMonitor(monitors.subList(1, 2));
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		for (int i = 0; i < 2; i++) {
			ASubscriber s = new ASubscriber();
			subscribers.add(s);
			exec.submit(s);
		}
		monitors.get(0).registerSubscriber(3, subscribers.get(0));
		monitors.get(1).registerSubscriber(2, subscribers.get(0));
		monitors.get(1).registerSubscriber(4, subscribers.get(1));
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.shutdown();
		System.out.println("Main thread (thread " + Thread.currentThread().getId() + ") has issued shutdown.");
	}

}
