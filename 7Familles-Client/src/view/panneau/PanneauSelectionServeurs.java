package view.panneau;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import remote.IServeur;
import view.composants.BoutonBack;
import view.composants.BoutonPlay;
import view.composants.ListeServeurs;

public class PanneauSelectionServeurs extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField name;
	private JScrollPane scroll;
	private ListeServeurs servers;
	private BoutonBack back;
	private BoutonPlay play;
	private IServeur serveur;
	
	public PanneauSelectionServeurs(JFrame frame, JPanel panel, IServeur serveur) throws RemoteException {
		this.label = new JLabel("Nom du joueur: ");
		this.name = new JTextField();
		this.servers = new ListeServeurs(serveur.listerJeux());
		this.scroll = new JScrollPane(this.servers);
		this.back = new BoutonBack(frame,panel);
		this.play = new BoutonPlay(frame);
		this.serveur = serveur;
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(this.label,gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;
		this.name.setPreferredSize(new Dimension(300,24));
		this.add(this.name,gbc);
		gbc.gridx = 1;
		gbc.gridy = 1;
		this.scroll.setPreferredSize(new Dimension(300,200));
		this.add(this.scroll,gbc);
		gbc.gridx = 0;
		gbc.gridy = 2;
		this.add(this.back,gbc);
		gbc.gridx = 2;
		gbc.gridy = 2;
		this.add(this.play,gbc);
	}
	
	
	
}
