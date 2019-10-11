package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Carte;
import model.Familles;

public interface IJoueur extends Remote {
	
	public void donner(Carte carte) throws RemoteException;
	
	public void donner(List<Carte> cartes) throws RemoteException;
	
	public void definir(IJoueur opposant) throws RemoteException;
	
	public Carte demander(Carte expected) throws RemoteException;
	
	public Familles familles() throws RemoteException;
}
