package de.rpgframework.shadowrun6.comlink;

import java.util.Locale;
import java.util.Map.Entry;

public class ComLinkStarter {

	public static void main(String[] args) {
    	System.setProperty("prism.forceGPU", "true");
    	System.setProperty("prism.verbose", "true");
		Locale.setDefault(Locale.ENGLISH);
		ComLinkMain.main(args);
	}

}
