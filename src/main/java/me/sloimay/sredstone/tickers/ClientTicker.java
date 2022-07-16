package me.sloimay.sredstone.tickers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

import java.util.Objects;

/**
 * A class with only a run() function where the client
 * is passed through to tick at every start of
 */
public abstract class ClientTicker
{
    // Fields
    private ExecutionTiming executionTiming;

    // Init
    protected ClientTicker(ExecutionTiming executionTiming) {
        Objects.requireNonNull(executionTiming);
        this.executionTiming = executionTiming;
    }

    // Abstract
    public abstract void run(MinecraftClient client);

    // Public
    public void register() {
        if (this.executionTiming == ExecutionTiming.START_CLIENT_TICK)
            ClientTickEvents.START_CLIENT_TICK.register(client -> this.run(client));
        else if (this.executionTiming == ExecutionTiming.END_CLIENT_TICK)
            ClientTickEvents.END_CLIENT_TICK.register(client ->   this.run(client));
    }

    // Classes
    public enum ExecutionTiming { START_CLIENT_TICK, END_CLIENT_TICK }
}
