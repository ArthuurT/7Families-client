package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import remote.IJeu;

public class SelectServerController implements Initializable {
	
	private Map<String, IJeu> jeux;

	@FXML
	private ScrollPane scrollPaneServers;
	
	@FXML
	private ListView<String> serversListView;
	
	private ObservableList<String> serverNames;
	
	private Stage stage;
	
	public SelectServerController(Map<String, IJeu> jeux) {
		this.jeux = jeux;
		this.init();
	}
	
	public void init() {
		List<String> names = new ArrayList<String>();
		for(Map.Entry<String,IJeu> entry : this.jeux.entrySet()){
			names.add(entry.getKey());
		}
		this.serverNames = FXCollections.observableList(names);
		System.out.println(this.serverNames);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.serversListView.setItems(this.serverNames);
	}
}
