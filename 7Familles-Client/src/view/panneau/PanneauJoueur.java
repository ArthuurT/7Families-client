package view.panneau;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import remote.IPlayer;

public class PanneauJoueur extends JPanel {

	private JLabel name;
	private JList<String> families;
	private IPlayer player;
	
	
	public PanneauJoueur(IPlayer player) throws RemoteException, InterruptedException {
		
		this.player = player;
		this.name = new JLabel(player.pseudo());
		this.families = new JList<String>(player.getCreatedFamilies()
						.stream()
						.map((family) -> family.toString())
						.toArray(String[] :: new));
		
		this.setLayout(new BorderLayout());
		this.add(this.name,BorderLayout.NORTH);
		JScrollPane js = new JScrollPane(families);
		//families.setPreferredSize(new Dimension(200, 200));
		this.add(js,BorderLayout.CENTER);
	}
	
	public void updateFamilies() throws RemoteException, InterruptedException {
		this.families.setListData(player.getCreatedFamilies()
						.stream()
						.map((family) -> family.toString())
						.toArray(String[] :: new));
	}
	
}
