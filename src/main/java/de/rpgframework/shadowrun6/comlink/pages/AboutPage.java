package de.rpgframework.shadowrun6.comlink.pages;

import java.io.File;

import org.prelle.javafx.PagePile;

import de.rpgframework.eden.client.jfx.EdenDebugPage;

/**
 * @author prelle
 *
 */
public class AboutPage extends PagePile {

	private InfoPage pgInfo;
	private CopyrightPage pgCopy;
	private EdenDebugPage pgDebug;
	
	//-------------------------------------------------------------------
	public AboutPage(File[] directories) {
		pgInfo = new InfoPage();
		pgCopy  = new CopyrightPage();
		pgDebug = new EdenDebugPage(directories);
		
		getPages().addAll(pgInfo, pgCopy, pgDebug);
//		setStartPage(pgInfo);
		pgDebug.refresh();
		
		visibleProperty().addListener( (ov,o,n) -> pgDebug.refresh());
	}
	
}
