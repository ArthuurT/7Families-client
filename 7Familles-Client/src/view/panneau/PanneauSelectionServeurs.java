package view.panneau;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import remote.IServeur;
import view.composants.BoutonBack;
import view.composants.BoutonPlay;
import view.composants.ListeServeurs;

public class PanneauSelectionServeurs extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField name;
	private JScrollPane scroll;
	private ListeServeurs servers;
	private BoutonBack back;
	private BoutonPlay play;
	private IServeur serveur;
	private JLabel loading;
	
	public PanneauSelectionServeurs(JFrame frame, JPanel panel, IServeur serveur) throws RemoteException {
		this.label = new JLabel("Nom du joueur: ");
		this.name = new JTextField();
		this.servers = new ListeServeurs(serveur.listerJeux());
		this.scroll = new JScrollPane(this.servers);
		this.back = new BoutonBack(frame,serveur);
		this.serveur = serveur;
		this.loading = new JLabel("Chargement de la partie...");
		this.play = new BoutonPlay(frame,serveur,name,servers,loading);
		
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
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(this.loading,gbc);
		this.loading.setVisible(false);
		gbc.gridx = 2;
		gbc.gridy = 2;
		this.add(this.play,gbc);
		
		
		DocumentListener documentListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				boolean enable = !servers.isSelectionEmpty() && name.getDocument().getLength() != 0;
		        play.setEnabled(enable);	
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				boolean enable = !servers.isSelectionEmpty() && name.getDocument().getLength() != 0;
		        play.setEnabled(enable);	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				boolean enable = !servers.isSelectionEmpty() && name.getDocument().getLength() != 0;
		        play.setEnabled(enable);
			}
		};
		
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				boolean enable = !servers.isSelectionEmpty() && name.getDocument().getLength() != 0;
		        play.setEnabled(enable);
			}	
		};
		
		this.servers.addListSelectionListener(listSelectionListener);
		this.name.getDocument().addDocumentListener(documentListener);
		
	}

	
	
}
