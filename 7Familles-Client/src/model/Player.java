package model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import remote.IPlayer;

public class Player extends UnicastRemoteObject implements IPlayer, Serializable {

	private static final long serialVersionUID = -8447885863066141486L;
	
	private String pseudo;
	private List<Card> cards;
	private List<IPlayer> opponents;
	private List<Family> families;
	private LocalDateTime lastFamilyCreatedDate;
	
	private ReadWriteLock lock = new ReadWriteLock();
	
	public Player(String pseudo) throws RemoteException {
		super();
		this.pseudo = pseudo;
		this.cards = new ArrayList<Card>();
		this.opponents = new ArrayList<IPlayer>();
		this.families = new ArrayList<Family>();
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
	public void registerOpponent(IPlayer opponent) throws RemoteException {
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
	
	public String getPseudo() {
		return this.pseudo;
	}
	
	public List<Card> getCards() throws InterruptedException {
		this.lock.startWrite();
		List<Card> cards = this.cards;
		this.lock.endWrite();
		return cards;
	}
	
	public List<IPlayer> getOpponents() {
		return this.opponents;
	}

	public boolean tryCreateFamily(Card lastCardReceived) throws InterruptedException {
		this.lock.startWrite();
		boolean newFamilyCreated = false;
		Long nbCartes = this.cards
			.stream()
			.filter((Card c) -> lastCardReceived.getFamily().equals(c.getFamily()))
			.count();
		if (nbCartes == 6) {
			this.cards.removeIf((Card c) -> lastCardReceived.getFamily().equals(c.getFamily()));
			this.families.add(lastCardReceived.getFamily());
			newFamilyCreated = true;
		}
		this.lock.endWrite();
		return newFamilyCreated;
	}
	
	@Override
	public String pseudo() throws RemoteException {
		return this.pseudo;
	}
}
