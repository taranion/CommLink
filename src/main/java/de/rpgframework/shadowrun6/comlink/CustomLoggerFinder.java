package de.rpgframework.shadowrun6.comlink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.System.LoggerFinder;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author prelle
 *
 */
public class CustomLoggerFinder extends LoggerFinder {

	private static Map<String, Logger> loggerByName = new HashMap<>();
	private static Map<String, Level> levelByPrefix = new HashMap<>();

	//-------------------------------------------------------------------
	static void initialize() {
		InputStream ins = ClassLoader.getSystemResourceAsStream("loglevel.properties");
		if (ins!=null) {
			try {
				BufferedReader read = new BufferedReader(new InputStreamReader(ins));
				while (true) {
					String line = read.readLine();
					if (line==null) break;
					if (line.startsWith("#")) continue;
					int pos = line.indexOf("=");
					if (pos<0) continue;
					String key = line.substring(0, pos).trim();
					Level level = Level.valueOf(line.substring(pos+1, line.length()).trim());
					levelByPrefix.put(key, level);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//-------------------------------------------------------------------
	private static Level getLevelFor(String name) {
		if (levelByPrefix.isEmpty()) initialize();
		List<String> keys = levelByPrefix.keySet().stream().filter(key -> name.startsWith(key)).collect(Collectors.toList());
		Collections.sort(keys, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return -Integer.compare(o1.length(), o2.length());
			}
		});
		if (keys.isEmpty())
			return Level.INFO;
		return levelByPrefix.get(keys.get(0));
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.LoggerFinder#getLogger(java.lang.String, java.lang.Module)
	 */
	@Override
	public Logger getLogger(String name, Module module) {
		if (loggerByName.containsKey(name))
			return loggerByName.get(name);
		ConsoleLogger ret = null;

		ret = new ConsoleLogger(name, getLevelFor(name));
		loggerByName.put(name, ret);

		return ret;
	}

}
