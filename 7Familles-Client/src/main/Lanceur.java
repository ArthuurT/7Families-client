package main;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import remote.IServeur;

public class Lanceur {

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		System.setProperty(
			"java.security.policy",
			"file:///home/asao/Documents/Universite/Semestre9/Middlewares/7Families/7Families-client/7Familles-Client/src/security.policy");
		if (System.getSecurityManager() == null) {
		    System.setSecurityManager(new SecurityManager());
		}
		try {
			Registry registry = LocateRegistry.getRegistry(port);
			IServeur serveur = (IServeur) registry.lookup("serveur");
			new ThreadJoueur(serveur).start();
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
	}
}
