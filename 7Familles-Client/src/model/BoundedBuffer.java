package model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import remote.IBoundedBuffer;

/**
 * 
 * This class is used to store messages sent by the other players. It use a bounded buffer
 * to store them. Players are producers and add their messages in the buffer. The ThreadMessageReader
 * is the consumer and process added messages.
 *
 */
public class BoundedBuffer extends UnicastRemoteObject implements IBoundedBuffer {

	private static final long serialVersionUID = -6806115573374875412L;
	
	private int size;
	private String[] buffer;
	private int count; // number of elements in the buffer
	private int insertIndex; // index to which the next element will be inserted
	
	private final Lock lock = new ReentrantLock();
	private final Condition nonFull = lock.newCondition();
	private final Condition nonEmpty = lock.newCondition();
	
	public BoundedBuffer(int size) throws RemoteException {
		super();
		this.size = size;
		this.buffer = new String[size];
		this.count = 0;
		this.insertIndex = 0;
	}
	
	@Override
	public void write(String message) throws InterruptedException {
		this.lock.lock();
		if (this.count == this.size) {
			this.nonFull.await();
		}
		this.buffer[this.insertIndex] = message;
		this.insertIndex = (this.insertIndex + 1) % this.size;
		this.count++;
		this.nonEmpty.signal();
		this.lock.unlock();
	}
	
	public String read() throws InterruptedException {
		this.lock.lock();
		if (this.count == 0) {
			this.nonEmpty.await();
		}
		String message = this.buffer[Math.floorMod(this.insertIndex - this.count, this.size)];
		this.count--;
		this.nonFull.signal();
		this.lock.unlock();
		return message;
	}
}
