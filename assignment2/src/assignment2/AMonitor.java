package assignment2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AMonitor implements Monitor, Runnable {
	
	private ConcurrentLinkedQueue<SensorReading> pendingReadings = new ConcurrentLinkedQueue<SensorReading>();
	private List<SensorReading> currentReadings = new ArrayList<SensorReading>();
	private ConcurrentHashMap<Integer, List<Subscriber>> subscribers = new ConcurrentHashMap<Integer, List<Subscriber>>();
	private Lock internallock;
	private Condition readingsEmpty;
	
	private final int MAX_CURRENT_READINGS = 10;
	private final int MIN_DISCOMFORT_LEVEL = 0;
	private final int MAX_DISCOMFORT_LEVEL = 5;
	
	public AMonitor() {
		for (int i = this.MIN_DISCOMFORT_LEVEL; i < this.MAX_DISCOMFORT_LEVEL + 1; i++) {
			this.subscribers.put(i, new ArrayList<Subscriber>());
		}
		this.internallock = new ReentrantLock();
		this.readingsEmpty = this.internallock.newCondition();
	}
	
	@Override
	public synchronized void pushReading(SensorReading sensorInput) {
		System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " invoked pushReading on AMonitor "+ this + ".");
		this.pendingReadings.add(sensorInput);
		this.readingsEmpty.signal();
	}

	@Override
	public void processReading(SensorReading sensorInput) {
		System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " going to process sensor reading.");
		this.currentReadings.add(sensorInput);
		if (this.currentReadings.size() > this.MAX_CURRENT_READINGS) {
			this.currentReadings.remove(0);
		}
		float h_average = 0.0f;
		float t_average = 0.0f;
		for (SensorReading sr : this.currentReadings) {
			h_average += sr.getHumidity();
			t_average += sr.getTemperature();
		}
		h_average /= this.currentReadings.size();
		t_average /= this.currentReadings.size();
		int discomfortLevel = this.GetDiscomfortLevel(h_average, t_average);
		
		System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " going to push discomfort warnings.");
		// Iterate over the hashtable.
		Iterator<Entry<Integer, List<Subscriber>>> it = this.subscribers.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Integer, List<Subscriber>> entry = it.next();
			if (entry.getKey() >= discomfortLevel) {
				// Iterate over the list.
				for (Subscriber s : entry.getValue()) {
					s.pushDiscomfortWarning(discomfortLevel);
				}
			}
		}
	}

	@Override
	public synchronized void registerSubscriber(int discomfortLevel, Subscriber subscriber) {
		System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " invoked registerSubscriber on AMonitor "+ this + ".");
		if (!this.subscribers.get(discomfortLevel).contains(subscriber)) {
			this.subscribers.get(discomfortLevel).add(subscriber);
		}
	}

	@Override
	public SensorReading getSensorReading() {
		System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " going to get sensor reading.");
		try {
			System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " locking internal lock.");
			this.internallock.lock();
			while (this.pendingReadings.isEmpty()) {
				System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " going to wait (there were no pending readings).");
				this.readingsEmpty.await();
				System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " was signalled.");
			}
			System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " finished waiting (now there are pending readings).");
			SensorReading result = this.pendingReadings.poll();
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} finally {
			this.internallock.unlock();
		}
	}

	public void run() {
		System.out.println("AMonitor: Thread " + Thread.currentThread().getId() + " started.");
		SensorReading sensorInput = null;
		while(true) {
			sensorInput = getSensorReading();
			this.processReading(sensorInput);
		}
	}
	
	private int GetDiscomfortLevel(float t, float h) {
		int t_discomfort;
		int h_discomfort;
		
		if (t < 10.0f) {
			t_discomfort = 0;
		} else if (t < 20.0f) {
			t_discomfort = 1;
		} else if (t < 30.0f) {
			t_discomfort = 2;
		} else if (t < 40.0f) {
			t_discomfort = 3;
		} else if (t < 50.0f) {
			t_discomfort = 4;
		} else {
			t_discomfort = 5;
		}
		
		if (h < 50.0f) {
			h_discomfort = 0;
		} else if (h < 60.0f) {
			h_discomfort = 1;
		} else if (h < 70.0f) {
			h_discomfort = 2;
		} else if (h < 80.0f) {
			h_discomfort = 3;
		} else if (h < 90.0f) {
			h_discomfort = 4;
		} else {
			h_discomfort = 5;
		}
		return Math.max(t_discomfort, h_discomfort);
	}
	
}
