package model;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import remote.IJoueur;

public class Joueur extends UnicastRemoteObject implements IJoueur, Serializable {

	private static final long serialVersionUID = -8447885863066141486L;
	
	private String pseudo;
	private List<Carte> main;
	private List<IJoueur> opposants;
	private Familles familles;
	
	public Joueur(String pseudo) throws RemoteException {
		super();
		this.pseudo = pseudo;
		this.main = new ArrayList<Carte>();
		this.opposants = new ArrayList<IJoueur>();
		this.familles = new Familles();
	}

	@Override
	public Carte demander(Carte expected) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void donner(Carte carte) throws RemoteException {
		this.main.add(carte);
	}
	
	@Override
	public void donner(List<Carte> cartes) throws RemoteException {
		this.main.addAll(cartes);
	}
	
	@Override
	public void definir(IJoueur opposant) throws RemoteException {
		this.opposants.add(opposant);
	}

	@Override
	public Familles familles() throws RemoteException {
		return this.familles;
	}
	
	public String getPseudo() {
		return this.pseudo;
	}
	
	public List<Carte> getMain() {
		return this.main;
	}

}
