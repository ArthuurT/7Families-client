package model;

import java.io.Serializable;

public class Carte implements Serializable {

	private static final long serialVersionUID = 4682776995853998235L;

	private Famille famille;
	private Statut statut;
	private String nom;
	
	public Carte(Famille famille, Statut statut, String nom) {
		this.famille = famille;
		this.statut = statut;
		this.nom = nom;
	}

	public Famille getFamille() {
		return famille;
	}

	public Statut getStatut() {
		return statut;
	}

	public String getNom() {
		return nom;
	}
	
	@Override
	public String toString() {
		return String.format("{Famille: %s, Statut: %s, Nom: %s}", this.famille, this.statut, this.nom);
	}
}
