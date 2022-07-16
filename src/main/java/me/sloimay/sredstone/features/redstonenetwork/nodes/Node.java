package me.sloimay.sredstone.features.redstonenetwork.nodes;

import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;



/**
 * Represents a node in a {@code RedstoneNetwork}.
 */
public abstract class Node
{
    // ### Fields

    /**
     * The world in which resides this node.
     */
    protected World world;

    /**
     * The position in the world of this node.
     */
    protected BlockPos position;

    /**
     * The block state of this node.
     */
    protected BlockState blockState;

    /**
     * The redstone components receiving the outputs of this node.
     */
    protected List<Node> children;

    /**
     * The latency of this node in GameTick.
     */
    protected int latency;


    // ## Defaults

    /**
     * Horizontal directions clockwise (when looking down) where index 0 is east.
     */
    protected Direction[] directionsHorizontalClockwise =
            new Direction[] { Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH };

    // ##

    // ###



    // ### Init

    protected Node(World world, BlockPos position, BlockState blockState)
    {
        this.world = world;
        this.position = position;
        this.blockState = blockState;

        this.children = new ArrayList<Node>();
    }

    // ###



    // ### Public methods

    /**
     * Adds a child to this node.
     *
     * @param node
     */
    public void addChild(Node node)
    {
        Objects.requireNonNull(node);
        this.children.add(node);
    }

    /**
     * Adds children to this node.
     *
     * @param nodes
     */
    public void addChildren(List<Node> nodes)
    {
        nodes.forEach(node -> this.addChild(node));
    }

    /**
     *
     * @param other
     * @return True if the inputted other node is almost-deeply the same as this node.
     *         Almost deeply, because if it was truly deeply we'd have to check if each child
     *         is the same as well which would be hella inefficient. (We also don't really
     *         need deep equals, as we aim for each node to be at a unique block position in
     *         the world.)
     */
    public boolean unrigorousEquals(Node other)
    {
        if (!SFabricLib.VectorUtils.blockPosEquals(this.position, other.position))
            return false;
        if (!SFabricLib.BlockUtils.BlockStateUtils.isSameBlockState(this.blockState, other.blockState))
            return false;
        if (!this.world.equals(other.world))
            return false;


        return true;
    }

    // ###

    // ### Public static methods

    /**
     * Creates a new node of the right class depending on the blockState
     * present at the inputted block pos in the inputted world.
     *
     * @param world
     * @param blockPos
     * @return
     */
    public static Node create(World world, BlockPos blockPos)
    {
        // ## Setup
        BlockState blockState = world.getBlockState(blockPos);


        // ## If statements to know which node it should be
        if (blockState.getBlock().equals(Blocks.REDSTONE_WIRE))
        {
            return new RedstoneWireNode(world, blockPos, blockState);
        }
        if (blockState.getBlock().equals(Blocks.REPEATER))
        {
            return new RepeaterNode(world, blockPos, blockState);
        }
        if (blockState.getBlock().equals(Blocks.COMPARATOR))
        {
            return new ComparatorNode(world, blockPos, blockState);
        }


        // ## Return if nothing matched
        return null;
    }

    /**
     * Creates a list of nodes that are connected to this node.
     *
     * @return
     */
    public abstract void populateChildren();

    // ###



    // ### Getters

    public BlockState getBlockState()   { return this.blockState; }
    public BlockPos getPosition()       { return this.position; }
    public List<Node> getChildren()     { return Collections.unmodifiableList(this.children); }
    public int getLatency()             { return this.latency; }

    // ###
}
