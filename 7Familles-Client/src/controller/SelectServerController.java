package controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import exception.GameFullException;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.GameService;
import main.MessageType;
import model.Joueur;
import remote.IJeu;

public class SelectServerController implements Initializable {
	
	private Map<String, IJeu> jeux;
	private HomeController homeController;

	@FXML
	private ScrollPane scrollPaneServers;
	
	@FXML
	private ListView<String> serversListView;
	
	@FXML
	private Button backButton;
	
	@FXML
	private Button joinButton;
	
	@FXML
	private TextField nameTextField;
	
	@FXML
	private Text loadingText;
	
	@FXML
	private ProgressIndicator loadingWheel;
	
	private ObservableList<String> serverNames;
	
	private Stage stage;
	
	// Player's name
	private String playerName;
	
	// Selected game
	private IJeu selectedJeu;

	
	public SelectServerController(Map<String, IJeu> jeux, HomeController c) {
		this.homeController = c;
		this.jeux = jeux;
		this.init();
	}
	
	public void init() {
		List<String> names = new ArrayList<String>();
		for(Map.Entry<String,IJeu> entry : this.jeux.entrySet()){
			names.add(entry.getKey());
		}
		this.serverNames = FXCollections.observableList(names);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.serversListView.setItems(this.serverNames);
		
		BooleanBinding bb = new BooleanBinding(){
		    {
		        super.bind(nameTextField.textProperty(),serversListView.getSelectionModel().getSelectedItems());
		    }
		    @Override
		    protected boolean computeValue() {
		    	String item = serversListView.getSelectionModel().getSelectedItem();
		    	playerName = nameTextField.getText();
		    	selectedJeu = jeux.get(item);
		    	return (nameTextField.getText().isEmpty() || item == null || item == "");
		    }
		};	
		this.joinButton.disableProperty().bind(bb);
		this.loadingText.setVisible(false);
		this.loadingWheel.setVisible(false);
	}
	
	public void pressBackButton(ActionEvent event) throws IOException {
		// Create a FXMLLoader instance
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Launch.fxml"));
        // Set it in the FXMLLoader
        loader.setController(this.homeController);
        // Set the new scene
        AnchorPane root = (AnchorPane) loader.load();	        
        stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
	}
	
	public synchronized void pressJoinButton(ActionEvent event) throws IOException, GameFullException {
		IJeu jeu = this.selectedJeu;
		Joueur joueur = new Joueur(this.playerName);
        
        Service<Void> GameService = new GameService(jeu,joueur);
        
        GameService.messageProperty().addListener((obs, oldMsg, newMsg) -> {
        	System.out.println("Haut");
        	if(newMsg == MessageType.REJOINDRE_PARTIE) {
        		this.joinButton.setText("Joining...");
        		this.disableAll();
        		Button source = (Button) event.getSource();
        		source.setVisible(false);
        		this.loadingText.setVisible(true);
        		this.loadingWheel.setVisible(true);
        	}else if(newMsg == MessageType.DEBUT_PARTIE) {
        		System.out.println("Test");
    			// Create a FXMLLoader instance
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Game.fxml"));
    			// Create a SelectServerController instance
    	        GameController controller = new GameController(jeu,joueur);
    	        // Set it in the FXMLLoader
    	        loader.setController(controller);

    	        // Set the new scene
    	        AnchorPane root;
				try {
					root = (AnchorPane) loader.load();
					stage = (Stage) joinButton.getScene().getWindow();
	    	        Scene scene = new Scene(root);
	    	        stage.setScene(scene);
				} catch (IOException e) {
					e.printStackTrace();
				}	        
    	        
        	}
        });
        
        GameService.start();
	}
	
	public void disableAll() {
		this.backButton.setVisible(false);
		this.nameTextField.setDisable(true);
		this.serversListView.setDisable(true);
		this.scrollPaneServers.setDisable(true);
	}
	
	
}
