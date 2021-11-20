package de.rpgframework.shadowrun6.comlink;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import org.prelle.javafx.BitmapIcon;
import org.prelle.javafx.DebugPage;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.FontIcon;
import org.prelle.javafx.NavigationPane;
import org.prelle.javafx.Page;
import org.prelle.javafx.SymbolIcon;

import com.gluonhq.attach.browser.BrowserService;
import com.gluonhq.attach.browser.impl.DummyBrowserService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.impl.ServiceFactory;

import de.rpgframework.ResourceI18N;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.eden.client.jfx.EdenClientApplication;
import de.rpgframework.shadowrun.ASpell;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import de.rpgframework.shadowrun6.chargen.jfx.SR6CharactersOverviewPage;
import de.rpgframework.shadowrun6.comlink.pages.LibraryPage;
import de.rpgframework.shadowrun6.comlink.pages.Shadowrun6ContentPacksPage;
import de.rpgframework.shadowrun6.data.Shadowrun6DataPlugin;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ComLinkMain extends EdenClientApplication {
	
	private ResourceBundle RES = ResourceBundle.getBundle(ComLinkMain.class.getName(), ComLinkMain.class.getModule());

	//-------------------------------------------------------------------
    public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
       launch(args);
    }
	
    //-------------------------------------------------------------------
	public ComLinkMain() {
		super(RoleplayingSystem.SHADOWRUN6, "ComLink6");
	}

	//-------------------------------------------------------------------
	protected void prepareBrowser() {
        /*
         * If this is a desktop system, install a BrowserServic4e
         */
        if (Platform.isDesktop()) {
        	com.gluonhq.attach.util.Services.registerServiceFactory(new ServiceFactory<BrowserService>() {

        		@Override
        		public Class<BrowserService> getServiceType() {
        			return BrowserService.class;
        		}

        		@Override
        		public Optional<BrowserService> getInstance() {
        			BrowserService foo = new DummyBrowserService() {
        				@Override
        				public void launchExternalBrowser(String url) throws IOException, URISyntaxException {
        					System.err.println("Browse "+url);
        					getHostServices().showDocument(url);
        				}
        			};
        			return Optional.of(foo);
        		}
        	});
        }
	}
	
    //-------------------------------------------------------------------
    /**
     * @throws IOException 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    public void start(Stage stage) throws Exception {
		stage.setMaxWidth(1800);
		stage.setMaxHeight(1100);
		stage.setMinWidth(360);
		stage.setMinHeight(600);
		super.start(stage);
        setStyle(stage.getScene(), FlexibleApplication.DARK_STYLE);
        stage.getScene().getStylesheets().add(de.rpgframework.jfx.Constants.class.getResource("css/rpgframework.css").toExternalForm());
        stage.getScene().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
    }

	//-------------------------------------------------------------------
	protected void loadData() {
		Shadowrun6DataPlugin plugin = new Shadowrun6DataPlugin();
		plugin.init( );
		logger.info("Loaded "+Shadowrun6Core.getItemList(ASpell.class).size()+" spells");
//		logger.info("Loaded "+SplitterMondCore.getItemList(CreatureType.class).size()+" creature types");

	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.FlexibleApplication#populateNavigationPane(org.prelle.javafx.NavigationPane)
	 */
	@Override
	public void populateNavigationPane(NavigationPane drawer) {
		// Header
		Label header = new Label("ComLink6");
		BitmapIcon icoCommLink = new BitmapIcon(ComLinkMain.class.getResource("AppLogo.png").toString());
		icoCommLink.setStyle("-fx-pref-width: 3em");
		header.setGraphic(icoCommLink);
		drawer.setHeader(header);

		// Items
		SymbolIcon icoLookup = new SymbolIcon("library");
		FontIcon icoAbout = new FontIcon("\uD83D\uDEC8");
		FontIcon icoAccount = new FontIcon("\uE2AF");
		navigChars  = new MenuItem(ResourceI18N.get(RES, "navig.chars"), new SymbolIcon("people"));
		navigLookup = new MenuItem(ResourceI18N.get(RES, "navig.lookup"), icoLookup);
		navigAccount= new MenuItem(ResourceI18N.get(RES, "navig.account"), icoAccount);
		navigAbout  = new MenuItem(ResourceI18N.get(RES, "navig.about"), icoAbout);
		navigChars  .setId("navig-chars");
		navigLookup .setId("navig-lookup");
		navigAbout  .setId("navig-about");
		navigAccount.setId("navig-account");
		
		drawer.getItems().addAll(navigChars, navigLookup, navigAccount, navigAbout);
		
		// Footer
		Image img = new Image(ComLinkMain.class.getResourceAsStream("SR6Logo2.png"));
		if (img!=null) {
			ImageView ivShadowrun = new ImageView(img);
			ivShadowrun.setId("footer-logo");
			ivShadowrun.setPreserveRatio(true);
			ivShadowrun.fitWidthProperty().bind(drawer.prefWidthProperty());
			drawer.setFooter(ivShadowrun);
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.FlexibleApplication#createPage(org.prelle.javafx.NavigationItem)
	 */
	@Override
	public Page createPage(MenuItem menuItem) {
		// TODO Auto-generated method stub
		logger.info("createPage("+menuItem+")");
		try {
		if (menuItem==navigAbout) {
			return new DebugPage();
		} else if (menuItem==navigLookup) {
			return new LibraryPage();
		} else if (menuItem==navigChars) {
			SR6CharactersOverviewPage pg = new SR6CharactersOverviewPage();
			CharacterProviderLoader.getCharacterProvider().setListener(pg);
			return pg;
		} else if (menuItem==navigAccount) {
			return new Shadowrun6ContentPacksPage(eden);
		} else {
			logger.warn("No page for "+menuItem.getText());
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			if (menuItem==navLibrary) {
//				return ScreenLoader.loadLibraryPage();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
		logger.warn("No page for "+menuItem.getText());
		return null;
	}

}