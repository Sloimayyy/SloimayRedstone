package me.sloimay.sredstone.features.redstonenetwork.nodes;

import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import static me.sloimay.sredstone.utils.SRedstoneHelpers.RedstoneNetworkHelper.NodeHelper.*;

public class RedstoneTorchNode extends Node
{
    // ### Fields

    // ###



    // ### Init

    public RedstoneTorchNode(World world, BlockPos position, BlockState blockState)
    {
        super(world, position, blockState);
        this.latency = 2;
    }

    // ###



    // ### Public methods

    public void populateChildren()
    {
        // ## Setup
        List<Node> receivingNodes = new ArrayList<Node>();


        // ## Checking for nodes and creating them
        // # Setup
        SFabricLib.BlockUtils.PositionedBlockState up =
                SFabricLib.BlockUtils.PositionedBlockState.of(world, this.position.offset(Direction.UP));
        SFabricLib.BlockUtils.PositionedBlockState upUp =
                SFabricLib.BlockUtils.PositionedBlockState.of(world, up.getBlockPos().offset(Direction.UP));


        // # REDSTONE WIRES
        receivingNodes.addAll( findRedstoneWireNodesAroundBlock(world, this.position) );

        if (isSolidBlock(world, up))
            receivingNodes.addAll( findRedstoneWireNodesAroundBlock(world, up.getBlockPos()) );


        // # REPEATERS
        receivingNodes.addAll( findRepeatersConnectedToBlock(world, this.position) );

        if (isSolidBlock(world, up))
            receivingNodes.addAll( findRepeatersConnectedToBlock(world, up.getBlockPos()) );


        // # COMPARATORS
        receivingNodes.addAll( findComparatorsConnectedToBlock(world, this.position) );

        if (isSolidBlock(world, up) && !isBlockEntity(up))
            receivingNodes.addAll( findComparatorsConnectedToBlock(world, up.getBlockPos()) );


        // # REDSTONE WALL TORCHES
        if (isSolidBlock(world, up))
            receivingNodes.addAll( findRedstoneWallTorchesConnectedToBlock(world, up.getBlockPos()) );


        // # REDSTONE TORCHES
        if (isSolidBlock(world, up))
            if (isBlock(upUp, Blocks.REDSTONE_TORCH))
                receivingNodes.add( Node.create(world, upUp.getBlockPos()) );



        // ## At the end, add all the child nodes to this node
        this.addChildren(receivingNodes);
    }
    // ###
}
