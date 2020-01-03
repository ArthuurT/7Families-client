package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import model.BoundedBuffer;
import view.panneau.PanneauJeu;

/**
 * 
 * This class is used to process messages sent by other players. Players communicate 
 * with each other thanks to a bounded buffer.
 * Messages are used to inform waiting players of the evolution of the game.
 *
 */
public class ThreadMessageReaderUI extends Thread {

	private BoundedBuffer tampon;
	private JFrame frame;
	
	public ThreadMessageReaderUI(BoundedBuffer tampon, JFrame frame) {
		this.tampon = tampon;
		this.frame = frame;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String message = this.tampon.read();
	
				SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
		            	((PanneauJeu) frame.getContentPane()).appendText(message);
		            }
		        });
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
