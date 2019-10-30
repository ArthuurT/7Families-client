package view.composants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import remote.IServeur;
import view.panneau.PanneauSelectionServeurs;

public class BoutonSelectionServeurs extends JButton implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private JPanel panel;
	private IServeur serveur;

	public BoutonSelectionServeurs(JFrame frame, JPanel panel, IServeur serveur) {
		this.addActionListener(this);
		this.setText("Selection des serveurs");
		this.frame = frame;
		this.panel = panel;
		this.serveur = serveur;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		try {
			frame.getContentPane().setVisible(false);
			frame.getContentPane().removeAll();
			frame.setContentPane(new PanneauSelectionServeurs(frame,panel,serveur));
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
