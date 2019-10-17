package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Familles implements Serializable {

	private static final long serialVersionUID = -9165015115310232465L;

	private List<Famille> familles;
	private LocalDateTime timestamp;
	
	public Familles() {
		this.familles = new ArrayList<Famille>();
		this.timestamp = LocalDateTime.now();
	}

	public List<Famille> getFamilles() {
		return familles;
	}
	
	public void addFamille(Famille famille) {
		this.familles.add(famille);
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
}
