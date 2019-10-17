package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import remote.IJoueur;
import model.Carte;
import model.Famille;
import model.Statut;
import remote.IJeu;

public class Controlleur {

	private String lire(String prompt) {
		System.out.print(String.format(">>> %s : ", prompt));
		BufferedReader lecteur = new BufferedReader(new InputStreamReader(System.in));
		try {
			String saisie = lecteur.readLine();
			System.out.println();
			return saisie;
		} catch (IOException e) {
			System.out.println();
			System.out.println("Une erreur est survenue, merci de rééssayer !");
			return this.lire(prompt);
		}
	}
	
	public void afficherListeJeux(Map<String, IJeu> jeux) {
		System.out.println("Liste des serveurs :");
		List<String> nomJeux = jeux.keySet().stream().collect(Collectors.toList());
		for (int i = 0; i < nomJeux.size(); i++) {
			System.out.println(String.format("%d - %s", i, nomJeux.get(i)));
		}
	}
	
	public IJeu selectionnerJeu(Map<String, IJeu> jeux) {
		List<String> nomJeux = jeux.keySet().stream().collect(Collectors.toList());
		String prompt = String.format("Sélectionner un jeu [%d, %d]", 0, nomJeux.size() - 1);
		int indiceJeu = Integer.parseInt(this.lire(prompt));
		return jeux.get(nomJeux.get(indiceJeu));
	}
	
	public String saisirPseudo() {
		return this.lire("Entrer votre pseudo");
	}
	
	public void afficherMain(List<Carte> main) {
		System.out.println("Main :");
		main.stream()
			.sorted(Comparator.comparing(Carte::getFamille))
			.sorted(Comparator.comparing(Carte::getStatut))
			.forEach((Carte carte) -> {
				System.out.println(carte.toString());
			});
	}
	
	private List<Famille> getFamillesSelectionnables(List<Carte> main) {
		List<Famille> familles = new ArrayList<Famille>();
		for (Carte carte : main) {
			if (!familles.contains(carte.getFamille())) {
				familles.add(carte.getFamille());
			}
		}
		return familles;
	}
	
	private List<Statut> getStatutsSelectionnables(Famille famille, List<Carte> main) {
		List<Statut> statuts = new ArrayList<Statut>();
		for (Statut statut : Statut.values()) {
			statuts.add(statut);
		}
		List<Carte> membresFamille = main.stream()
			.filter((Carte c) -> c.getFamille().equals(famille))
			.collect(Collectors.toList());
		for (Carte carte : membresFamille) {
			if (statuts.contains(carte.getStatut())) {
				statuts.remove(carte.getStatut());
			}
		}
		return statuts;
	}
	
	public void afficherFamillesSelectionnables(List<Carte> main) {
		List<Famille> familles = this.getFamillesSelectionnables(main);
		System.out.println("Familles sélectionnables :");
		for (int i = 0; i < familles.size(); i++) {
			System.out.println(String.format("%d - %s", i, familles.get(i).toString()));
		}
	}
	
	public void afficherStatutsSelectionnables(Famille famille, List<Carte> main) {
		List<Statut> statuts = this.getStatutsSelectionnables(famille, main);
		System.out.println("Statuts sélectionnables :");
		for (int i = 0; i < statuts.size(); i++) {
			System.out.println(String.format("%d - %s", i, statuts.get(i).toString()));
		}
	}
	
	public Famille selectionnerFamille(List<Carte> main) {
		List<Famille> familles = this.getFamillesSelectionnables(main);
		String prompt = String.format("Sélectionner une famille [%d, %d]", 0, familles.size() - 1);
		int indiceFamille = Integer.parseInt(this.lire(prompt));
		return familles.get(indiceFamille);
	}
	
	public Statut selectionnerStatut(Famille famille, List<Carte> main) {
		List<Statut> statuts = this.getStatutsSelectionnables(famille, main);
		String prompt = String.format("Sélectionner un statut [%d, %d]", 0, statuts.size() - 1);
		int indiceStatut = Integer.parseInt(this.lire(prompt));
		return statuts.get(indiceStatut);
	}
	
	public void afficherOpposants(List<IJoueur> opposants) throws RemoteException {
		System.out.println("Opposants :");
		for (int i = 0; i < opposants.size(); i++) {
			System.out.println(String.format("%d - %s", i, opposants.get(i).pseudo()));
		}
	}
	
	public IJoueur selectionnerOpposant(List<IJoueur> opposants) throws RemoteException {
		String prompt = String.format("Sélectionner un opposant [%d, %d]", 0, opposants.size() - 1);
		int indiceOpposant = Integer.parseInt(this.lire(prompt));
		return opposants.get(indiceOpposant);
	}
}
