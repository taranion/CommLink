package de.rpgframework.shadowrun6.comlink;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.logging.LogManager;
import java.util.prefs.Preferences;

import com.gluonhq.attach.settings.SettingsService;

public class ComLinkStarter {

	public static void main(String[] args) {
		LogManager.getLogManager().reset();
		Locale.setDefault(getPreferredLangauge());
//    	Locale.setDefault(Locale.ENGLISH);
		ComLinkMain.main(args);
	}

	//-------------------------------------------------------------------
	private static Locale getPreferredLangauge() {
		String key = "commlink6.lang";
		// Tell generic packagaes which key to use
		System.setProperty("eden.langkey", key);
		if (SettingsService.create().isPresent()) {
			String val = SettingsService.create().get().retrieve(key);
			if (val!=null) {
				System.err.println("Language settings is "+val);
				return Locale.forLanguageTag(val);
			}
			System.err.println("No language settings");
			return Locale.getDefault();
		} else {
			Preferences pref = Preferences.userNodeForPackage(ComLinkMain.class);
			System.err.println("Language settings is "+pref.get(key,null));
			return Locale.forLanguageTag(pref.get(key, Locale.getDefault().getLanguage()));
		}
	}

}
