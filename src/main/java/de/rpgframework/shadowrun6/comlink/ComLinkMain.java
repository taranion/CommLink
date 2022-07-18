package de.rpgframework.shadowrun6.comlink;

import java.io.IOException;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.prelle.javafx.BitmapIcon;
import org.prelle.javafx.DebugPage;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.FontIcon;
import org.prelle.javafx.NavigationPane;
import org.prelle.javafx.Page;
import org.prelle.javafx.SymbolIcon;

import de.rpgframework.ResourceI18N;
import de.rpgframework.character.Attachment;
import de.rpgframework.character.Attachment.Format;
import de.rpgframework.character.Attachment.Type;
import de.rpgframework.character.CharacterHandle;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.eden.client.jfx.EdenClientApplication;
import de.rpgframework.eden.client.jfx.PDFPage;
import de.rpgframework.jfx.ReferencePDFViewer;
import de.rpgframework.shadowrun.ASpell;
import de.rpgframework.shadowrun6.Shadowrun6Character;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import de.rpgframework.shadowrun6.Shadowrun6Tools;
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
//    	System.setProperty("prism.forceGPU", "true");
//    	System.setProperty("prism.verbose", "true");
//    	List<String> keys = new ArrayList<String>();
//    	System.getProperties().keySet().forEach(k -> keys.add( (String)k));
//    	Collections.sort(keys);
//		for (String key : keys) {
//			System.out.println(key+" \t= "+System.getProperties().getProperty(key));
//		}
		Locale.setDefault(Locale.ENGLISH);
       launch(args);
    }
	
    //-------------------------------------------------------------------
	public ComLinkMain() {
		super(RoleplayingSystem.SHADOWRUN6, "CommLink6");
	}

	//-------------------------------------------------------------------
	protected void prepareBrowser() {
        /*
         * If this is a desktop system, install a BrowserServic4e
         */
//        if (Platform.isDesktop()) {
//        	com.gluonhq.attach.util.Services.registerServiceFactory(new ServiceFactory<BrowserService>() {
//
//        		@Override
//        		public Class<BrowserService> getServiceType() {
//        			return BrowserService.class;
//        		}
//
//        		@Override
//        		public Optional<BrowserService> getInstance() {
//        			BrowserService foo = new DummyBrowserService() {
//        				@Override
//        				public void launchExternalBrowser(String url) throws IOException, URISyntaxException {
//        					System.err.println("Browse "+url);
//        					getHostServices().showDocument(url);
//        				}
//        			};
//        			return Optional.of(foo);
//        		}
//        	});
//        }
	}
	
    //-------------------------------------------------------------------
    /**
     * @throws IOException 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    public void start(Stage stage) throws Exception {
//		stage.setMaxWidth(1800);
//		stage.setMaxHeight(1100);
		stage.setMinWidth(360);
		stage.setMinHeight(600);
		super.start(stage);
        setStyle(stage.getScene(), FlexibleApplication.DARK_STYLE);
        stage.getScene().getStylesheets().add(de.rpgframework.jfx.Constants.class.getResource("css/rpgframework.css").toExternalForm());
        stage.getScene().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
//       ScenicView.show(stage.getScene());

        ReferencePDFViewer.setPDFPathResolver( (id,lang) -> getPDFPathFor(RoleplayingSystem.SHADOWRUN6,id,lang));
    }

	//-------------------------------------------------------------------
	protected void loadData() {
		Shadowrun6DataPlugin plugin = new Shadowrun6DataPlugin();
		plugin.init( );
		logger.log(Level.INFO, "Loaded "+Shadowrun6Core.getItemList(ASpell.class).size()+" spells");
//		logger.log(Level.INFO, "Loaded "+SplitterMondCore.getItemList(CreatureType.class).size()+" creature types");

		Thread thread = new Thread(() -> {
			try {
				List<CharacterHandle> handles = CharacterProviderLoader.getCharacterProvider().getMyCharacters(RoleplayingSystem.SHADOWRUN6);
				for (CharacterHandle handle : handles) {
					try {
						Attachment attach = null;
						try {
							attach = CharacterProviderLoader.getCharacterProvider().getFirstAttachment(handle, Type.CHARACTER, Format.RULESPECIFIC);
						} catch (Exception e) {
							logger.log(Level.ERROR, "Error loading character attachment",e);
							continue;
						}
						if (attach==null) continue;
						Shadowrun6Character parsed = Shadowrun6Core.decode(attach.getData());
						Shadowrun6Tools.resolveChar(parsed);
						logger.log(Level.INFO, "Parsed character1: "+parsed);
						handle.setCharacter(parsed);
						handle.setShortDescription(parsed.getShortDescription());
						logger.log(Level.INFO, "Parsed character2: "+parsed.getShortDescription());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		thread.run();
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
		FontIcon icoAbout   = new FontIcon("\uD83D\uDEC8");
		FontIcon icoAccount = new FontIcon("\uE2AF");
		SymbolIcon icoPDF     = new SymbolIcon("pdf");
		navigChars  = new MenuItem(ResourceI18N.get(RES, "navig.chars"), new SymbolIcon("people"));
		navigLookup = new MenuItem(ResourceI18N.get(RES, "navig.lookup"), icoLookup);
		navigAccount= new MenuItem(ResourceI18N.get(RES, "navig.account"), icoAccount);
		navigPDF    = new MenuItem(ResourceI18N.get(RES, "navig.pdf"), icoPDF);
		navigAbout  = new MenuItem(ResourceI18N.get(RES, "navig.about"), icoAbout);
		navigChars  .setId("navig-chars");
		navigLookup .setId("navig-lookup");
		navigAbout  .setId("navig-about");
		navigPDF    .setId("navig-pdf");
		navigAccount.setId("navig-account");
		
		drawer.getItems().addAll(navigChars, navigLookup, navigAccount, navigPDF, navigAbout);
		
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
		logger.log(Level.INFO, "createPage("+menuItem+")");
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
		} else if (menuItem==navigPDF) {
			return new PDFPage(this, RoleplayingSystem.SHADOWRUN6);
		} else {
			logger.log(Level.WARNING, "No page for "+menuItem.getText());
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
		logger.log(Level.WARNING, "No page for "+menuItem.getText());
		return null;
	}

}