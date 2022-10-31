package de.rpgframework.shadowrun6.comlink.pages;

import java.util.ResourceBundle;

import org.prelle.javafx.Page;

import de.rpgframework.ResourceI18N;
import de.rpgframework.shadowrun6.comlink.ComLinkMain;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * @author prelle
 *
 */
public class CopyrightPage extends Page {
	
	private static ResourceBundle RES = ResourceBundle.getBundle(ComLinkMain.class.getName());
	
	private Label text1;
	private Label text2;

	//-------------------------------------------------------------------
	public CopyrightPage() {
		super(ResourceI18N.get(RES, "page.about.copyright.title"));
		initComponents();
		initLayout();
	}
	
	//-------------------------------------------------------------------
	private void initComponents() {
		text1 = new Label(ResourceI18N.get(RES, "page.about.copyright.text1"));
		text2 = new Label(ResourceI18N.get(RES, "page.about.copyright.text2"));
		text1.setWrapText(true);
		text2.setWrapText(true);
		text1.setTextAlignment(TextAlignment.CENTER);
		text2.setTextAlignment(TextAlignment.CENTER);
	}
	
	//-------------------------------------------------------------------
	private void initLayout() {
		VBox layout = new VBox(20,  text1, text2);
		layout.setAlignment(Pos.CENTER);
		setContent(layout);
	}
	
}
