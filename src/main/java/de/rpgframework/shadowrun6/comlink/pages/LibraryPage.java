package de.rpgframework.shadowrun6.comlink.pages;

import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.prelle.javafx.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rpgframework.ResourceI18N;
import de.rpgframework.genericrpg.modification.Modification;
import de.rpgframework.jfx.FilteredListPage;
import de.rpgframework.shadowrun.MetaType;
import de.rpgframework.shadowrun.Spell;
import de.rpgframework.shadowrun.chargen.jfx.listcell.SpellListCell;
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

	private final static Logger logger = LoggerFactory.getLogger(LibraryPage.class);

	private FlowPane content;
	private Button btnSpells;
	private Button btnMetatypes;

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

		btnMetatypes    = new Button(ResourceI18N.get(RES, "category.metatypes"));
		btnMetatypes.setId("metatypes");
		btnMetatypes.getStyleClass().add("category-button");
		btnMetatypes.graphicProperty().addListener( scaleButtons);
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		content = new FlowPane(btnSpells, btnMetatypes);
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
		btnMetatypes.setOnAction(ev -> openMetatypes(ev));
	}

	//-------------------------------------------------------------------
	private void openSpells(ActionEvent ev) {
		logger.info("Navigate Spells");
		logger.info("   history: "+getAppLayout().canGoBack());
		try {
			FilteredListPage<Spell> page =new FilteredListPage<Spell>(
					ResourceI18N.get(LibraryPage.RES, "category.spells"), 
					() -> Shadowrun6Core.getItemList(Spell.class), 
					new SpellDescriptionPane()
					);
			page.setCellFactory(lv -> new SpellListCell());
			page.setFilterInjector(new FilterSpells());
			getAppLayout().navigateTo(page, false);
		} catch (Exception e) {
			logger.error("Error opening SpellPage",e);
		}
	}

	//-------------------------------------------------------------------
	private void openMetatypes(ActionEvent ev) {
		logger.info("Navigate Metatypes");
		logger.info("   history: "+getAppLayout().canGoBack());
		logger.info("Navigate Metatypes  "+Shadowrun6Core.getItemList(SR6MetaType.class));
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
			getAppLayout().navigateTo(page, false);
		} catch (Exception e) {
			logger.error("Error opening MetatypesPage",e);
		}
	}
}
