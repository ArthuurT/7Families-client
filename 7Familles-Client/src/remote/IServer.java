package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;

public interface IServer extends Remote {

	public IGame searchGame(int capacity, IPlayer player) throws IllegalArgumentException, UnexpectedException, RemoteException, InterruptedException;
}