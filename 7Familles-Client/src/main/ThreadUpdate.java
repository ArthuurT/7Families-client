package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import view.panneau.PanneauJeu;

public class ThreadUpdate extends Thread {

	private JFrame frame;
	
	public ThreadUpdate(JFrame frame) {
		this.frame = frame;
	}
	
	@Override
	public void run() {
		
		while(true) {
			
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	((PanneauJeu) frame.getContentPane()).updateFamilies();
	            	((PanneauJeu) frame.getContentPane()).updateHand();
	            }
	        });
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
	}
		
}
