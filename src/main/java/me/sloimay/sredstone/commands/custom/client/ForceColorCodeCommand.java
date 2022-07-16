package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.db.Db;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * /forcecolorcode command, which forces your build in your worldedit selection
 * to only be one color.
 */
public class ForceColorCodeCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public ForceColorCodeCommand()
    {

    }

    // ###



    // ### Public methods

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        // Main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("forcecolorcode")
                        .then(argument("wool color", StringArgumentType.string())
                                .executes(context ->
                                {
                                    // Force color code a build without exception blocks

                                    String chosenCol = StringArgumentType.getString(context, "wool color");
                                    this.forceColorCode(chosenCol, "");

                                    return 0;
                                })

                                .then(argument("blocks to not replace", StringArgumentType.greedyString())
                                        .executes(context ->
                                        {
                                            // Force color code a build with exception blocks

                                            String chosenCol =
                                                    StringArgumentType.getString(context, "wool color");
                                            String exceptions =
                                                    StringArgumentType.getString(context, "blocks to not replace");
                                            this.forceColorCode(chosenCol, exceptions);

                                            return 0;
                                        })
                                )
                        )
        );

        // Aliases
        dispatcher.register(literal("cc").redirect(mainNode));
        dispatcher.register(literal("f").redirect(mainNode));
    }

    // ###



    // ### Private methods

    /**
     * The main method used in the command
     * Sends the right commands to force color code your selected build.
     */
    private void forceColorCode
    (
            String chosenCol,
            String exceptions
    )
    {
        // ### Setup
        String chosenBlockId = chosenCol + "_wool";
        String chosenTransparentId = chosenCol + "_stained_glass";
        String[] exceptionBlocks = exceptions.replaceAll(" ", "").split(",");


        // ## The list of solid color coding blocks that we are going to replace
        // ## minus the exception blocks
        // We are building those lists so that we can join them with worldedit to dispatch
        // only one command, instead of 16 or something. As we can do //replace <block1>,<block2>, etc
        // instead of doing //replace <block1> and then //replace <block2>
        List<String> solidColorCodingBlocksToReplace = new ArrayList<String>();
        // Same but with transparent blocks
        List<String> transparentColorCodingBlocksToReplace = new ArrayList<String>();


        // ## Build the list of blocks that are gonna get replaced
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

        // ### Now we generate the commands to dispatch, can't go over 256 characters
        List<String> replacingSolidBlocksCommands = buildReplaceCommands(solidColorCodingBlocksToReplace, chosenBlockId);
        List<String> replacingTransparentBlocksCommands = buildReplaceCommands(transparentColorCodingBlocksToReplace, chosenTransparentId);

        // ### Dispatch the commands
        replacingSolidBlocksCommands.forEach(command -> {
            ClientDB.mcClient.player.sendChatMessage(command); System.out.println(command);});
        replacingTransparentBlocksCommands.forEach(command -> {ClientDB.mcClient.player.sendChatMessage(command); System.out.println(command);});


    }

    /**
     * Builds a //replace command with the inputted blocks to replace
     * and the block that will replace them.
     */
    public String getReplaceCommand
    (
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
        // ## Setup
        List<String> commandsOut = new ArrayList<String>();
        int fromIndex = 0;
        int toIndex = 1;

        // ## We loopin bois and grills
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

    // ###
}
