package remote;

import java.rmi.RemoteException;

import model.Carte;

public interface IJeu {

	public void jouer(IJoueur joueur) throws RemoteException, InterruptedException;
	
	public Carte piocher(Carte expected) throws RemoteException;
}
