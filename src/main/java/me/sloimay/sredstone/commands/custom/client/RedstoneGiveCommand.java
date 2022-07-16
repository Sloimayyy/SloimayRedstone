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

/**
 * /redstonegive is used to quickly get redstone components using aliases for the command
 * itself, and also aliases for the redstone component names!
 * Such as /rg h which would give you a hopper.
 *
 * The way it does that is by first looking up which alias corresponds to which item ID,
 * then from this item ID get the ItemStack corresponding.
 */
public class RedstoneGiveCommand extends SClientCommand
{
    // ### Fields

    /**
     * The hashmap linking all aliases to item IDs.
     */
    private HashMap<String, String> redstoneComponentAliasToItemID = new HashMap<String, String>();

    /**
     * The hashmap linking all item IDs to item stacks.
     */
    private HashMap<String, ItemStack> itemIDToItemStack = new HashMap<String, ItemStack>();

    // ###



    // ### Init

    public RedstoneGiveCommand()
    {
        this.initRedstoneComponentAliasToItemID();
        this.initItemIDToItemStack();
    }

    // ###



    // ### Public methods

    /**
     * Registers the command.
     */
    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        // The main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("redstonegive")
                        .then(argument("redstone component alias", StringArgumentType.string())
                                .executes(context ->
                                {
                                    // Get the right redstone component ID, then put the redstone component
                                    // in the player's hand.
                                    String redstoneComponentAlias =
                                            StringArgumentType.getString(context, "redstone component alias");
                                    ItemStack redstoneComponentItem =
                                            this.itemIDToItemStack.get(
                                                    this.redstoneComponentAliasToItemID.get(redstoneComponentAlias)
                                            );
                                    SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, redstoneComponentItem);

                                    return 0;
                                })
                        )
        );

        // Aliases
        dispatcher.register(literal("rgive").redirect(mainNode));
        dispatcher.register(literal("rg").redirect(mainNode));
    }

    // ###



    // ### Private methods

    /**
     * Initializes the redstone component alias to item ID look up table.
     */
    private void initRedstoneComponentAliasToItemID()
    {
        this.redstoneComponentAliasToItemID.put("r", "redstone");
        this.redstoneComponentAliasToItemID.put("re", "redstone");
        this.redstoneComponentAliasToItemID.put("red", "redstone");
        this.redstoneComponentAliasToItemID.put("reds", "redstone");
        this.redstoneComponentAliasToItemID.put("redst", "redstone");
        this.redstoneComponentAliasToItemID.put("redsto", "redstone");
        this.redstoneComponentAliasToItemID.put("redston", "redstone");
        this.redstoneComponentAliasToItemID.put("redstone", "redstone");
        this.redstoneComponentAliasToItemID.put("redstone_", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("redstone_t", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("redstone_to", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("redstone_tor", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("redstone_torc", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("redstone_b", "redstone_block");
        this.redstoneComponentAliasToItemID.put("redstone_bl", "redstone_block");
        this.redstoneComponentAliasToItemID.put("redstone_blo", "redstone_block");
        this.redstoneComponentAliasToItemID.put("redstone_bloc", "redstone_block");
        this.redstoneComponentAliasToItemID.put("rep", "repeater");
        this.redstoneComponentAliasToItemID.put("repe", "repeater");
        this.redstoneComponentAliasToItemID.put("repea", "repeater");
        this.redstoneComponentAliasToItemID.put("repeat", "repeater");
        this.redstoneComponentAliasToItemID.put("repeate", "repeater");
        this.redstoneComponentAliasToItemID.put("repeater", "repeater");
        this.redstoneComponentAliasToItemID.put("c", "comparator");
        this.redstoneComponentAliasToItemID.put("co", "comparator");
        this.redstoneComponentAliasToItemID.put("com", "comparator");
        this.redstoneComponentAliasToItemID.put("comp", "comparator");
        this.redstoneComponentAliasToItemID.put("compa", "comparator");
        this.redstoneComponentAliasToItemID.put("compar", "comparator");
        this.redstoneComponentAliasToItemID.put("compara", "comparator");
        this.redstoneComponentAliasToItemID.put("comparat", "comparator");
        this.redstoneComponentAliasToItemID.put("comparato", "comparator");
        this.redstoneComponentAliasToItemID.put("comparator", "comparator");
        this.redstoneComponentAliasToItemID.put("p", "piston");
        this.redstoneComponentAliasToItemID.put("pi", "piston");
        this.redstoneComponentAliasToItemID.put("pis", "piston");
        this.redstoneComponentAliasToItemID.put("pist", "piston");
        this.redstoneComponentAliasToItemID.put("pisto", "piston");
        this.redstoneComponentAliasToItemID.put("piston", "piston");
        this.redstoneComponentAliasToItemID.put("s", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("st", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sti", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("stic", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("stick", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky_", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky_p", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky_pi", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky_pis", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky_pist", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky_pisto", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sticky_piston", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("h", "hopper");
        this.redstoneComponentAliasToItemID.put("ho", "hopper");
        this.redstoneComponentAliasToItemID.put("hop", "hopper");
        this.redstoneComponentAliasToItemID.put("hopp", "hopper");
        this.redstoneComponentAliasToItemID.put("hoppe", "hopper");
        this.redstoneComponentAliasToItemID.put("hopper", "hopper");
        this.redstoneComponentAliasToItemID.put("sl", "slime_block");
        this.redstoneComponentAliasToItemID.put("sli", "slime_block");
        this.redstoneComponentAliasToItemID.put("slim", "slime_block");
        this.redstoneComponentAliasToItemID.put("slime", "slime_block");
        this.redstoneComponentAliasToItemID.put("slime_", "slime_block");
        this.redstoneComponentAliasToItemID.put("slime_b", "slime_block");
        this.redstoneComponentAliasToItemID.put("slime_bl", "slime_block");
        this.redstoneComponentAliasToItemID.put("slime_blo", "slime_block");
        this.redstoneComponentAliasToItemID.put("slime_bloc", "slime_block");
        this.redstoneComponentAliasToItemID.put("slime_block", "slime_block");
        this.redstoneComponentAliasToItemID.put("hon", "honey_block");
        this.redstoneComponentAliasToItemID.put("hone", "honey_block");
        this.redstoneComponentAliasToItemID.put("honey", "honey_block");
        this.redstoneComponentAliasToItemID.put("honey_", "honey_block");
        this.redstoneComponentAliasToItemID.put("honey_b", "honey_block");
        this.redstoneComponentAliasToItemID.put("honey_bl", "honey_block");
        this.redstoneComponentAliasToItemID.put("honey_blo", "honey_block");
        this.redstoneComponentAliasToItemID.put("honey_bloc", "honey_block");
        this.redstoneComponentAliasToItemID.put("honey_block", "honey_block");
        this.redstoneComponentAliasToItemID.put("o", "observer");
        this.redstoneComponentAliasToItemID.put("ob", "observer");
        this.redstoneComponentAliasToItemID.put("obs", "observer");
        this.redstoneComponentAliasToItemID.put("obse", "observer");
        this.redstoneComponentAliasToItemID.put("obser", "observer");
        this.redstoneComponentAliasToItemID.put("observ", "observer");
        this.redstoneComponentAliasToItemID.put("observe", "observer");
        this.redstoneComponentAliasToItemID.put("observer", "observer");
        this.redstoneComponentAliasToItemID.put("d", "dispenser");
        this.redstoneComponentAliasToItemID.put("di", "dispenser");
        this.redstoneComponentAliasToItemID.put("dis", "dispenser");
        this.redstoneComponentAliasToItemID.put("disp", "dispenser");
        this.redstoneComponentAliasToItemID.put("dispe", "dispenser");
        this.redstoneComponentAliasToItemID.put("dispen", "dispenser");
        this.redstoneComponentAliasToItemID.put("dispens", "dispenser");
        this.redstoneComponentAliasToItemID.put("dispense", "dispenser");
        this.redstoneComponentAliasToItemID.put("dispenser", "dispenser");
        this.redstoneComponentAliasToItemID.put("dr", "dropper");
        this.redstoneComponentAliasToItemID.put("dro", "dropper");
        this.redstoneComponentAliasToItemID.put("drop", "dropper");
        this.redstoneComponentAliasToItemID.put("dropp", "dropper");
        this.redstoneComponentAliasToItemID.put("droppe", "dropper");
        this.redstoneComponentAliasToItemID.put("dropper", "dropper");
        this.redstoneComponentAliasToItemID.put("l", "lever");
        this.redstoneComponentAliasToItemID.put("le", "lever");
        this.redstoneComponentAliasToItemID.put("lev", "lever");
        this.redstoneComponentAliasToItemID.put("leve", "lever");
        this.redstoneComponentAliasToItemID.put("lever", "lever");
        this.redstoneComponentAliasToItemID.put("lec", "lectern");
        this.redstoneComponentAliasToItemID.put("lect", "lectern");
        this.redstoneComponentAliasToItemID.put("lecte", "lectern");
        this.redstoneComponentAliasToItemID.put("lecter", "lectern");
        this.redstoneComponentAliasToItemID.put("lectern", "lectern");
        this.redstoneComponentAliasToItemID.put("t", "target");
        this.redstoneComponentAliasToItemID.put("ta", "target");
        this.redstoneComponentAliasToItemID.put("tar", "target");
        this.redstoneComponentAliasToItemID.put("targ", "target");
        this.redstoneComponentAliasToItemID.put("targe", "target");
        this.redstoneComponentAliasToItemID.put("target", "target");
        this.redstoneComponentAliasToItemID.put("tr", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tri", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("trip", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripw", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwi", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwir", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwire", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwire_", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwire_h", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwire_ho", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwire_hoo", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tripwire_hook", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("tn", "tnt");
        this.redstoneComponentAliasToItemID.put("tnt", "tnt");
        this.redstoneComponentAliasToItemID.put("redstone_l", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("redstone_la", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("redstone_lam", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("redstone_lamp", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("n", "note_block");
        this.redstoneComponentAliasToItemID.put("no", "note_block");
        this.redstoneComponentAliasToItemID.put("not", "note_block");
        this.redstoneComponentAliasToItemID.put("note", "note_block");
        this.redstoneComponentAliasToItemID.put("note_", "note_block");
        this.redstoneComponentAliasToItemID.put("note_b", "note_block");
        this.redstoneComponentAliasToItemID.put("note_bl", "note_block");
        this.redstoneComponentAliasToItemID.put("note_blo", "note_block");
        this.redstoneComponentAliasToItemID.put("note_bloc", "note_block");
        this.redstoneComponentAliasToItemID.put("note_block", "note_block");
        this.redstoneComponentAliasToItemID.put("sto", "stone_button");
        this.redstoneComponentAliasToItemID.put("ston", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone_", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone_b", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone_bu", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone_but", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone_butt", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone_butto", "stone_button");
        this.redstoneComponentAliasToItemID.put("stone_button", "stone_button");
        this.redstoneComponentAliasToItemID.put("rt", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("to", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("tor", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("torc", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("torch", "redstone_torch");
        this.redstoneComponentAliasToItemID.put("rb", "redstone_block");
        this.redstoneComponentAliasToItemID.put("b", "redstone_block");
        this.redstoneComponentAliasToItemID.put("bl", "redstone_block");
        this.redstoneComponentAliasToItemID.put("blo", "redstone_block");
        this.redstoneComponentAliasToItemID.put("bloc", "redstone_block");
        this.redstoneComponentAliasToItemID.put("block", "redstone_block");
        this.redstoneComponentAliasToItemID.put("sp", "sticky_piston");
        this.redstoneComponentAliasToItemID.put("sb", "slime_block");
        this.redstoneComponentAliasToItemID.put("hb", "honey_block");
        this.redstoneComponentAliasToItemID.put("th", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("hoo", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("hook", "tripwire_hook");
        this.redstoneComponentAliasToItemID.put("rl", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("la", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("lam", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("lamp", "redstone_lamp");
        this.redstoneComponentAliasToItemID.put("nb", "note_block");
        this.redstoneComponentAliasToItemID.put("bu", "stone_button");
        this.redstoneComponentAliasToItemID.put("but", "stone_button");
        this.redstoneComponentAliasToItemID.put("butt", "stone_button");
        this.redstoneComponentAliasToItemID.put("butto", "stone_button");
        this.redstoneComponentAliasToItemID.put("button", "stone_button");
    }

    /**
     * Initializes the itemIDToItemStack look up table.
     */
    private void initItemIDToItemStack()
    {
        this.itemIDToItemStack.put("redstone", new ItemStack(Items.REDSTONE, 1));
        this.itemIDToItemStack.put("redstone_torch", new ItemStack(Items.REDSTONE_TORCH, 1));
        this.itemIDToItemStack.put("redstone_block", new ItemStack(Items.REDSTONE_BLOCK, 1));
        this.itemIDToItemStack.put("repeater", new ItemStack(Items.REPEATER, 1));
        this.itemIDToItemStack.put("comparator", new ItemStack(Items.COMPARATOR, 1));
        this.itemIDToItemStack.put("piston", new ItemStack(Items.PISTON, 1));
        this.itemIDToItemStack.put("sticky_piston", new ItemStack(Items.STICKY_PISTON, 1));
        this.itemIDToItemStack.put("hopper", new ItemStack(Items.HOPPER, 1));
        this.itemIDToItemStack.put("slime_block", new ItemStack(Items.SLIME_BLOCK, 1));
        this.itemIDToItemStack.put("honey_block", new ItemStack(Items.HONEY_BLOCK, 1));
        this.itemIDToItemStack.put("observer", new ItemStack(Items.OBSERVER, 1));
        this.itemIDToItemStack.put("dispenser", new ItemStack(Items.DISPENSER, 1));
        this.itemIDToItemStack.put("dropper", new ItemStack(Items.DROPPER, 1));
        this.itemIDToItemStack.put("lectern", new ItemStack(Items.LECTERN, 1));
        this.itemIDToItemStack.put("target", new ItemStack(Items.TARGET, 1));
        this.itemIDToItemStack.put("lever", new ItemStack(Items.LEVER, 1));
        this.itemIDToItemStack.put("tripwire_hook", new ItemStack(Items.TRIPWIRE_HOOK, 1));
        this.itemIDToItemStack.put("tnt", new ItemStack(Items.TNT, 1));
        this.itemIDToItemStack.put("redstone_lamp", new ItemStack(Items.REDSTONE_LAMP, 1));
        this.itemIDToItemStack.put("note_block", new ItemStack(Items.NOTE_BLOCK, 1));
        this.itemIDToItemStack.put("stone_button", new ItemStack(Items.STONE_BUTTON, 1));
    }

    // ###
}
