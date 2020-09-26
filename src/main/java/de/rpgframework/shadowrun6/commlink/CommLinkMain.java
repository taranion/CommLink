package de.rpgframework.shadowrun6.commlink;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.javafx.AppLayout;
import org.prelle.javafx.CloseType;
import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.Page;
import org.prelle.rpgframework.shadowrun6.data.Shadowrun6DataPlugin;
import org.prelle.shadowrun6.ShadowrunCore;
import org.prelle.shadowrun6.Spell;

import com.gluonhq.attach.settings.SettingsService;
import com.gluonhq.attach.util.Services;

import de.rpgframework.eden.client.EdenConnection;
import de.rpgframework.eden.client.EdenConnection.EdenPingInfo;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


public class CommLinkMain extends FlexibleApplication {
	
	private final static Logger logger = LogManager.getLogger(CommLinkMain.class.getName());
	private final static String PREF_USER = "eden.user";
	private final static String PREF_PASS = "eden.pass";
	
	private ResourceBundle RES = ResourceBundle.getBundle(CommLinkMain.class.getName(), CommLinkMain.class.getModule());
	
	private int noLoginAttempts;
	
	private EdenConnection eden;
	private EdenPingInfo edenInfo;
	
    //-------------------------------------------------------------------
    public static void main(String[] args) {
		Locale.setDefault(Locale.GERMANY);
       launch(args);
    }
	
    //-------------------------------------------------------------------
	public CommLinkMain() {
		// Preliminary connection object
		eden = new EdenConnection("localhost", 5000);
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
			Preferences pref = Preferences.userNodeForPackage(CommLinkMain.class);
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
				 Object foo = CommLinkMain.this.showAndWait(dialog);
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
	 * Override to create your own custom applayout
	 */
	public AppLayout createDefaultAppLayout() {
		try {
			AppLayout app = ScreenLoader.loadMainScreen();
			app.setApplication(this);
			return app;
		} catch (Exception e) {
			logger.fatal("Failed creating layout",e);
		}
		AppLayout app =  new AppLayout();
		app.setApplication(this);
		return app;
	}
	
    //-------------------------------------------------------------------
    /**
     * @throws IOException 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    public void start(Stage stage) throws Exception {
    	super.start(stage);
     	
		setAuthViaLoginDialog();
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        Label label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        Label label2 = new Label("Filesystem "+FileSystems.getDefault().getRootDirectories().iterator().next()+" = "+System.getProperty("user.home"));
//        Label label3 = new Label("Screen = "+Screen.getPrimary().getVisualBounds().getWidth()+"x"+Screen.getPrimary().getVisualBounds().getHeight());
//        label3.setWrapText(true);
//        Label label4 = new Label("Stage = "+stage.getWidth()+"x"+stage.getHeight());
//        Label label5 = new Label("Scale = "+stage.getRenderScaleX());
//        
//        ImageView imageView = new ImageView(new Image(MondtorMain.class.getResourceAsStream("openduke.png")));
//        imageView.setFitHeight(150);
//        imageView.setPreserveRatio(true);
//        
////		FXMLLoader loader = new FXMLLoader(
////				MondtorMain.class.getResource("MainView.fxml"),
////				ResourceBundle.getBundle(MondtorMain.class.getName())
////				);
////		FXMLLoader.setDefaultClassLoader(JavaFXConstants.class.getClassLoader());
////		loader.setBuilderFactory(new ExtendedComponentBuilderFactory());
////		loader.setController(new MainViewController());
//		ManagedScreen screen = ScreenLoader.loadMainScreen();
////        MainScreen screen = new MainScreen();
////		manager.navigateTo(screen);
        
        loadData();

//        VBox root = new VBox(30, imageView, label, label2, label3, label4, label5);
//        root.setAlignment(Pos.CENTER);
//        Scene scene = new Scene(screen, 360,574);
//    	ModernUI.initialize(scene);
//        scene.getStylesheets().add(MondtorMain.class.getResource("styles.css").toExternalForm());
//        stage.setScene(scene);
//        stage.setTitle("Mondtor");
//        stage.show();
        
        stepPages();
        
        stage.getScene().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stepAccount();
    }

	//-------------------------------------------------------------------
	/**
	 * @see org.prelle.javafx.FlexibleApplication#createPage(org.prelle.javafx.NavigationItem)
	 */
	@SuppressWarnings("exports")
	@Override
	public Page createPage(MenuItem menuItem) {
		// TODO Auto-generated method stub
		logger.info("createPage("+menuItem+")");
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
		logger.info("Loaded "+ShadowrunCore.getItemList(Spell.class).size()+" spells");
//		logger.info("Loaded "+SplitterMondCore.getItemList(CreatureType.class).size()+" creature types");

	}

	//-------------------------------------------------------------------
	private void stepPages() {
		try {
			Page page = ScreenLoader.loadLibraryPage();
			this.getAppLayout().setVisiblePage(page);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}