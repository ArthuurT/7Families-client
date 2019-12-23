package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import remote.IPlayer;
import model.Card;
import model.Family;
import model.Status;

/**
 * 
 * This class is used to interact with the player through the terminal.
 * 
 */
public class CommandLineInterface {

	private String readString(String prompt) {
		System.out.print(String.format(">>> %s : ", prompt));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String line;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			System.out.println();
			System.out.println("Saisie incorrecte, merci de réessayer !");
			return this.readString(prompt);
		}
		System.out.println();
		return line;
	}
	
	private int readInt(String prompt) {
		System.out.print(String.format(">>> %s : ", prompt));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		int line;
		try {
			line = Integer.parseInt(reader.readLine());
		} catch (IOException | NumberFormatException e) {
			System.out.println();
			System.out.println("Saisie incorrecte, merci de réessayer !");
			return this.readInt(prompt);
		}
		System.out.println();
		return line;
	}
	
	public String readPseudo() {
		return this.readString("Entrer votre pseudo");
	}
	
	public void printCards(List<Card> cards) {
		System.out.println(" ==================== ============== ==================== ");
		System.out.println(" ==================== MAIN DU JOUEUR ==================== ");
		System.out.println(" ==================== ============== ==================== ");
		cards.stream()
			.sorted(Comparator.comparing(Card::getFamily))
			.forEach((Card card) -> {
				System.out.println(card.toString());
			});
		System.out.println();
	}
	
	public List<Family> getSelectableFamilies(List<Card> cards) {
		List<Family> selectableFamilies = new ArrayList<Family>();
		for (Card card : cards) {
			if (!selectableFamilies.contains(card.getFamily())) {
				selectableFamilies.add(card.getFamily());
			}
		}
		return selectableFamilies;
	}
	
	public List<Status> getSelectableStatus(Family family, List<Card> cards) {
		List<Status> selectableStatus = new ArrayList<Status>();
		for (Status status : Status.values()) {
			selectableStatus.add(status);
		}
		List<Card> familyMembers = cards.stream()
			.filter((Card c) -> c.getFamily().equals(family))
			.collect(Collectors.toList());
		for (Card card : familyMembers) {
			if (selectableStatus.contains(card.getStatus())) {
				selectableStatus.remove(card.getStatus());
			}
		}
		return selectableStatus;
	}
		
	public Family selectFamily(List<Card> cards) {
		List<Family> selectableFamilies = this.getSelectableFamilies(cards);
		System.out.println(" ==================== =================== ==================== ");
		System.out.println(" ==================== SELECTABLE FAMILIES ==================== ");
		System.out.println(" ==================== =================== ==================== ");
		for (int familyIndex = 0; familyIndex < selectableFamilies.size(); familyIndex++) {
			System.out.println(String.format("%d - %s", familyIndex, selectableFamilies.get(familyIndex).toString()));
		}
		System.out.println();
		
		String prompt = String.format("Sélectionner une famille [%d, %d]", 0, selectableFamilies.size() - 1);
		int selectedFamilyIndex = this.readInt(prompt);
		while (selectedFamilyIndex < 0 || selectedFamilyIndex >= selectableFamilies.size()) {
			selectedFamilyIndex = this.readInt(prompt);
		}
		
		System.out.println();
		
		return selectableFamilies.get(selectedFamilyIndex);
	}
	
	public Status selectFamilyMember(Family family, List<Card> cards) {
		List<Status> selectableMembers = this.getSelectableStatus(family, cards);
		System.out.println(" ==================== ================== ==================== ");
		System.out.println(" ==================== SELECTABLE Members ==================== ");
		System.out.println(" ==================== ================== ==================== ");
		for (int memberIndex = 0; memberIndex < selectableMembers.size(); memberIndex++) {
			System.out.println(String.format("%d - %s", memberIndex, selectableMembers.get(memberIndex).toString()));
		}
		System.out.println();
		
		String prompt = String.format("Sélectionner un statut [%d, %d]", 0, selectableMembers.size() - 1);
		int selectedMemberIndex = this.readInt(prompt);
		while (selectedMemberIndex < 0 || selectedMemberIndex >= selectableMembers.size()) {
			selectedMemberIndex = this.readInt(prompt);
		}
		
		System.out.println();
		
		return selectableMembers.get(selectedMemberIndex);
	}
	
	public IPlayer selectOpponent(List<IPlayer> opponents) throws RemoteException {
		System.out.println(" ==================== ==================== ==================== ");
		System.out.println(" ==================== SELECTABLE Opponents ==================== ");
		System.out.println(" ==================== ==================== ==================== ");
		for (int opponentIndex = 0; opponentIndex < opponents.size(); opponentIndex++) {
			System.out.println(String.format("%d - %s", opponentIndex, opponents.get(opponentIndex).getPseudo()));
		}
		System.out.println();
		
		String prompt = String.format("Sélectionner un opposant [%d, %d]", 0, opponents.size() - 1);
		int selectedOpponentIndex = this.readInt(prompt);
		while (selectedOpponentIndex < 0 || selectedOpponentIndex >= opponents.size()) {
			selectedOpponentIndex = this.readInt(prompt);
		}
		
		System.out.println();
		
		return opponents.get(selectedOpponentIndex);
	}
	
	public int selectNumberOfPlayers() {
		String prompt = String.format("Sélectionner le nombre de joueur dans la partie [%d, %d]", 2, 6);
		int numberOfPlayers = this.readInt(prompt);
		while (numberOfPlayers < 2 || numberOfPlayers > 6) {
			numberOfPlayers = this.readInt(prompt);
		}
		return numberOfPlayers;
	}
	
	public void printMessage(String message) {
		System.out.println();
		System.out.println(" ---------------------------------------- ");
		System.out.println(message);
		System.out.println(" ---------------------------------------- ");
		System.out.println();
	}
	
	public void printError(String error) {
		System.err.println();
		System.err.println(" ---------------------------------------- ");
		System.err.println(error);
		System.err.println(" ---------------------------------------- ");
		System.err.println();
	}
}
