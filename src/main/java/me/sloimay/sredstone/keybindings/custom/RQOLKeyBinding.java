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
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;

/**
 * The Redstone Quality of Life Keybinding.
 *
 * Pick block mechanic when looking at a block:
 *  - Looking at no block: redstone dust
 *  - Looking at any block that aren't referenced: white wool
 *  - Any colored blocks that aren't stained glasses: stained glass of the color of the aimed at block
 *  - Any stained glass: the wool with the same color
 *
 *  - Redstone wire: Repeater
 *  - Repeater: Comparator
 *  - Comparator: Repeater
 *
 *  - Dispenser: Dropper
 *  - Dropper: Dispenser
 *
 *  - Piston: Sticky piston
 *  - Sticky piston: Piston
 *
 *  - Sneak + lookat redstone wire: Lever
 *  - Sneak + lookat repeater: torch
 *
 *  - Lever: button
 *  - button: Lever
 */
public class RQOLKeyBinding extends SKeyBinding
{

    // ###

    /**
     * The pick block hashmap to refer to when the player isn't in any state
     * in particular.
     */
    private HashMap<String, Item> mainPickBlockHashMap = new HashMap<>();
    /**
     * The pick block hashmap to refer to when the player is sneaking.
     */
    private HashMap<String, Item> sneakPickBlockHashMap = new HashMap<>();

    // ###



    // ### Init
    public RQOLKeyBinding()
    {
        super(
                "key.sredstone.rqol_binding",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_CONTROL,
                "key.category.sredstone"
        );

        this.setupPickBlockHashMaps();
    }
    // ###



    // ### Public methods
    @Override
    public void execute(MinecraftClient client)
    {
        // ## Setup
        if(!client.player.getAbilities().creativeMode) return;

        // Send a ray from the player's head, and see which block they're facing, then give it as a pick block to the player
        SFabricLib.BlockUtils.PositionedBlock pb =
                SFabricLib.PlayerUtils.getBlockLookingAtClient(client.player, 10, 1.0f, false);
        Block blockLookingAt = pb == null ? null : pb.getBlock();
        client.player.getInventory().updateItems();
        // The translation key of the block we're aiming at
        String blockKey = blockLookingAt == null ? null : blockLookingAt.getTranslationKey();

        // ## Pick the block, depending on the states of the player
        if (client.player.isSneaking())
        {
            if (this.sneakPickBlockHashMap.containsKey(blockKey))
                SFabricLib.PlayerUtils.doPickBlock(client, new ItemStack(this.sneakPickBlockHashMap.get(blockKey), 1));
            else
                SFabricLib.PlayerUtils.doPickBlock(client, new ItemStack(Items.WHITE_WOOL, 1));
        }
        else
        {
            // The player isn't in any particular state, so we use the main hashmap
            if (this.mainPickBlockHashMap.containsKey(blockKey))
                SFabricLib.PlayerUtils.doPickBlock(client, new ItemStack(this.mainPickBlockHashMap.get(blockKey), 1));
            else
                SFabricLib.PlayerUtils.doPickBlock(client, new ItemStack(Items.WHITE_WOOL, 1));
        }
    }
    // ###



    // ### Private methods

