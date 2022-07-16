package me.sloimay.sredstone.commands.custom.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.sloimay.sredstone.commands.SClientCommand;
import me.sloimay.sredstone.db.ClientDB;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

/**
 * /qs command which stands for "/quickstack" but wouldn't be quick if we
 * had to write it out LOL
 */
public class QuickStackCommand extends SClientCommand
{
    // ### Fields

    // ###



    // ### Init

    public QuickStackCommand()
    {

    }

    // ###



    // ### Public methods

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher)
    {
        // Main command
        LiteralCommandNode mainNode = dispatcher.register(
                literal("qs")
                        .then(argument("//stack basic args", StringArgumentType.string())
                                .executes(context ->
                                {
                                    // Get the argument
                                    String arg = StringArgumentType.getString(context, "//stack basic args");

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

                                    return 0;
                                })
                        )
        );

        // Aliases
        dispatcher.register(literal("s").redirect(mainNode));
    }

    // ###
}
