package de.rpgframework.shadowrun6.updater;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.update4j.Configuration;
import org.update4j.service.Delegate;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JavaFxDelegate extends Application implements Delegate {

	@Override
	public long version() {
		return 0;
	}

	@Override
	public void main(List<String> args) throws Throwable {
		launch();
	}

	// for testing purposes only
	public static void main(String[] args) {
		launch();
	}

	public static List<Image> images;
	public static Image inverted;

	@Override
	public void init() {
		System.setProperty("update4j.suppress.warning", "false");

		List<String> sizes = List.of("tiny", "small", "medium", "large", "xlarge");
//		images = sizes.stream()
//						.map(s -> ("/icons/update4j-icon-" + s + ".png"))
//						.map(s -> getClass().getResource(s).toExternalForm())
//						.map(Image::new)
//						.collect(Collectors.toList());
//		inverted = new Image("/icons/update4j-icon-invert.png");
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		primaryStage.setMinWidth(650);
//		primaryStage.setMinHeight(500);
		primaryStage.initStyle(StageStyle.TRANSPARENT);

		StartupView startup = new StartupView(primaryStage);
		startup.releaseTypeProperty().addListener( (ov,o,n) -> {});

		Scene scene = new Scene(startup, Color.TRANSPARENT);
		primaryStage.setOpacity(0.2);
//		scene.getStylesheets().add(getClass().getResource("root.css").toExternalForm());

//		primaryStage.getIcons().addAll(images);
		primaryStage.setScene(scene);

		primaryStage.setTitle("Commlink 6 Updater");
		Platform.setImplicitExit(true);
		primaryStage.show();
	}

}