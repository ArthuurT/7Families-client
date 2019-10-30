package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BoutonQuitter extends JButton implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	public BoutonQuitter() {
		this.addActionListener(this);
		this.setText("Quitter");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Remove window
		
	}

	
}
