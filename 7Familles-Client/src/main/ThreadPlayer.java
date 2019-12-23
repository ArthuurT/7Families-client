package main;

import remote.IGame;
import remote.IPlayer;
import remote.IServer;
import model.Card;
import model.Family;
import model.Player;
import model.Status;
import model.BoundedBuffer;

/**
 * 
 * This class is the main class of the player. It's in charge of running the logic of the game :
 * - picking a card
 * - asking a card to another player
 * - creating families
 *
 */
public class ThreadPlayer extends Thread {

	private IServer server;
	private CommandLineInterface console;
	
	public ThreadPlayer(IServer server) {
		this.server = server;
		this.console = new CommandLineInterface();
	}
	
	@Override
	public void run() {
		try {
			String pseudo = this.console.readPseudo();
			BoundedBuffer tampon = new BoundedBuffer(20);
			Player player = new Player(pseudo, tampon);
			
			int numberOfPlayers = this.console.selectNumberOfPlayers();

			ThreadMessageReader messageReader = new ThreadMessageReader(tampon);
			messageReader.start();
			
			IGame game = this.server.searchGame(numberOfPlayers, player, tampon);
			
			player.sendLocalMessage("La partie commence");
			
			while (true) {
				game.waitTurnOf(player);
				// the player can be woken up for two reasons : it's his turn or the game is over 
				if (game.isGameOver()) {
					break;
				}
				player.sendBroadcastMessage("C'est à moi de jouer");
				boolean error = false; // true if the player has asked a card that the opponent did not have 
									   // or if the card he has picked is not the card he was looking for, false otherwise
				boolean newFamilyCreated = false; // true if the player has created a new family, false otherwise
				while (!error && !newFamilyCreated) {
					this.console.printCards(player.getCards());
					if (player.getCards().size() == 0) { // if the player's hand is empty, he picks a card and pass his turn
						Card card = game.pickCard();
						if (card != null) {
							player.giveCard(card);
							this.console.printCards(player.getCards());
						} else {
							player.sendBroadcastMessage("La pioche est vide...");
							error = true;
						}
					} else {
						Family family = this.console.selectFamily(player.getCards());
						Status status = this.console.selectFamilyMember(family, player.getCards());
						IPlayer opponent = this.console.selectOpponent(player.getOpponents());
						player.sendBroadcastMessage(String.format("Je demande la carte (%s, %s) à %s", family, status, opponent.getPseudo()));
						Card card = opponent.requestCard(family, status);
						if (card != null) { // the opponent has the card he's looking for
							player.sendBroadcastMessage(String.format("%s m'a donné la carte (%s, %s)", opponent.getPseudo(), family, status));
							player.giveCard(card);
							this.console.printCards(player.getCards());
							newFamilyCreated = player.tryCreateFamily(card);
							if (newFamilyCreated) { // the card he received allowed him to create a family
								player.sendBroadcastMessage(String.format("J'ai réuni la famille %s", family));
								this.console.printCards(player.getCards());
							}
						} else { // the opponent has not the card he's looking for
							player.sendBroadcastMessage(String.format("%s n'a pas la carte (%s, %s)", opponent.getPseudo(), family, status));
							error = true;
						}
						if (error) { // the player must pick a card if he has asked a card that the opponent did not have
							player.sendBroadcastMessage("Je pioche une carte");
							card = game.pickCard();
							if (card != null) {
								player.sendLocalMessage(String.format("Vous avez pioché la carte (%s, %s)", card.getFamily(), card.getStatus()));
								player.giveCard(card);
								this.console.printCards(player.getCards());
								if (card.getFamily().equals(family) && card.getStatus().equals(status)) { // the card he picked is the one he was looking for
									player.sendBroadcastMessage("Bonne pioche !");
									error = false; // if the player picked the card he was looking for, he can play again
								} else {
									player.sendBroadcastMessage("Mauvaise pioche !");
								}
								newFamilyCreated = player.tryCreateFamily(card);
								if (newFamilyCreated) { // the card he received allowed him to create a family
									player.sendBroadcastMessage(String.format("J'ai réuni la famille %s", family));
									this.console.printCards(player.getCards());
								}
							} else {
								player.sendBroadcastMessage("La pioche est vide...");
							}
						}
					}
				}
				game.nextTurn();
				player.sendBroadcastMessage("J'ai terminé mon tour");
			}
			player.sendLocalMessage("La partie est terminée");
			if (game.isWinner(player)) {
				player.sendLocalMessage("Vous avez gagné !");
			} else {
				player.sendLocalMessage("Vous avez perdu...");
			}
			System.exit(0);
		} catch (Exception exception) {
			System.err.println("Une erreur est survenue, vous avez été déconnecté");
			System.exit(1);
		}
	}
}
