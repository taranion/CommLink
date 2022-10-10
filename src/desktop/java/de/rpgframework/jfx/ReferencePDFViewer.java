package de.rpgframework.jfx;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Path;
import java.util.function.BiFunction;

import org.prelle.javafx.FlexibleApplication;
import org.prelle.javafx.SymbolIcon;

import de.rpgframework.core.RoleplayingSystem;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author prelle
 *
 */
public class ReferencePDFViewer {
	
	public static class PathAndOffset {
		public String path;
		public int offset;
		public PathAndOffset(String path, int offset) {
			this.path = path;
			this.offset = offset;
		}
	}
	
	private final static Logger logger = System.getLogger("rpgframework.pdf");
	
	private static String path = "/home/data/Rollenspiel/Shadowrun/SR6_Englisch/Shadowrun_Sixth_World_Core_Rulebook_City_Edition_Seattle.pdf";

	private static BiFunction<String, String, PathAndOffset> pathResolver;
	
	private static boolean enabled = false;
	private static ImageView iView;
	private static Scene scene;
	private static Stage stage;
	private static VBox parent;
	private static ButtonBar bar;
	private static Button btnPrev, btnNext;
	private static int currentPage;
	
	//-------------------------------------------------------------------
	public static void setPDFPathResolver(BiFunction<String, String, PathAndOffset> resolver) {
		ReferencePDFViewer.pathResolver = resolver;
		System.out.println("~~~~~~ReferencePDFViewer: desktop ~~~~~~~~~");
	}
	
	//-------------------------------------------------------------------
	private static void ensureStage() {
		if (stage==null) {
			iView = new ImageView();
			btnPrev = new Button(null,new SymbolIcon("back"));
			btnNext = new Button(null,new SymbolIcon("forward"));
			btnPrev.setOnAction(ev -> {
				if (currentPage>0) showPage(currentPage-1);
			});
			btnNext.setOnAction(ev -> showPage(currentPage+1));
			
			bar = new ButtonBar();
			bar.getButtons().addAll(btnPrev, btnNext);
			parent = new VBox(10,iView, bar);
			parent.setAlignment(Pos.CENTER);
			scene = new Scene(parent);
			scene.getStylesheets().addAll( FlexibleApplication.getInstance().getAppLayout().getScene().getStylesheets() );
			stage = new Stage();
			stage.setScene(scene);
			stage.show();
		}
	}
	
	//-------------------------------------------------------------------
	public static void setFile(RoleplayingSystem rules, String productID, String lang, Path path) {
		
	}
	
	//-------------------------------------------------------------------
	public static void show(RoleplayingSystem rules, String productID, String lang, int page) {
		logger.log(Level.INFO, "Show {1} for RPG {0} in language ''{2}'' on page {3}", rules, productID, lang, page );
		if (!enabled) return;
		if (pathResolver==null || page==0)
			return;
		
		ensureStage();
		PathAndOffset pair = pathResolver.apply(productID, lang);
		path = pair.path;
		page += pair.offset;
		logger.log(Level.INFO, "..resolved to path: {0}", path);
		if (path==null) return;
//		path = "/home/data/Rollenspiel/Shadowrun/SR6_Englisch/Shadowrun_Sixth_World_Core_Rulebook_City_Edition_Seattle.pdf";
		showPage(page);
	}
	
	//-------------------------------------------------------------------
	private static void showPage(int page) {
		currentPage = page;
		if (path==null || path.isEmpty()) return;
		Thread thread = new Thread( () -> {
		try {
//			FileInputStream in = new FileInputStream(path);
////			PDDocument pdDocument = Loader.loadPDF(in);
//			PDDocument pdDocument = PDDocument.load(in);
//			PDFRenderer renderer = new PDFRenderer(pdDocument);
//			// Evil line
//			BufferedImage image = renderer.renderImageWithDPI(page, 100,ImageType.RGB);
//			image.createGraphics();
//			Image fxImg = SwingFXUtils.toFXImage(image, null);
			Image fxImg = new Image(ClassLoader.getSystemResourceAsStream("Rendering.jpg"));
			iView.setImage(fxImg);
			stage.setHeight(fxImg.getHeight()+56+bar.getHeight());
			stage.setWidth(fxImg.getWidth()+11);
			parent.requestLayout();
			if (!stage.isShowing())
				stage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		});
		thread.run();
	}

	//-------------------------------------------------------------------
	public static void setEnabled(boolean value) {
		ReferencePDFViewer.enabled = value;
	}

}
