package de.rpgframework.shadowrun6.comlink;

import de.rpgframework.eden.client.jfx.steps.StartupStep;
import de.rpgframework.shadowrun6.data.Shadowrun6DataPlugin;

/**
 * @author prelle
 *
 */
public class LoadSR6DataStep implements StartupStep {

	//-------------------------------------------------------------------
	public LoadSR6DataStep() {
	}

	//-------------------------------------------------------------------
	/**
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Shadowrun6DataPlugin plugin = new Shadowrun6DataPlugin();
		plugin.init( );
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.eden.client.jfx.steps.StartupStep#canRun()
	 */
	@Override
	public boolean canRun() {
		return true;
	}

}
