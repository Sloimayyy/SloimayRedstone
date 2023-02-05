package me.sloimay.sredstone.db;

import com.sk89q.worldedit.history.changeset.ArrayListHistory;
import com.sk89q.worldedit.world.block.BlockType;
import com.sk89q.worldedit.world.block.BlockTypes;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Db
{

    // Common color coding blocks ids (solid and transparent)
    public static List<String> commonSolidColorCodingBlockIds;
    public static List<String> commonTransparentColorCodingBlockIds;

    public static boolean test = false;

    // The minecraft colors
    public static List<String> minecraftColors = List.of(
            "white",
            "orange",
            "magenta",
            "light_blue",
            "yellow",
            "lime",
            "pink",
            "gray",
            "light_gray",
            "cyan",
            "purple",
            "blue",
            "brown",
            "green",
            "red",
            "black"
    );

    // The hashmaps linking a block type to a certain minecraft color
    public static HashMap<BlockType, String> woolBlockTypeToColor;
    public static HashMap<BlockType, String> concreteBlockTypeToColor;
    public static HashMap<BlockType, String> stainedGlassBlockTypeToColor;



    static {
        // Init the common color coding block ids
        commonSolidColorCodingBlockIds = new ArrayList<String>();
        commonTransparentColorCodingBlockIds = new ArrayList<String>();

        initCommonSolidColorCodingBlockIds();
        initCommonTransparentColorCodingBlockIds();

        // Init the world edit block types to their color hashmap
        woolBlockTypeToColor = new HashMap<BlockType, String>();
        concreteBlockTypeToColor = new HashMap<BlockType, String>();
        stainedGlassBlockTypeToColor = new HashMap<BlockType, String>();

        initColoredBlockTypesToColor(woolBlockTypeToColor, "wool");
        initColoredBlockTypesToColor(concreteBlockTypeToColor, "concrete");
        initColoredBlockTypesToColor(stainedGlassBlockTypeToColor, "stained_glass");
    }


    // Private methods

    private static void initCommonSolidColorCodingBlockIds()
    {
        commonSolidColorCodingBlockIds.add("white_wool");
        commonSolidColorCodingBlockIds.add("white_concrete");
        commonSolidColorCodingBlockIds.add("orange_wool");
        commonSolidColorCodingBlockIds.add("orange_concrete");
        commonSolidColorCodingBlockIds.add("magenta_wool");
        commonSolidColorCodingBlockIds.add("magenta_concrete");
        commonSolidColorCodingBlockIds.add("light_blue_wool");
        commonSolidColorCodingBlockIds.add("light_blue_concrete");
        commonSolidColorCodingBlockIds.add("yellow_wool");
        commonSolidColorCodingBlockIds.add("yellow_concrete");
        commonSolidColorCodingBlockIds.add("lime_wool");
        commonSolidColorCodingBlockIds.add("lime_concrete");
        commonSolidColorCodingBlockIds.add("pink_wool");
        commonSolidColorCodingBlockIds.add("pink_concrete");
        commonSolidColorCodingBlockIds.add("gray_wool");
        commonSolidColorCodingBlockIds.add("gray_concrete");
        commonSolidColorCodingBlockIds.add("light_gray_wool");
        commonSolidColorCodingBlockIds.add("light_gray_concrete");
        commonSolidColorCodingBlockIds.add("cyan_wool");
        commonSolidColorCodingBlockIds.add("cyan_concrete");
        commonSolidColorCodingBlockIds.add("purple_wool");
        commonSolidColorCodingBlockIds.add("purple_concrete");
        commonSolidColorCodingBlockIds.add("blue_wool");
        commonSolidColorCodingBlockIds.add("blue_concrete");
        commonSolidColorCodingBlockIds.add("brown_wool");
        commonSolidColorCodingBlockIds.add("brown_concrete");
        commonSolidColorCodingBlockIds.add("green_wool");
        commonSolidColorCodingBlockIds.add("green_concrete");
        commonSolidColorCodingBlockIds.add("red_wool");
        commonSolidColorCodingBlockIds.add("red_concrete");
        commonSolidColorCodingBlockIds.add("black_wool");
        commonSolidColorCodingBlockIds.add("black_concrete");
    }

    private static void initCommonTransparentColorCodingBlockIds()
    {
        commonTransparentColorCodingBlockIds.add("white_stained_glass");
        commonTransparentColorCodingBlockIds.add("orange_stained_glass");
        commonTransparentColorCodingBlockIds.add("magenta_stained_glass");
        commonTransparentColorCodingBlockIds.add("light_blue_stained_glass");
        commonTransparentColorCodingBlockIds.add("yellow_stained_glass");
        commonTransparentColorCodingBlockIds.add("lime_stained_glass");
        commonTransparentColorCodingBlockIds.add("pink_stained_glass");
        commonTransparentColorCodingBlockIds.add("gray_stained_glass");
        commonTransparentColorCodingBlockIds.add("light_gray_stained_glass");
        commonTransparentColorCodingBlockIds.add("cyan_stained_glass");
        commonTransparentColorCodingBlockIds.add("purple_stained_glass");
        commonTransparentColorCodingBlockIds.add("blue_stained_glass");
        commonTransparentColorCodingBlockIds.add("brown_stained_glass");
        commonTransparentColorCodingBlockIds.add("green_stained_glass");
        commonTransparentColorCodingBlockIds.add("red_stained_glass");
        commonTransparentColorCodingBlockIds.add("black_stained_glass");
        commonTransparentColorCodingBlockIds.add("smooth_stone_slab");
        commonTransparentColorCodingBlockIds.add("glass");
        commonTransparentColorCodingBlockIds.add("glowstone");
    }

    private static void initColoredBlockTypesToColor(HashMap<BlockType, String> blockTypeToColor, String blockIdSuffix)
    {
        for (String col : minecraftColors)
        {
            blockTypeToColor.put(BlockTypes.get("minecraft:" + col + "_" + blockIdSuffix), col);
        }
    }
}
