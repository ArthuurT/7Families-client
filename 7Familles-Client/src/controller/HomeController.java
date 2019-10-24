package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import remote.IJeu;
import remote.IServeur;

public class HomeController implements Initializable {
	
	@FXML
	private Button afficherServeurs;
	@FXML
	private Button quitter; 
	
	private Stage stage;
	
	private IServeur server;
	
	public HomeController(IServeur server) {
		this.server = server;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}
	
	public void quitter(ActionEvent event) {
	    Stage stage = (Stage) quitter.getScene().getWindow();
	    stage.close(); 
	}
	
	public void afficherServeurs(ActionEvent event) throws IOException {
			// Create a FXMLLoader instance
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ServerList.fxml"));
			// Create a SelectServerController instance
	        SelectServerController controller = new SelectServerController(this.server.listerJeux(),this);
	        // Set it in the FXMLLoader
	        loader.setController(controller);

	        // Set the new scene
	        AnchorPane root = (AnchorPane) loader.load();	        
	        stage = (Stage) afficherServeurs.getScene().getWindow();
	        Scene scene = new Scene(root);
	        stage.setScene(scene);
	        
	}
}
