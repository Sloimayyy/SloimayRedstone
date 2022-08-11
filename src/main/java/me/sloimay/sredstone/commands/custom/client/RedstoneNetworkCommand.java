package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.features.redstonenetwork.RedstoneNetwork;
import me.sloimay.sredstone.features.redstonenetwork.nodes.Node;
import me.sloimay.sredstone.utils.SFabricLib;
import me.sloimay.sredstone.utils.SRedstoneHelpers;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.block.Blocks;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * /redstonenetwork map maxSpan:int maxTraversalCount:int : Creates a redstone netwrok from the node you're aiming at
 * TODO:/redstonenetwork map maxSpan:int maxTraversalCount:int block_position: Creates a redstone network from the node at the block position you inputted
 *
 * /redstonenetwork pool timings: Pools the redstone network for the timings of the node you're aiming at.
 * TODO:/redstonenetwork pool block_position: Pools the redstone network for the node you inputted the block position of
 *
 * /redstonenetwork pool stats: Gives you various stats about the network
 *
 * /redstonenetwork settings constantlyPoolTimings
 * /redstonenetwork settings poolTimingsAsRedstoneTicks
 *
 * TODO:
 *      /redstonenetwork help: Gives indications on different command arguments like what is maxSpan and maxTraversalCount.
 *      Find out a way to use BlockPos arguments in client commands (BlockPosArgumentType.getBlockPos()) needs a
 *      {@code CommandContext<ServerCommandSource>} and not a {@code CommandContext<FabricClientCommandSource}
 *      /redstonenetwork clear: Clears the redstone network currently held in memory
 *
 */
