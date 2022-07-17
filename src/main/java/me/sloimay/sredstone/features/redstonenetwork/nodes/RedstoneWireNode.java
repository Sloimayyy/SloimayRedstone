package me.sloimay.sredstone.features.redstonenetwork.nodes;

import me.sloimay.sredstone.utils.SRedstoneHelpers;
import me.sloimay.sredstone.utils.SRedstoneHelpers.RedstoneNetworkHelper.NodeHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.WireConnection;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.sloimay.sredstone.utils.SFabricLib.BlockUtils.*;
import static me.sloimay.sredstone.utils.SRedstoneHelpers.RedstoneNetworkHelper.NodeHelper.*;

public class RedstoneWireNode extends Node
{
    // ### Fields

    // ###



    // ### Init

    public RedstoneWireNode(World world, BlockPos position, BlockState blockState)
    {
        super(world, position, blockState);

        this.latency = 0;
    }

    // ###



    // ### Public methods

    public void populateChildren()
    {
        /*
         * So far I've been trying to implement every redstone component node by
         * hardcoding them, like "if [block here] and [block there is redstone] then [node]"
         * but that is really inneficient. I need to think of a way to mimic redstone with
         * code, and the simplest and I think best way would be to implement searching
         * the same way redstone works: with weak and strong powering.
         *
         * Observations:
         *  - Weak powering is a subset of strong powering. There is no redstone component
         *      that will get powered by weak powering that will not get powered by strong powering.
         *
         * will think about it more later
         *
         *
         */

        // ## Setup
        List<Node> receivingNodes = new ArrayList<Node>();


        /* OLD "HARDCODE" IMPLEMENTATION */

        // # Setting up the properties
        // The hashmap that links directions to the wire connections of the redstone wire
        HashMap<Direction, WireConnection> wireConnectionOfDirection = new HashMap<Direction, WireConnection>();
        wireConnectionOfDirection.put(Direction.EAST,  (WireConnection) this.blockState.getEntries().get(Properties.EAST_WIRE_CONNECTION));
        wireConnectionOfDirection.put(Direction.SOUTH, (WireConnection) this.blockState.getEntries().get(Properties.SOUTH_WIRE_CONNECTION));
        wireConnectionOfDirection.put(Direction.WEST,  (WireConnection) this.blockState.getEntries().get(Properties.WEST_WIRE_CONNECTION));
        wireConnectionOfDirection.put(Direction.NORTH, (WireConnection) this.blockState.getEntries().get(Properties.NORTH_WIRE_CONNECTION));


        // ## Checking and creating nodes
        for (Direction directionChecked : NodeHelper.horizontalDirections)
        {
            // # Setup
            PositionedBlockState offsetEqual =  PositionedBlockState.of(world, this.position.offset(directionChecked));
            PositionedBlockState offsetDown =   PositionedBlockState.of(world, offsetEqual.getBlockPos().offset(Direction.DOWN));
            PositionedBlockState down =         PositionedBlockState.of(world, this.position.offset(Direction.DOWN));
            PositionedBlockState up =           PositionedBlockState.of(world, this.position.offset(Direction.UP));
            PositionedBlockState offsetUp =     PositionedBlockState.of(world, offsetEqual.getBlockPos().offset(Direction.UP));


            // # REDSTONE WIRES
            if (isSolidBlock(world, down))
                if (isBlock(offsetDown, Blocks.REDSTONE_WIRE) && !isSolidBlock(world, offsetEqual))
                    receivingNodes.add( Node.create(world, offsetDown.getBlockPos()) );

            if (isBlock(offsetEqual, Blocks.REDSTONE_WIRE))
                receivingNodes.add( Node.create(world, offsetEqual.getBlockPos()) );

            if (!isSolidBlock(world, up))
                if (isBlock(offsetUp, Blocks.REDSTONE_WIRE))
                    receivingNodes.add( Node.create(world, offsetUp.getBlockPos()) );


            // # REPEATERS
            if (isSolidBlock(world, down))
                if (isBlock(offsetDown, Blocks.REPEATER))
                    if (getProperty(offsetDown, Properties.HORIZONTAL_FACING) == directionChecked.getOpposite())
                        receivingNodes.add( Node.create(world, offsetDown.getBlockPos()) );

            if (isBlock(offsetEqual, Blocks.REPEATER))
                if (getProperty(offsetEqual, Properties.HORIZONTAL_FACING) == directionChecked.getOpposite())
                    receivingNodes.add( Node.create(world, offsetEqual.getBlockPos()) );

            if (isSolidBlock(world, offsetEqual))
                if (wireConnectionOfDirection.get(directionChecked).isConnected())
                    receivingNodes.addAll( findRepeatersConnectedToBlock(world, offsetEqual.getBlockPos()) );


            // # COMPARATORS
            if (isSolidBlock(world, down))
                if (isBlock(offsetDown, Blocks.COMPARATOR))
                    // Checking for comparators not facing in our redstone wire node, as they can take
                    // a redstone wire input from all 3 sides except this one.
                    if (getProperty(offsetDown, Properties.HORIZONTAL_FACING) != directionChecked)
                        receivingNodes.add( Node.create(world, offsetDown.getBlockPos()) );

            if (isBlock(offsetEqual, Blocks.COMPARATOR))
                if (getProperty(offsetEqual, Properties.HORIZONTAL_FACING) == directionChecked.getOpposite())
                    receivingNodes.add( Node.create(world, offsetEqual.getBlockPos()) );

            if (isSolidBlock(world, offsetEqual))
                if (wireConnectionOfDirection.get(directionChecked).isConnected())
                    receivingNodes.addAll( findComparatorsConnectedToBlock(world, offsetEqual.getBlockPos()) );
        }


        // ## At the end, add all the child nodes to this node
        this.addChildren(receivingNodes);
    }

    // ###
}
