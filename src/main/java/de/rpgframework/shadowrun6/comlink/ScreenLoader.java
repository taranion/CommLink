package de.rpgframework.shadowrun6.comlink;

import java.io.IOException;
import java.util.ResourceBundle;

import org.prelle.javafx.ExtendedComponentBuilderFactory;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.Page;

import de.rpgframework.shadowrun.Spell;
import de.rpgframework.shadowrun.chargen.jfx.fxml.SpellPageController;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import javafx.fxml.FXMLLoader;

/**
 * @author prelle
 *
 */
public class ScreenLoader {

//	//-------------------------------------------------------------------
//	public static Page loadLibraryPage() throws IOException {
//		FXMLLoader loader = new FXMLLoader(
//				ScreenLoader.class.getResource("fxml/LibraryPage.fxml"),
//				ResourceBundle.getBundle("de.rpgframework.shadowrun6.comlink.fxml.LibraryPage")
//				);
//		FXMLLoader.setDefaultClassLoader(FlexibleApplication.class.getClassLoader());
//		loader.setBuilderFactory(new ExtendedComponentBuilderFactory());
//		Page ret = loader.load();
//		ret.setId("library");
//		((LibraryPageController)loader.getController()).setComponent(ret);
//		return ret;
//	}

	//-------------------------------------------------------------------
	public static Page loadSpellsPage() throws IOException {
		FXMLLoader loader = new FXMLLoader(
				ScreenLoader.class.getResource("fxml/FilteredListPage.fxml"),
				ResourceBundle.getBundle("de.rpgframework.shadowrun6.comlink.fxml.FilteredListPage")
				);
		FXMLLoader.setDefaultClassLoader(FlexibleApplication.class.getClassLoader());
		loader.setBuilderFactory(new ExtendedComponentBuilderFactory());
		loader.setController(new SpellPageController( () -> Shadowrun6Core.getItemList(Spell.class)));
		Page ret = loader.load();
		ret.setId("spells");
		((SpellPageController)loader.getController()).setComponent(
				ret, 
				()-> Shadowrun6Core.getItemList(Spell.class),
				(req) -> req.toString()
				);
		return ret;
	}

}
