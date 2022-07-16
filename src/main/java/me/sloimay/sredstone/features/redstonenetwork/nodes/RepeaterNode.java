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
        List<Node> connectedNodes = new ArrayList<Node>();
        // # Setting up the properties
        // Repeater output facing and not plain facing, because the facing of a repeater is where
        // its getting its input from which is really counter intuitive.
        Direction repeaterOutputFacing = this.blockState.get(Properties.HORIZONTAL_FACING).getOpposite();


        // ## Checking for nodes and creating them
        // # Setup
        BlockPos offsetEqualBlockPos = this.position.offset(repeaterOutputFacing);
        BlockState offsetEqual = this.world.getBlockState(offsetEqualBlockPos);

        BlockPos offsetDownBlockPos = offsetEqualBlockPos.offset(Direction.DOWN);
        BlockState offsetDown = this.world.getBlockState(offsetDownBlockPos);

        BlockPos downBlockPos = this.position.offset(Direction.DOWN);
        BlockState down = this.world.getBlockState(downBlockPos);

        BlockPos upBlockPos = this.position.offset(Direction.UP);
        BlockState up = this.world.getBlockState(upBlockPos);

        BlockPos offsetUpBlockPos = offsetEqualBlockPos.offset(Direction.UP);
        BlockState offsetUp = this.world.getBlockState(offsetUpBlockPos);

        // # Check for redstone dust in front of the repeater
        if (offsetEqual.getBlock().equals(Blocks.REDSTONE_WIRE))
        {
            connectedNodes.add(Node.create(world, offsetEqualBlockPos));
        }

        // # Check for repeater in the right orientation in front of the repeater
        if (offsetEqual.getBlock().equals(Blocks.REPEATER))
        {
            if (offsetEqual.getEntries().get(Properties.HORIZONTAL_FACING) == repeaterOutputFacing.getOpposite())
            {
                connectedNodes.add(Node.create(world, offsetEqualBlockPos));
            }
        }

        // # Check for redstone dust around the block in front if it's a solid block
        if (offsetEqual.isSolidBlock(world, offsetEqualBlockPos))
        {
            
        }

        // #


        // ## At the end, add all the child nodes to this node
        this.addChildren(connectedNodes);
    }
    // ###
}
