package exception;

public class GameFullException extends Exception {

	private static final long serialVersionUID = -9043982804412507727L;

	public GameFullException(String message) {
		super(message);
	}
}
