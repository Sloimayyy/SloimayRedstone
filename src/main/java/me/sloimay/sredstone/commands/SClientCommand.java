package me.sloimay.sredstone.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

/**
 * Sloimay Client Command class, which has a "register" method that registers the command
 * to the client.
 */
public abstract class SClientCommand
{
    /**
     * Called when we want to register the command to the client.
     */
    public abstract void register(CommandDispatcher<FabricClientCommandSource> dispatcher);
}
