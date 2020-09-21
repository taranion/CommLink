package de.rpgframework.shadowrun6.commlink;

import java.io.IOException;
import java.util.ResourceBundle;

import org.prelle.javafx.AppLayout;
import org.prelle.javafx.ExtendedComponentBuilderFactory;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.Page;

import javafx.fxml.FXMLLoader;

/**
 * @author prelle
 *
 */
public class ScreenLoader {

	//-------------------------------------------------------------------
	public static AppLayout loadMainScreen() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				CommLinkMain.class.getResource("fxml/MainScreen.fxml"),
				ResourceBundle.getBundle(CommLinkMain.class.getName())
				);
		FXMLLoader.setDefaultClassLoader(FlexibleApplication.class.getClassLoader());
		loader.setBuilderFactory(new ExtendedComponentBuilderFactory());
		AppLayout ret = loader.load();
		((MainScreenController)loader.getController()).setComponent(ret);
		return ret;
	}

	//-------------------------------------------------------------------
	public static Page loadLibraryPage() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				CommLinkMain.class.getResource("fxml/LibraryPage.fxml"),
				ResourceBundle.getBundle("de.rpgframework.shadowrun6.commlink.LibraryPage")
				);
		FXMLLoader.setDefaultClassLoader(FlexibleApplication.class.getClassLoader());
		loader.setBuilderFactory(new ExtendedComponentBuilderFactory());
		Page ret = loader.load();
		((LibraryPageController)loader.getController()).setComponent(ret);
		return ret;
	}

//	//-------------------------------------------------------------------
//	public static Page loadBeastiaryPage() throws IOException {
//		FXMLLoader loader = new FXMLLoader(
//				CommLinkMain.class.getResource("fxml/BeastiaryPage.fxml"),
//				ResourceBundle.getBundle("de.rpgframework.splittermond.mondtor.BeastiaryPage")
//				);
//		FXMLLoader.setDefaultClassLoader(FlexibleApplication.class.getClassLoader());
//		loader.setBuilderFactory(new ExtendedComponentBuilderFactory());
//		Page ret = loader.load();
//		((BeastiaryPageController)loader.getController()).setComponent(ret);
//		return ret;
//	}

}
