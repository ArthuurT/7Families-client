package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import exception.GameFullException;
import model.Carte;

public interface IJeu extends Remote {
	
	public void rejoindre(IJoueur joueur) throws RemoteException, GameFullException;
	
	public void jouer(IJoueur joueur) throws RemoteException;
	
	public Carte piocher() throws RemoteException;
	
	public void passerTour() throws RemoteException;
	
	public boolean finPartie() throws RemoteException;
	
	public boolean estGagnant(IJoueur joueur) throws RemoteException;
	
	public void quitter(IJoueur joueur) throws RemoteException;
}
