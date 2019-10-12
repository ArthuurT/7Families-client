package remote;

import java.rmi.RemoteException;

import exception.GameFullException;
import model.Carte;
import model.Famille;
import model.Statut;

public interface IJeu {

	public void rejoindre(IJoueur joueur) throws RemoteException, GameFullException;
	
	public void jouer(IJoueur joueur) throws RemoteException, InterruptedException;
	
	public Carte piocher(Famille famille, Statut satut) throws RemoteException;
	
	public boolean finPartie() throws RemoteException;
}
