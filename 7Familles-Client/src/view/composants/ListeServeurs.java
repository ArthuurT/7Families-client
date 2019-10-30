package view.composants;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.JList;

import remote.IJeu;

public class ListeServeurs extends JList<String>{

	private static final long serialVersionUID = 1L;

	public ListeServeurs(Map<String, IJeu> jeux) {
		
		ArrayList<String> names = new ArrayList<String>();
		
		for (Map.Entry<String,IJeu> entry : jeux.entrySet()) {
			names.add(entry.getKey());
		}
		
		this.setListData(names.toArray(new String[names.size()]));
	}

}
