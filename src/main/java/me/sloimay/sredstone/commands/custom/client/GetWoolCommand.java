package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.utils.SFabricLib;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class GetWoolCommand extends SClientCommand
{
    // ### Fields

    /**
     * The hashmap that links any wool alias to the actual wool id it represents.
     * Keys are generated with python.
     */
    private HashMap<String, String> woolAliasToWoolItemID = new HashMap<String, String>();

    /**
     * The hashmap that links any wool ID to the item stack it represents.
     */
    private HashMap<String, ItemStack> woolItemIDToWoolItemStack = new HashMap<String, ItemStack>();

    // ###



    // ### Init

    public GetWoolCommand()
    {
        this.initWoolAliasToWoolItemID();
        this.initWoolItemIDToWoolItemStack();
    }

    // ###



    // ### Public methods

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {

        // The main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("getwool")
                        .then(argument("wool color", StringArgumentType.string())
                                .executes(context ->
                                {
                                    // Get the right wool and then put it in the player's hand.
                                    String woolColor = StringArgumentType.getString(context, "wool color");
                                    ItemStack woolChosen = this.woolItemIDToWoolItemStack.get(this.woolAliasToWoolItemID.get(woolColor));
                                    SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, woolChosen);

                                    return 0;
                                })
                        )
        );

        // Aliases
        dispatcher.register(literal("wool").redirect(mainNode));
        dispatcher.register(literal("wo").redirect(mainNode));
    }

    // ###



    // ### Private methods

    /**
     * Inits the woolItemIDToWoolItemStack hashmap
     */
    private void initWoolAliasToWoolItemID()
    {
        woolAliasToWoolItemID.put("white", "white_wool");
        woolAliasToWoolItemID.put("0", "white_wool");
        woolAliasToWoolItemID.put("w", "white_wool");
        woolAliasToWoolItemID.put("wh", "white_wool");
        woolAliasToWoolItemID.put("whi", "white_wool");
        woolAliasToWoolItemID.put("whit", "white_wool");
        woolAliasToWoolItemID.put("orange", "orange_wool");
        woolAliasToWoolItemID.put("1", "orange_wool");
        woolAliasToWoolItemID.put("o", "orange_wool");
        woolAliasToWoolItemID.put("or", "orange_wool");
        woolAliasToWoolItemID.put("ora", "orange_wool");
        woolAliasToWoolItemID.put("oran", "orange_wool");
        woolAliasToWoolItemID.put("orang", "orange_wool");
        woolAliasToWoolItemID.put("magenta", "magenta_wool");
        woolAliasToWoolItemID.put("2", "magenta_wool");
        woolAliasToWoolItemID.put("m", "magenta_wool");
        woolAliasToWoolItemID.put("ma", "magenta_wool");
        woolAliasToWoolItemID.put("mag", "magenta_wool");
        woolAliasToWoolItemID.put("mage", "magenta_wool");
        woolAliasToWoolItemID.put("magen", "magenta_wool");
        woolAliasToWoolItemID.put("magent", "magenta_wool");
        woolAliasToWoolItemID.put("light_blue", "light_blue_wool");
        woolAliasToWoolItemID.put("3", "light_blue_wool");
        woolAliasToWoolItemID.put("light_b", "light_blue_wool");
        woolAliasToWoolItemID.put("light_bl", "light_blue_wool");
        woolAliasToWoolItemID.put("light_blu", "light_blue_wool");
        woolAliasToWoolItemID.put("light_blue", "light_blue_wool");
        woolAliasToWoolItemID.put("yellow", "yellow_wool");
        woolAliasToWoolItemID.put("4", "yellow_wool");
        woolAliasToWoolItemID.put("y", "yellow_wool");
        woolAliasToWoolItemID.put("ye", "yellow_wool");
        woolAliasToWoolItemID.put("yel", "yellow_wool");
        woolAliasToWoolItemID.put("yell", "yellow_wool");
        woolAliasToWoolItemID.put("yello", "yellow_wool");
        woolAliasToWoolItemID.put("lime", "lime_wool");
        woolAliasToWoolItemID.put("5", "lime_wool");
        woolAliasToWoolItemID.put("lim", "lime_wool");
        woolAliasToWoolItemID.put("pink", "pink_wool");
        woolAliasToWoolItemID.put("6", "pink_wool");
        woolAliasToWoolItemID.put("pi", "pink_wool");
        woolAliasToWoolItemID.put("pin", "pink_wool");
        woolAliasToWoolItemID.put("gray", "gray_wool");
        woolAliasToWoolItemID.put("7", "gray_wool");
        woolAliasToWoolItemID.put("gra", "gray_wool");
        woolAliasToWoolItemID.put("light_gray", "light_gray_wool");
        woolAliasToWoolItemID.put("8", "light_gray_wool");
        woolAliasToWoolItemID.put("light_g", "light_gray_wool");
        woolAliasToWoolItemID.put("light_gr", "light_gray_wool");
        woolAliasToWoolItemID.put("light_gra", "light_gray_wool");
        woolAliasToWoolItemID.put("light_gray", "light_gray_wool");
        woolAliasToWoolItemID.put("cyan", "cyan_wool");
        woolAliasToWoolItemID.put("9", "cyan_wool");
        woolAliasToWoolItemID.put("c", "cyan_wool");
        woolAliasToWoolItemID.put("cy", "cyan_wool");
        woolAliasToWoolItemID.put("cya", "cyan_wool");
        woolAliasToWoolItemID.put("purple", "purple_wool");
        woolAliasToWoolItemID.put("10", "purple_wool");
        woolAliasToWoolItemID.put("pu", "purple_wool");
        woolAliasToWoolItemID.put("pur", "purple_wool");
        woolAliasToWoolItemID.put("purp", "purple_wool");
        woolAliasToWoolItemID.put("purpl", "purple_wool");
        woolAliasToWoolItemID.put("blue", "blue_wool");
        woolAliasToWoolItemID.put("11", "blue_wool");
        woolAliasToWoolItemID.put("blu", "blue_wool");
        woolAliasToWoolItemID.put("brown", "brown_wool");
        woolAliasToWoolItemID.put("12", "brown_wool");
        woolAliasToWoolItemID.put("br", "brown_wool");
        woolAliasToWoolItemID.put("bro", "brown_wool");
        woolAliasToWoolItemID.put("brow", "brown_wool");
        woolAliasToWoolItemID.put("green", "green_wool");
        woolAliasToWoolItemID.put("13", "green_wool");
        woolAliasToWoolItemID.put("gre", "green_wool");
        woolAliasToWoolItemID.put("gree", "green_wool");
        woolAliasToWoolItemID.put("red", "red_wool");
        woolAliasToWoolItemID.put("14", "red_wool");
        woolAliasToWoolItemID.put("r", "red_wool");
        woolAliasToWoolItemID.put("re", "red_wool");
        woolAliasToWoolItemID.put("black", "black_wool");
        woolAliasToWoolItemID.put("15", "black_wool");
        woolAliasToWoolItemID.put("bla", "black_wool");
        woolAliasToWoolItemID.put("blac", "black_wool");

        woolAliasToWoolItemID.put("lb", "light_blue_wool");
        woolAliasToWoolItemID.put("lg", "light_gray_wool");
    }

    private void initWoolItemIDToWoolItemStack()
    {
        woolItemIDToWoolItemStack.put("white_wool", new ItemStack(Items.WHITE_WOOL, 1));
        woolItemIDToWoolItemStack.put("orange_wool", new ItemStack(Items.ORANGE_WOOL, 1));
        woolItemIDToWoolItemStack.put("magenta_wool", new ItemStack(Items.MAGENTA_WOOL, 1));
        woolItemIDToWoolItemStack.put("light_blue_wool", new ItemStack(Items.LIGHT_BLUE_WOOL, 1));
        woolItemIDToWoolItemStack.put("yellow_wool", new ItemStack(Items.YELLOW_WOOL, 1));
        woolItemIDToWoolItemStack.put("lime_wool", new ItemStack(Items.LIME_WOOL, 1));
        woolItemIDToWoolItemStack.put("pink_wool", new ItemStack(Items.PINK_WOOL, 1));
        woolItemIDToWoolItemStack.put("gray_wool", new ItemStack(Items.GRAY_WOOL, 1));
        woolItemIDToWoolItemStack.put("light_gray_wool", new ItemStack(Items.LIGHT_GRAY_WOOL, 1));
        woolItemIDToWoolItemStack.put("cyan_wool", new ItemStack(Items.CYAN_WOOL, 1));
        woolItemIDToWoolItemStack.put("purple_wool", new ItemStack(Items.PURPLE_WOOL, 1));
        woolItemIDToWoolItemStack.put("blue_wool", new ItemStack(Items.BLUE_WOOL, 1));
        woolItemIDToWoolItemStack.put("brown_wool", new ItemStack(Items.BROWN_WOOL, 1));
        woolItemIDToWoolItemStack.put("green_wool", new ItemStack(Items.GREEN_WOOL, 1));
        woolItemIDToWoolItemStack.put("red_wool", new ItemStack(Items.RED_WOOL, 1));
        woolItemIDToWoolItemStack.put("black_wool", new ItemStack(Items.BLACK_WOOL, 1));
    }

    // ###
}
