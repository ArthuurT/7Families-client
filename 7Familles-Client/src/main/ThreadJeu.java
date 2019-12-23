package main;

import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import model.Card;
import model.Family;
import model.Player;
import model.Status;
import remote.IGame;
import remote.IPlayer;
import remote.IServer;
import view.panneau.PanneauJeu;

public class ThreadJeu extends Thread implements Runnable {
	
	private IServer serveur;
	private JFrame frame;
	private Player player;
	private int nombre;
	private CommandLineInterface console;

	public ThreadJeu(IServer serveur, JFrame frame, Player player, int nombre) {
		this.serveur = serveur;
		this.frame = frame;
		this.nombre = nombre;
		this.player = player;
		this.console = new CommandLineInterface();
		this.frame.setAlwaysOnTop(true);
	}
	
	@Override
	public void run() {
		
		/******** CONNEXION AU SERVEUR *********/
		
		IGame game;
		
		try {
			
			game = this.serveur.searchGame(nombre,this.player);
			
		/******** MIS EN PLACE DU PLATEAU *********/
		
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	frame.getContentPane().setVisible(false);
    			frame.getContentPane().removeAll();
    			frame.setContentPane(new PanneauJeu(player));
            }
        });
	 
		
		/******** BOUCLE DE JEU *********/
		
		while (!game.isGameOver()) {
			game.waitTurnOf(player);
			if (game.isGameOver()) {
				break;
			}
			
			/******** REFRESH *********/
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	((PanneauJeu) frame.getContentPane()).updateFamilies();
	            	((PanneauJeu) frame.getContentPane()).updateHand();
	            }
	        });
				
			System.out.println("C'est à vous de jouer");
			boolean error = false;
			boolean newFamilyCreated = false;
			while (!error && !newFamilyCreated) {
				if (player.getCards().size() == 0) {
					Card carte = game.pickCard();
					if (carte != null) {
						player.giveCard(carte);
					} else {
						System.out.println("La pioche est vide...");
						error = true;
					}
				} else {
					/******** Choix de la famille *********/
	            	List<Family> listFamilies = this.console.getSelectableFamilies(player.getCards());
	            	Family[] families = listFamilies.toArray(new Family[listFamilies.size()]);	            	
	            	int indexFamily = JOptionPane.showOptionDialog(frame,
	            										"Choose the family", 
	            										"Choose the family", 
	            										JOptionPane.DEFAULT_OPTION, 
	            										JOptionPane.QUESTION_MESSAGE, 
	            										null, 
	            										families, 
	            										families[0]);
	            	Family family = families[indexFamily];
	            	/******** Choix du statut *********/
	            	List<Status> listStatus = this.console.getSelectableStatus(family,player.getCards());
	            	Status[] status = listStatus.toArray(new Status[listStatus.size()]);
	            	int indexStatus = JOptionPane.showOptionDialog(frame,
							"Choose the status", 
							"Choose the status", 
							JOptionPane.DEFAULT_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, 
							status, 
							status[0]);
	            	Status s = status[indexStatus];
	            	/******** Choix de l'adversaire *********/
	            	List<IPlayer> opponentsList = player.getOpponents();
	            	String[] opponentsName = new String[opponentsList.size()];
	            	
	            	for(int i = 0; i < opponentsList.size(); i++) {
	            		opponentsName[i] = opponentsList.get(i).pseudo();
	            	}
	            	
	            	IPlayer[] opponents = opponentsList.toArray(new IPlayer[opponentsList.size()]);
	            	int indexOpponent = JOptionPane.showOptionDialog(frame,
							"Choose the opponent", 
							"Choose the opponent", 
							JOptionPane.DEFAULT_OPTION, 
							JOptionPane.QUESTION_MESSAGE, 
							null, 
							opponentsName, 
							opponentsName[0]);
					IPlayer opponent = opponents[indexOpponent];
					Card card = opponent.requestCard(family, s);
					if (card != null) {
						System.out.println(String.format("Vous avez reçu la carte (%s, %s), VOUS POUVEZ REJOUER !", card.getFamily(), card.getStatus()));
						player.giveCard(card);
						/******** REFRESH *********/
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				            	((PanneauJeu) frame.getContentPane()).updateFamilies();
				            	((PanneauJeu) frame.getContentPane()).updateHand();
				            }
				        });
						newFamilyCreated = player.tryCreateFamily(card);
						if (newFamilyCreated) {
							System.out.println(String.format("BRAVO, vous venez de réunir la famille %s", card.getFamily()));
							/******** REFRESH *********/
							SwingUtilities.invokeLater(new Runnable() {
					            public void run() {
					            	((PanneauJeu) frame.getContentPane()).updateFamilies();
					            	((PanneauJeu) frame.getContentPane()).updateHand();
					            }
					        });
						}
					} else {
						System.out.println(String.format("%s n'a pas la carte que vous avez demandé", opponent.pseudo()));
						error = true;
					}
					if (error) {
						card = game.pickCard();
						if (card != null) {
							player.giveCard(card);
							if (card.getFamily().equals(family) && card.getStatus().equals(status)) {
								System.out.println(String.format("Vous avez piochez la carte (%s, %s), BONNE PIOCHE !", card.getFamily(), card.getStatus()));
								error = false;
							} else {
								System.out.println(String.format("Vous avez piochez la carte (%s, %s), MAUVAISE PIOCHE...", card.getFamily(), card.getStatus()));
							}
							/******** REFRESH *********/
							SwingUtilities.invokeLater(new Runnable() {
					            public void run() {
					            	((PanneauJeu) frame.getContentPane()).updateFamilies();
					            	((PanneauJeu) frame.getContentPane()).updateHand();
					            }
					        });
							newFamilyCreated = player.tryCreateFamily(card);
							if (newFamilyCreated) {
								System.out.println(String.format("BRAVO, vous venez de réunir la famille %s", card.getFamily()));
								/******** REFRESH *********/
								SwingUtilities.invokeLater(new Runnable() {
						            public void run() {
						            	((PanneauJeu) frame.getContentPane()).updateFamilies();
						            	((PanneauJeu) frame.getContentPane()).updateHand();
						            }
						        });
							}
						} else {
							System.out.println("La pioche est vide...");
						}
					}
				}
			}
			game.nextTurn();
			System.out.println("J'ai terminé mon tour !");
		}
		System.out.println("La partie est terminée");
		if (game.isWinner(player)) {
			System.out.println("VOUS AVEZ GAGNÉ !");
		} else {
			System.out.println("Vous avez perdu...");
		}
		
		
		} catch (Exception exception) {
			exception.printStackTrace();
			System.err.println("Une erreur est survenue, la partie a été coupée...");
		}
		
		
	}
	
}
