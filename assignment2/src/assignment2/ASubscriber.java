package assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ASubscriber implements Subscriber, Runnable {
	
	private ConcurrentLinkedQueue<Integer> pendingNotifications = new ConcurrentLinkedQueue<Integer>();
	private Lock internallock;
	private Condition notificationsEmpty;
	
	public ASubscriber() {
		this.internallock = new ReentrantLock();
		this.notificationsEmpty = this.internallock.newCondition();
	}
	
	public void run() {
		while (true) {
			int discomfortLevel = this.getDiscomfortWarning();
			this.processDiscomfortWarning(discomfortLevel);
		}
	}

	@Override
	public void pushDiscomfortWarning(int discomfortlevel) {
		this.pendingNotifications.add(discomfortlevel);
		this.notificationsEmpty.signal();
	}

	@Override
	public void processDiscomfortWarning(int discomfortLevel) {
		System.out.println("Subscriber running in thread " + Thread.currentThread().getId() + ": Discomfort level is " + discomfortLevel);
	}

	@Override
	public int getDiscomfortWarning() {
		try {
			this.internallock.lock();
			while (this.pendingNotifications.isEmpty()) {
				this.notificationsEmpty.await();
			}
			int result = this.pendingNotifications.poll();
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return 0;
		} finally {
			this.internallock.unlock();
		}
	}
}
