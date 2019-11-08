package view.composants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import main.ThreadJeu;
import remote.IServer;
import view.panneau.PanneauJeu;

public class BoutonPlay extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private IServer serveur;
	private JTextField nom;
	private JLabel loading;

	public BoutonPlay(JFrame frame, IServer serveur, JTextField nom, JLabel loading) {
		this.frame = frame;
		this.serveur = serveur;
		this.nom = nom;
		this.setText("Jouer");
		this.setEnabled(false);
		this.addActionListener(this);
		this.loading = loading;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		this.loading.setVisible(true);
		//new ThreadJeu(serveur,frame,new PanneauJeu(),nom.getText(),(String)serveurs.getSelectedValue()).start();

	}

}
