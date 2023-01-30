package de.rpgframework.shadowrun6.comlink.pages;

import java.io.File;
import java.util.Locale;

import org.prelle.javafx.PagePile;

import com.gluonhq.attach.util.Platform;

import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.eden.client.jfx.EdenClientApplication;
import de.rpgframework.eden.client.jfx.EdenDebugPage;
import de.rpgframework.eden.client.jfx.EdenSettingsPage;
import de.rpgframework.eden.client.jfx.PDFPage;
import de.rpgframework.jfx.attach.PDFViewerServiceFactory;

/**
 * @author prelle
 *
 */
public class AboutPage extends PagePile {

	private EdenSettingsPage pgSettings;
	private PDFPage pgPDF;
	private InfoPage pgInfo;
	private CopyrightPage pgCopy;
	private EdenDebugPage pgDebug;

	//-------------------------------------------------------------------
	public AboutPage(File[] directories, EdenClientApplication app, RoleplayingSystem rules) {
		pgSettings = new EdenSettingsPage(Locale.ENGLISH, Locale.GERMAN, Locale.FRENCH, Locale.forLanguageTag("pt"));
//		pgSettings.addSettingsOneLine(ComLinkMain.RES ,"page.about.settings.style", cbStyle);

		pgPDF = new PDFPage(app, rules);
		pgInfo = new InfoPage();
		pgCopy  = new CopyrightPage();
		pgDebug = new EdenDebugPage(directories);

		getPages().addAll(pgSettings, pgPDF, pgInfo, pgCopy, pgDebug);
		if (!Platform.isDesktop() || !PDFViewerServiceFactory.create().isPresent()) {
			getPages().remove(pgPDF);
		}
//		setStartPage(pgInfo);
		pgDebug.refresh();

		visibleProperty().addListener( (ov,o,n) -> pgDebug.refresh());
	}

}
