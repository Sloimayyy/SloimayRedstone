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
import static me.sloimay.sredstone.utils.SRedstoneHelpers.RedstoneNetworkHelper.NodeHelper.findComparatorsConnectedToBlock;

public class ComparatorNode extends Node
{
    // ### Fields

    // ###



    // ### Init

    public ComparatorNode(World world, BlockPos position, BlockState blockState)
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
        // # Setting up the properties
        // Comparator output facing and not plain facing, because the facing of a comparator is where
        // its getting its input from which is really counter intuitive.
        Direction comparatorOutputFacing = this.blockState.get(Properties.HORIZONTAL_FACING).getOpposite();


        // ## Checking for nodes and creating them
        // # Setup
        SFabricLib.BlockUtils.PositionedBlockState offsetEqual =
                SFabricLib.BlockUtils.PositionedBlockState.of(world, this.position.offset(comparatorOutputFacing));
        SFabricLib.BlockUtils.PositionedBlockState offsetUp =
                SFabricLib.BlockUtils.PositionedBlockState.of(world, offsetEqual.getBlockPos().offset(Direction.UP));


        // # REDSTONE WIRES
        if (isBlock(offsetEqual, Blocks.REDSTONE_WIRE))
            receivingNodes.add(Node.create(world, offsetEqual.getBlockPos()));

        if (isSolidBlock(world, offsetEqual))
            receivingNodes.addAll( findRedstoneWireNodesAroundBlock(world, offsetEqual.getBlockPos()) );


        // # REPEATERS
        if (isBlock(offsetEqual, Blocks.REPEATER))
            if (getProperty(offsetEqual, Properties.HORIZONTAL_FACING) == comparatorOutputFacing.getOpposite())
                receivingNodes.add(Node.create(world, offsetEqual.getBlockPos()));

        if (isSolidBlock(world, offsetEqual))
            receivingNodes.addAll( findRepeatersConnectedToBlock(world, offsetEqual.getBlockPos()) );


        // # COMPARATORS
        if (isBlock(offsetEqual, Blocks.COMPARATOR))
            // Checking for comparators not facing in our comparator node, as they can take
            // a comparator input from all 3 sides except this one.
            if (getProperty(offsetEqual, Properties.HORIZONTAL_FACING) != comparatorOutputFacing)
                receivingNodes.add(Node.create(world, offsetEqual.getBlockPos()));

        if (isSolidBlock(world, offsetEqual) && !isBlockEntity(offsetEqual))
            // Block entities have priority on a comparator, so the repeater doesn't actually
            // go through the comparator if its reading from a block entity.
            receivingNodes.addAll( findComparatorsConnectedToBlock(world, offsetEqual.getBlockPos()) );


        // # REDSTONE WALL TORCHES
        if (isSolidBlock(world, offsetEqual))
            receivingNodes.addAll( findRedstoneWallTorchesConnectedToBlock(world, offsetEqual.getBlockPos()) );


        // # REDSTONE TORCHES
        if (isSolidBlock(world, offsetEqual))
            if (isBlock(offsetUp, Blocks.REDSTONE_TORCH))
                receivingNodes.add( Node.create(world, offsetUp.getBlockPos()) );



        // ## At the end, add all the child nodes to this node
        this.addChildren(receivingNodes);
    }
    // ###
}
