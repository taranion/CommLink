package de.rpgframework.shadowrun6.comlink;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.System.LoggerFinder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author prelle
 *
 */
public class CustomLoggerFinder extends LoggerFinder {

	private static Map<String, Logger> loggerByName = new HashMap<>();
	
	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.LoggerFinder#getLogger(java.lang.String, java.lang.Module)
	 */
	@Override
	public Logger getLogger(String name, Module module) {
		if (loggerByName.containsKey(name))
			return loggerByName.get(name);
		ConsoleLogger ret = null;
		if (name.startsWith("prelle")) {
			ret = new ConsoleLogger(name, Level.WARNING);
		} else if (name.startsWith("javafx")) {
			ret = new ConsoleLogger(name, Level.ERROR);
		} else if (name.startsWith("de.rpgframework.genericrpg.items") || name.startsWith("de.rpgframework.shadowrun6.items")) {
			ret = new ConsoleLogger(name, Level.WARNING);
		} else if (name.contains(".proc")) {
			ret = new ConsoleLogger(name, Level.WARNING);
//		} else if (name.equals("de.rpgframework.shadowrun6.chargen.gen.priority.attrib")) {
//			ret = new ConsoleLogger(name, Level.TRACE);
		} else {
//			System.err.println("getLogger: "+name);
			ret = new ConsoleLogger(name, Level.INFO);
		}
		loggerByName.put(name, ret);
		
		return ret;
	}

}
