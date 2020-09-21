package de.rpgframework.shadowrun6.commlink;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.javafx.Page;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * @author prelle
 *
 */
public class LibraryPageController {
	
	private final static Logger logger = LogManager.getLogger(LibraryPageController.class);
	
	@FXML
	private Button btnBeastiary;
	@FXML
	private Button btnSpells;
	@FXML
	private Button btnMaster;
	@FXML
	private Button btnPowers;
	@FXML
	private Button btnWeapons;

	
	private transient Page component;
	
	//-------------------------------------------------------------------
	public LibraryPageController() {
	}

	//-------------------------------------------------------------------
   @FXML
    public void initialize() {
		ChangeListener<Node> scaleButtons = (ov,o,n) -> {
        	if (n!=null && (n instanceof ImageView)) {
        		((ImageView)n).setFitHeight(60);
        		((ImageView)n).setPreserveRatio(true);
        	}
        };
       
        btnBeastiary.graphicProperty().addListener( scaleButtons);
        btnSpells.graphicProperty().addListener( scaleButtons);
//        btnMaster.graphicProperty().addListener( scaleButtons);
//        btnPowers.graphicProperty().addListener( scaleButtons);
//        btnWeapons.graphicProperty().addListener( scaleButtons);
   }

	//-------------------------------------------------------------------
	public void setComponent(Page page) {
		this.component = page;
		logger.warn("setComponent("+page+")");
	}
    
	//-------------------------------------------------------------------
	@FXML
	public void openBeastiary(ActionEvent ev) {
		logger.warn("Navigate Beastiary");
//		System.err.println("Navigate Beastiary");
//		try {
//			Page page = ScreenLoader.loadBeastiaryPage();
//			component.getAppLayout().setVisiblePage(page);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
    
	//-------------------------------------------------------------------
	@FXML
	public void openSpells(ActionEvent ev) {
		logger.warn("Navigate Spells");
		System.err.println("TODO: Navigate Spells");
	}
    
	//-------------------------------------------------------------------
	@FXML
	public void openMasterships(ActionEvent ev) {
		logger.warn("Navigate Masterships");
		System.err.println("TODO: Navigate Masterships");
	}
    
	//-------------------------------------------------------------------
	@FXML
	public void openPowers(ActionEvent ev) {
		logger.warn("Navigate Powers");
		System.err.println("TODO: Navigate Powers");
	}
    
	//-------------------------------------------------------------------
	@FXML
	public void openWeapons(ActionEvent ev) {
		logger.warn("Navigate Weapons");
		System.err.println("TODO: Navigate Weapons");
	}

}
