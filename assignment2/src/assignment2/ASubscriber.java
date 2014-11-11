package assignment2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ASubscriber implements Subscriber, Runnable {
	
	private ThreadLocal<List<Integer>> pendingNotifications = new ThreadLocal<List<Integer>>();
	private ThreadLocal<Lock> internallock = new ThreadLocal<Lock>();
	private ThreadLocal<Condition> notificationsEmpty = new ThreadLocal<Condition>();
	
	public ASubscriber() {
		this.pendingNotifications.set(new ArrayList<Integer>());
		this.internallock.set(new ReentrantLock());
		this.notificationsEmpty.set(this.internallock.get().newCondition());
	}
	
	public void run() {
		while (true) {
			int discomfortLevel = this.getDiscomfortWarning();
			this.processDiscomfortWarning(discomfortLevel);
		}
	}

	@Override
	public void pushDiscomfortWarning(int discomfortlevel) {
		this.pendingNotifications.get().add(discomfortlevel);
		this.notificationsEmpty.get().signal();
	}

	@Override
	public void processDiscomfortWarning(int discomfortLevel) {
		System.out.println("Subscriber running in thread " + Thread.currentThread().getId() + ": Discomfort level is " + discomfortLevel);
	}

	@Override
	public int getDiscomfortWarning() {
		try {
			this.internallock.get().lock();
			while (this.pendingNotifications.get().isEmpty()) {
				this.notificationsEmpty.get().await();
			}
			int result = this.pendingNotifications.get().get(0);
			this.pendingNotifications.get().remove(0);
			return result;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return 0;
		} finally {
			this.internallock.get().unlock();
		}
	}
}
