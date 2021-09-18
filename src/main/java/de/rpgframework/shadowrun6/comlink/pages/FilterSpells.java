package de.rpgframework.shadowrun6.comlink.pages;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.rpgframework.jfx.AFilterInjector;
import de.rpgframework.jfx.FilteredListPage;
import de.rpgframework.shadowrun.ASpell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;

/**
 * @author prelle
 *
 */
public class FilterSpells extends AFilterInjector<ASpell> {

	private ChoiceBox<ASpell.Category> cbType;

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.jfx.AFilterInjector#addFilter(de.rpgframework.jfx.FilteredListPage, javafx.scene.layout.FlowPane)
	 */
	@Override
	public void addFilter(FilteredListPage<ASpell> page, FlowPane filterPane) {
		/*
		 * Spell Category
		 */
		cbType = new ChoiceBox<ASpell.Category>();
		cbType.getItems().add(null);
		cbType.getItems().addAll(ASpell.Category.values());
		Collections.sort(cbType.getItems(), new Comparator<ASpell.Category>() {
			public int compare(ASpell.Category o1, ASpell.Category o2) {
				if (o1==null) return -1;
				if (o2==null) return  1;
				return Collator.getInstance().compare(o1.getName(), o2.getName());
			}
		});
		cbType.getSelectionModel().selectedItemProperty().addListener( (ov,o,n) -> page.refreshList());
		cbType.setConverter(new StringConverter<ASpell.Category>() {
			public String toString(ASpell.Category val) {
				if (val==null) return "alle Typen";
				return val.getName();
			}
			public ASpell.Category fromString(String string) { return null; }
		});
		filterPane.getChildren().add(cbType);

	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.jfx.AFilterInjector#applyFilter(de.rpgframework.jfx.FilteredListPage, java.util.List)
	 */
	@Override
	public List<ASpell> applyFilter(FilteredListPage<ASpell> page, List<ASpell> input) {
		// Match spell category
		if (cbType.getValue()!=null) {
			input = input.stream()
					.filter(spell -> spell.getCategory()==cbType.getValue())
					.collect(Collectors.toList());
		}
		return input;
	}

}
