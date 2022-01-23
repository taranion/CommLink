package de.rpgframework.shadowrun6.comlink.pages;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import org.prelle.javafx.ApplicationScreen;
import org.prelle.javafx.Page;

import de.rpgframework.ResourceI18N;
import de.rpgframework.genericrpg.modification.Modification;
import de.rpgframework.jfx.FilteredListPage;
import de.rpgframework.jfx.GenericDescriptionVBox;
import de.rpgframework.shadowrun.AdeptPower;
import de.rpgframework.shadowrun.Quality;
import de.rpgframework.shadowrun.ASpell;
import de.rpgframework.shadowrun.chargen.jfx.listcell.AdeptPowerListCell;
import de.rpgframework.shadowrun.chargen.jfx.listcell.QualityListCell;
import de.rpgframework.shadowrun.chargen.jfx.listcell.SpellListCell;
import de.rpgframework.shadowrun.chargen.jfx.pages.FilterQualities;
import de.rpgframework.shadowrun.chargen.jfx.pane.AdeptPowerPane;
import de.rpgframework.shadowrun.chargen.jfx.pane.MetatypePane;
import de.rpgframework.shadowrun.chargen.jfx.pane.SpellDescriptionPane;
import de.rpgframework.shadowrun6.SR6MetaType;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import de.rpgframework.shadowrun6.Shadowrun6Tools;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

/**
 * @author prelle
 *
 */
public class LibraryPage extends Page {

	public final static ResourceBundle RES = ResourceBundle.getBundle(LibraryPage.class.getName());

	private final static Logger logger = System.getLogger(LibraryPage.class.getPackageName());

	private FlowPane content;
	private Button btnSpells;
	private Button btnPowers;
	private Button btnMetatypes;
	private Button btnQualities;

	//-------------------------------------------------------------------
	public LibraryPage() {
		super(ResourceI18N.get(RES,"page.title"));
		setId("library");
		
		initComponents();
		initLayout();
		initStyle();
		initInteractivity();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		ChangeListener<Node> scaleButtons = (ov,o,n) -> {
        	if (n!=null && (n instanceof ImageView)) {
        		((ImageView)n).setFitHeight(60);
        		((ImageView)n).setPreserveRatio(true);
        	}
        };

		btnSpells    = new Button(ResourceI18N.get(RES, "category.spells"));
		btnSpells.setId("spells");
		btnSpells.getStyleClass().add("category-button");
		btnSpells.graphicProperty().addListener( scaleButtons);

		btnPowers    = new Button(ResourceI18N.get(RES, "category.powers"));
		btnPowers.setId("powers");
		btnPowers.getStyleClass().add("category-button");
		btnPowers.graphicProperty().addListener( scaleButtons);

		btnQualities    = new Button(ResourceI18N.get(RES, "category.qualities"));
		btnQualities.setId("qualities");
		btnQualities.getStyleClass().add("category-button");
		btnQualities.graphicProperty().addListener( scaleButtons);

		btnMetatypes    = new Button(ResourceI18N.get(RES, "category.metatypes"));
		btnMetatypes.setId("metatypes");
		btnMetatypes.getStyleClass().add("category-button");
		btnMetatypes.graphicProperty().addListener( scaleButtons);
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		content = new FlowPane(btnSpells, btnPowers, btnQualities, btnMetatypes);
		content.setVgap(10);
		content.setHgap(10);
		content.setId("categories");
		setContent(content);
	}

	//-------------------------------------------------------------------
	private void initStyle() {
		ChangeListener<Node> scaleButtons = (ov,o,n) -> {
			if (n!=null && (n instanceof ImageView)) {
				((ImageView)n).setFitHeight(60);
				((ImageView)n).setPreserveRatio(true);
			}
		};

		content.getChildren().forEach(node -> {
			node.getStyleClass().add("category-button");
			((Button)node).graphicProperty().addListener( scaleButtons);
		});
	}

