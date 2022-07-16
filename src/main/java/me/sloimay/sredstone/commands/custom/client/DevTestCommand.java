package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.SRedstone;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.scheduler.STask;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * Command for testing purposes.
 */
public class DevTestCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public DevTestCommand()
    {

    }

    // ###



    // ### Public methods

    /**
     * Registers the command.
     */
    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        // Main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("sredstonedevtest")
                        .executes(context ->
                        {
                            long startTime = System.currentTimeMillis();

                            SRedstone.clientScheduler.addTask(new STask() {
                                @Override
                                public void run(MinecraftClient client) {
                                    System.out.println("Beep" + (System.currentTimeMillis() - startTime));
                                }
                            }, 40);


                            return 0;
                        })
        );
    }

    // ###
}
