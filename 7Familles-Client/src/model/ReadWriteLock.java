package model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteLock {
	private boolean busy = false;
	private int readerCount = 0;
	
	private int waitingOKToRead = 0;
	private int waitingOKToWrite = 0;
	
	private final Lock lock = new ReentrantLock();
	
	private final Condition OKToRead = lock.newCondition();
	private final Condition OKToWrite = lock.newCondition();
	
	public void startRead() throws InterruptedException {
		this.lock.lock();
		try {
			if (this.busy || this.waitingOKToWrite > 0) {
				this.waitingOKToRead++;
				this.OKToRead.await();
				this.waitingOKToRead--;
			}
			this.readerCount++;
		} catch (InterruptedException e) {
			throw e;
		} finally {
			this.lock.unlock();
		}
	}
	
	public void endRead() {
		this.lock.lock();
		this.readerCount--;
		if (this.readerCount == 0) {
			this.OKToWrite.signal();
		}
		this.lock.unlock();
	}
	
	public void startWrite() throws InterruptedException {
		this.lock.lock();
		try {
			if (this.readerCount > 0 || this.busy) {
				this.waitingOKToWrite++;
				this.OKToWrite.await();
				this.waitingOKToWrite--;
			}
			this.busy = true;
		} catch (InterruptedException e) {
			throw e;
		} finally {
			this.lock.unlock();
		}
	}
	
	public void endWrite() {
		this.lock.lock();
		this.busy = false;
		if (this.waitingOKToRead > 0) {
			this.OKToRead.signalAll();;
		} else {
			this.OKToWrite.signal();
		}
		this.lock.unlock();
	}
}
