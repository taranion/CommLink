package de.rpgframework.shadowrun6.comlink;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.prelle.javafx.BitmapIcon;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.FontIcon;
import org.prelle.javafx.NavigationPane;
import org.prelle.javafx.Page;
import org.prelle.javafx.ResponsiveControlManager;
import org.prelle.javafx.SymbolIcon;
import org.prelle.shadowrun6.export.beginner.plugin.SR6BeginnerPDFPlugin;
import org.prelle.shadowrun6.export.compact.plugin.SR6CompactPDFPlugin;
import org.prelle.shadowrun6.export.standard.StandardPDFPlugin;

import de.rpgframework.ResourceI18N;
import de.rpgframework.character.CharacterIOException;
import de.rpgframework.character.CharacterProviderLoader;
import de.rpgframework.core.RoleplayingSystem;
import de.rpgframework.eden.client.jfx.EdenClientApplication;
import de.rpgframework.eden.client.jfx.EdenSettings;
import de.rpgframework.eden.client.jfx.PDFPage;
import de.rpgframework.eden.client.jfx.steps.StartupStep;
import de.rpgframework.genericrpg.LicenseManager;
import de.rpgframework.genericrpg.export.ExportPluginRegistry;
import de.rpgframework.jfx.attach.PDFViewerConfig;
import de.rpgframework.shadowrun6.Shadowrun6Character;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import de.rpgframework.shadowrun6.Shadowrun6Tools;
import de.rpgframework.shadowrun6.chargen.jfx.SR6CharactersOverviewPage;
import de.rpgframework.shadowrun6.comlink.pages.AboutPage;
import de.rpgframework.shadowrun6.comlink.pages.LibraryPage;
import de.rpgframework.shadowrun6.comlink.pages.Shadowrun6ContentPacksPage;
import de.rpgframework.shadowrun6.export.fvtt.SR6FoundryExportPlugin;
import de.rpgframework.shadowrun6.export.json.SR6JSONExportPlugin;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class ComLinkMain extends EdenClientApplication {

	public final static ResourceBundle RES = ResourceBundle.getBundle(ComLinkMain.class.getName(), ComLinkMain.class.getModule());

	static PrintWriter out;

	//-------------------------------------------------------------------
    public static void main(String[] args) {
    	//LicenseManager.storeGlobalLicenses(List.of("SHADOWRUN6/CORE","SHADOWRUN6/COMPANION","SHADOWRUN6/FIRING_SQUAD","SHADOWRUN6/STREET_WYRD","SHADOWRUN6/DOUBLE_CLUTCH","SHADOWRUN6/HACK_SLASH"));
    	System.out.println("ComLinkMain.main");
    	checkInit();
    	System.out.println("Default locale = "+Locale.getDefault());
//    	System.setProperty("prism.forceGPU", "true");
    	System.setProperty("prism.verbose", "false");
//    	List<String> keys = new ArrayList<String>();
//    	System.getProperties().keySet().forEach(k -> keys.add( (String)k));
//    	Collections.sort(keys);
//		for (String key : keys) {
//			if (key.startsWith("com.sun") || key.startsWith("java."))
//				continue;
//			System.out.println("PROP "+key+" \t= "+System.getProperties().getProperty(key));
//		}
//		for (String key : args) {
//			System.out.println("argument "+key);
//		}

    	//System.setProperty("javafx.preloader", CommlinkPreloader.class.getName());
       launch(args);

    }

    //-------------------------------------------------------------------
	public ComLinkMain() {
		super(RoleplayingSystem.SHADOWRUN6, "CommLink6");
    	checkInit();

		ExportPluginRegistry.register(new StandardPDFPlugin());
		ExportPluginRegistry.register(new SR6BeginnerPDFPlugin());
		ExportPluginRegistry.register(new SR6CompactPDFPlugin());
		ExportPluginRegistry.register(new SR6FoundryExportPlugin());
		ExportPluginRegistry.register(new SR6JSONExportPlugin());
	}

    //-------------------------------------------------------------------
	private static void checkInit() {
		try {
			if (out != null) {
				System.out.println("Already initialized");
				return;
			}
			//Path home = Paths.get(System.getProperty("user.home"));
			Path logDir = EdenSettings.logDir; //home.resolve("commlink-logs");
			System.setProperty("logdir", logDir.toAbsolutePath().toString());
			System.out.println("Log directory = " + logDir.toAbsolutePath().toString());
			if (!Files.exists(logDir)) {
				System.out.println("Log dir does not exist");
				Files.createDirectories(logDir);
			}
			// Delete all files
			Files.newDirectoryStream(logDir).forEach(file -> {
				if (Files.isWritable(file)) {
					try {
						Files.delete(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			Path logFile = logDir.resolve("logfile.txt");
			out = new PrintWriter(new FileWriter(logFile.toFile()));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}

	//-------------------------------------------------------------------
	@Override
	protected List<StartupStep> getPreGUISteps() {
		List<StartupStep> merged = new ArrayList<>(super.getPreGUISteps());
		merged.add(new LoadSR6DataStep());
		return merged;
	}

	//-------------------------------------------------------------------
	@Override
	protected List<StartupStep> getPostGUISteps() {
		List<StartupStep> merged = new ArrayList<>(super.getPostGUISteps());
		merged.add(new LoadSR6CharactersStep(this));
		return merged;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.eden.client.jfx.EdenClientApplication#init()
	 */
	@Override
	public void init() {
		super.init();
	    //   loadData();
	}

    //-------------------------------------------------------------------
    /**
     * @throws IOException
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
	@Override
    public void start(Stage stage) throws Exception {
    	stage.setOnShowing((ev) -> System.out.println("----SHOWING"));
    	stage.setOnShown((ev) -> System.out.println("----SHOWN"));
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

        PDFViewerConfig.setPDFPathResolver( (id,lang) -> getPDFPathFor(RoleplayingSystem.SHADOWRUN6,id,lang));
        PDFViewerConfig.setEnabled( super.isPDFEnabled());

        getAppLayout().visibleProperty().addListener( (ov,o,n) -> {
        	logger.log(Level.INFO, "Visibility changed to "+n+"-------------------");
        	System.err.println("Visibility changed to "+n+"-------------------");
            ResponsiveControlManager.initialize(getAppLayout());
        });
		logger.log(Level.INFO, "LEAVE start (thread {0})", Thread.currentThread());
    }

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.eden.client.jfx.EdenClientApplication#loadData()
	 */
    @Override
	protected void loadData() {
    	logger.log(Level.INFO, "Loading data-------------------------------------");
//		Shadowrun6DataPlugin plugin = new Shadowrun6DataPlugin();
//		plugin.init( );
//		logger.log(Level.INFO, "Loaded "+Shadowrun6Core.getItemList(SR6Spell.class).size()+" spells");
	}

    //-------------------------------------------------------------------
    /**
     * @see de.rpgframework.eden.client.jfx.EdenClientApplication#getErrorDialogResourceBundle()
     */
    @Override
	protected ResourceBundle getErrorDialogResourceBundle() {
		return RES;
	}

    //-------------------------------------------------------------------
    /**
     * @see de.rpgframework.eden.client.jfx.EdenClientApplication#getErrorDialogImage()
     */
    @Override
	protected Image getErrorDialogImage() {
		return new Image(ComLinkMain.class.getResourceAsStream("ErrorDialog.png"));
	}

    //-------------------------------------------------------------------
    /**
     * @see de.rpgframework.eden.client.jfx.EdenClientApplication#getWarningDialogImage()
     */
    @Override
	protected Image getWarningDialogImage() {
		return new Image(ComLinkMain.class.getResourceAsStream("WarningDialog.png"));
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.eden.client.jfx.EdenClientApplication#getInfoDialogImage()
	 */
    @Override
	protected Image getInfoDialogImage() {
		return new Image(ComLinkMain.class.getResourceAsStream("InfoDialog.png"));
	}

    //-------------------------------------------------------------------
    /**
     * @see de.rpgframework.eden.client.jfx.EdenClientApplication#getUpdateDialogImage()
     */
    @Override
	protected Image getUpdateDialogImage() {
		return new Image(ComLinkMain.class.getResourceAsStream("UpdateDialog.png"));
	}

    //-------------------------------------------------------------------
    /**
     * @see de.rpgframework.eden.client.jfx.EdenClientApplication#getSecurityDialogImage()
     */
    @Override
	public Image getSecurityDialogImage() {
		return new Image(ComLinkMain.class.getResourceAsStream("SecurityDialog.png"));
	}

//	//-------------------------------------------------------------------
//	/**
//	 * @see de.rpgframework.eden.client.jfx.EdenClientApplication#showUIMessage(int, java.lang.String, java.lang.Throwable)
//	 */
//    @Override
//	protected void showUIMessage(int type, String mess, Throwable cause, Path file) {
//		String title = (type==0)?ResourceI18N.get(RES, "uimessage.info"):((type==1)?ResourceI18N.get(RES, "uimessage.warning"):ResourceI18N.get(RES, "uimessage.error"));
//
//		Label content= new Label(mess);
//		content.setWrapText(true);
//		content.getStyleClass().add("text-body");
//
//
//		VBox layout = new VBox(10, content);
//		if (cause!=null) {
//			StringWriter out = new StringWriter();
//			cause.printStackTrace(new PrintWriter(out));
//			TextArea taTrace = new TextArea(out.toString());
//			taTrace.setPrefColumnCount(40);
//			TitledPane tpTrace = new TitledPane(ResourceI18N.get(RES, "uimessage.stacktrace"), taTrace);
//			tpTrace.setStyle("-fx-font-size: 100%");
//			Accordion accord = new Accordion(tpTrace);
//			if (file!=null) {
//				TitledPane tpPath = new TitledPane(ResourceI18N.get(RES, "uimessage.file"), new Label(file.toAbsolutePath().toString()));
//				tpPath.setStyle("-fx-font-size: 100%");
//				accord.getPanes().add(tpPath);
//			}
//			layout.getChildren().add(accord);
//		}
//
//		ManagedDialog dialog = new ManagedDialog(title, layout, CloseType.OK);
//		dialog.setImage(new Image(ComLinkMain.class.getResourceAsStream("Dialog1.png")));
//
//		showAlertAndCall(dialog, null);
//	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.FlexibleApplication#populateNavigationPane(org.prelle.javafx.NavigationPane)
	 */
	@Override
	public void populateNavigationPane(NavigationPane drawer) {
		// Header
		Label header = new Label("CommLink6");
		BitmapIcon icoCommLink = new BitmapIcon(ComLinkMain.class.getResource("AppLogo.png").toString());
		icoCommLink.setStyle("-fx-pref-width: 3em");
		header.setGraphic(icoCommLink);
		drawer.setHeader(header);

		// Items
		SymbolIcon icoLookup = new SymbolIcon("library");
		//FontIcon icoAbout   = new FontIcon("\uD83D\uDEC8");
		SymbolIcon icoAbout = new SymbolIcon("setting");
		FontIcon icoAccount = new FontIcon("\uE2AF");
//		SymbolIcon icoPDF     = new SymbolIcon("pdf");
		navigChars  = new MenuItem(ResourceI18N.get(RES, "navig.chars"), new SymbolIcon("people"));
		navigLookup = new MenuItem(ResourceI18N.get(RES, "navig.lookup"), icoLookup);
		navigAccount= new MenuItem(ResourceI18N.get(RES, "navig.account"), icoAccount);
//		navigPDF    = new MenuItem(ResourceI18N.get(RES, "navig.pdf"), icoPDF);
		navigAbout  = new MenuItem(ResourceI18N.get(RES, "navig.about"), icoAbout);
		navigChars  .setId("navig-chars");
		navigLookup .setId("navig-lookup");
		navigAbout  .setId("navig-about");
//		navigPDF    .setId("navig-pdf");
		navigAccount.setId("navig-account");

		drawer.getItems().addAll(navigChars, navigLookup, navigAbout);
//		if (!Platform.isDesktop() || !PDFViewerServiceFactory.create().isPresent()) {
//			drawer.getItems().remove(navigPDF);
//		}

		// Footer
		Image img = new Image(ComLinkMain.class.getResourceAsStream("SR6Logo2.png"));
		if (img!=null) {
			ImageView ivShadowrun = new ImageView(img);
			ivShadowrun.setId("footer-logo");
			ivShadowrun.setPreserveRatio(true);
			ivShadowrun.fitWidthProperty().bind(drawer.prefWidthProperty());
			bxFooter.getChildren().add(ivShadowrun);
		}
		drawer.setFooter(bxFooter);
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
				return new AboutPage(super.getDirectories(), this, RoleplayingSystem.SHADOWRUN6);
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
			logger.log(Level.ERROR, "Failed creating page: "+menuItem,e);
		}
		logger.log(Level.WARNING, "No page for " + menuItem.getText());
		return null;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.eden.client.jfx.EdenClientApplication#importXML(byte[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Shadowrun6Character importXML(byte[] xml) {
		try {
			Shadowrun6Character model = Shadowrun6Core.decode(xml);
			Shadowrun6Tools.resolveChar(model);
			Shadowrun6Tools.runProcessors(model, Locale.getDefault());
			return model;
		} catch (CharacterIOException e) {
			logger.log(Level.ERROR, "Failed decoding imported XML",e);
			return null;
		}
	}

}