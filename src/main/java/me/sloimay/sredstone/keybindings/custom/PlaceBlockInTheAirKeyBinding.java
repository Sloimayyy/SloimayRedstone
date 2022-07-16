package me.sloimay.sredstone.keybindings.custom;

import me.sloimay.sredstone.keybindings.SKeyBinding;
import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

/**
 * The Redstone Quality of Life Keybinding.
 */
public class PlaceBlockInTheAirKeyBinding extends SKeyBinding
{

    // ### Fields

    // ###



    // ### Init
    public PlaceBlockInTheAirKeyBinding()
    {
        super(
                "key.sredstone.place_block_in_the_air",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_2,
                "key.category.sredstone"
        );
    }
    // ###



    // ### Public methods
    @Override
    public void execute(MinecraftClient client)
    {
        // ## Setup
        if(!client.player.getAbilities().creativeMode) return;

        // ## Place the epic block in the air
        float blockPlaceDist = 3;
        // Create a rigged block hit result, telling the server a block has been hit
        // and with the information of which block we want to place
        float eyeHeight = client.player.getEyeHeight(client.player.getPose());
        Vec3d blockPosRelative = client.player.getRotationVecClient().multiply(blockPlaceDist).add(0, eyeHeight, 0);
        // # Actually send the packet n o w
        SFabricLib.PlayerUtils.placeBlockInHandRelative(client, blockPosRelative, true, client.player.getHorizontalFacing().getOpposite());
    }
    // ###
}
