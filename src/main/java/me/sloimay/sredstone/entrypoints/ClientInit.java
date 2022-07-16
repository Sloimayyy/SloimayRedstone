package me.sloimay.sredstone.entrypoints;

import ca.weblite.objc.Client;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.sloimay.sredstone.commands.ClientCommandsInit;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.db.Db;
import me.sloimay.sredstone.db.StringDB;
import me.sloimay.sredstone.tickers.ClientTicker;
import me.sloimay.sredstone.tickers.ClientTickersInit;
import me.sloimay.sredstone.utils.SFabricLib;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class ClientInit implements ClientModInitializer
{

    // ### Init
    @Override
    public void onInitializeClient()
    {
        // Init client commands
        ClientCommandsInit clientCommandsInit = new ClientCommandsInit();
        clientCommandsInit.initCommands();
        //

        // Init tickers
        ClientTickersInit clientTickersInit = new ClientTickersInit();
        clientTickersInit.initTickers();
        //

        CommandRegistrationCallback.EVENT.register((dispatcher, integrated) -> {

            /*// ## BlockToBin command
            dispatcher.register(
                literal("blocktobin")
                // MAIN COMMAND!
                .then(argument("start coords", BlockPosArgumentType.blockPos())
                    .then(argument("end coords", BlockPosArgumentType.blockPos())
                        .then(argument("bit 0 block", BlockStateArgumentType.blockState())
                            .then(argument("bit 1 block", BlockStateArgumentType.blockState())
                                .then(argument("bit count", IntegerArgumentType.integer())
                                    .then(argument("reversed", BoolArgumentType.bool())
                                        .then(argument("is 2s complement", BoolArgumentType.bool())
                                            .executes(context -> {
                                                blockToBinCommand(
                                                        context,
                                                        BlockPosArgumentType.getBlockPos(context, "start coords"),
                                                        BlockPosArgumentType.getBlockPos(context, "end coords"),
                                                        BlockStateArgumentType.getBlockState(context, "bit 0 block"),
                                                        BlockStateArgumentType.getBlockState(context, "bit 1 block"),
                                                        IntegerArgumentType.getInteger(context, "bit count"),
                                                        BoolArgumentType.getBool(context, "reversed"),
                                                        BoolArgumentType.getBool(context, "is 2s complement")
                                                );


                                                return 0;
                                            })
                                        )
                                    )
                                )
                            )
                        )
                        // PRESETS!
                        .then(literal("preset")
                            .then(argument("preset name", StringArgumentType.string())
                                .executes(context -> {
                                    String presetName = StringArgumentType.getString(context, "preset name");

                                    // If the preset is empty, then we return in chat how presets work
                                    if (presetName.equals("help"))
                                    {
                                        context.getSource().getPlayer().sendSystemMessage(
                                                new LiteralText(
                                                        StringDB.Logging.prefix + StringDB.Commands.blockToBinPresetHelper
                                                ),
                                                Util.NIL_UUID
                                        );

                                        return 0;
                                    }

                                    // # Setup the variables that are gonna change
                                    // # while checking for which preset is selected
                                    BlockPos startCoords = BlockPosArgumentType.getBlockPos(context, "start coords");
                                    BlockPos endCoords = BlockPosArgumentType.getBlockPos(context, "end coords");
                                    BlockState bit0State = null;
                                    BlockState bit1State = null;
                                    int bitCount = -1;
                                    boolean reversed = false;
                                    boolean is2sComplement = false;

                                    // Parse the preset
                                    String[] parsedPreset = parseBlockToBinPreset(presetName);
                                    String blockGroupName = parsedPreset[0];
                                    String bitCountStr = parsedPreset[1];
                                    String options = parsedPreset[2];

                                    // Change the blocks based on the block name in the preset
                                    if (blockGroupName.equals("redstonelamps"))
                                    {
                                        bit0State = Blocks.REDSTONE_LAMP.getStateManager().getStates().get(1);
                                        bit1State = Blocks.REDSTONE_LAMP.getStateManager().getStates().get(0);
                                    }
                                    else if (blockGroupName.equals("wools"))
                                    {
                                        bit0State = Blocks.BLACK_WOOL.getStateManager().getDefaultState();
                                        bit1State = Blocks.WHITE_WOOL.getStateManager().getDefaultState();
                                    }

                                    // Change the bit count based on the bitcount in the preset
                                    bitCount = Integer.parseInt(bitCountStr);

                                    System.out.println("aaaaaaaaaaa");

                                    System.out.println(bit0State.toString());
                                    System.out.println(bit1State.toString());

                                    System.out.println("aaaaaaaaaaa");

                                    // Parse the options
                                    if (options.contains("2c"))
                                    {
                                        is2sComplement = true;
                                    }
                                    if (options.contains("rev"))
                                    {
                                        reversed = true;
                                    }


                                    // At the end, call the command
                                    blockToBinCommand(
                                            context,
                                            startCoords,
                                            endCoords,
                                            bit0State,
                                            bit1State,
                                            bitCount,
                                            reversed,
                                            is2sComplement
                                    );

                                    return 0;
                                })
                            )
                        )
                    )
                )
            );





            // /b Command!
            dispatcher.register(
                literal("b")
                .then(argument("desired signal strength", IntegerArgumentType.integer())
                    .executes(context -> {
                        int ss = IntegerArgumentType.getInteger(context, "desired signal strength");

                        SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, Db.ssBarrels.get(ss));

                        return 0;
                    })
                )
            );





            // /wo or /wool command!
            dispatcher.register(
                literal("wo")
                .then(argument("wool color", StringArgumentType.string())
                    .executes(context -> {
                        String woolIdOrAlias = StringArgumentType.getString(context, "wool color");

                        ItemStack woolChosen = Db.woolItemIDToWoolItemStack.get(Db.woolAliasToWoolItemID.get(woolIdOrAlias));
                        SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, woolChosen);

                        return 0;
                    })
                )
            );





            // /forcecolorcode command!
            dispatcher.register(
                literal("forcecolorcode")
                .then(argument("wool color", StringArgumentType.string())
                    .executes(context -> {
                        String chosenCol = StringArgumentType.getString(context, "wool color");

                        forceColorCodeCommand(chosenCol, "");

                        return 0;
                    })

                    .then(argument("blocks to not replace", StringArgumentType.greedyString())
                        .executes(context -> {
                            String chosenCol = StringArgumentType.getString(context, "wool color");
                            String exceptions = StringArgumentType.getString(context, "blocks to not replace");

                            forceColorCodeCommand(chosenCol, exceptions);

                            return 0;
                        })
                    )
                )
            );*/

            // ### /bintoblocks <start block>





            /*// ### /s command! Which is //stack but you can only do //stack <number> <direction> thats it
            dispatcher.register(
                literal("s")
                .then(argument("stack basic args", StringArgumentType.string())
                    .executes(context -> {
                        String arg = StringArgumentType.getString(context, "stack basic args");

                        sCommand(arg);

                        return 0;
                    })
            ));*/





            // ### /updateredstone command!
            /*dispatcher.register(
                literal("updateredstone")
                .executes(context -> {
                    // dispatch 2 commands to update redstone
                    // And then do the undoing yourself, because the mod can't currently know how many
                    // of the 2 commands have been successful, this can't know how many times to undo
                    ClientDB.mcClient.player.sendChatMessage("//replace <comparator,repeater,redstone_wire stone");
                    ClientDB.mcClient.player.sendChatMessage("//replace barrel stone");

                    return 0;
                })
            );*/




        });
    }


    /**
     * The /s command turns "/s 1u" into "//stack 1 u"
     *
     * @param arg
     */
    public void sCommand(
            String arg
    )
    {
        // Parse the number by going through the args 1 char at a time
        // and stopping when the char isn't a number anymore
        String num = "";
        for (int i = 0; i < arg.length(); i++)
        {
            char c = arg.charAt(i);
            if ("0123456789".indexOf(c) == -1)
            {
                // As soon as we don't find a number anymore, we know we've hit the end of it
                num = arg.substring(0, i);
            }
        }

        // Get the direction of the stack by getting the remaining part of the arg
        String dir = arg.substring(num.length());

        // Create and dispatch the new command
        String cmd = "//stack " + num + " " + dir;
        ClientDB.mcClient.player.sendChatMessage(cmd);
    }





    public void forceColorCodeCommand(
            String chosenCol,
            String exceptions
    )
    {
        String chosenBlockId = chosenCol + "_wool";
        String chosenTransparentId = chosenCol + "_stained_glass";
        String[] exceptionBlocks = exceptions.replaceAll(" ", "").split(",");

        // The list of solid color coding blocks that we are going to replace
        // minus the exception blocks
        // We are building those lists so that we can join them with worldedit to dispatch
        // only one command, instead of 16 or something. As we can do //replace <block1>,<block2>, etc
        // instead of doing //replace <block1> and then //replace <block2>
        List<String> solidColorCodingBlocksToReplace = new ArrayList<String>();
        // Same but with transparent blocks
        List<String> transparentColorCodingBlocksToReplace = new ArrayList<String>();

        // Build the list of blocks that are gonna get replaced
        // Solid blocks first
        Db.commonSolidColorCodingBlockIds.forEach(id ->
        {
            // See if the choosen block is not in the exceptions
            if (Arrays.stream(exceptionBlocks).anyMatch(exception -> id.equals(exception)))
                return;

            solidColorCodingBlocksToReplace.add(id);

        });
        // Transparent blocks after
        Db.commonTransparentColorCodingBlockIds.forEach(id ->
        {
            // See if the choosen block is not in the exceptions
            if (Arrays.stream(exceptionBlocks).anyMatch(exception -> id.equals(exception)))
                return;

            transparentColorCodingBlocksToReplace.add(id);
        });

        // Now we generate the commands to dispatch, can't go over 256 characters
        List<String> replacingSolidBlocksCommands = buildReplaceCommands(solidColorCodingBlocksToReplace, chosenBlockId);
        List<String> replacingTransparentBlocksCommands = buildReplaceCommands(transparentColorCodingBlocksToReplace, chosenTransparentId);

        // Dispatch the commands
        replacingSolidBlocksCommands.forEach(command -> {ClientDB.mcClient.player.sendChatMessage(command); System.out.println(command);});
        replacingTransparentBlocksCommands.forEach(command -> {ClientDB.mcClient.player.sendChatMessage(command); System.out.println(command);});



        /* CODE IF THE COMMANDS COULD NOT HAVE A CHAR LIMIT (keeping it in case I find a way to dispatch
         * commands of any length
        // Build the commands
        String replaceSolidCmd = "//replace " + String.join(",", solidColorCodingBlocksToReplace) + " " + chosenBlockId;
        String replaceTransparentCmd = "//replace " + String.join(",", transparentColorCodingBlocksToReplace) + " " + chosenTransparentId;
        // And now dispatch them
        ClientDB.mcClient.player.sendChatMessage(replaceSolidCmd);
        ClientDB.mcClient.player.sendChatMessage(replaceTransparentCmd);
        */
    }
    /**
     * Command associated with /forcecolorcode
     */
    public String getReplaceCommand(
            List<String> blocksToReplace,
            String replacingBlock
    )
    {
        return "//replace " + String.join(",", blocksToReplace) + " " + replacingBlock;
    }
    /**
     * Build the replace commands. Basically producing under 256 character limits commands to most efficiently
     * dispatch the replace commands
     */
    public List<String> buildReplaceCommands(
            List<String> blocksToReplace,
            String replaceBlockId
    )
    {
        // Setup
        List<String> commandsOut = new ArrayList<String>();
        int fromIndex = 0;
        int toIndex = 1;

        while(toIndex <= blocksToReplace.size())
        {
            // Build the command with the current fromIndex and toIndex to see if it would fit the 256 chars limit
            String command = getReplaceCommand(blocksToReplace.subList(fromIndex, Math.min(toIndex, blocksToReplace.size())), replaceBlockId);
            String commandNext = getReplaceCommand(blocksToReplace.subList(fromIndex, Math.min(toIndex + 1, blocksToReplace.size())), replaceBlockId);

            // The current command is good to be dispatched, as it is the maximum length it can be until
            // it goes over 256. This if can't happen if toIndex == blocksToReplace.size(), as if it were, both
            // command and commandNext would be the same length
            if (command.length() <= 256 && commandNext.length() > 256)
            {
                commandsOut.add(command);
                fromIndex = toIndex;
            }

            // Checking if we've hit the end of the list, if we did, it's either fromIndex is size() - 1 or size() - n where n > 1
            // If size() - 1 we can just dispatch the command as one element can't go over the limit of the command char count
            // If size() - n we also know the command can't go over the limit, as we checked in the previous iteration. If the command
            //               was able to go over the limit, then fromIndex would be size() - 1.
            // So in both cases we just dispatch the command. We could fuse both the first if statement and the second one, but
            // I'm keeping them both separate for clarity
            if (Math.min(toIndex, blocksToReplace.size()) == Math.min(toIndex + 1, blocksToReplace.size()))
            {
                commandsOut.add(command);
            }

            toIndex++;
        }

        return commandsOut;
    }





    public void blockToBinCommand(
            CommandContext<ServerCommandSource> context,
            BlockPos startCoords,
            BlockPos endCoords,
            BlockStateArgument bit0BlockArg,
            BlockStateArgument bit1BlockArg,
            int bitCount,
            boolean reversed,
            boolean is2sComplement
    ) throws CommandSyntaxException {
        // ## Setup
        BlockState bit0BlockState = bit0BlockArg.getBlockState();
        BlockState bit1BlockState = bit1BlockArg.getBlockState();

        blockToBinCommand(
                context,
                startCoords,
                endCoords,
                bit0BlockState,
                bit1BlockState,
                bitCount,
                reversed,
                is2sComplement
        );
    }

    public void blockToBinCommand(
            CommandContext<ServerCommandSource> context,
            BlockPos startCoords,
            BlockPos endCoords,
            BlockState bit0BlockState,
            BlockState bit1BlockState,
            int bitCount,
            boolean reversed,
            boolean is2sComplement
    ) throws CommandSyntaxException {
        // ## Setup
        long out = 0;
        long minusOne = -1;
        long one = 1;
        // # Calculate the dv vector between each bit
        BlockPos startToEndVec = endCoords.subtract(startCoords);
        Vec3d startToEndVec3d = SFabricLib.VectorUtils.blockPosToVec3d(startToEndVec);
        Vec3d dv = startToEndVec3d.multiply(1d / (bitCount - 1));

        System.out.println(dv);
        System.out.println(bit0BlockState);
        System.out.println(bit1BlockState);

        // # Setup start coords and dv
        // they're gonna be processed by "reversed", which will change
        // the start coords and direction if true
        if (reversed)
        {
            // Switch the end coords and start coords around
            BlockPos temp = startCoords;
            startCoords = endCoords;
            endCoords = temp;
            // start coords processed now points to the other side of the num in mc

            // Need to reverse dv
            dv = dv.multiply(-1);
        }

        // Setup a ray that is gonna start marching
        Vec3d ray = SFabricLib.VectorUtils.vec3iToVec3d(startCoords.mutableCopy());


        longFillingLoop:
        for (int i = 0; i < bitCount; i++)
        {
            // If the index of the long we're iterating is less than the last bit
            // pointer of the number in mc, then we havent reached the sign bit yet, thus
            // the correct bits must be set to the long

            // Get the blockstate where the ray is, for future use
            // The ray may be in an inexact position, so we snap it to a block pos
            BlockPos rayBlockPos = SFabricLib.VectorUtils.vec3dToBlockPos(ray);
            BlockState blockStateAtRay = context.getSource().getWorld().getBlockState(rayBlockPos);
            System.out.println(blockStateAtRay.toString());
            // See if the current block is block representing bit 1
            boolean isBit1 =
                    SFabricLib.BlockUtils.BlockStateUtils.isSameBlockState(
                            blockStateAtRay, bit1BlockState
                    );

            // The bit is 0 if the bit isn't 1.
            // we don't check if we have the correct block, because any absence of
            // block is also considered as a bit being 0
            // We're also using 2 booleans so that it's easier organisation-wise
            // if i want a different behavior if the block isn't present.
            boolean isBit0 = !isBit1;


            // Executes if we're not at the last iteration
            if (i < bitCount - 1)
            {
                // Setting the bit if the bit is 1 (since the default is all 0s)
                if (isBit1)
                {
                    out = out | (one << i);
                }
            }
            // Executes at the last iteration
            else if (i == bitCount - 1)
            {
                // We reached the possible sign bit, if we are not using
                // 2s complement, then proceed like the previous if statement
                // otherwise store the sign bit to the possible sign bit variable
                // and apply it to every bits left in the long

                if (!is2sComplement)
                {
                    if (isBit1) out = out | (one << i);
                }
                else
                {
                    // 2s complement

                    // Don't need any logic if the bit is 0, because
                    // all the bits in the out long are already 0

                    if (isBit1)
                    {
                        // But if the sign bit is bit 1, then we need to set the remaining bits
                        // of the out long to be 1 (since default is 0)
                        out = out | (minusOne << i);
                    }
                }
            }

            // Make the ray march at the end if we haven't reached the end of the
            // bin num in mc
            ray = ray.add(dv);
        }



        // The total has been calculated, so now lets display it
        context.getSource().getPlayer().sendSystemMessage(
                new LiteralText(
                        StringDB.Logging.prefix + "The binary number reads : " + out
                ),
                Util.NIL_UUID
        );
    }

    /**
     * Parses a blocktobin preset that is represented like this:
     * <block>_<bitCount>_<options>
     *
     * @param preset
     */
    public String[] parseBlockToBinPreset(String preset)
    {
        // The returned string array has 3 slots:
        // 0 is the block
        // 1 is the bitcount in string form
        // 2 are the options
        String[] out = new String[3];


        // Get the 2 _ positions
        int firstUnderscorePos = -1;
        int secondUnderscorePos = -1;
        for (int i = 0; i < preset.length(); i++)
        {
            char c = preset.charAt(i);
            if (firstUnderscorePos == -1)
            {
                if (c == '_') firstUnderscorePos = i;
            }
            else if (secondUnderscorePos == -1)
            {
                if (c == '_') secondUnderscorePos = i;
            }
        }

        // The block is the substr before the first _
        out[0] = preset.substring(0, firstUnderscorePos);

        // The bitcount is the substr after the first _ and
        // before the second _
        out[1] = preset.substring(firstUnderscorePos + 1, secondUnderscorePos);

        // The options are the substr after the second _ to the end
        out[2] = preset.substring(secondUnderscorePos + 1, preset.length());


        return out;
    }
}
