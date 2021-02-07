package de.rpgframework.shadowrun6.comlink;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.prelle.javafx.Page;
import org.prelle.javafx.ResponsiveControlManager;
import org.prelle.javafx.WindowMode;

import de.rpgframework.shadowrun6.Shadowrun6Core;
import de.rpgframework.shadowrun6.Spell;
import de.rpgframework.shadowrun6.chargen.jfx.SR6JFXUtil;
import de.rpgframework.shadowrun6.chargen.jfx.pane.SpellDescriptionPane;
import de.rpgframework.shadowrun6.comlink.cells.SpellListCell;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;

public class SpellsPageController {
	
	private Logger logger = LoggerFactory.getLogger(SpellsPageController.class);

	private transient Page page;

	@FXML
	private ChoiceBox<Spell.Category> cbType;
	@FXML
	private TextField tfSearch;
	@FXML
	private ListView<Spell> lvResult;
	@FXML
	private VBox description;
	@FXML
	private Label descTitle;
	@FXML
	private Label descSources;
	@FXML
	private TextFlow descFlow;

	//-------------------------------------------------------------------
	public SpellsPageController() {
		// TODO Auto-generated constructor stub
	}

	//-------------------------------------------------------------------
	@FXML
	public void initialize() {
		cbType.getItems().addAll(Spell.Category.values());
		Collections.sort(cbType.getItems(), new Comparator<Spell.Category>() {
			public int compare(Spell.Category o1, Spell.Category o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		cbType.setConverter(new StringConverter<Spell.Category>() {

			@Override
			public String toString(Spell.Category val) {
				if (val==null) return "";
				return val.getName();
			}

			@Override
			public Spell.Category fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		cbType.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> {
			refresh();
			tfSearch.clear();
		});
		
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
	private void showAction(Spell value) {
		logger.info("Show spell "+value);
		
		description.setVisible(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);
		description.setManaged(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);

		if (ResponsiveControlManager.getCurrentMode()==WindowMode.MINIMAL) {
			try {
//				Page toOpen = ScreenLoader.loadSpellDescriptionPage(value);
				Page toOpen = new Page(value.getName());
				SpellDescriptionPane box = new SpellDescriptionPane(value);
				toOpen.setContent(box);
				page.getAppLayout().navigateTo(toOpen, false);
			} catch (Exception e) {
				logger.error("Error opening SpellDescriptionPage",e);
			} 
		} else {
			descTitle.setText(value.getName());
			descSources.setText(SR6JFXUtil.createSourceText(value));
			description.getChildren().retainAll(descTitle, descSources);
			SpellDescriptionPane box = new SpellDescriptionPane(value);
//				VBox box = ScreenLoader.loadSpellDescriptionVBox(value);
				description.getChildren().add(box);
		}
	}

	//-------------------------------------------------------------------
	public void setComponent(Page page) {
		this.page = page;
		description.setVisible(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);
		description.setManaged(ResponsiveControlManager.getCurrentMode()!=WindowMode.MINIMAL);
		refresh();
		
		
	}

	//-------------------------------------------------------------------
	private void refresh() {
		logger.warn("TODO: refresh");
		List<Spell> list = Shadowrun6Core.getItemList(Spell.class);
		String key = tfSearch.getText(); 
		if (key!=null && !key.isBlank()) {
			list = list.stream().filter(crea -> crea.getName(Locale.getDefault()).contains(key)).collect(Collectors.toList());
		}
		if (cbType.getValue()!=null) {
			list = list.stream().filter(crea -> crea.getCategory()==cbType.getValue()).collect(Collectors.toList());
		}
		Collections.sort(list, new Comparator<Spell>() {
			public int compare(Spell o1, Spell o2) {
				return Collator.getInstance().compare(o1.getName(), o2.getName());
			}
		});
		lvResult.getItems().setAll(list);
	}

}
