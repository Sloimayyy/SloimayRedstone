package me.sloimay.sredstone.features.autodustplacing;

import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * Main class for the automatic redstone dust placing on blocks feature.
 */
public class AutomaticRedstoneDustPlacing
{
    // ### Fields

    // ###



    // ### Init

    // ###



    // ### Public static methods

    /**
     * Handles the feature, it's called by the PlayerPlaceBlock event.
     * We basically run the checks to see if we should be sending a place
     * redstone dust packet, and if yes   d o   i t
     */
    public static void handleFeature(BlockState blockPlacedState, BlockPos blockPlacedPos)
    {
        // ## Setup / checks
        if (!ClientDB.isRedstoneDustAutoPlacingActivated) return;
        if (!ClientDB.mcClient.player.getAbilities().creativeMode) return;
        if (blockPlacedState.getBlock().equals(Blocks.REDSTONE_WIRE)) return;

        // ## P l a c e   t h e   r e d s t o n e
        // # Setup
        BlockPos redstoneDustPos = blockPlacedPos.up();
        ItemStack mainhandItem = ClientDB.mcClient.player.getInventory().getMainHandStack();

        // # Pick -> Place -> Rollback
        SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, new ItemStack(Items.REDSTONE, 1));
        SFabricLib.PlayerUtils.placeBlockInHandAbsolute(
                ClientDB.mcClient,
                SFabricLib.VectorUtils.blockPosToVec3d(redstoneDustPos),
                true,
                Direction.UP
        );
        SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, mainhandItem);
    }

    // ###

    // ### Private static methods

    // ###
}
