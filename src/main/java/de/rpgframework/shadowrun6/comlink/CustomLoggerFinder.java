package de.rpgframework.shadowrun6.comlink;

import java.lang.System.Logger;
import java.lang.System.LoggerFinder;

/**
 * @author prelle
 *
 */
public class CustomLoggerFinder extends LoggerFinder {

	//-------------------------------------------------------------------
	/**
	 */
	public CustomLoggerFinder() {
		// TODO Auto-generated constructor stub
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.System.LoggerFinder#getLogger(java.lang.String, java.lang.Module)
	 */
	@Override
	public Logger getLogger(String name, Module module) {
		// TODO Auto-generated method stub
		return new ConsoleLogger();
	}

}
