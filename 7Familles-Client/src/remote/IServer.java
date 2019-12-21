package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;

public interface IServer extends Remote {

	public IGame searchGame(int capacity, IPlayer iPlayer, IBoundedBuffer iTampon) throws IllegalArgumentException, UnexpectedException, RemoteException, InterruptedException;
}