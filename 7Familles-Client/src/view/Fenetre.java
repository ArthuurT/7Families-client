package view;

import javax.swing.JFrame;

import remote.IServer;
import view.panneau.PanneauAccueil;

public class Fenetre extends JFrame {

	private static final long serialVersionUID = 1L;
	private PanneauAccueil accueil;

	public Fenetre(IServer serveur) {
			this.setTitle("7Families");
			this.setSize(600,400);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			this.accueil = new PanneauAccueil(this,serveur);
			this.setContentPane(accueil);
	}
	
}
