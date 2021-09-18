package de.rpgframework.shadowrun6.comlink.pages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gluonhq.attach.browser.BrowserService;
import com.gluonhq.attach.util.Platform;

import de.rpgframework.ResourceI18N;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.eden.client.EdenConnection;
import de.rpgframework.jfx.pages.ContentPacksPage;
import de.rpgframework.reality.BoughtItem;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;

/**
 * @author prelle
 *
 */
public class Shadowrun6ContentPacksPage extends ContentPacksPage {
	
	private final static Logger logger = LoggerFactory.getLogger(Shadowrun6ContentPacksPage.class);
	
	private final static ResourceBundle RES = ResourceBundle.getBundle(Shadowrun6ContentPacksPage.class.getName());
	
	private TextFlow flow; 
	private Hyperlink link;

	//-------------------------------------------------------------------
	/**
	 * @param hostServices 
	 */
	public Shadowrun6ContentPacksPage(EdenConnection con) {
		super();
		initComponentsHere();
		initLayoutHere();
		initActivityHere();
		
		
		List<BoughtItem> data = con.getContentPacks(RoleplayingSystem.SHADOWRUN6);
		data.add(new BoughtItem(null, Platform.getCurrent().getName(), Instant.now(), 2000));
		
		setData(data);
	}

	//-------------------------------------------------------------------
	private void initComponentsHere() {
		link = new Hyperlink(ResourceI18N.get(RES, "page.contents.more.link.name"));
	}

	//-------------------------------------------------------------------
	private void initLayoutHere() {
		Label pre  = new Label(ResourceI18N.get(RES, "page.contents.more.pre"));
		Label post = new Label(ResourceI18N.get(RES, "page.contents.more.post"));
		flow = new TextFlow(pre, link, post);
		
		if (Platform.isDesktop()) {
			layout.getChildren().add(1,flow);
		}
	}

	//-------------------------------------------------------------------
	private void initActivityHere() {
		link.setOnAction(ev -> {
			String url = ResourceI18N.get(RES, "page.contents.more.link.url");
			logger.info("Visit "+url);
			
			BrowserService.create().ifPresent(service -> {
			      try {
					service.launchExternalBrowser(url);
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  });
		});
	}

}