	//-------------------------------------------------------------------
	private void initInteractivity() {
		btnSpells.setOnAction(ev -> openSpells(ev));
		btnPowers.setOnAction(ev -> openPowers(ev));
		btnQualities.setOnAction(ev -> openQualities(ev));
		btnMetatypes.setOnAction(ev -> openMetatypes(ev));
	}

	//-------------------------------------------------------------------
	private void openSpells(ActionEvent ev) {
		logger.log(Level.WARNING, "Navigate Spells");
		try {
			FilteredListPage<ASpell> page =new FilteredListPage<ASpell>(
					ResourceI18N.get(LibraryPage.RES, "category.spells"), 
					() -> Shadowrun6Core.getItemList(ASpell.class), 
					new SpellDescriptionPane()
					);
			page.setCellFactory(lv -> new SpellListCell());
			page.setFilterInjector(new FilterSpells());
//			getAppLayout().navigateTo(page, false);
			getAppLayout().getApplication().openScreen(new ApplicationScreen(page));
		} catch (Exception e) {
			logger.log(Level.ERROR, "Error opening SpellPage",e);
		}
	}

	//-------------------------------------------------------------------
	private void openPowers(ActionEvent ev) {
		logger.log(Level.DEBUG, "Navigate Powers");
		try {
			FilteredListPage<AdeptPower> page =new FilteredListPage<AdeptPower>(
					ResourceI18N.get(LibraryPage.RES, "category.powers"), 
					() -> Shadowrun6Core.getItemList(AdeptPower.class), 
					new AdeptPowerPane((r) -> Shadowrun6Tools.getRequirementString(r, Locale.getDefault()))
					);
			page.setCellFactory(lv -> new AdeptPowerListCell());
//			page.setFilterInjector(new FilterQualities());
			getAppLayout().getApplication().openScreen(new ApplicationScreen(page));
		} catch (Exception e) {
			logger.log(Level.ERROR, "Error opening Powers",e);
		}
	}

	//-------------------------------------------------------------------
	private void openQualities(ActionEvent ev) {
		logger.log(Level.DEBUG, "Navigate Qualities");
		try {
			FilteredListPage<Quality> page =new FilteredListPage<Quality>(
					ResourceI18N.get(LibraryPage.RES, "category.qualities"), 
					() -> Shadowrun6Core.getItemList(Quality.class), 
					new GenericDescriptionVBox<Quality>((r) -> Shadowrun6Tools.getRequirementString(r, Locale.getDefault()))
					);
			page.setCellFactory(lv -> new QualityListCell(null));
			page.setFilterInjector(new FilterQualities());
			getAppLayout().getApplication().openScreen(new ApplicationScreen(page));
		} catch (Exception e) {
			logger.log(Level.ERROR, "Error opening QualityPage",e);
		}
	}

	//-------------------------------------------------------------------
	private void openMetatypes(ActionEvent ev) {
		logger.log(Level.INFO, "Navigate Metatypes");
		logger.log(Level.INFO, "Navigate Metatypes  "+Shadowrun6Core.getItemList(SR6MetaType.class));
		BiFunction<SR6MetaType,Modification,String> modResolver = new BiFunction<SR6MetaType,Modification, String>() {
			public String apply(SR6MetaType data, Modification t) {
				return Shadowrun6Tools.getModificationString(data, t);
			}
		};
		MetatypePane<SR6MetaType> descPane = new MetatypePane<SR6MetaType>(modResolver);
		descPane.setItems(Shadowrun6Core.getItemList(SR6MetaType.class));
		try {
			FilteredListPage<SR6MetaType> page =new FilteredListPage<SR6MetaType>(
					ResourceI18N.get(LibraryPage.RES, "category.metatypes"), 
					() -> Shadowrun6Core.getItemList(SR6MetaType.class), 
					descPane
					);
//			page.setCellFactory(lv -> new SpellListCell());
//			page.setFilterInjector(new FilterSpells());
			getAppLayout().getApplication().openScreen(new ApplicationScreen(page));
		} catch (Exception e) {
			logger.log(Level.ERROR, "Error opening MetatypesPage",e);
		}
	}
}
