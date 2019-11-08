package view.composants;

import javax.swing.JButton;

public class BoutonNombre extends JButton{
	
	private int value;
	
	public BoutonNombre(String text, int value) {
		super(text);
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	
}
