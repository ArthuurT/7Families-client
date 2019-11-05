package main;

import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import exception.GameFullException;
import model.Joueur;
import remote.IJeu;
import remote.IServeur;
import view.panneau.PanneauAccueil;
import view.panneau.PanneauJeu;

public class ThreadJeu extends Thread implements Runnable {
	
	private IServeur serveur;
	private JPanel panel;
	private JFrame frame;
	private String nom_joueur;
	private String nom_jeu;

	public ThreadJeu(IServeur serveur, JFrame frame, JPanel panel, String nom_joueur, String nom_jeu) {
		this.serveur = serveur;
		this.panel = panel;
		this.frame = frame;
		this.nom_jeu = nom_jeu;
		this.nom_joueur = nom_joueur;
	}
	
	@Override
	public void run() {
		
		/******** CONNEXION AU SERVEUR *********/
		
		try {
			IJeu jeu = serveur.listerJeux().get(this.nom_jeu);
			jeu.rejoindre(new Joueur(this.nom_joueur));
		} catch (RemoteException | GameFullException e) {
			e.printStackTrace();
		}
		
		/******** ENTRE DANS LE JEU *********/
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	frame.getContentPane().setVisible(false);
    			frame.getContentPane().removeAll();
    			frame.setContentPane(panel);
            }
        });	
	}
	
}