    /**
     * Sets up the hashmaps used for the pick block mechanic of the keybind.
     */
    private void setupPickBlockHashMaps()
    {
        mainPickBlockHashMap.put(Blocks.WHITE_TERRACOTTA.getTranslationKey(), Items.WHITE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.ORANGE_TERRACOTTA.getTranslationKey(), Items.ORANGE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.MAGENTA_TERRACOTTA.getTranslationKey(), Items.MAGENTA_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_BLUE_TERRACOTTA.getTranslationKey(), Items.LIGHT_BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.YELLOW_TERRACOTTA.getTranslationKey(), Items.YELLOW_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIME_TERRACOTTA.getTranslationKey(), Items.LIME_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PINK_TERRACOTTA.getTranslationKey(), Items.PINK_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GRAY_TERRACOTTA.getTranslationKey(), Items.GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_GRAY_TERRACOTTA.getTranslationKey(), Items.LIGHT_GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.CYAN_TERRACOTTA.getTranslationKey(), Items.CYAN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PURPLE_TERRACOTTA.getTranslationKey(), Items.PURPLE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLUE_TERRACOTTA.getTranslationKey(), Items.BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BROWN_TERRACOTTA.getTranslationKey(), Items.BROWN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GREEN_TERRACOTTA.getTranslationKey(), Items.GREEN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.RED_TERRACOTTA.getTranslationKey(), Items.RED_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLACK_TERRACOTTA.getTranslationKey(), Items.BLACK_STAINED_GLASS);

        mainPickBlockHashMap.put(Blocks.WHITE_WOOL.getTranslationKey(), Items.WHITE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.ORANGE_WOOL.getTranslationKey(), Items.ORANGE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.MAGENTA_WOOL.getTranslationKey(), Items.MAGENTA_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_BLUE_WOOL.getTranslationKey(), Items.LIGHT_BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.YELLOW_WOOL.getTranslationKey(), Items.YELLOW_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIME_WOOL.getTranslationKey(), Items.LIME_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PINK_WOOL.getTranslationKey(), Items.PINK_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GRAY_WOOL.getTranslationKey(), Items.GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_GRAY_WOOL.getTranslationKey(), Items.LIGHT_GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.CYAN_WOOL.getTranslationKey(), Items.CYAN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PURPLE_WOOL.getTranslationKey(), Items.PURPLE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLUE_WOOL.getTranslationKey(), Items.BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BROWN_WOOL.getTranslationKey(), Items.BROWN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GREEN_WOOL.getTranslationKey(), Items.GREEN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.RED_WOOL.getTranslationKey(), Items.RED_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLACK_WOOL.getTranslationKey(), Items.BLACK_STAINED_GLASS);

        mainPickBlockHashMap.put(Blocks.WHITE_CONCRETE_POWDER.getTranslationKey(), Items.WHITE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.ORANGE_CONCRETE_POWDER.getTranslationKey(), Items.ORANGE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.MAGENTA_CONCRETE_POWDER.getTranslationKey(), Items.MAGENTA_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_BLUE_CONCRETE_POWDER.getTranslationKey(), Items.LIGHT_BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.YELLOW_CONCRETE_POWDER.getTranslationKey(), Items.YELLOW_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIME_CONCRETE_POWDER.getTranslationKey(), Items.LIME_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PINK_CONCRETE_POWDER.getTranslationKey(), Items.PINK_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GRAY_CONCRETE_POWDER.getTranslationKey(), Items.GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_GRAY_CONCRETE_POWDER.getTranslationKey(), Items.LIGHT_GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.CYAN_CONCRETE_POWDER.getTranslationKey(), Items.CYAN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PURPLE_CONCRETE_POWDER.getTranslationKey(), Items.PURPLE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLUE_CONCRETE_POWDER.getTranslationKey(), Items.BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BROWN_CONCRETE_POWDER.getTranslationKey(), Items.BROWN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GREEN_CONCRETE_POWDER.getTranslationKey(), Items.GREEN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.RED_CONCRETE_POWDER.getTranslationKey(), Items.RED_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLACK_CONCRETE_POWDER.getTranslationKey(), Items.BLACK_STAINED_GLASS);

        mainPickBlockHashMap.put(Blocks.WHITE_GLAZED_TERRACOTTA.getTranslationKey(), Items.WHITE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.ORANGE_GLAZED_TERRACOTTA.getTranslationKey(), Items.ORANGE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.MAGENTA_GLAZED_TERRACOTTA.getTranslationKey(), Items.MAGENTA_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA.getTranslationKey(), Items.LIGHT_BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.YELLOW_GLAZED_TERRACOTTA.getTranslationKey(), Items.YELLOW_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIME_GLAZED_TERRACOTTA.getTranslationKey(), Items.LIME_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PINK_GLAZED_TERRACOTTA.getTranslationKey(), Items.PINK_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GRAY_GLAZED_TERRACOTTA.getTranslationKey(), Items.GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA.getTranslationKey(), Items.LIGHT_GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.CYAN_GLAZED_TERRACOTTA.getTranslationKey(), Items.CYAN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PURPLE_GLAZED_TERRACOTTA.getTranslationKey(), Items.PURPLE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLUE_GLAZED_TERRACOTTA.getTranslationKey(), Items.BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BROWN_GLAZED_TERRACOTTA.getTranslationKey(), Items.BROWN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GREEN_GLAZED_TERRACOTTA.getTranslationKey(), Items.GREEN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.RED_GLAZED_TERRACOTTA.getTranslationKey(), Items.RED_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLACK_GLAZED_TERRACOTTA.getTranslationKey(), Items.BLACK_STAINED_GLASS);

        mainPickBlockHashMap.put(Blocks.WHITE_CONCRETE.getTranslationKey(), Items.WHITE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.ORANGE_CONCRETE.getTranslationKey(), Items.ORANGE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.MAGENTA_CONCRETE.getTranslationKey(), Items.MAGENTA_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_BLUE_CONCRETE.getTranslationKey(), Items.LIGHT_BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.YELLOW_CONCRETE.getTranslationKey(), Items.YELLOW_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIME_CONCRETE.getTranslationKey(), Items.LIME_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PINK_CONCRETE.getTranslationKey(), Items.PINK_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GRAY_CONCRETE.getTranslationKey(), Items.GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.LIGHT_GRAY_CONCRETE.getTranslationKey(), Items.LIGHT_GRAY_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.CYAN_CONCRETE.getTranslationKey(), Items.CYAN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.PURPLE_CONCRETE.getTranslationKey(), Items.PURPLE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLUE_CONCRETE.getTranslationKey(), Items.BLUE_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BROWN_CONCRETE.getTranslationKey(), Items.BROWN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.GREEN_CONCRETE.getTranslationKey(), Items.GREEN_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.RED_CONCRETE.getTranslationKey(), Items.RED_STAINED_GLASS);
        mainPickBlockHashMap.put(Blocks.BLACK_CONCRETE.getTranslationKey(), Items.BLACK_STAINED_GLASS);

        mainPickBlockHashMap.put(null, Items.REDSTONE);

        mainPickBlockHashMap.put(Blocks.WHITE_STAINED_GLASS.getTranslationKey(), Items.WHITE_WOOL);
        mainPickBlockHashMap.put(Blocks.ORANGE_STAINED_GLASS.getTranslationKey(), Items.ORANGE_WOOL);
        mainPickBlockHashMap.put(Blocks.MAGENTA_STAINED_GLASS.getTranslationKey(), Items.MAGENTA_WOOL);
        mainPickBlockHashMap.put(Blocks.LIGHT_BLUE_STAINED_GLASS.getTranslationKey(), Items.LIGHT_BLUE_WOOL);
        mainPickBlockHashMap.put(Blocks.YELLOW_STAINED_GLASS.getTranslationKey(), Items.YELLOW_WOOL);
        mainPickBlockHashMap.put(Blocks.LIME_STAINED_GLASS.getTranslationKey(), Items.LIME_WOOL);
        mainPickBlockHashMap.put(Blocks.PINK_STAINED_GLASS.getTranslationKey(), Items.PINK_WOOL);
        mainPickBlockHashMap.put(Blocks.GRAY_STAINED_GLASS.getTranslationKey(), Items.GRAY_WOOL);
        mainPickBlockHashMap.put(Blocks.LIGHT_GRAY_STAINED_GLASS.getTranslationKey(), Items.LIGHT_GRAY_WOOL);
        mainPickBlockHashMap.put(Blocks.CYAN_STAINED_GLASS.getTranslationKey(), Items.CYAN_WOOL);
        mainPickBlockHashMap.put(Blocks.PURPLE_STAINED_GLASS.getTranslationKey(), Items.PURPLE_WOOL);
        mainPickBlockHashMap.put(Blocks.BLUE_STAINED_GLASS.getTranslationKey(), Items.BLUE_WOOL);
        mainPickBlockHashMap.put(Blocks.BROWN_STAINED_GLASS.getTranslationKey(), Items.BROWN_WOOL);
        mainPickBlockHashMap.put(Blocks.GREEN_STAINED_GLASS.getTranslationKey(), Items.GREEN_WOOL);
        mainPickBlockHashMap.put(Blocks.RED_STAINED_GLASS.getTranslationKey(), Items.RED_WOOL);
        mainPickBlockHashMap.put(Blocks.BLACK_STAINED_GLASS.getTranslationKey(), Items.BLACK_WOOL);

        mainPickBlockHashMap.put(Blocks.REDSTONE_WIRE.getTranslationKey(), Items.REPEATER);
        mainPickBlockHashMap.put(Blocks.REPEATER.getTranslationKey(), Items.COMPARATOR);
        mainPickBlockHashMap.put(Blocks.COMPARATOR.getTranslationKey(), Items.REPEATER);

        mainPickBlockHashMap.put(Blocks.DISPENSER.getTranslationKey(), Items.DROPPER);
        mainPickBlockHashMap.put(Blocks.DROPPER.getTranslationKey(), Items.DISPENSER);

        sneakPickBlockHashMap.put(Blocks.REDSTONE_WIRE.getTranslationKey(), Items.LEVER);

        sneakPickBlockHashMap.put(Blocks.REPEATER.getTranslationKey(), Items.REDSTONE_TORCH);

        mainPickBlockHashMap.put(Blocks.LEVER.getTranslationKey(), Items.STONE_BUTTON);
        mainPickBlockHashMap.put(Blocks.STONE_BUTTON.getTranslationKey(), Items.LEVER);
    }

    // ###
}
