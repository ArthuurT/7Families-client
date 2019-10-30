package view.composants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class BoutonPlay extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFrame frame;

	public BoutonPlay(JFrame frame) {
		this.frame = frame;
		this.setText("Jouer");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

}
