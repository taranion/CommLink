package de.rpgframework.shadowrun6.comlink.pages;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.rpgframework.jfx.AFilterInjector;
import de.rpgframework.jfx.FilteredListPage;
import de.rpgframework.shadowrun.Spell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;

/**
 * @author prelle
 *
 */
public class FilterSpells extends AFilterInjector<Spell> {

	private ChoiceBox<Spell.Category> cbType;

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.jfx.AFilterInjector#addFilter(de.rpgframework.jfx.FilteredListPage, javafx.scene.layout.FlowPane)
	 */
	@Override
	public void addFilter(FilteredListPage<Spell> page, FlowPane filterPane) {
		/*
		 * Spell Category
		 */
		cbType = new ChoiceBox<Spell.Category>();
		cbType.getItems().add(null);
		cbType.getItems().addAll(Spell.Category.values());
		Collections.sort(cbType.getItems(), new Comparator<Spell.Category>() {
			public int compare(Spell.Category o1, Spell.Category o2) {
				if (o1==null) return -1;
				if (o2==null) return  1;
				return Collator.getInstance().compare(o1.getName(), o2.getName());
			}
		});
		cbType.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> page.refreshList());
		cbType.setConverter(new StringConverter<Spell.Category>() {
			public String toString(Spell.Category val) {
				if (val==null) return "alle Typen";
				return val.getName();
			}
			public Spell.Category fromString(String string) { return null; }
		});
		filterPane.getChildren().add(cbType);

	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.jfx.AFilterInjector#applyFilter(de.rpgframework.jfx.FilteredListPage, java.util.List)
	 */
	@Override
	public List<Spell> applyFilter(FilteredListPage<Spell> page, List<Spell> input) {
		// Match spell category
		if (cbType.getValue()!=null) {
			input = input.stream()
					.filter(spell -> spell.getCategory()==cbType.getValue())
					.collect(Collectors.toList());
		}
		return input;
	}

}
