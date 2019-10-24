package main;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Joueur;
import remote.IJeu;

public class GameService extends Service<Void> {
	
	private IJeu jeu;
	private Joueur joueur;
	
	public GameService(IJeu jeu, Joueur joueur) {
		this.jeu = jeu;
		this.joueur = joueur;
	}

	@Override
	protected Task<Void> createTask() {
		return new GameTask(this.jeu,this.joueur);
	}
	

}
