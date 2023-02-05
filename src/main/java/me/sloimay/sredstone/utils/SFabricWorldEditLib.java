package me.sloimay.sredstone.utils;

/*import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.AbstractPlayerActor;
import com.sk89q.worldedit.fabric.FabricAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.session.SessionManager;
 */

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

/**
 * Contains a bunch of utils related to worldedit.
 */
public class SFabricWorldEditLib
{
    /**
     * About WE regions
     */
    public static class RegionUtils
    {
        /**
         * Returns the inputted player's cuboid region in the world he is in.
         * Returns null if the region isn't a cuboid region.
         */
        /*public static CuboidRegion getPlayerCuboidRegion(ServerPlayerEntity player)
        {
            // Get the player adapted to world edit's standards, and same for the world
            final AbstractPlayerActor editPlayer = FabricAdapter.adaptPlayer(player);
            final SessionManager manager = WorldEdit.getInstance().getSessionManager();
            final LocalSession session = manager.findByName(editPlayer.getSessionKey().getName());
            RegionSelector regionSelector = session.getRegionSelector(FabricAdapter.adapt(player.world));

            // Return null if the region isn't a cuboid
            if (!(regionSelector.getIncompleteRegion() instanceof CuboidRegion)) {
                return null;
            }

            // We returnin bois
            return (CuboidRegion) regionSelector.getIncompleteRegion();
        }*/

        /**
         * Sets the inputted actor's region selector to the inputted region selector.
         *
         * @param actor
         * @param regionSelector
         */
        public static void setActorCuboidRegion(Actor actor, RegionSelector regionSelector)
        {
            WorldEdit.getInstance().getSessionManager().get(actor).setRegionSelector(
                    regionSelector.getWorld(),
                    regionSelector);
        }
    }
}
