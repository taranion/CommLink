package de.rpgframework.shadowrun6.comlink;

import java.util.Locale;
import java.util.Map.Entry;

import com.gluonhq.attach.util.Constants;

public class ComLinkStarter {

	public static void main(String[] args) {
//    	System.setProperty("prism.forceGPU", "true");
//    	System.setProperty("prism.verbose", "true");
		System.setProperty(Constants.ATTACH_DEBUG, "true");
		Locale.setDefault(Locale.ENGLISH);
		
		ComLinkMain.main(args);
	}

}
