package de.rpgframework.shadowrun6.comlink;

import java.lang.System.Logger;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author prelle
 *
 */
public class ConsoleLogger implements Logger {

	//-------------------------------------------------------------------
	/**
	 */
	public ConsoleLogger() {
		// TODO Auto-generated constructor stub
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#getName()
	 */
	@Override
	public String getName() {
		return "ConsoleLogger";
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#isLoggable(java.lang.System.Logger.Level)
	 */
	@Override
	public boolean isLoggable(Level level) {
		return level.getSeverity()>=Level.WARNING.getSeverity();
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#log(java.lang.System.Logger.Level, java.util.ResourceBundle, java.lang.String, java.lang.Throwable)
	 */
	@Override
	public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
		if (!isLoggable(level)) return;
		System.out.printf("ConsoleLogger [%s]: %s - %s%n", level, msg, thrown);
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.Logger#log(java.lang.System.Logger.Level, java.util.ResourceBundle, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void log(Level level, ResourceBundle bundle, String format, Object... params) {
		if (!isLoggable(level)) return;
		try {
			System.out.printf("ConsoleLogger [%s]: %s%n", level, 
			MessageFormat.format(format, params));
		} catch (Exception e) {
		}
	}

}
