package main;

import java.rmi.RemoteException;
import java.util.Map;

import exception.GameFullException;
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
			Map<String, IJeu> jeux = serveur.listerJeux();
			IJeu jeu = jeux.get("ALMA");
			Joueur joueur = new Joueur("Guest");
			System.err.println("Je rejoinds une partie");
			jeu.rejoindre(joueur);
			System.err.println(String.format("La partie a commencé et j'ai %d cartes", joueur.getMain().size()));
		} catch (RemoteException | GameFullException e) {
			e.printStackTrace();
		}
	}
}
