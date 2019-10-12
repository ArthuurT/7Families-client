package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Carte;
import model.Famille;
import model.Familles;
import model.Statut;

public interface IJoueur extends Remote {
	
	public void donner(Carte carte) throws RemoteException;
	
	public void donner(List<Carte> cartes) throws RemoteException;
	
	public void definir(IJoueur opposant) throws RemoteException;
	
	public Carte demander(Famille famille, Statut statut) throws RemoteException;
	
	public Familles familles() throws RemoteException;
}
