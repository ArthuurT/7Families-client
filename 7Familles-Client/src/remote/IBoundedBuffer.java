package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBoundedBuffer extends Remote {

	public void write(String message) throws RemoteException, InterruptedException;
	
	public String read() throws RemoteException, InterruptedException;
}
