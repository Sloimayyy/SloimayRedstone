package me.sloimay.sredstone.features.redstonenetwork.nodes;

import me.sloimay.sredstone.utils.SFabricLib;
import me.sloimay.sredstone.utils.SRedstoneHelpers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

import static me.sloimay.sredstone.utils.SRedstoneHelpers.RedstoneNetworkHelper.NodeHelper.*;

public class RepeaterNode extends Node
{
    // ### Fields

    // ###



    // ### Init

    public RepeaterNode(World world, BlockPos position, BlockState blockState)
    {
        super(world, position, blockState);

        // Get the latency of this node.
        this.latency = ((Integer) blockState.getEntries().get(Properties.DELAY)) * 2;
    }

    // ###



    // ### Public methods

    public void populateChildren()
    {
// ## Setup
        List<Node> receivingNodes = new ArrayList<Node>();
        // # Setting up the properties
        // Repeater output facing and not plain facing, because the facing of a repeater is where
        // its getting its input from which is really counter intuitive.
        Direction repeaterOutputFacing = this.blockState.get(Properties.HORIZONTAL_FACING).getOpposite();


        // ## Checking for nodes and creating them
        // # Setup
        SFabricLib.BlockUtils.PositionedBlockState offsetEqual =
                SFabricLib.BlockUtils.PositionedBlockState.of(world, this.position.offset(repeaterOutputFacing));


        // # REDSTONE WIRES
        if (isBlock(offsetEqual, Blocks.REDSTONE_WIRE))
            receivingNodes.add(Node.create(world, offsetEqual.getBlockPos()));

        if (isSolidBlock(world, offsetEqual))
            receivingNodes.addAll( findRedstoneWireNodesAroundBlock(world, offsetEqual.getBlockPos()) );


        // # REPEATERS
        if (isBlock(offsetEqual, Blocks.REPEATER))
            if (getProperty(offsetEqual, Properties.HORIZONTAL_FACING) == repeaterOutputFacing.getOpposite())
                receivingNodes.add(Node.create(world, offsetEqual.getBlockPos()));

        if (isSolidBlock(world, offsetEqual))
            receivingNodes.addAll( findRepeatersConnectedToBlock(world, offsetEqual.getBlockPos()) );


        // # COMPARATORS
        if (isBlock(offsetEqual, Blocks.COMPARATOR))
            if (getProperty(offsetEqual, Properties.HORIZONTAL_FACING) == repeaterOutputFacing.getOpposite())
                receivingNodes.add(Node.create(world, offsetEqual.getBlockPos()));

        if (isSolidBlock(world, offsetEqual) && !isBlockEntity(offsetEqual))
            // Block entities have priority on a comparator, so the repeater doesn't actually
            // go through the comparator if its reading from a block entity.
            receivingNodes.addAll( findComparatorsConnectedToBlock(world, offsetEqual.getBlockPos()) );



        // ## At the end, add all the child nodes to this node
        this.addChildren(receivingNodes);
    }
    // ###
}
