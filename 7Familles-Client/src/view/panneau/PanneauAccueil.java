package view.panneau;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import remote.IServer;
import view.composants.BoutonQuitter;
import view.composants.BoutonSelectionServeurs;

public class PanneauAccueil extends JPanel {

	private static final long serialVersionUID = 1L;
	private BoutonSelectionServeurs select;
	private BoutonQuitter quitter;
	
	public PanneauAccueil(JFrame frame, IServer serveur) {
		this.quitter = new BoutonQuitter();
		this.select = new BoutonSelectionServeurs(frame,this,serveur);
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		this.add(select,c);
		c.gridx = 1;
		this.add(quitter,c);	
	}
	
}
