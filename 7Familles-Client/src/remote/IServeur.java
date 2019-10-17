package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IServeur extends Remote {
	
	public boolean creerJeu(String nom, int nbJoueurs) throws RemoteException;
	
	public Map<String, IJeu> listerJeux() throws RemoteException;
		
}
