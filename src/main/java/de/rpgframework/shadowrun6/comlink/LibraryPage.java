package de.rpgframework.shadowrun6.comlink;

import java.util.ResourceBundle;

import org.prelle.javafx.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rpgframework.ResourceI18N;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

/**
 * @author prelle
 *
 */
public class LibraryPage extends Page {
	
	private final static Logger logger = LoggerFactory.getLogger(LibraryPage.class);
	
	private final static ResourceBundle RES = ResourceBundle.getBundle(LibraryPage.class.getName());
	
	private FlowPane content;
	private Button btnSpells;
	
	//-------------------------------------------------------------------
	public LibraryPage() {
		super(ResourceI18N.get(RES, "page.title"));
		setId("library");
		
		ChangeListener<Node> scaleButtons = (ov,o,n) -> {
        	if (n!=null && (n instanceof ImageView)) {
        		((ImageView)n).setFitHeight(60);
        		((ImageView)n).setPreserveRatio(true);
        	}
        };

		
		btnSpells = new Button(ResourceI18N.get(RES,"category.spells"));
		btnSpells.setId("spells");
		btnSpells.getStyleClass().add("category-button");
		btnSpells.graphicProperty().addListener( scaleButtons);
		btnSpells.setOnAction(ev -> openSpells(ev));
		
		content = new FlowPane();
		content.setId("categories");
		content.getChildren().add(btnSpells);
		
		setContent(content);
	}
    
	//-------------------------------------------------------------------
	public void openSpells(ActionEvent ev) {
		logger.warn("Navigate Spells");
		try {
			getAppLayout().navigateTo(new SpellsPage(), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
