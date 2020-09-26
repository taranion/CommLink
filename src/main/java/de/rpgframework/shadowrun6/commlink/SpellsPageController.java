package de.rpgframework.shadowrun6.commlink;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.javafx.Page;
import org.prelle.shadowrun6.ShadowrunCore;
import org.prelle.shadowrun6.Spell;

import de.rpgframework.shadowrun6.commlink.cells.SpellListCell;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class SpellsPageController {
	
	private Logger logger = LogManager.getLogger(SpellsPageController.class);

	private transient Page page;

	@FXML
	private ChoiceBox<Spell.Category> cbType;
	@FXML
	private TextField tfSearch;
	@FXML
	private ListView<Spell> lvResult;

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
			List<Spell> list = ShadowrunCore.getItemList(Spell.class);
			if (n!=null) {
				tfSearch.clear();
				list = list.stream().filter(crea -> crea.getCategory()==n).collect(Collectors.toList());
			}
			logger.debug("Setting creature type to "+n+" returns "+list.size()+" spells");
			Collections.sort(list, new Comparator<Spell>() {
				public int compare(Spell o1, Spell o2) {
					return Collator.getInstance().compare(o1.getName(), o2.getName());
				}
			});
			lvResult.getItems().setAll(list);
		});
		
		lvResult.setCellFactory(lv -> new SpellListCell());
		
		tfSearch.setOnAction(ev -> {
			String key = tfSearch.getText();
			List<Spell> list = ShadowrunCore.getItemList(Spell.class);
			if (key!=null && !key.isBlank()) {
				cbType.getSelectionModel().clearSelection();
				list = list.stream().filter(crea -> crea.getName().toLowerCase().contains(key.toLowerCase())).collect(Collectors.toList());
			}
			Collections.sort(list, new Comparator<Spell>() {
				public int compare(Spell o1, Spell o2) {
					return Collator.getInstance().compare(o1.getName(), o2.getName());
				}
			});
			lvResult.getItems().setAll(list);
		});
	}

	//-------------------------------------------------------------------
	public void setComponent(Page page) {
		this.page = page;
	}

}
