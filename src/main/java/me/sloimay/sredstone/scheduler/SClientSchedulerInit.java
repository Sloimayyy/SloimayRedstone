package me.sloimay.sredstone.scheduler;

import me.sloimay.sredstone.SRedstone;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

/**
 * Inits this mod's client scheduler.
 */
public class SClientSchedulerInit
{
    // ### Fields

    // ###



    // ### Init

    public SClientSchedulerInit()
    {

    }

    // ###



    // ### Public methods

    /**
     * Initializes this mod's scheduler
     */
    public void initScheduler()
    {
        // ## Tick the scheduler every tick
        ClientTickEvents.START_CLIENT_TICK.register(client -> SRedstone.clientScheduler.update(client));
    }

    // ###
}
