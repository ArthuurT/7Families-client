package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServeur extends Remote {
	
	public IJeu join(IJoueur joueur) throws RemoteException;
}
