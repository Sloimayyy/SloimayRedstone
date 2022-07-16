package me.sloimay.sredstone.utils;

import me.sloimay.sredstone.db.ClientDB;
import net.minecraft.text.LiteralText;

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
    public static class RedstoneNetworkHelpers
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
    }
}
