package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.db.Db;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.antlr.v4.runtime.misc.OrderedHashSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class AvailableColorsCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public AvailableColorsCommand()
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
        // The main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("availablecolors")
                        .then(literal("wool")
                                .executes(context ->
                                { getRemainingBlockColorsInClientSelection("wool"); return 0; })
                        )
                        .then(literal("concrete")
                                .executes(context ->
                                { getRemainingBlockColorsInClientSelection("concrete"); return 0; })
                        )
                        .then(literal("stained_glass")
                                .executes(context ->
                                { getRemainingBlockColorsInClientSelection("stained_glass"); return 0; })
                        )
        );
    }

    // ###



    // ### Private methods

    private void getRemainingBlockColorsInClientSelection(String mode)
    {
        // Setup
        ServerPlayerEntity serverPlayer =
                ClientDB.mcClient.getServer().getPlayerManager().getPlayer(
                        ClientDB.mcClient.player.getUuid()
                );
        AbstractPlayerActor playerActor = FabricAdapter.adaptPlayer(serverPlayer);
        WorldEdit we = WorldEdit.getInstance();
        LocalSession clientSession = we.getSessionManager().get(playerActor);

        // Get the region
        Region pooledRegion = null;
        try { pooledRegion = clientSession.getSelection(); }
        catch(IncompleteRegionException e) { return; }
        final Region selectedRegion = pooledRegion;

        // Get which colored block hashmap we're gonna use
        HashMap<BlockType, String> coloredBlockToColor =
                mode.equals("wool") ? Db.woolBlockTypeToColor :
                mode.equals("concrete") ? Db.concreteBlockTypeToColor : Db.stainedGlassBlockTypeToColor;

        // The thread that will go through the entire selected region and get the colored blocks
        // and tell us which ones are still not used.
        Thread thread = new Thread(){
            @Override
            public void run()
            {
                // Log message
                String feedbackStart = "[SRedstone] >> Analyzing selected region..";
                ClientDB.mcClient.inGameHud.addChatMessage(
                        MessageType.SYSTEM,
                        new LiteralText(feedbackStart),
                        ClientDB.mcClient.player.getUuid()
                );

                // ## Iterate over every block of the selection, and get the remaining colors
                World selectionWorld = selectedRegion.getWorld();
                HashSet<String> availableColors = new HashSet<>();
                for (String col : Db.minecraftColors) availableColors.add(col);

                for (BlockVector3 b : selectedRegion)
                {
                    BlockType blockTypeHere = selectionWorld.getFullBlock(b).getBlockType();

                    // # Remove the current block's color from the available colors set
                    if (availableColors.size() > 0)
                        availableColors.remove(coloredBlockToColor.get(blockTypeHere));
                    else { break; }
                }

                // ## Log message
                String feedbackEnd = "[SRedstone] >> ";
                if (availableColors.size() > 0)
                {
                    feedbackEnd += "The remaining colors for block type \"" + mode +
                            "\" are: " + String.join(", ", availableColors) + ".";
                }
                else
                {
                    feedbackEnd += "No colors remaining for block type \"" + mode + "\"";
                }

                ClientDB.mcClient.inGameHud.addChatMessage(
                        MessageType.SYSTEM,
                        new LiteralText(feedbackEnd),
                        ClientDB.mcClient.player.getUuid()
                );
            }
        };
        thread.start();
    }

    // ###
}
