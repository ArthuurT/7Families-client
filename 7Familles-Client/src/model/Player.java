package model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import remote.IPlayer;
import remote.IBoundedBuffer;

/**
 * 
 * This class contains all player's information and it's used as an interface to communicate
 * with other players and with the server.
 * Because the refresh of the UI interface is done periodically, this information is more often read than updated.
 * Consequently, to improve performance by allowing multiple read accesses at the same time, we use a ReadWriteLock 
 * rather than a simple ReentrantLock.
 *
 */
public class Player extends UnicastRemoteObject implements IPlayer, Serializable {
	
	private static final long serialVersionUID = -8447885863066141486L;
	
	private String pseudo;
	private List<Card> cards;
	private List<Opponent> opponents;
	private List<Family> families;
	private LocalDateTime lastFamilyCreatedDate; // used to decide which player won when two players have the same number of families
	private BoundedBuffer messageBuffer;
	
	private ReadWriteLock lock = new ReadWriteLock();
	
	public Player(String pseudo, BoundedBuffer messageBuffer) throws RemoteException {
		super();
		this.pseudo = pseudo;
		this.cards = new ArrayList<Card>();
		this.opponents = new ArrayList<Opponent>();
		this.families = new ArrayList<Family>();
		this.messageBuffer = messageBuffer;
	}
	
	public List<Card> getCards() throws InterruptedException {
		this.lock.startWrite();
		List<Card> cards = this.cards;
		this.lock.endWrite();
		return cards;
	}
	
	public List<IPlayer> getOpponents() {
		return this.opponents.stream().map((Opponent opponent) -> {
			return opponent.remote;
		}).collect(Collectors.toList());
	}

	public BoundedBuffer getMessageBuffer() {
		return this.messageBuffer;
	}
	
	private String formatMessage(String message, boolean local) {
		if (local) {
			return String.format(">>> %s", message);
		}
		return String.format("[%s] > %s", this.pseudo, message);
	}
	
	public void sendLocalMessage(String message) throws InterruptedException {
		this.messageBuffer.write(this.formatMessage(message, true));
	}
	
	public void sendBroadcastMessage(String message) throws InterruptedException, RemoteException {
		this.sendLocalMessage(message);
		for (Opponent opponent : this.opponents) {
			opponent.tampon.write(this.formatMessage(message, false));
		}
	}
	
	@Override
	public Card requestCard(Family family, Status status) throws RemoteException, InterruptedException {
		this.lock.startWrite();
		Optional<Card> card = this.cards.stream().filter((Card c) -> {
			return c.getFamily().equals(family) && c.getStatus().equals(status);
		}).findFirst();
		if (card.isPresent()) {
			this.cards.remove(card.get());
		}
		this.lock.endWrite();
		return card.isPresent() ? card.get() : null;
	}

	@Override
	public void giveCard(Card card) throws RemoteException, InterruptedException {
		this.lock.startWrite();
		this.cards.add(card);
		this.lock.endWrite();
	}
	
	@Override
	public void registerOpponent(IPlayer iOpponent, IBoundedBuffer iTampon) throws RemoteException {
		Opponent opponent = new Opponent(iOpponent, iTampon);
		this.opponents.add(opponent);
	}

	@Override
	public List<Family> getCreatedFamilies() throws RemoteException, InterruptedException {
		this.lock.startRead();
		List<Family> families = this.families;
		this.lock.endRead();
		return families;
	}
	
	@Override
	public LocalDateTime getLastFamilyCreatedDate() throws RemoteException, InterruptedException {
		this.lock.startRead();
		LocalDateTime date = this.lastFamilyCreatedDate;
		this.lock.endRead();
		return date;
	}
	
	@Override
	public String getPseudo() throws RemoteException {
		return this.pseudo;
	}

	public boolean tryCreateFamily(Card lastCardReceived) throws InterruptedException {
		this.lock.startWrite();
		boolean newFamilyCreated = false;
		Long numberOfCards = this.cards
			.stream()
			.filter((Card c) -> lastCardReceived.getFamily().equals(c.getFamily()))
			.count();
		if (numberOfCards == 6) {
			this.cards.removeIf((Card c) -> lastCardReceived.getFamily().equals(c.getFamily()));
			this.families.add(lastCardReceived.getFamily());
			newFamilyCreated = true;
		}
		this.lock.endWrite();
		return newFamilyCreated;
	}
}
