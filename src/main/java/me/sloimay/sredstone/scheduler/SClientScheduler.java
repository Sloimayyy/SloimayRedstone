package me.sloimay.sredstone.scheduler;

import me.sloimay.sredstone.db.ClientDB;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A client-side task scheduler.
 */
public class SClientScheduler
{
    // ### Fields

    /**
     * The tasks to schedule.
     */
    HashMap<Long, List<STask>> scheduledTasks;

    /**
     * We need to update the scheduler externally every tick, while doing that we increase
     * the client tick time as update() is called every tick. However it is not "really" the
     * client tick time, it's more like a tick tracker that at least goes as fast as the client tick
     * which is all we need for scheduling.
     */
    private long clientTickTime = 0;

    // ###



    // ### Init

    public SClientScheduler()
    {
        this.scheduledTasks = new HashMap<Long, List<STask>>();
    }

    // ###



    // ### Public methods

    /**
     * Updates this scheduler. Do not call except from {@code SClientSchedulerInit}.
     * This is where tasks are going to be called.
     *
     * @param client
     */
    public void update(MinecraftClient client)
    {
        // ## See if we have a task at this tick, and if we do execute them, then remove them.
        if (this.scheduledTasks.containsKey(this.clientTickTime))
        {
            // Execute
            this.scheduledTasks.get(this.clientTickTime).forEach(sTask -> sTask.run(client));

            // Remove
            scheduledTasks.remove(this.clientTickTime);
        }



        // ## Increment client tick
        this.clientTickTime++;
    }

    /**
     * Adds a task to this scheduler to execute in [delay] amount of client ticks.
     * A delay of 0 ticks will still result in the action happening in 1 tick.
     *
     * @param task
     * @param delay
     */
    public void addTask(STask task, long delay)
    {
        // ## Get at which tick to execute the task
        long taskTimestamp = this.clientTickTime + delay;

        // ## If we don't have a task list at the timestamp, add a list. Basically
        // ## init a list if needed
        this.scheduledTasks.putIfAbsent(taskTimestamp, new ArrayList<STask>());

        // ## Add the task to the list
        this.scheduledTasks.get(taskTimestamp).add(task);
    }

    // ###
}