public class RedstoneNetworkCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public RedstoneNetworkCommand()
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
                literal("redstonenetwork")
                        .then(literal("map")
                                .then(argument("maxSpan", IntegerArgumentType.integer())
                                        .then(argument("maxTraversalCount", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    int maxSpan = IntegerArgumentType.getInteger(context, "maxSpan");
                                                    int maxTraversalCount = IntegerArgumentType.getInteger(context, "maxTraversalCount");
                                                    this.map(maxSpan, maxTraversalCount);

                                                    return 0;
                                                })
                                        )
                                )
                        )
                        .then(literal("pool")
                                .then(literal("timings")
                                        .executes(context -> {
                                            this.poolTimings();
                                            return 0;
                                        })
                                )
                                .then(literal("stats")
                                        .executes(context -> {
                                            this.poolStats();
                                            return 0;
                                        })
                                )
                        )
                        .then(literal("settings")
                                .then(literal("constantlyPoolTimings")
                                        .executes(context -> {
                                            this.settingsConstantlyPoolTimings();
                                            return 0;
                                        })
                                )
                                .then(literal("poolTimingsAsRedstoneTicks")
                                        .executes(context -> {
                                            this.settingsPoolTimingsAsRedstoneTicks();
                                            return 0;
                                        })
                                )
                        )
        );

        // Aliases
        dispatcher.register(literal("rn").redirect(mainNode));
    }

    // ###



    // ### Private methods

    /**
     * Creates a new client side {@code RedstoneNetwork} object,
     * and maps it.
     *
     * @param rootNodeBlockPos
     * @param maxSpan
     * @param maxTraversalCount
     */
    private void createClientRedstoneNetwork(BlockPos rootNodeBlockPos, int maxSpan, int maxTraversalCount)
    {
        ClientDB.redstoneNetwork = new RedstoneNetwork(ClientDB.mcClient.player.world, rootNodeBlockPos);
        ClientDB.redstoneNetwork.map(maxSpan, maxTraversalCount);

        // Message in chat
        ClientDB.mcClient.inGameHud.addChatMessage(
                MessageType.SYSTEM,
                new LiteralText("[SRedstone] >> Redstone network successfully mapped."),
                ClientDB.mcClient.player.getUuid()
        );
    }

    // ###

    // ### Commands stuff!!

    private void map(int maxSpan, int maxTraversalCount)
    {
        /**
         * /redstonenetwork map maxSpan:int maxTraversalCount:int :
         *      Creates a redstone netwrok from the node you're aiming at
         */

        // ## Get block looked at
        SFabricLib.BlockUtils.PositionedBlock blockLookedAt =
                SFabricLib.PlayerUtils.getBlockLookingAtClient(
                        ClientDB.mcClient.player, 5.0, 0, false
                );
        if (blockLookedAt == null)
        {
            // No block looked at, stop the command
            ClientDB.mcClient.inGameHud.addChatMessage(
                    MessageType.SYSTEM,
                    new LiteralText("[SRedstone] >> Can't map redstone network, no block is looked at."),
                    ClientDB.mcClient.player.getUuid()
            );

            return;
        }


        // ## Check if the block looked at is a valid node to start with
        Node possibleRootNode = Node.create(ClientDB.mcClient.player.world, blockLookedAt.getBlockPos());
        if (possibleRootNode == null)
        {
            // Invalid first node
            ClientDB.mcClient.inGameHud.addChatMessage(
                    MessageType.SYSTEM,
                    new LiteralText("[SRedstone] >> Invalid start node for a redstone network."),
                    ClientDB.mcClient.player.getUuid()
            );

            return;
        }


        // ## Create the redstone network
        this.createClientRedstoneNetwork(
                blockLookedAt.getBlockPos(),
                maxSpan,
                maxTraversalCount
        );
    }

    private void poolTimings()
    {
        /**
         * /redstonenetwork poolTimings: Pools the redstone network for the timings
         *                               of the node you're aiming at.
         */

        // ## Check if we have a redstone network mapped
        if (ClientDB.redstoneNetwork == null)
        {
            // No redstone network mapped, so we stop the command.
            ClientDB.mcClient.inGameHud.addChatMessage(
                    MessageType.SYSTEM,
                    new LiteralText("[SRedstone] >> Please map a redstone network first."),
                    ClientDB.mcClient.player.getUuid()
            );

            return;
        }


        // ## Get block looked at
        SFabricLib.BlockUtils.PositionedBlock blockLookedAt =
                SFabricLib.PlayerUtils.getBlockLookingAtClient(
                        ClientDB.mcClient.player, 5.0, 0, false
                );
        if (blockLookedAt == null)
        {
            // No block looked at, stop the command
            ClientDB.mcClient.inGameHud.addChatMessage(
                    MessageType.SYSTEM,
                    new LiteralText("[SRedstone] >> Can't pool redstone network, no block is looked at."),
                    ClientDB.mcClient.player.getUuid()
            );

            return;
        }


        // ## Get the timings of the nodes looked at
        Set<Integer> nodeTimings = ClientDB.redstoneNetwork.getNodeTimings(blockLookedAt.getBlockPos());
        if (nodeTimings == null)
        {
            // The node doesn't have any timing registered, it may be too far from the
            // root node, not connected to the network at all, or an invalid node.
            ClientDB.mcClient.inGameHud.addChatMessage(
                    MessageType.SYSTEM,
                    new LiteralText(
                            "[SRedstone] >> This node has no timing registered. " +
                                    "It may be too far from the root node, " +
                                    "not connected to it, " +
                                    "or is not a valid node at all."
                    ),
                    ClientDB.mcClient.player.getUuid()
            );

            return;
        }


        // ## Display the node timings
        String nodeTimingsString = SRedstoneHelpers.RedstoneNetworkHelper.timingsSetToString(nodeTimings);
        ClientDB.mcClient.inGameHud.setOverlayMessage(new LiteralText(nodeTimingsString), false);
    }

    private void poolStats()
    {

    }

    private void settingsConstantlyPoolTimings()
    {
        /**
         * /redstonenetwork settings constantlyPoolTimings
         */

        ClientDB.constantlyPoolRedstoneNetwork = !ClientDB.constantlyPoolRedstoneNetwork;

        String feedbackMessage =
                "[SRedstone] >> Set the constantlyPoolTimings setting to "
                        + Boolean.toString(ClientDB.constantlyPoolRedstoneNetwork).toUpperCase() + ".";
        ClientDB.mcClient.inGameHud.setOverlayMessage(new LiteralText(feedbackMessage), false);
    }

    private void settingsPoolTimingsAsRedstoneTicks()
    {
        /**
         * /redstonenetwork settings poolTimingsAsRedstoneTicks
         */

        ClientDB.showTimingsAsRedstoneTicks = !ClientDB.showTimingsAsRedstoneTicks;

        String feedbackMessage =
                "[SRedstone] >> Set the poolTimingsAsRedstoneTicks setting to "
                        + Boolean.toString(ClientDB.showTimingsAsRedstoneTicks).toUpperCase() + ".";
        ClientDB.mcClient.inGameHud.setOverlayMessage(new LiteralText(feedbackMessage), false);
    }



    // ###
}
