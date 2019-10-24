package main;

import javafx.concurrent.Task;
import model.Joueur;
import remote.IJeu;

public class GameTask extends Task<Void> {
	
	private IJeu jeu;
	private Joueur joueur;
	
	public GameTask(IJeu jeu, Joueur joueur) {
		this.jeu = jeu;
		this.joueur = joueur;
	}

	@Override
	protected Void call() throws Exception {
		updateMessage(MessageType.REJOINDRE_PARTIE);
		System.out.println("Avant");
		this.jeu.rejoindre(joueur);
		System.out.println("Après");
		updateMessage(MessageType.DEBUT_PARTIE);
		return null;
	}

}
