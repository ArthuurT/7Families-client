package main;

import java.rmi.RemoteException;
import java.util.Map;

import exception.GameFullException;
import remote.IJeu;
import remote.IJoueur;
import remote.IServeur;
import model.Carte;
import model.Famille;
import model.Joueur;
import model.Statut;

public class ThreadJoueur extends Thread {

	private IServeur serveur;
	private Controlleur controlleur;
	
	public ThreadJoueur(IServeur serveur) {
		this.serveur = serveur;
		this.controlleur = new Controlleur();
	}
	
	@Override
	public void run() {
		try {
			String pseudo = this.controlleur.saisirPseudo();
			Joueur joueur = new Joueur(pseudo);
			Map<String, IJeu> jeux = serveur.listerJeux();
			this.controlleur.afficherListeJeux(jeux);
			IJeu jeu = this.controlleur.selectionnerJeu(jeux);
			System.err.println("Connection à la partie, en attente de joueurs...");
			jeu.rejoindre(joueur);
			System.out.println("Début de la partie");
			while (!jeu.finPartie()) {
				jeu.jouer(joueur);
				if (jeu.finPartie()) {
					break;
				}
				System.err.println("C'est à vous de jouer !");
				boolean erreur = false;
				boolean nouvelleFamille = false;
				while (!erreur && !nouvelleFamille) {
					this.controlleur.afficherMain(joueur.getMain());
					if (joueur.getMain().size() == 0) {
						Carte carte = jeu.piocher();
						if (carte != null) {
							joueur.donner(carte);
							this.controlleur.afficherMain(joueur.getMain());
						} else {
							erreur = true;
						}
					} else {
						this.controlleur.afficherFamillesSelectionnables(joueur.getMain());
						Famille famille = this.controlleur.selectionnerFamille(joueur.getMain());
						this.controlleur.afficherStatutsSelectionnables(famille, joueur.getMain());
						Statut statut = this.controlleur.selectionnerStatut(famille, joueur.getMain());
						this.controlleur.afficherOpposants(joueur.getOpposants());
						IJoueur opposant = this.controlleur.selectionnerOpposant(joueur.getOpposants());
						Carte carte = opposant.demander(famille, statut);
						if (carte != null) {
							System.out.println("Demande correcte !");
							joueur.donner(carte);
							this.controlleur.afficherMain(joueur.getMain());
							nouvelleFamille = joueur.possiblePoserFamille(carte);
							if (nouvelleFamille) {
								System.out.println("Famille !");
								this.controlleur.afficherMain(joueur.getMain());
							}
						} else {
							System.out.println("Pioche !");
							erreur = true;
						}
						if (erreur) {
							carte = jeu.piocher();
							if (carte != null) {
								joueur.donner(carte);
								this.controlleur.afficherMain(joueur.getMain());
								if (carte.getFamille().equals(famille) && carte.getStatut().equals(statut)) {
									System.out.println("Bonne pioche !");
									erreur = false;
								} else {
									System.out.println("Mauvaise pioche !");
								}
								nouvelleFamille = joueur.possiblePoserFamille(carte);
								if (nouvelleFamille) {
									System.out.println("Famille !");
									this.controlleur.afficherMain(joueur.getMain());
								}
							}
						}
					}
				}
				jeu.passerTour();
				System.out.println("J'ai terminé mon tour");
			}
			System.out.println("Partie terminé");
			if (jeu.estGagnant(joueur)) {
				System.err.println("Vous avez gagné !");
			} else {
				System.err.println("Vous avez perdu");
			}
			jeu.quitter(joueur);
		} catch (RemoteException | GameFullException e) {
			e.printStackTrace();
		}
	}
}
