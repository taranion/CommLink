package de.rpgframework.shadowrun6.comlink;

import java.io.IOException;
import java.lang.System.Logger.Level;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

import javax.print.event.PrintServiceAttributeListener;

import org.prelle.javafx.BitmapIcon;
import org.prelle.javafx.DebugPage;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.FontIcon;
import org.prelle.javafx.NavigationPane;
import org.prelle.javafx.Page;
import org.prelle.javafx.ResponsiveControlManager;
import org.prelle.javafx.SymbolIcon;
import org.prelle.shadowrun6.export.beginner.plugin.SR6BeginnerPDFPlugin;
import org.prelle.shadowrun6.export.compact.plugin.SR6CompactPDFPlugin;
import org.prelle.shadowrun6.export.standard.StandardPDFPlugin;

import com.gluonhq.attach.browser.BrowserService;
import com.gluonhq.attach.device.DeviceService;
import com.gluonhq.attach.util.Platform;

import de.rpgframework.ResourceI18N;
import de.rpgframework.character.Attachment;
import de.rpgframework.character.Attachment.Format;
import de.rpgframework.character.Attachment.Type;
import de.rpgframework.character.CharacterHandle;
import de.rpgframework.character.CharacterIOException;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.core.BabylonEventBus;
import de.rpgframework.core.BabylonEventType;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.eden.client.jfx.EdenClientApplication;
import de.rpgframework.eden.client.jfx.PDFPage;
import de.rpgframework.genericrpg.export.ExportPluginRegistry;
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
import javafx.stage.Screen;
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
//		LogManager.getLogManager().reset();
       launch(args);
    }
	
    //-------------------------------------------------------------------
	public ComLinkMain() {
		super(RoleplayingSystem.SHADOWRUN6, "CommLink6");
		
		ExportPluginRegistry.register(new StandardPDFPlugin());
		ExportPluginRegistry.register(new SR6BeginnerPDFPlugin());
		ExportPluginRegistry.register(new SR6CompactPDFPlugin());
//		ExportPluginRegistry.register(new SR6FoundryExportPlugin());
//		ExportPluginRegistry.register(new SR6JSONExportPlugin());
	}

	//-------------------------------------------------------------------
	protected void prepareBrowser() {
		Optional<BrowserService> opt = BrowserService.create();
		System.out.println("BrowserService = "+opt.isPresent());
		System.out.println("DeviceService = "+DeviceService.create().isPresent());
		if (opt.isPresent()) {
			logger.log(Level.DEBUG, "Found BrowserService: "+opt.get());
		} else {
			logger.log(Level.WARNING, "Found no BrowserService!");
		}
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
	
	protected void openFile(Path path) {
		logger.log(Level.WARNING, "openFile");
		System.out.println("openFile: "+BrowserService.create()+" for "+path.toUri());
		
		if (BrowserService.create().isPresent()) {
			try {
				BrowserService.create().get().launchExternalBrowser(path.toUri().toString());
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.log(Level.ERROR, "No BrowserService found");
		}
		
	}
	
    //-------------------------------------------------------------------
    /**
     * @throws IOException 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    public void start(Stage stage) throws Exception {
    	int prefWidth = Math.min( (int)Screen.getPrimary().getVisualBounds().getWidth(), 1500);
    	int prefHeight = Math.min( (int)Screen.getPrimary().getVisualBounds().getHeight(), 900);
    	System.out.println("Start with "+prefWidth+"x"+prefHeight);
		stage.setWidth(prefWidth);
		stage.setHeight(prefHeight);
    	int minWidth = Math.min( (int)Screen.getPrimary().getVisualBounds().getWidth(), 360);
    	int minHeight = Math.min( (int)Screen.getPrimary().getVisualBounds().getHeight(), 650);
		stage.setMinWidth(minWidth);
		stage.setMinHeight(minHeight);
		try {
			super.start(stage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        setStyle(stage.getScene(), FlexibleApplication.DARK_STYLE);
        stage.getScene().getStylesheets().add(de.rpgframework.jfx.Constants.class.getResource("css/rpgframework.css").toExternalForm());
        stage.getScene().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        
//       ScenicView.show(stage.getScene());

        ReferencePDFViewer.setPDFPathResolver( (id,lang) -> getPDFPathFor(RoleplayingSystem.SHADOWRUN6,id,lang));
        ReferencePDFViewer.setEnabled( super.isPDFEnabled());
        
        getAppLayout().visibleProperty().addListener( (ov,o,n) -> {
        	logger.log(Level.INFO, "Visibility changed to "+n);
            ResponsiveControlManager.initialize(getAppLayout());        	
        });
    }

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.eden.client.jfx.EdenClientApplication#loadData()
	 */
    @Override
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
						Shadowrun6Tools.runProcessors(parsed);
						handle.setCharacter(parsed);
						handle.setShortDescription(parsed.getShortDescription());
						logger.log(Level.INFO, "Parsed character2: "+parsed.getShortDescription());
					} catch (CharacterIOException e) {
						// TODO Auto-generated catch block
						logger.log(Level.ERROR, "Failed loading character {0}: {1}", handle.getName(),e.getCode(),e);
						e.printStackTrace();
						BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, ResourceI18N.format(RES, "error.loading_character", handle.getName(), e.getCode()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						logger.log(Level.ERROR, "Failed loading character {0}", handle.getName(),e);
						e.printStackTrace();
						BabylonEventBus.fireEvent(BabylonEventType.UI_MESSAGE, 2, ResourceI18N.format(RES, "error.loading_character", handle.getName()));
					}
				}
			} catch (CharacterIOException e) {
				logger.log(Level.ERROR, "Error accessing characters",e);
				handleError(e);
			} catch (Exception e) {
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
		
		drawer.getItems().addAll(navigChars, navigLookup, navigPDF, navigAbout);
		if (!Platform.isDesktop()) {
			drawer.getItems().remove(navigPDF);
		}
		
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
		logger.log(Level.INFO, "createPage(" + menuItem + ")");
		try {
			if (menuItem == navigAbout) {
				return new DebugPage(super.getDirectories());
			} else if (menuItem == navigLookup) {
				return new LibraryPage();
			} else if (menuItem == navigChars) {
				SR6CharactersOverviewPage pg = new SR6CharactersOverviewPage();
				CharacterProviderLoader.getCharacterProvider().setListener(pg);
				return pg;
			} else if (menuItem == navigAccount) {
				return new Shadowrun6ContentPacksPage(eden);
			} else if (menuItem == navigPDF) {
				return new PDFPage(this, RoleplayingSystem.SHADOWRUN6);
			} else {
				logger.log(Level.WARNING, "No page for " + menuItem.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.log(Level.WARNING, "No page for " + menuItem.getText());
		return null;
	}

}