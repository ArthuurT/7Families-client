package view.panneau;

import java.awt.Color;
import java.awt.GridLayout;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;

import model.Player;
import remote.IPlayer;

public class PanneauJoueurs extends JPanel{
	
	private List<PanneauJoueur> joueurs;

	public PanneauJoueurs(Player player, List<IPlayer> opponents) throws RemoteException, InterruptedException {
		this.joueurs = new ArrayList<PanneauJoueur>();
		this.setLayout(new GridLayout(1,opponents.size()+1,10,10));
		joueurs.add(new PanneauJoueur(player));
		for(IPlayer opponent : opponents) {
			joueurs.add(new PanneauJoueur(opponent));
		}
		for(PanneauJoueur p : joueurs) {
			this.add(p);
		}	
	}
	
	public void updateFamilies() throws RemoteException, InterruptedException {
		for(PanneauJoueur p : joueurs) {
			p.updateFamilies();
		}	
	}
	
}
