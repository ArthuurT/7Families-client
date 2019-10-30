package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class BoutonSelectionServeurs extends JButton implements ActionListener{
	
	private static final long serialVersionUID = 1L;

	public BoutonSelectionServeurs() {
		this.addActionListener(this);
		this.setText("Selection des serveurs");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Change window	
	}

}
