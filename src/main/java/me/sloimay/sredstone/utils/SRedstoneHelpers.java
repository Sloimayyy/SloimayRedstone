package me.sloimay.sredstone.utils;

import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.features.redstonenetwork.nodes.Node;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Contains a lot of useful methods related to the SRedstone mod.
 */
public class SRedstoneHelpers
{
    /**
     * Helpers about redstone networks.
     */
    public static class RedstoneNetworkHelper
    {
        /**
         * Converts a set of timings coming from a node in a redstone
         * network to a string.
         *
         * @return
         */
        public static String timingsSetToString(Set<Integer> nodeTimings)
        {
            List<Integer> nodeTimingsInOrder = nodeTimings.stream().sorted().toList();
            StringBuilder displayedStringBuilder = new StringBuilder();
            String separation = " | ";
            // Add a separator between each timing
            nodeTimingsInOrder.forEach(nodeTiming ->
            {
                // Compute the node timing (not timingS) string
                // If we want to show the timing as redstone ticks we need to divide by 2, but if
                // we turn the int into a double it's gonna show "1.0" isn't of "1" for 1rt timings.
                // So to fix that we divide the int by 2, then add ".5" if it's odd.
                String nodeTimingStr = "";
                if (ClientDB.showTimingsAsRedstoneTicks)
                    nodeTimingStr = Integer.toString(nodeTiming / 2) + (nodeTiming % 2 == 1 ? ".5" : "");
                else
                    nodeTimingStr = Integer.toString(nodeTiming);

                // Add the timing string to the main string
                displayedStringBuilder.append(nodeTimingStr + separation);
            });
            // Make it so that the separator doesn't show up at the end
            String displayedString =
                    nodeTimings.size() >= 1 ?
                            displayedStringBuilder.substring(0, displayedStringBuilder.length() - separation.length()) :
                            displayedStringBuilder.toString();

            // Retrun
            return displayedString;
        }


        /**
         * Contains useful methods for nodes
         */
        public static class NodeHelper
        {
            // ### Fields
            /**
             * An array of all 6 {@code Direction} that MC has. Used for caching purposes
             * to not use Direction.values() as it creates a new array everytime we call
             * the method.
             */
            public static final Direction[] allDirections =
                    new Direction[] {
                            Direction.EAST,
                            Direction.SOUTH,
                            Direction.WEST,
                            Direction.NORTH,
                            Direction.UP,
                            Direction.DOWN
                    };

            /**
             * Array of all horizontal directions.
             */
            public static final Direction[] horizontalDirections =
                    new Direction[] {
                            Direction.EAST,
                            Direction.SOUTH,
                            Direction.WEST,
                            Direction.NORTH,
                    };

            /**
             * Array of all vertical directions.
             */
             public static final Direction[] verticalDirections =
                    new Direction[] {
                            Direction.UP,
                            Direction.DOWN
                    };
            // ###



            /**
             * Returns the redstone wire nodes around the inputted solid block.
             * (Basically checks in all 6 {@code Direction}s)
             *
             * @param world
             * @param blockPos
             * @return
             */
            public static List<Node> findRedstoneWireNodesAroundBlock(World world, BlockPos blockPos)
            {
                // ## Setup
                List<Node> redstoneWireNodesAround = new ArrayList<Node>();

                // ## Loop through all 6 directions and check
                for (Direction dir : NodeHelper.allDirections)
                {
                    BlockPos checkedPos = blockPos.offset(dir);
                    if (world.getBlockState(checkedPos).getBlock().equals(Blocks.REDSTONE_WIRE))
                    {
                        redstoneWireNodesAround.add(Node.create(world, checkedPos));
                    }
                }

                // ## Retrun
                return redstoneWireNodesAround;
            }



            /**
             * Returns the redstone wire nodes around the inputted solid block.
             * (Basically checks in all 6 {@code Direction}s)
             *
             * @param world
             * @param blockPos
             * @return
             */
            public static List<Node> findRedstoneWireNodesAroundBlock(World world, BlockPos blockPos, Direction[] directions)
            {
                // ## Setup
                List<Node> redstoneWireNodesAround = new ArrayList<Node>();

                // ## Loop through all 6 directions and check
                for (Direction dir : directions)
                {
                    BlockPos checkedPos = blockPos.offset(dir);
                    if (world.getBlockState(checkedPos).getBlock().equals(Blocks.REDSTONE_WIRE))
                    {
                        redstoneWireNodesAround.add(Node.create(world, checkedPos));
                    }
                }

                // ## Retrun
                return redstoneWireNodesAround;
            }



            /**
             * Returns the possible repeater nodes around the inputted solid block
             * that uses that block as an input.
             *
             * @param world
             * @param blockPos
             * @return
             */
            public static List<Node> findRepeatersConnectedToBlock(World world, BlockPos blockPos)
            {
                return
                        findHorizontalFacingRedstoneComponentConnectedToBlock(
                                world,
                                blockPos,
                                Blocks.REPEATER,
                                true
                        );
            }

