package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import model.Card;

public interface IGame extends Remote {
	
	public void waitTurnOf(IPlayer player) throws RemoteException, InterruptedException;
	
	public Card pickCard() throws RemoteException;
	
	public void nextTurn() throws RemoteException;
	
	public boolean isGameOver() throws RemoteException, InterruptedException;
	
	public boolean isWinner(IPlayer player) throws RemoteException, InterruptedException;
}
