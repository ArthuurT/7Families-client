package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import main.GameService;
import model.Joueur;
import remote.IJeu;

public class GameController implements Initializable {
	
	private IJeu jeu;
	private Joueur joueur;
	
	@FXML
	private TextArea textLogs;
	@FXML
	private HBox stateBox;
	@FXML
	private ListView familyList;
	@FXML
	private ListView statusList;
	

	public GameController(IJeu jeu, Joueur joueur) {
		this.jeu = jeu;
		this.joueur = joueur;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {}

}
