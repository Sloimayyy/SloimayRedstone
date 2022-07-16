package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * /copyflippaste command, copies the selection, flips it in the
 * direction you're looking and paste it.
 */
public class CopyFlipPasteCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public CopyFlipPasteCommand()
    {

    }

    // ###



    // ### Public methods

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        // Main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("copyflippaste")
                        .executes(context ->
                        {
                            ClientDB.mcClient.player.sendChatMessage("//copy");
                            ClientDB.mcClient.player.sendChatMessage("//flip");
                            ClientDB.mcClient.player.sendChatMessage("//paste");

                            return 0;
                        })
        );

        // Aliases
        dispatcher.register(literal("cfv").redirect(mainNode));
        dispatcher.register(literal("cfp").redirect(mainNode));
    }

    // ###
}
