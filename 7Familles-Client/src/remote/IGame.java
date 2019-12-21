package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;

import model.Card;

public interface IGame extends Remote {
	
	public void waitTurnOf(IPlayer iPlayer) throws RemoteException, InterruptedException, UnexpectedException;
	
	public Card pickCard() throws RemoteException;
	
	public void nextTurn() throws RemoteException, InterruptedException;
	
	public boolean isGameOver() throws RemoteException;
	
	public boolean isWinner(IPlayer iPlayer) throws RemoteException, InterruptedException;
}
