package view.panneau;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
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

import main.ThreadJeu;
import model.Player;
import remote.IServer;
import view.composants.BoutonBack;
import view.composants.BoutonNombre;
import view.composants.BoutonPlay;

public class PanneauSelectionServeurs extends JPanel{

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField name;
	private BoutonBack back;
	private BoutonPlay play;
	private BoutonNombre two;
	private BoutonNombre three;
	private BoutonNombre four;
	private BoutonNombre five;
	private BoutonNombre six;
	private IServer serveur;
	private JLabel loading;
	
	public PanneauSelectionServeurs(JFrame frame, JPanel panel, IServer serveur) throws RemoteException {
		this.label = new JLabel("Nom du joueur: ");
		this.name = new JTextField();
		this.two = new BoutonNombre("2 players",2);
		this.three = new BoutonNombre("3 players",3);
		this.four = new BoutonNombre("4 players",4);
		this.five = new BoutonNombre("5 players",5);
		this.six = new BoutonNombre("6 players",6);
		this.back = new BoutonBack(frame,serveur);
		this.serveur = serveur;
		this.loading = new JLabel("Chargement de la partie...");
		
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
		this.add(this.two,gbc);
		this.two.setEnabled(false);
		gbc.gridx = 1;
		gbc.gridy = 2;
		this.add(this.three,gbc);
		this.three.setEnabled(false);
		gbc.gridx = 1;
		gbc.gridy = 3;
		this.add(this.four,gbc);
		this.four.setEnabled(false);
		gbc.gridx = 1;
		gbc.gridy = 4;
		this.add(this.five,gbc);
		this.five.setEnabled(false);
		gbc.gridx = 1;
		gbc.gridy = 5;
		this.add(this.six,gbc);
		this.six.setEnabled(false);
		gbc.gridx = 0;
		gbc.gridy = 6;
		this.add(this.back,gbc);
		gbc.gridx = 1;
		gbc.gridy = 6;
		this.add(this.loading,gbc);
		this.loading.setVisible(false);
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loading.setVisible(true);
				Player j = null;
				try {
					j = new Player(name.getText());
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				new ThreadJeu(serveur,frame,j,((BoutonNombre) e.getSource()).getValue()).start();
			}	
		};
		
		name.getDocument().addDocumentListener(new DocumentListener() {
			Boolean enable;
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				enable = name.getDocument().getLength() != 0;
				two.setEnabled(enable);
				three.setEnabled(enable);
				four.setEnabled(enable);
				five.setEnabled(enable);
				six.setEnabled(enable);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				enable = name.getDocument().getLength() != 0;
				two.setEnabled(enable);
				three.setEnabled(enable);
				four.setEnabled(enable);
				five.setEnabled(enable);
				six.setEnabled(enable);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				enable = name.getDocument().getLength() != 0;
				two.setEnabled(enable);
				three.setEnabled(enable);
				four.setEnabled(enable);
				five.setEnabled(enable);
				six.setEnabled(enable);
			}	
		});
		
		this.two.addActionListener(al);
		this.three.addActionListener(al);
		this.four.addActionListener(al);
		this.five.addActionListener(al);
		this.six.addActionListener(al);
				
	}

	
	
}
