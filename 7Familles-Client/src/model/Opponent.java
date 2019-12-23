package model;

import remote.IPlayer;
import remote.IBoundedBuffer;

public class Opponent {
	public IPlayer remote;
	public IBoundedBuffer tampon;
	
	public Opponent(IPlayer remote, IBoundedBuffer tampon) {
		this.remote = remote;
		this.tampon = tampon;
	}
}
