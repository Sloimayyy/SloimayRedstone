package me.sloimay.sredstone.features.redstonenetwork.nodes;

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
        // ## Setup
        List<Node> connectedNodes = new ArrayList<Node>();
        // # Setting up the properties
        // The hashmap that links directions to the wire connections of the redstone wire
        HashMap<Direction, WireConnection> wireConnectionOfDirection = new HashMap<Direction, WireConnection>();
        wireConnectionOfDirection.put(Direction.EAST,  (WireConnection) this.blockState.getEntries().get(Properties.EAST_WIRE_CONNECTION));
        wireConnectionOfDirection.put(Direction.SOUTH, (WireConnection) this.blockState.getEntries().get(Properties.SOUTH_WIRE_CONNECTION));
        wireConnectionOfDirection.put(Direction.WEST,  (WireConnection) this.blockState.getEntries().get(Properties.WEST_WIRE_CONNECTION));
        wireConnectionOfDirection.put(Direction.NORTH, (WireConnection) this.blockState.getEntries().get(Properties.NORTH_WIRE_CONNECTION));


        // ## Checking and creating nodes
        Direction directionChecked = Direction.EAST;
        for (int i = 0; i < 4; i++)
        {
            // # Setup
            BlockPos offsetEqualBlockPos = this.position.offset(directionChecked);
            BlockState offsetEqual = this.world.getBlockState(offsetEqualBlockPos);

            BlockPos offsetDownBlockPos = offsetEqualBlockPos.offset(Direction.DOWN);
            BlockState offsetDown = this.world.getBlockState(offsetDownBlockPos);

            BlockPos downBlockPos = this.position.offset(Direction.DOWN);
            BlockState down = this.world.getBlockState(downBlockPos);

            BlockPos upBlockPos = this.position.offset(Direction.UP);
            BlockState up = this.world.getBlockState(upBlockPos);

            BlockPos offsetUpBlockPos = offsetEqualBlockPos.offset(Direction.UP);
            BlockState offsetUp = this.world.getBlockState(offsetUpBlockPos);



            // # First, check for the redstone dust or repeater -1 in y of the redstone dust
            if (down.isSolidBlock(this.world, downBlockPos))
            {
                // Check for redstone wire
                if (offsetDown.getBlock().equals(Blocks.REDSTONE_WIRE) && !offsetEqual.isOpaque())
                {
                    connectedNodes.add(Node.create(world, offsetDownBlockPos));
                }

                // Check for repeater
                if (offsetDown.getBlock().equals(Blocks.REPEATER))
                {
                    if (offsetDown.getEntries().get(Properties.HORIZONTAL_FACING) == directionChecked.getOpposite())
                    {
                        connectedNodes.add(Node.create(world, offsetDownBlockPos));
                    }
                }
            }

            // # Second, check for repeaters or dust directly connected to this dust
            // Redstone wire
            if (offsetEqual.getBlock().equals(Blocks.REDSTONE_WIRE))
            {
                connectedNodes.add(Node.create(world, offsetEqualBlockPos));
            }
            // Repeater
            if (offsetEqual.getBlock().equals(Blocks.REPEATER))
            {
                if (offsetEqual.getEntries().get(Properties.HORIZONTAL_FACING) == directionChecked.getOpposite())
                {
                    connectedNodes.add(Node.create(world, offsetEqualBlockPos));
                }
            }

            // # Third, check for redstone dust at offsetUp
            if (!up.isSolidBlock(this.world, downBlockPos))
            {
                if (offsetUp.getBlock().equals(Blocks.REDSTONE_WIRE))
                {
                    connectedNodes.add(Node.create(world, offsetUpBlockPos));
                }
            }



            // # At the end, rotate the direction for the next iteration
            directionChecked = directionChecked.rotateClockwise(Direction.Axis.Y);
        }


        // ## At the end, add all the child nodes to this node
        this.addChildren(connectedNodes);
    }

    // ###
}
