package de.rpgframework.shadowrun6.commlink;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.javafx.AppLayout;
import org.prelle.javafx.Page;

import de.rpgframework.shadowrun6.chargen.jfx.SR6CharactersOverviewPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * @author prelle
 *
 */
public class MainScreenController {
	
	private final static Logger logger = LogManager.getLogger(MainScreenController.class);

	private transient AppLayout screen;

	//-------------------------------------------------------------------
	public MainScreenController() {
	}

	//-------------------------------------------------------------------
	@FXML
	public void initialize() {
	}

	//-------------------------------------------------------------------
	public void setComponent(AppLayout screen) {
		this.screen = screen;
		try {
			Page page = ScreenLoader.loadLibraryPage();
			screen.navigateTo(page, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//-------------------------------------------------------------------
	@FXML
	public void navigateCharacters(ActionEvent ev) {
		logger.debug("Navigate Characters");
		Page page = new SR6CharactersOverviewPage();

		screen.navigateTo(page, true);
	}

	//-------------------------------------------------------------------
	@FXML
	public void navigateLibraries(ActionEvent ev) {
		logger.debug("Navigate Libraries");
		try {
			Page page = ScreenLoader.loadLibraryPage();

			screen.navigateTo(page, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
