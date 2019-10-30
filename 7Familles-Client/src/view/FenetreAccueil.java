package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FenetreAccueil extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private JPanel container = new JPanel();
	private BoutonSelectionServeurs select = new BoutonSelectionServeurs();
	private BoutonQuitter quitter = new BoutonQuitter();

	public FenetreAccueil() {
			this.setTitle("7Families");
			this.setSize(600,400);
			this.setResizable(false);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			
			container.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			container.add(select,c);
			c.gridx = 1;
			container.add(quitter,c);
			
			this.setContentPane(container);
	}
	
}