            /**
             * Returns the possible comparator nodes around the inputted solid block
             * that uses that block as an input.
             *
             * @param world
             * @param blockPos
             * @return
             */
            public static List<Node> findComparatorsConnectedToBlock(World world, BlockPos blockPos)
            {
                return
                        findHorizontalFacingRedstoneComponentConnectedToBlock(
                                world,
                                blockPos,
                                Blocks.COMPARATOR,
                                true
                        );
            }

            /**
             * Returns the possible redstone wall torches nodes around the inputted solid block
             * that uses that block as an input.
             *
             * @param world
             * @param blockPos
             * @return
             */
            public static List<Node> findRedstoneWallTorchesConnectedToBlock(World world, BlockPos blockPos)
            {
                return
                        findHorizontalFacingRedstoneComponentConnectedToBlock(
                                world,
                                blockPos,
                                Blocks.REDSTONE_WALL_TORCH,
                                false
                        );
            }



            /**
             * Returns redstone component nodes that are connected to the block at the inputted
             * block position.
             *
             * @param world
             * @param blockPos
             * @param nodeRedstoneComponent
             * @param reversedHorizontalFacing: Whether or not we reverse the horizontal facing of the pooled block to
             *                                  match the direction we're iterating over.
             *                                  For example, imagine the pooled block is a repeater and it's pointing
             *                                  away from the block, which is what we want to say that it's connected
             *                                  to the block. Let's say the direction we're iterating over is east, then
             *                                  the repeater is going to be facing west.
             *                                  However, if we were looking for a redstone_wall_torch, it would be
             *                                  facing east as well.
             *                                  So that's why we have conditional horizontal facing reversing, we would
             *                                  reverse for a repeater but not a torch.
             * @return
             */
            public static List<Node> findHorizontalFacingRedstoneComponentConnectedToBlock
            (
                    World world,
                    BlockPos blockPos,
                    Block nodeRedstoneComponent,
                    boolean reversedHorizontalFacing
            )
            {
                // ## Setup
                List<Node> nodes = new ArrayList<Node>();

                // ## Loop through all 4 directions and check
                for (Direction dir : NodeHelper.horizontalDirections)
                {
                    BlockPos checkedPos = blockPos.offset(dir);
                    BlockState checkedBlockState = world.getBlockState(checkedPos);
                    if (checkedBlockState.getBlock().equals(nodeRedstoneComponent))
                    {
                        Direction facingToCheck = reversedHorizontalFacing ? dir.getOpposite() : dir;
                        if (checkedBlockState.getEntries().get(Properties.HORIZONTAL_FACING) == facingToCheck)
                            nodes.add(Node.create(world, checkedPos));
                    }
                }

                // ## Retrun
                return nodes;
            }



            /**
             * Returns true if the inputted positioned block state in the inputted world is
             * a solid block.
             *
             * @param world
             * @param positionedBlockState
             * @return
             */
            public static boolean isSolidBlock(World world, SFabricLib.BlockUtils.PositionedBlockState positionedBlockState)
            {
                return positionedBlockState.getBlockState().isSolidBlock(world, positionedBlockState.getBlockPos());
            }

            /**
             * Returns true if the inputted positioned block state's block
             * is the inputted block.
             *
             * @param positionedBlockState
             * @param block
             * @return
             */
            public static boolean isBlock(SFabricLib.BlockUtils.PositionedBlockState positionedBlockState, Block block)
            {
                return positionedBlockState.getBlockState().getBlock().equals(block);
            }

            /**
             * Returns true if the inputted positioned block state is one of the inputted blocks.
             *
             * @param positionedBlockState
             * @param blocks
             * @return
             */
            public static boolean isBlock(SFabricLib.BlockUtils.PositionedBlockState positionedBlockState, Block... blocks)
            {
                for (Block block : blocks)
                    if (isBlock(positionedBlockState, block))
                        return true;

                return false;
            }

            /**
             * Returns true if the inputted positioned block state is one of the inputted blocks.
             *
             * @param positionedBlockState
             * @param blocks
             * @return
             */
            public static boolean isBlock(SFabricLib.BlockUtils.PositionedBlockState positionedBlockState, List<Block> blocks)
            {
                for (Block block : blocks)
                    if (isBlock(positionedBlockState, block))
                        return true;

                return false;
            }

            /**
             * Checks if the inputted block is a block entity.
             *
             * @param block
             * @return
             */
            public static boolean isBlockEntity(Block block)
            {
                return block instanceof BlockWithEntity;
            }

            /**
             * Checks if the inputted block state is a block entity.
             *
             * @param blockState
             * @return
             */
            public static boolean isBlockEntity(BlockState blockState)
            {
                return isBlockEntity(blockState.getBlock());
            }

            /**
             * Checks if the inputted positioned block state is a block entity.
             *
             * @param positionedBlockState
             * @return
             */
            public static boolean isBlockEntity(SFabricLib.BlockUtils.PositionedBlockState positionedBlockState)
            {
                return isBlockEntity(positionedBlockState.getBlockState());
            }

            /**
             * Returns the value for the inputted property of the inputted block state.
             *
             * @param positionedBlockState
             * @param property
             * @return
             */
            public static Comparable<?> getProperty(SFabricLib.BlockUtils.PositionedBlockState positionedBlockState, Property property)
            {
                return positionedBlockState.getBlockState().get(property);
            }
        }
    }
}
