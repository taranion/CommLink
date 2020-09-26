package de.rpgframework.shadowrun6.commlink.cells;

import org.prelle.shadowrun6.Spell;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author prelle
 *
 */
public class SpellListCell extends ListCell<Spell> {

	private Label lbName;
	private Label lbLevel;
	private HBox layout;
	
	//-------------------------------------------------------------------
	public SpellListCell() {
		lbName = new Label();
		lbLevel= new Label();
		layout = new HBox(20,lbName,lbLevel);
		HBox.setHgrow(lbName, Priority.ALWAYS);
		lbName.setMaxWidth(Double.MAX_VALUE);
		
	}


	//-------------------------------------------------------------------
	public void updateItem(Spell item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			lbName.setText(item.getName());
			lbLevel.setText(item.getRange().getShortName());
			setGraphic(layout);
		}
	}
}
