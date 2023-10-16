package de.rpgframework.shadowrun6.commlinkbinary;

import java.util.List;
import java.util.Locale;
import java.util.logging.LogManager;

import de.rpgframework.eden.client.jfx.EdenSettings;
import de.rpgframework.genericrpg.LicenseManager;
import de.rpgframework.shadowrun6.comlink.ComLinkMain;

public class ComLinkStarter {

	public static void main(String[] args) {
//    	LicenseManager.storeGlobalLicenses(List.of("SHADOWRUN6/CORE","SHADOWRUN6/COMPANION","SHADOWRUN6/FIRING_SQUAD","SHADOWRUN6/STREET_WYRD","SHADOWRUN6/DOUBLE_CLUTCH","SHADOWRUN6/HACK_SLASH"));
		LogManager.getLogManager().reset();
		EdenSettings.setupDirectories("CommLink6");
		Locale.setDefault(EdenSettings.getPreferredLangauge("CommLink6"));
		ComLinkMain.main(args);
	}

}
