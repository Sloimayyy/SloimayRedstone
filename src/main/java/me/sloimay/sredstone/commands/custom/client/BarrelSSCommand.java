package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.db.Db;
import me.sloimay.sredstone.utils.SFabricLib;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.command.argument.NumberRangeArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.predicate.NumberRange;

import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * /barrelss [int]
 *
 * Gives you a barrel with the inputted signal strength. Only values between 0 and 15 are accepted.
 */
public class BarrelSSCommand extends SClientCommand
{
    // ### Fields

    /**
     * The database containing all signal strength barrels.
     * The index at which they are in the list is their signal strength.
     */
    private List<ItemStack> ssBarrels = new ArrayList<ItemStack>();

    // ###



    // ### Init

    public BarrelSSCommand()
    {
        // Initalize the SS barrels database
        this.initSSBarrelsDB();
    }

    // ###



    // ### Public methods

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {

        // The main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("barrelss")
                        .then(argument("Desired signal strength", IntegerArgumentType.integer())
                                .executes(context ->
                                {
                                    // Get the right SS barrel from the argument
                                    int ss = IntegerArgumentType.getInteger(context, "Desired signal strength");
                                    ItemStack barrel = this.ssBarrels.get(ss);
                                    SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, barrel);

                                    return 0;
                                })
                        )
        );

        // Aliases
        dispatcher.register(literal("b").redirect(mainNode));
    }

    // ###



    // ### Private methods

    private void initSSBarrelsDB()
    {
        // Init the objects
        ItemStack barrel0 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel1 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel2 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel3 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel4 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel5 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel6 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel7 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel8 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel9 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel10 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel11 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel12 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel13 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel14 = new ItemStack(Items.BARREL, 1);
        ItemStack barrel15 = new ItemStack(Items.BARREL, 1);


        // Change each barrel's NBT
        try
        {
            barrel0.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"0\"}'},Enchantments:[{}],BlockEntityTag:{Items:[],CustomName:'{\"text\":\"0\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel1.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"1\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"1\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel2.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"2\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"2\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel3.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"3\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"3\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel4.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"4\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"4\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel5.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"5\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"5\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel6.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"6\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"6\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel7.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"7\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"7\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel8.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"8\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"8\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel9.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"9\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b},{Slot:14b,id:\"minecraft:redstone\",Count:64b},{Slot:15b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"9\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel10.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"10\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b},{Slot:14b,id:\"minecraft:redstone\",Count:64b},{Slot:15b,id:\"minecraft:redstone\",Count:64b},{Slot:16b,id:\"minecraft:redstone\",Count:64b},{Slot:17b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"10\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel11.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"11\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b},{Slot:14b,id:\"minecraft:redstone\",Count:64b},{Slot:15b,id:\"minecraft:redstone\",Count:64b},{Slot:16b,id:\"minecraft:redstone\",Count:64b},{Slot:17b,id:\"minecraft:redstone\",Count:64b},{Slot:18b,id:\"minecraft:redstone\",Count:64b},{Slot:19b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"11\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel12.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"12\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b},{Slot:14b,id:\"minecraft:redstone\",Count:64b},{Slot:15b,id:\"minecraft:redstone\",Count:64b},{Slot:16b,id:\"minecraft:redstone\",Count:64b},{Slot:17b,id:\"minecraft:redstone\",Count:64b},{Slot:18b,id:\"minecraft:redstone\",Count:64b},{Slot:19b,id:\"minecraft:redstone\",Count:64b},{Slot:20b,id:\"minecraft:redstone\",Count:64b},{Slot:21b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"12\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel13.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"13\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b},{Slot:14b,id:\"minecraft:redstone\",Count:64b},{Slot:15b,id:\"minecraft:redstone\",Count:64b},{Slot:16b,id:\"minecraft:redstone\",Count:64b},{Slot:17b,id:\"minecraft:redstone\",Count:64b},{Slot:18b,id:\"minecraft:redstone\",Count:64b},{Slot:19b,id:\"minecraft:redstone\",Count:64b},{Slot:20b,id:\"minecraft:redstone\",Count:64b},{Slot:21b,id:\"minecraft:redstone\",Count:64b},{Slot:22b,id:\"minecraft:redstone\",Count:64b},{Slot:23b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"13\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel14.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"14\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b},{Slot:14b,id:\"minecraft:redstone\",Count:64b},{Slot:15b,id:\"minecraft:redstone\",Count:64b},{Slot:16b,id:\"minecraft:redstone\",Count:64b},{Slot:17b,id:\"minecraft:redstone\",Count:64b},{Slot:18b,id:\"minecraft:redstone\",Count:64b},{Slot:19b,id:\"minecraft:redstone\",Count:64b},{Slot:20b,id:\"minecraft:redstone\",Count:64b},{Slot:21b,id:\"minecraft:redstone\",Count:64b},{Slot:22b,id:\"minecraft:redstone\",Count:64b},{Slot:23b,id:\"minecraft:redstone\",Count:64b},{Slot:24b,id:\"minecraft:redstone\",Count:64b},{Slot:25b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"14\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
            barrel15.setNbt(StringNbtReader.parse("{display:{Name:'{\"text\":\"15\"}'},Enchantments:[{}],BlockEntityTag:{Items:[{Slot:0b,id:\"minecraft:redstone\",Count:64b},{Slot:1b,id:\"minecraft:redstone\",Count:64b},{Slot:2b,id:\"minecraft:redstone\",Count:64b},{Slot:3b,id:\"minecraft:redstone\",Count:64b},{Slot:4b,id:\"minecraft:redstone\",Count:64b},{Slot:5b,id:\"minecraft:redstone\",Count:64b},{Slot:6b,id:\"minecraft:redstone\",Count:64b},{Slot:7b,id:\"minecraft:redstone\",Count:64b},{Slot:8b,id:\"minecraft:redstone\",Count:64b},{Slot:9b,id:\"minecraft:redstone\",Count:64b},{Slot:10b,id:\"minecraft:redstone\",Count:64b},{Slot:11b,id:\"minecraft:redstone\",Count:64b},{Slot:12b,id:\"minecraft:redstone\",Count:64b},{Slot:13b,id:\"minecraft:redstone\",Count:64b},{Slot:14b,id:\"minecraft:redstone\",Count:64b},{Slot:15b,id:\"minecraft:redstone\",Count:64b},{Slot:16b,id:\"minecraft:redstone\",Count:64b},{Slot:17b,id:\"minecraft:redstone\",Count:64b},{Slot:18b,id:\"minecraft:redstone\",Count:64b},{Slot:19b,id:\"minecraft:redstone\",Count:64b},{Slot:20b,id:\"minecraft:redstone\",Count:64b},{Slot:21b,id:\"minecraft:redstone\",Count:64b},{Slot:22b,id:\"minecraft:redstone\",Count:64b},{Slot:23b,id:\"minecraft:redstone\",Count:64b},{Slot:24b,id:\"minecraft:redstone\",Count:64b},{Slot:25b,id:\"minecraft:redstone\",Count:64b},{Slot:26b,id:\"minecraft:redstone\",Count:64b}],CustomName:'{\"text\":\"15\"}'},BlockStateTag:{facing:\"up\",open:\"false\"}}"));
        }
        catch (Exception e) {}

        // Add the barrels to the database
        this.ssBarrels.add(barrel0);
        this.ssBarrels.add(barrel1);
        this.ssBarrels.add(barrel2);
        this.ssBarrels.add(barrel3);
        this.ssBarrels.add(barrel4);
        this.ssBarrels.add(barrel5);
        this.ssBarrels.add(barrel6);
        this.ssBarrels.add(barrel7);
        this.ssBarrels.add(barrel8);
        this.ssBarrels.add(barrel9);
        this.ssBarrels.add(barrel10);
        this.ssBarrels.add(barrel11);
        this.ssBarrels.add(barrel12);
        this.ssBarrels.add(barrel13);
        this.ssBarrels.add(barrel14);
        this.ssBarrels.add(barrel15);
    }

    // ###
}
