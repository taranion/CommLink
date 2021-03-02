package de.rpgframework.shadowrun6.comlink.pages;

import java.util.ResourceBundle;

import org.prelle.javafx.PagePile;

import de.rpgframework.ResourceI18N;

/**
 * @author prelle
 *
 */
public class LookupPage extends PagePile {
	
	private static ResourceBundle RES = ResourceBundle.getBundle(LookupPage.class.getName());

	//-------------------------------------------------------------------
	public LookupPage() {
		super(ResourceI18N.get(RES, "page.title"));
	}
}
