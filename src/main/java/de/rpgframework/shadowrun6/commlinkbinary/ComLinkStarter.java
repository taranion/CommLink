package de.rpgframework.shadowrun6.commlinkbinary;

import java.util.Locale;
import java.util.logging.LogManager;

import de.rpgframework.eden.client.jfx.EdenSettings;
import de.rpgframework.shadowrun6.comlink.ComLinkMain;

public class ComLinkStarter {

	public static void main(String[] args) {
		LogManager.getLogManager().reset();
		EdenSettings.setupDirectories("CommLink6");
		Locale.setDefault(EdenSettings.getPreferredLangauge("CommLink6"));
		ComLinkMain.main(args);
	}

}
