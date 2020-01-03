package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import remote.IServer;
import view.Fenetre;

public class LauncherUI {
	
	public static void main(String[] args) {
	
		try {
			
			String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String configPath = rootPath + "config.properties";
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(configPath));
			
			String securityPolicyPath = "file:" + properties.getProperty("security.file.path");
			System.setProperty("java.security.policy", securityPolicyPath);
			
			if (System.getSecurityManager() == null) {
			    System.setSecurityManager(new SecurityManager());
			}
			
			int port = Integer.parseInt(properties.getProperty("server.port"));
			String serverInterface = properties.getProperty("server.interface");
			
			Registry registry = LocateRegistry.getRegistry(port);
			
			// Recover the RMI Server instance
			IServer serveur = (IServer) registry.lookup(serverInterface);
			
			Fenetre fenetre = new Fenetre(serveur);
			
			fenetre.setVisible(true);
			
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
