package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Familles {

	private List<Famille> familles;
	private LocalDateTime timestamp;
	
	public Familles() {
		this.familles = new ArrayList<Famille>();
		this.timestamp = LocalDateTime.now();
	}

	public List<Famille> getFamilles() {
		return familles;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
}
