package de.rpgframework.shadowrun6.comlink.pages;

import java.io.File;
import java.util.Locale;

import org.prelle.javafx.PagePile;

import de.rpgframework.eden.client.jfx.EdenDebugPage;
import de.rpgframework.eden.client.jfx.EdenSettingsPage;

/**
 * @author prelle
 *
 */
public class AboutPage extends PagePile {

	private EdenSettingsPage pgSettings;
	private InfoPage pgInfo;
	private CopyrightPage pgCopy;
	private EdenDebugPage pgDebug;

	//-------------------------------------------------------------------
	public AboutPage(File[] directories) {
		pgSettings = new EdenSettingsPage(Locale.ENGLISH, Locale.GERMAN, Locale.FRENCH, Locale.forLanguageTag("pt"));
		pgInfo = new InfoPage();
		pgCopy  = new CopyrightPage();
		pgDebug = new EdenDebugPage(directories);

		getPages().addAll(pgSettings, pgInfo, pgCopy, pgDebug);
//		setStartPage(pgInfo);
		pgDebug.refresh();

		visibleProperty().addListener( (ov,o,n) -> pgDebug.refresh());
	}

}
