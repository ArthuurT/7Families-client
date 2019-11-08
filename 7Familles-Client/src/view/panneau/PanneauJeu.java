package view.panneau;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.rmi.RemoteException;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import model.Player;

public class PanneauJeu extends JPanel {
	
	private Player player;
	
	private JTextArea logs;
	private JList<String> hand;
	private PanneauJoueurs players;
	

	public PanneauJeu(Player player) {
		this.player = player;
		this.logs = new JTextArea(5,20);
		this.hand = null;
		this.players = null;
		try {
			this.hand = new JList<String>(player.getCards().stream().map(card -> card.toString()).toArray(String[] :: new));
			this.players = new PanneauJoueurs(player,player.getOpponents());
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
		
		this.setLayout(null);
		this.setSize(new Dimension(600,400));
		int margin = 25;		
		
		TitledBorder tbp = BorderFactory.createTitledBorder("Players families");
		this.players.setBorder(tbp);
		this.add(players);
		this.players.setBounds(0 + margin, 0 + margin, 600 - 2*margin, 200 - 2*margin);
		
		JScrollPane spl = new JScrollPane(logs,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		spl.setBounds(0 + margin, 200 + margin, 300 - margin, 200 - 2*margin);
		TitledBorder tbl = BorderFactory.createTitledBorder("Logs");
		spl.setBorder(tbl);
		this.add(spl);
		
		JScrollPane sph = new JScrollPane(hand,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sph.setBounds(300 + margin, 200 + margin, 300 - 2*margin, 200 - 2*margin);
		TitledBorder tbh = BorderFactory.createTitledBorder("Hand");
		sph.setBorder(tbh);
		this.add(sph);
		
	}
	
	public void updateFamilies() {
		try {
			this.players.updateFamilies();
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void updateHand(){
		try {
			this.hand.setListData(player.getCards().stream().map(card -> card.toString()).toArray(String[] :: new));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
