package main;

import remote.IGame;
import remote.IPlayer;
import remote.IServer;
import model.Card;
import model.Family;
import model.Player;
import model.Status;

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
			Player player = new Player(pseudo);
			IGame game = this.server.searchGame(2, player);
			System.out.println("Début de la partie");
			while (!game.isGameOver()) {
				game.waitTurnOf(player);
				if (game.isGameOver()) {
					break;
				}
				this.console.printMessage("C'est à vous de jouer");
				boolean error = false;
				boolean newFamilyCreated = false;
				while (!error && !newFamilyCreated) {
					this.console.printCards(player.getCards());
					if (player.getCards().size() == 0) {
						Card carte = game.pickCard();
						if (carte != null) {
							player.giveCard(carte);
							this.console.printCards(player.getCards());
						} else {
							this.console.printMessage("La pioche est vide...");
							error = true;
						}
					} else {
						Family family = this.console.selectFamily(player.getCards());
						Status status = this.console.selectFamilyMember(family, player.getCards());
						IPlayer opponent = this.console.selectOpponent(player.getOpponents());
						Card card = opponent.requestCard(family, status);
						if (card != null) {
							this.console.printMessage(String.format("Vous avez reçu la carte (%s, %s), VOUS POUVEZ REJOUER !", card.getFamily(), card.getStatus()));
							player.giveCard(card);
							this.console.printCards(player.getCards());
							newFamilyCreated = player.tryCreateFamily(card);
							if (newFamilyCreated) {
								this.console.printMessage(String.format("BRAVO, vous venez de réunir la famille %s", card.getFamily()));
								this.console.printCards(player.getCards());
							}
						} else {
							this.console.printMessage(String.format("%s n'a pas la carte que vous avez demandé", opponent.pseudo()));
							error = true;
						}
						if (error) {
							card = game.pickCard();
							if (card != null) {
								player.giveCard(card);
								this.console.printCards(player.getCards());
								if (card.getFamily().equals(family) && card.getStatus().equals(status)) {
									this.console.printMessage(String.format("Vous avez piochez la carte (%s, %s), BONNE PIOCHE !", card.getFamily(), card.getStatus()));
									error = false;
								} else {
									this.console.printMessage(String.format("Vous avez piochez la carte (%s, %s), MAUVAISE PIOCHE...", card.getFamily(), card.getStatus()));
								}
								newFamilyCreated = player.tryCreateFamily(card);
								if (newFamilyCreated) {
									this.console.printMessage(String.format("BRAVO, vous venez de réunir la famille %s", card.getFamily()));
									this.console.printCards(player.getCards());
								}
							} else {
								this.console.printMessage("La pioche est vide...");
							}
						}
					}
				}
				game.nextTurn();
				this.console.printMessage("J'ai terminé mon tour !");
			}
			this.console.printMessage("La partie est terminée");
			if (game.isWinner(player)) {
				this.console.printMessage("VOUS AVEZ GAGNÉ !");
			} else {
				this.console.printMessage("Vous avez perdu...");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			this.console.printError("Une erreur est survenue, la partie a été coupée...");
		}
	}
}
