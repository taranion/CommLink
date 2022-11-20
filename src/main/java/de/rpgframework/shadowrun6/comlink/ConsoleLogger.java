package de.rpgframework.shadowrun6.comlink;

import java.lang.System.Logger;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author prelle
 *
 */
public class ConsoleLogger implements Logger {
	
	private String name;
	private Level minLevel;

	//-------------------------------------------------------------------
	public ConsoleLogger(String name, Level minLevel) {
		this.name = name;
		this.minLevel = minLevel;
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#isLoggable(java.lang.System.Logger.Level)
	 */
	@Override
	public boolean isLoggable(Level level) {
		return level.getSeverity()>=minLevel.getSeverity();
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#log(java.lang.System.Logger.Level, java.util.ResourceBundle, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
		if (!isLoggable(level)) return;
		System.out.printf("CL [%s]: %s - %s%n", level, msg, thrown);
		if (ComLinkMain.out!=null && !ComLinkMain.out.checkError()) {
			ComLinkMain.out.printf("[%s]: %s - %s%n", level, msg, thrown);
		}
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#log(java.lang.System.Logger.Level, java.util.ResourceBundle, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void log(Level level, ResourceBundle bundle, String format, Object... params) {
		if (!isLoggable(level)) return;
		String prefix = "";
		try {
			throw new RuntimeException("trace");
		} catch (Exception e) {
			StackTraceElement element = e.getStackTrace()[2];
			if (element.getClassName().equals("de.rpgframework.MultiLanguageResourceBundle"))
				element = e.getStackTrace()[5];
			prefix="("+element.getClassName().substring(element.getClassName().lastIndexOf(".")+1)+".java:"+element.getLineNumber()+") : ";
		}
		try {
			System.out.printf("[%7s][%10s]: %s%n", level, name, prefix+MessageFormat.format(format, params));
			if (ComLinkMain.out!=null && !ComLinkMain.out.checkError()) {
				ComLinkMain.out.printf("[%7s][%10s]: %s%n", level, name, prefix+MessageFormat.format(format, params));
				
			}
		} catch (Exception e) {
		}
	}

}
