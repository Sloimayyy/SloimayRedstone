package me.sloimay.sredstone;

import me.sloimay.sredstone.scheduler.SClientScheduler;
import net.fabricmc.api.ModInitializer;


/**
 * TODO:
 *
 * 	- Find a fix for adding aliases to single literal commands like /autodustplacing -> /adp
 * 		/adp shows up, but cannot be executed.
 */

public class SRedstone implements ModInitializer
{
	// ### Fields

	// ## Statics
	/**
	 * This mod's client scheduler. The assignment acts as a part of initialization just so that
	 * we can keep the final modifier. As we can't modify this variable from {@code SClientSchedulerInit}.
	 */
	public static final SClientScheduler clientScheduler = new SClientScheduler();

	// ###



	// ### Init
	@Override
	public void onInitialize()
	{
	}
	// ###
}

