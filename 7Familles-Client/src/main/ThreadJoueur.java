package main;

import java.rmi.RemoteException;

import remote.IJeu;
import remote.IServeur;
import model.Joueur;

public class ThreadJoueur extends Thread {

	private IServeur serveur;
	
	public ThreadJoueur(IServeur serveur) {
		this.serveur = serveur;
	}
	
	@Override
	public void run() {
		try {
			Joueur joueur = new Joueur("Guest");
			System.err.println("Je rejoinds une partie");
			IJeu jeu = this.serveur.join(joueur);
			System.err.println(String.format("La partie a commencé et j'ai %d cartes", joueur.getMain().size()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
