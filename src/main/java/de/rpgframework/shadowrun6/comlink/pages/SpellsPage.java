package de.rpgframework.shadowrun6.comlink.pages;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.prelle.javafx.JavaFXConstants;
import org.prelle.javafx.Page;
import org.prelle.javafx.ResponsiveControlManager;
import org.prelle.javafx.WindowMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.rpgframework.ResourceI18N;
import de.rpgframework.jfx.RPGFrameworkJavaFX;
import de.rpgframework.shadowrun.ASpell;
import de.rpgframework.shadowrun.chargen.jfx.listcell.SpellListCell;
import de.rpgframework.shadowrun.chargen.jfx.pane.SpellDescriptionPane;
import de.rpgframework.shadowrun6.Shadowrun6Core;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

/**
 * @author prelle
 *
 */
public class SpellsPage extends Page {
	
	private final static Logger logger = LoggerFactory.getLogger(SpellsPage.class);
	
	private final static ResourceBundle RES = ResourceBundle.getBundle(SpellsPage.class.getName());
	
	private ChoiceBox<ASpell.Category> cbType;
	private TextField tfSearch;
	private ListView<ASpell> lvResult;
	private VBox description;
	private Label descTitle;
	private Label descSources;
	private TextFlow descFlow;
				
	//-------------------------------------------------------------------
	public SpellsPage() {
		initComponents();
		initLayout();
		initStyle();
		description.setVisible(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);
		description.setManaged(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);
		refresh();
	}

	//-------------------------------------------------------------------
	private void initComponents() {
		cbType = new ChoiceBox<>();
		cbType.getItems().addAll(ASpell.Category.values());
		Collections.sort(cbType.getItems(), new Comparator<ASpell.Category>() {
			public int compare(ASpell.Category o1, ASpell.Category o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		cbType.setConverter(new StringConverter<ASpell.Category>() {
			public String toString(ASpell.Category val) {
				if (val==null) return "";
				return val.getName();
			}
			public ASpell.Category fromString(String string) { return null; }
		});
		cbType.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> {
			refresh();
			tfSearch.clear();
		});
		
		tfSearch = new TextField();
		descTitle = new Label();
		descTitle.getStyleClass().add(JavaFXConstants.STYLE_HEADING3);
		descTitle.setStyle("-fx-text-fill: highlight");
		descSources = new Label();
		
		lvResult = new ListView<ASpell>();
		lvResult.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		lvResult.setCellFactory(lv -> new SpellListCell());
		// React to list selections
		lvResult.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> {
			if (n!=null)
				showAction(n);
		});
		
		tfSearch.setOnAction(ev -> {
			refresh();
		});
	}

	//-------------------------------------------------------------------
	private void initLayout() {
		Label header = new Label(ResourceI18N.get(RES, "heading.type"));
		header.getStyleClass().add(JavaFXConstants.STYLE_HEADING5);
		VBox secondary = new VBox(5, header, cbType, tfSearch);
		setSecondaryContent(secondary);
		
		description = new VBox(5, descTitle, descSources);
		description.setId("description");
		
		HBox content = new HBox(20, lvResult, description);
		content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		VBox.setVgrow(content, Priority.ALWAYS);
		setContent(content);
	}

	//-------------------------------------------------------------------
	private void initStyle() {
		cbType.setStyle("-fx-max-width: 15em");
		tfSearch.setStyle("-fx-max-width: 15em");
		lvResult.setStyle("-fx-pref-width: 25em");
	}
	
	//-------------------------------------------------------------------
	private void showAction(ASpell value) {
		logger.info("Show spell "+value);
		
		description.setVisible(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);
		description.setManaged(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);

		if (ResponsiveControlManager.getCurrentMode()==WindowMode.MINIMAL) {
			try {
//				Page toOpen = ScreenLoader.loadSpellDescriptionPage(value);
				Page toOpen = new Page(value.getName());
				SpellDescriptionPane box = new SpellDescriptionPane();
				box.setData(value);
				toOpen.setContent(box);
				getAppLayout().navigateTo(toOpen, false);
			} catch (Exception e) {
				logger.error("Error opening SpellDescriptionPage",e);
			} 
		} else {
			descTitle.setText(value.getName());
			descSources.setText(RPGFrameworkJavaFX.createSourceText(value));
			description.getChildren().retainAll(descTitle, descSources);
			SpellDescriptionPane box = new SpellDescriptionPane();
			box.setData(value);
				description.getChildren().add(box);
		}
	}

	//-------------------------------------------------------------------
	private void refresh() {
		logger.warn("TODO: refresh");
		List<ASpell> list = Shadowrun6Core.getItemList(ASpell.class);
		String key = tfSearch.getText(); 
		if (key!=null && !key.isBlank()) {
			list = list.stream().filter(crea -> crea.getName(Locale.getDefault()).contains(key)).collect(Collectors.toList());
		}
		if (cbType.getValue()!=null) {
			list = list.stream().filter(crea -> crea.getCategory()==cbType.getValue()).collect(Collectors.toList());
		}
		Collections.sort(list, new Comparator<ASpell>() {
			public int compare(ASpell o1, ASpell o2) {
				return Collator.getInstance().compare(o1.getName(), o2.getName());
			}
		});
		lvResult.getItems().setAll(list);
	}
	
}
