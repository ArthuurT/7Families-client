package main;

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
			
			/******** LANCEMENT DU TAMPON DES LOGS *********/
			
			new ThreadMessageReaderUI(player.getMessageBuffer(),this.frame).start();
			
			/******** RECHERCHE D'UNE PARTIE *********/
			
			game = this.serveur.searchGame(nombre, this.player,this.player.getMessageBuffer());
			
			/******** MIS EN PLACE DU PLATEAU *********/
		
			SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	frame.getContentPane().setVisible(false);
	    			frame.getContentPane().removeAll();
	    			frame.setContentPane(new PanneauJeu(player));
	            }
	        });			
			
			player.sendLocalMessage("La partie commence");
			
			/******** LANCEMENT DU THREAD DE REFRESH *********/
			
			new ThreadUpdate(this.frame).start();
			
			/******** BOUCLE DE JEU *********/
			
			while (!game.isGameOver()) {
				game.waitTurnOf(player);
				if (game.isGameOver()) {
					break;
				}
				
				player.sendBroadcastMessage("C'est à moi de jouer");
				boolean error = false;
				boolean newFamilyCreated = false;
				while (!error && !newFamilyCreated) {
					if (player.getCards().size() == 0) {
						Card carte = game.pickCard();
						if (carte != null) {
							player.giveCard(carte);
						} else {
							player.sendBroadcastMessage("La pioche est vide...");
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
		            		opponentsName[i] = opponentsList.get(i).getPseudo();
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
						player.sendBroadcastMessage(String.format("Je demande la carte (%s, %s) à %s", family, s, opponent.getPseudo()));
						Card card = opponent.requestCard(family, s);
						if (card != null) {
							player.sendBroadcastMessage(String.format("%s m'a donné la carte (%s, %s)", opponent.getPseudo(), family, s));
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
								player.sendBroadcastMessage(String.format("J'ai réuni la famille %s", family));
								/******** REFRESH *********/
								SwingUtilities.invokeLater(new Runnable() {
						            public void run() {
						            	((PanneauJeu) frame.getContentPane()).updateFamilies();
						            	((PanneauJeu) frame.getContentPane()).updateHand();
						            }
						        });
							}
						} else {
							player.sendBroadcastMessage(String.format("%s n'a pas la carte (%s, %s)", opponent.getPseudo(), family, s));
							error = true;
						}
						if (error) {
							player.sendBroadcastMessage("Je pioche une carte");
							card = game.pickCard();
							if (card != null) {
								player.sendLocalMessage(String.format("Vous avez pioché la carte (%s, %s)", card.getFamily(), card.getStatus()));
								player.giveCard(card);
								if (card.getFamily().equals(family) && card.getStatus().equals(status)) {
									player.sendBroadcastMessage("Bonne pioche !");
									error = false;
								} else {
									player.sendBroadcastMessage("Mauvaise pioche !");							}
								/******** REFRESH *********/
								SwingUtilities.invokeLater(new Runnable() {
						            public void run() {
						            	((PanneauJeu) frame.getContentPane()).updateFamilies();
						            	((PanneauJeu) frame.getContentPane()).updateHand();
						            }
						        });
								newFamilyCreated = player.tryCreateFamily(card);
								if (newFamilyCreated) {
									player.sendBroadcastMessage(String.format("J'ai réuni la famille %s", family));
									/******** REFRESH *********/
									SwingUtilities.invokeLater(new Runnable() {
							            public void run() {
							            	((PanneauJeu) frame.getContentPane()).updateFamilies();
							            	((PanneauJeu) frame.getContentPane()).updateHand();
							            }
							        });
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
		
		
		} catch (Exception exception) {
			System.err.println("Une erreur est survenue, vous avez été déconnecté");
			System.exit(1);
		}
		
		
	}
	
}
