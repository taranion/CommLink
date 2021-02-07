package de.rpgframework.shadowrun6.comlink;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.prelle.javafx.AppLayout;
import org.prelle.javafx.BitmapIcon;
import org.prelle.javafx.CloseType;
import org.prelle.javafx.DebugPage;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.FontIcon;
import org.prelle.javafx.NavigationPane;
import org.prelle.javafx.Page;
import org.prelle.javafx.SymbolIcon;

import com.gluonhq.attach.settings.SettingsService;
import com.gluonhq.attach.util.Services;

import de.rpgframework.ResourceI18N;
import de.rpgframework.eden.client.EdenConnection;
import de.rpgframework.eden.client.EdenConnection.EdenPingInfo;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import de.rpgframework.shadowrun6.Spell;
import de.rpgframework.shadowrun6.data.Shadowrun6DataPlugin;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class ComLinkMain extends FlexibleApplication {
	
	private final static Logger logger = LoggerFactory.getLogger(ComLinkMain.class.getName());
	private final static String PREF_USER = "eden.user";
	private final static String PREF_PASS = "eden.pass";
	
	private ResourceBundle RES = ResourceBundle.getBundle(ComLinkMain.class.getName(), ComLinkMain.class.getModule());
	
	private int noLoginAttempts;
	
	private EdenConnection eden;
	private EdenPingInfo edenInfo;
	
	private MenuItem navigLookup;
	private MenuItem navigAccount;
	private MenuItem navigAbout;
	
    //-------------------------------------------------------------------
    public static void main(String[] args) {
		Locale.setDefault(Locale.GERMANY);
       launch(args);
    }
	
    //-------------------------------------------------------------------
	public ComLinkMain() {
	}

	//-------------------------------------------------------------------
	/**
	 * Read user credentials from SettingService - if present - or from
	 * Preferences otherwise
	 */
	private String[] readEdenCredentials() {
		SettingsService service = Services.get(SettingsService.class).orElse(null);
		if (service!=null) {
			logger.warn("SettingsService");
			String user = service.retrieve(PREF_USER);
			String pass = service.retrieve(PREF_PASS);			
			return new String[] {user,pass};
		}
		return new String[2];
	}

	//-------------------------------------------------------------------
	/**
	 * Write user credentials to SettingService - if present - or ro
	 * Preferences otherwise
	 */
	private void writeEdenCredentials(String user, String pass) {
		SettingsService service = Services.get(SettingsService.class).get();
		if (service!=null) {
			service.store(PREF_USER, user);
			service.store(PREF_PASS, pass);
		} else {
			Preferences pref = Preferences.userNodeForPackage(ComLinkMain.class);
			pref.put(PREF_USER, user);
			pref.put(PREF_PASS, pass);
		}
	}
	
	//-------------------------------------------------------------------
	private void setAuthViaLoginDialog() {
		Authenticator.setDefault(new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication() {
				logger.warn("getPasswordAuthentication after "+noLoginAttempts+" login attempts  "+getRequestingHost());
				LoginDialog dialog = new LoginDialog(noLoginAttempts);
				String[] pair = readEdenCredentials();
				dialog.setLogin(pair[0]);
				dialog.setPassword(pair[1]);
				 Object foo = ComLinkMain.this.showAndWait(dialog);
				 logger.warn("Returned "+foo);
				 if (foo!=CloseType.OK) {
					 try {
						stop();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 return null;
				 }
				 writeEdenCredentials(dialog.getLogin(), dialog.getPassword());
				noLoginAttempts++;
				return new PasswordAuthentication (dialog.getLogin(), dialog.getPassword().toCharArray());
			}
		});

	}

	//-------------------------------------------------------------------
	/**
	 * @see javafx.application.Application#init()
	 */
	@Override
	public void init() {
		// Preliminary connection object
		eden = new EdenConnection("localhost", 5000);
	}
	
    //-------------------------------------------------------------------
    /**
     * @throws IOException 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    public void start(Stage stage) throws Exception {
		stage.setMaxWidth(1400);
		stage.setMaxHeight(900);
		stage.setMinWidth(360);
		stage.setMinHeight(560);
		super.start(stage);
		getAppLayout().getNavigationPane().setSettingsVisible(false);
     	
//		ScenicView.show(stage.getScene());
		
		setAuthViaLoginDialog();
        
        loadData();
        
        stepPages();
        
        stage.getScene().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stepAccount();
    }

	//-------------------------------------------------------------------
    private boolean isOffline() {
    	return edenInfo==null;
    }

	//-------------------------------------------------------------------
    /**
     * 1. If there is no account configured yet, open a login dialog.
     *    In that login dialog, also offer a chance to open a webpagefor
     *    account creation
     */
	private void stepAccount() {
		logger.info("STEP: Account");
		edenInfo = eden.getInfo();
		logger.info("Received "+edenInfo);
		if (isOffline()) {
			// No connection to server
			logger.warn("No connection to Eden server");
			return;
		}
		
		/*
		 * See if there already is a user information present
		 */
		eden.getAccountInfo();
//		String[] pair = readEdenCredentials();
//		if (pair[0]!=null && pair[1]!=null) {
//			 eden.login(user, pass);
//		}
//		Services.get(SettingsService.class).ifPresentOrElse(service -> {
//		      String user = service.retrieve("eden.user");
//		      String pass = service.retrieve("eden.pass");
//				logger.warn("Try memorized login "+user);
//		      if (user!=null && pass!=null) {
//		    	  eden.login(user, pass);
//		      }
//		  }, () -> {logger.warn("No Settings service");});
	}

	//-------------------------------------------------------------------
	private void loadData() {
		Locale.setDefault(Locale.GERMANY);
		Shadowrun6DataPlugin plugin = new Shadowrun6DataPlugin();
		plugin.init( );
		logger.info("Loaded "+Shadowrun6Core.getItemList(Spell.class).size()+" spells");
//		logger.info("Loaded "+SplitterMondCore.getItemList(CreatureType.class).size()+" creature types");

	}

	//-------------------------------------------------------------------
	private void stepPages() {
		try {
			Page page = createPage(navigLookup);
			this.getAppLayout().navigateTo(page, true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.FlexibleApplication#populateNavigationPane(org.prelle.javafx.NavigationPane)
	 */
	@Override
	public void populateNavigationPane(NavigationPane drawer) {
		// Header
		Label header = new Label("AppName");
		BitmapIcon icoCommLink = new BitmapIcon(ComLinkMain.class.getResource("ic_launcher.png").toString());
		icoCommLink.setStyle("-fx-pref-width: 3em");
		header.setGraphic(icoCommLink);
		drawer.setHeader(header);

		// Items
		SymbolIcon icoLookup = new SymbolIcon("library");
		FontIcon icoAbout = new FontIcon("\uD83D\uDEC8");
		FontIcon icoAccount = new FontIcon("\uE2AF");
		navigLookup = new MenuItem(ResourceI18N.get(RES, "navig.lookup"), icoLookup);
		navigAccount= new MenuItem(ResourceI18N.get(RES, "navig.account"), icoAccount);
		navigAbout  = new MenuItem(ResourceI18N.get(RES, "navig.about"), icoAbout);
		
		drawer.getItems().addAll(navigLookup, navigAccount, navigAbout);
		
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
		if (menuItem==navigAbout) {
			return new DebugPage();
		} else if (menuItem==navigLookup) {
			return new LibraryPage();
		} else {
			logger.warn("No page for "+menuItem.getText());
		}
//		try {
//			if (menuItem==navLibrary) {
//				return ScreenLoader.loadLibraryPage();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		logger.warn("No page for "+menuItem.getText());
		return null;
	}

}