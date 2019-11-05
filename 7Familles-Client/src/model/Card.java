package model;

import java.io.Serializable;

public class Card implements Serializable {

	private static final long serialVersionUID = 4682776995853998235L;

	private Family family;
	private Status status;
	private String name;
	
	public Card(Family family, Status status, String name) {
		this.family = family;
		this.status = status;
		this.name = name;
	}

	public Family getFamily() {
		return family;
	}

	public Status getStatus() {
		return status;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return String.format("{Famille: %s, Statut: %s, Nom: %s}", this.family, this.status, this.name);
	}
}
