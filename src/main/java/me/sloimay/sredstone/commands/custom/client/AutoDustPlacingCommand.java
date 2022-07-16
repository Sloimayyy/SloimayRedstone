package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * Places redstone dust automatically on blocks you place.
 */
public class AutoDustPlacingCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public AutoDustPlacingCommand()
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
                literal("autodustplacing")
                        .executes(context ->
                        {
                            // ## Invert bool
                            ClientDB.isRedstoneDustAutoPlacingActivated = !ClientDB.isRedstoneDustAutoPlacingActivated;

                            // ## No block looked at, stop the command
                            String feedbackMessage =
                                    "[SRedstone] >> Automatic redstone dust placing set to "
                                            + Boolean.toString(ClientDB.isRedstoneDustAutoPlacingActivated).toUpperCase() + ".";
                            ClientDB.mcClient.inGameHud.setOverlayMessage(new LiteralText(feedbackMessage), false);

                            return 0;
                        })
        );

        // Aliases
        dispatcher.register(literal("adp").redirect(mainNode));
    }

    // ###
}
