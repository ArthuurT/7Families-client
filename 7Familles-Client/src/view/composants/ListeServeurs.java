package view.composants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import remote.IJeu;

public class ListeServeurs extends JList<String> implements ListSelectionListener {

	private static final long serialVersionUID = 1L;

	public ListeServeurs(Map<String, IJeu> jeux) {
		
		this.addListSelectionListener(this);
		
		ArrayList<String> names = new ArrayList<String>();
		
		for (Map.Entry<String,IJeu> entry : jeux.entrySet()) {
			names.add(entry.getKey());
		}
		
		this.setListData(names.toArray(new String[names.size()]));
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!this.isSelectionEmpty()) {
			
		}
		
	}

}
