package de.rpgframework.shadowrun6.comlink;

import javafx.application.Preloader;
import javafx.application.Preloader.ErrorNotification;
import javafx.application.Preloader.PreloaderNotification;
import javafx.application.Preloader.ProgressNotification;
import javafx.application.Preloader.StateChangeNotification;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author prelle
 *
 */
public class CommlinkPreloader extends Preloader {

	ProgressBar bar;
    Stage stage;

	//-------------------------------------------------------------------
	/**
	 */
	public CommlinkPreloader() {
		System.out.println("Preloader.<init>");
		// TODO Auto-generated constructor stub
	}

	private Scene createPreloaderScene() {
        bar = new ProgressBar();
        HBox p = new HBox(bar);
        return new Scene(p, 300, 150);
    }

	//-------------------------------------------------------------------
	/**
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Preloader.start");
		this.stage = primaryStage;
        stage.setScene(createPreloaderScene());
        stage.show();
	}
	//-------------------------------------------------------------------
	/**
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Preloader.stop");
	}

	//-------------------------------------------------------------------
	/**
	 * @see javafx.application.Preloader#handleProgressNotification(javafx.application.Preloader.ProgressNotification)
	 */
	@Override
	public void handleProgressNotification(ProgressNotification info) {
		System.out.println("Preloader.handleProgressNotification: "+info.getProgress());
		bar.setProgress(info.getProgress());
    }

	//-------------------------------------------------------------------
	/**
	 * @see javafx.application.Preloader#handleStateChangeNotification(javafx.application.Preloader.StateChangeNotification)
	 */
	@Override
	public void handleStateChangeNotification(StateChangeNotification info) {
		System.out.println("Preloader.handleStateChangeNotification "+info.getType());
		if (info.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.hide();
        }
    }

	//-------------------------------------------------------------------
	/**
	 * @see javafx.application.Preloader#handleApplicationNotification(javafx.application.Preloader.PreloaderNotification)
	 */
	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		System.out.println("Preloader.handleApplicationNotification "+info.toString());
   }

	//-------------------------------------------------------------------
	/**
	 * @see javafx.application.Preloader#handleErrorNotification(javafx.application.Preloader.ErrorNotification)
	 */
	@Override
    public boolean handleErrorNotification(ErrorNotification info) {
		System.out.println("Preloader.handleErrorNotification "+info.toString());
        return false;
    }


}
