package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.WorldEditCommands;
import com.sk89q.worldedit.command.WorldEditCommandsRegistration;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.MathHelper;

import java.util.Set;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;
import static net.minecraft.util.math.MathHelper.floor;

/**
 * /minsel which minimizes the selection of the executing player.
 * Minimizing a selection means to find the smallest bounding box for
 * the blocks selected in the current selection and set the new selection as that.
 *
 * This command is computation-heavy for big selections, so the work-load is sent to a
 * different thread, meaning that if it takes 10 seconds to compute the new bounds, it may
 * overwrite the selection while we're trying to make a new one in-game.
 */
public class MinimizeWESelectionCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public MinimizeWESelectionCommand()
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
                literal("minsel")
                        .executes(context ->
                        {
                            ServerPlayerEntity serverPlayer =
                                    ClientDB.mcClient.getServer().getPlayerManager().getPlayer(
                                            ClientDB.mcClient.player.getUuid()
                                    );
                            minimizeSelection(FabricAdapter.adaptPlayer(serverPlayer));

                            return 0;
                        })
        );
    }

    // ###



    // ### Private methods

    /**
     * Minimizes the selection of the inputed player
     *
     * @param player
     */
    private void minimizeSelection(AbstractPlayerActor player)
    {
        // Setup
        WorldEdit we = WorldEdit.getInstance();
        LocalSession clientSession = we.getSessionManager().get(player);

        // Get the region
        Region pooledRegion = null;
        try { pooledRegion = clientSession.getSelection(); }
        catch(IncompleteRegionException e) { return; }
        final Region selectedRegion = pooledRegion;

        // The thread the computation is gonna get sent over to
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

                // ## Iterate over the region to get the min and max XYZ that aren't air
                // ## This finds the min and max XYZ that encloses every block of the selection
                World selectionWorld = selectedRegion.getWorld();
                boolean selectionHasNonAirBlocks = false;
                // # Setup the mins and maxes
                int minX, minY, minZ;
                minX = minY = minZ = Integer.MAX_VALUE;
                int maxX, maxY, maxZ;
                maxX = maxY = maxZ = Integer.MIN_VALUE;
                // # Iterate
                for (BlockVector3 b : selectedRegion)
                {
                    // Test if not air, if so update min and max XYZ if needed
                    BlockType blockTypeHere = selectionWorld.getFullBlock(b).getBlockType();
                    if (    blockTypeHere == BlockTypes.AIR ||
                            blockTypeHere == BlockTypes.VOID_AIR ||
                            blockTypeHere == BlockTypes.CAVE_AIR) continue;

                    // This selection has non-air blocks
                    selectionHasNonAirBlocks = true;

                    // The block isn't air, update XYZ
                    if (b.getX() < minX) minX = b.getX();
                    else if (b.getX() > maxX) maxX = b.getX();

                    if (b.getY() < minY) minY = b.getY();
                    else if (b.getY() > maxY) maxY = b.getY();

                    if (b.getZ() < minZ) minZ = b.getZ();
                    else if (b.getZ() > maxZ) maxZ = b.getZ();
                }

                // ## Now that we have found the min and max XYZ, replace our region by a new one
                // ## bounded by our found values.
                // ## Only update the selection if the selection has non-air blocks, otherwise it doesn't make
                // ## sense to minimize over nothing.
                BlockVector3 minVec = BlockVector3.at(minX, minY, minZ);
                BlockVector3 maxVec = BlockVector3.at(maxX, maxY, maxZ);
                if (selectionHasNonAirBlocks)
                {
                    clientSession.setRegionSelector(
                            selectedRegion.getWorld(),
                            new CuboidRegionSelector(selectionWorld, minVec, maxVec)
                    );

                    // Log message
                    String feedbackEnd = "[SRedstone] >> Finished analyzing region. Region set.";
                    ClientDB.mcClient.inGameHud.addChatMessage(
                            MessageType.SYSTEM,
                            new LiteralText(feedbackEnd),
                            ClientDB.mcClient.player.getUuid()
                    );
                }
            }
        };
        thread.start();
    }

    // ###
}
