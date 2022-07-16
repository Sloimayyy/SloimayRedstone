package me.sloimay.sredstone.tickers;

import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.features.redstonenetwork.nodes.Node;
import me.sloimay.sredstone.utils.SFabricLib;
import me.sloimay.sredstone.utils.SRedstoneHelpers;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

import java.util.Set;

/**
 * Pools the client's mapped redstone network for timings.
 */
public class RedstoneNetworkPoolTimingTicker extends ClientTicker
{
    // ### Fields

    // ###



    // ### Init

    public RedstoneNetworkPoolTimingTicker()
    {
        super(ExecutionTiming.START_CLIENT_TICK);
    }

    // ###



    // ### Public methods

    @Override
    public void run(MinecraftClient client)
    {
        // ## Setup
        if (!ClientDB.constantlyPoolRedstoneNetwork) return;
        if (ClientDB.redstoneNetwork == null) return;

        // ## Get the node timings
        // # Get the block looked at
        SFabricLib.PlayerUtils.PositionedBlock blockLookedAt =
                SFabricLib.PlayerUtils.getBlockLookingAtClient(
                        ClientDB.mcClient.player, 5.0, 0, false
                );
        // # Check if we're looking at a block
        if (blockLookedAt == null) return;
        // # Check if this position is node-able, if not we aren't aiming at any node
        Node pooledNode = Node.create(ClientDB.mcClient.player.world, blockLookedAt.getBlockPos());
        if (pooledNode == null) return;
        // # We get the timings of the node here, but the timings may be null if the node
        // # we're looking at isn't in the mapped network.
        Set<Integer> nodeTimings = ClientDB.redstoneNetwork.getNodeTimings(blockLookedAt.getBlockPos());
        if (nodeTimings == null) return;

        // ## Display the timings in the overlay message
        String nodeTimingsString = SRedstoneHelpers.RedstoneNetworkHelper.timingsSetToString(nodeTimings);
        ClientDB.mcClient.inGameHud.setOverlayMessage(new LiteralText(nodeTimingsString), false);


    }

    // ###
}
