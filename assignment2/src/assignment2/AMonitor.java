package assignment2;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AMonitor implements Monitor, Runnable {
	
	private ThreadLocal<List<SensorReading>> pendingReadings = new ThreadLocal<List<SensorReading>>();
	private ThreadLocal<List<SensorReading>> currentReadings = new ThreadLocal<List<SensorReading>>();
	private ThreadLocal<Hashtable<Integer, List<Subscriber>>> subscribers = new ThreadLocal<Hashtable<Integer, List<Subscriber>>>();
	private ThreadLocal<Lock> internallock = new ThreadLocal<Lock>();
	private ThreadLocal<Condition> readingsEmpty = new ThreadLocal<Condition>();
	
	private final int MAX_CURRENT_READINGS = 10;
	private final int MIN_DISCOMFORT_LEVEL = 0;
	private final int MAX_DISCOMFORT_LEVEL = 5;
	
	public AMonitor() {
		this.pendingReadings.set(new ArrayList<SensorReading>());
		this.currentReadings.set(new ArrayList<SensorReading>());
		this.subscribers.set(new Hashtable<Integer, List<Subscriber>>());
		for (int i = this.MIN_DISCOMFORT_LEVEL; i < this.MAX_DISCOMFORT_LEVEL + 1; i++) {
			this.subscribers.get().put(i, new ArrayList<Subscriber>());
		}
		this.internallock.set(new ReentrantLock());
		this.readingsEmpty.set(this.internallock.get().newCondition());
	}
	
	@Override
	public void pushReading(SensorReading sensorInput) {
		this.pendingReadings.get().add(sensorInput);
		this.readingsEmpty.get().signal();
	}

	@Override
	public void processReading(SensorReading sensorInput) {
		this.currentReadings.get().add(sensorInput);
		if (this.currentReadings.get().size() > this.MAX_CURRENT_READINGS) {
			this.currentReadings.get().remove(0);
		}
		float h_average = 0.0f;
		float t_average = 0.0f;
		for (SensorReading sr : this.currentReadings.get()) {
			h_average += sr.getHumidity();
			t_average += sr.getTemperature();
		}
		h_average /= this.currentReadings.get().size();
		t_average /= this.currentReadings.get().size();
		int discomfortLevel = this.GetDiscomfortLevel(h_average, t_average);
		
		// Iterate over the hashtable.
		Iterator<Entry<Integer, List<Subscriber>>> it = this.subscribers.get().entrySet().iterator();
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
	public void registerSubscriber(int discomfortLevel, Subscriber subscriber) {
		if (!this.subscribers.get().get(discomfortLevel).contains(subscriber)) {
			this.subscribers.get().get(discomfortLevel).add(subscriber);
		}
	}

	@Override
	public SensorReading getSensorReading() {
		try {
			this.internallock.get().lock();
			while (this.pendingReadings.get().isEmpty()) {
				this.readingsEmpty.get().await();
			}
			SensorReading result = this.pendingReadings.get().get(0);
			this.pendingReadings.get().remove(0);
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} finally {
			this.internallock.get().unlock();
		}
	}

	public void run() {
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
