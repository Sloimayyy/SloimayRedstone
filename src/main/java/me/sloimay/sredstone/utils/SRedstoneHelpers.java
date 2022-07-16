package me.sloimay.sredstone.utils;

import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.features.redstonenetwork.nodes.Node;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
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
            private static final Direction[] horizontalDirections =
                    new Direction[] {
                            Direction.EAST,
                            Direction.SOUTH,
                            Direction.WEST,
                            Direction.NORTH,
                    };

            /**
             * Array of all vertical directions.
             */
            private static final Direction[] verticalDirections =
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
             * @param solidBlockPosition
             * @return
             */
            public static List<Node> findRedstoneWireNodesAroundSolidBlock(World world, BlockPos solidBlockPosition)
            {
                // ## Setup
                List<Node> redstoneWireNodesAround = new ArrayList<Node>();

                // ## Loop through all 6 directions and check
                for (Direction dir : NodeHelper.allDirections)
                {
                    BlockPos checkedPos = solidBlockPosition.offset(dir);
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
             * @param solidBlockPosition
             * @return
             */
            public static List<Node> findRepeatersThatUsesThatSolidBlockAsInput(World world, BlockPos solidBlockPosition)
            {
                // ## Setup
                List<Node> repeaterNodes = new ArrayList<Node>();

                // ## Loop through all 4 directions and check
                for (Direction dir : NodeHelper.horizontalDirections)
                {
                    BlockPos checkedPos = solidBlockPosition.offset(dir);
                    BlockState checkedBlockState = world.getBlockState(checkedPos);
                    if (checkedBlockState.getBlock().equals(Blocks.REPEATER))
                    {
                        if (checkedBlockState.getEntries().get(Properties.HORIZONTAL_FACING) == dir.getOpposite())
                        {
                            repeaterNodes.add(Node.create(world, checkedPos));
                        }
                    }
                }

                // ## Retrun
                return repeaterNodes;
            }
        }
    }
}
