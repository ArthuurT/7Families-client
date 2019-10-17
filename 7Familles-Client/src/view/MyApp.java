package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.Properties;

import controller.HomeController;
import controller.SelectServerController;
import exception.GameFullException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.ThreadJoueur;
import model.Joueur;
import remote.IJeu;
import remote.IServeur;

public class MyApp extends Application {
	
	private IServeur serveur = null;

	@Override
	public void start(Stage primaryStage) throws Exception {
            
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
    			this.serveur = (IServeur) registry.lookup(serverInterface);
    			   			   			
    			new ThreadJoueur(serveur).start();
    			
    			FXMLLoader fxmlHomeLoader = new FXMLLoader(getClass().getResource("Launch.fxml"));
    			Parent root = (Parent)fxmlHomeLoader.load();
    	        HomeController controller = fxmlHomeLoader.<HomeController>getController();
    			controller.setServer(this.serveur);
                primaryStage.setTitle("My Application");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
                
                /*
                 
                Map<String, IJeu> jeux = serveur.listerJeux();
    			IJeu jeu = jeux.get("ALMA");
    			Joueur joueur = new Joueur("Guest");
    			System.err.println("Je rejoinds une partie");
    			jeu.rejoindre(joueur);
    			System.err.println(String.format("La partie a commencé et j'ai %d cartes", joueur.getMain().size()));
    			
    			*/
    		} catch (RemoteException | NotBoundException e) {
    			e.printStackTrace();
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		
	}
	
	public static void main(String [] args) {
		launch(args);
	}

}
