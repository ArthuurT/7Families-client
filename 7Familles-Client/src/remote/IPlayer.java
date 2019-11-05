package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

import model.Card;
import model.Family;
import model.Status;

public interface IPlayer extends Remote {
	
	public String pseudo() throws RemoteException;
	
	public void giveCard(Card card) throws RemoteException, InterruptedException;
		
	public void registerOpponent(IPlayer opponent) throws RemoteException;
	
	public Card requestCard(Family famille, Status statut) throws RemoteException, InterruptedException;
	
	public List<Family> getCreatedFamilies() throws RemoteException, InterruptedException;

	public LocalDateTime getLastFamilyCreatedDate() throws RemoteException, InterruptedException;
}
