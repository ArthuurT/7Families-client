package view.composants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import remote.IServer;
import view.panneau.PanneauAccueil;
import view.panneau.PanneauSelectionServeurs;

public class BoutonBack extends JButton implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private IServer serveur;

	public BoutonBack(JFrame frame, IServer serveur) {
		this.frame = frame;
		this.serveur = serveur;
		this.setText("Retour");
		this.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.getContentPane().setVisible(false);
		frame.getContentPane().removeAll();
		frame.setContentPane(new PanneauAccueil(frame,serveur));
	}

}
