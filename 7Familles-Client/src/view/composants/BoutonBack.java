package view.composants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoutonBack extends JButton implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private JPanel panel;

	public BoutonBack(JFrame frame, JPanel panel) {
		this.frame = frame;
		this.panel = panel;
		this.setText("Retour");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		frame.getContentPane().setVisible(false);
		frame.getContentPane().removeAll();
		frame.setContentPane(panel);
	}

}
