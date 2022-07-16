package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * /updateredstone Command
 */
public class UpdateRedstoneCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public UpdateRedstoneCommand()
    {

    }

    // ###



    // ### Public methods

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {

        // The main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("updateredstone")
                        .executes(context ->
                        {
                            // dispatch 2 commands to update redstone
                            // And then do the undoing yourself, because the mod can't currently know how many
                            // of the 2 commands have been successful, this can't know how many times to undo
                            ClientDB.mcClient.player.sendChatMessage("//replace <comparator,repeater,redstone_wire stone");
                            ClientDB.mcClient.player.sendChatMessage("//replace barrel stone");

                            return 0;
                        })
        );

        // Aliases
        dispatcher.register(literal("ur").redirect(mainNode));
    }

    // ###
}
