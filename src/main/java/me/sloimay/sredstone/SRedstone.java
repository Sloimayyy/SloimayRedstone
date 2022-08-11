package me.sloimay.sredstone;

import me.sloimay.sredstone.scheduler.SClientScheduler;
import net.fabricmc.api.ModInitializer;


/**
 * TODO:
 *
 * 	- Find a fix for adding aliases to single literal commands like /autodustplacing -> /adp
 * 		/adp shows up, but cannot be executed.
 * 	- The redstone network mapping system doesn't work in some cases (in my SIN/COS rom there are comparators
 * 		with tons of different timings that outputs into a redstone dust with only one timing its kinda weird).
 * 		Some of these bugs will probably be fixed in the iterative BFS search implementation, however some of
 * 		these bugs probably comes from errors in child node generation in code.
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

