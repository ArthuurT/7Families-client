package main;

import model.BoundedBuffer;

/**
 * 
 * This class is used to process messages sent by other players. Players communicate 
 * with each other thanks to a bounded buffer.
 * Messages are used to inform waiting players of the evolution of the game.
 *
 */
public class ThreadMessageReader extends Thread {

	private BoundedBuffer tampon;
	
	public ThreadMessageReader(BoundedBuffer tampon) {
		this.tampon = tampon;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				String message = this.tampon.read();
				System.err.println(message);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
