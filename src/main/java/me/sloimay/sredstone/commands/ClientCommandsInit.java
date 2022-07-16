package me.sloimay.sredstone.commands;

import me.sloimay.sredstone.commands.custom.client.*;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Initializes client-side commands!
 */
public class ClientCommandsInit
{
    // ### Fields

    // ###



    // ### Init

    public ClientCommandsInit()
    {

    }

    // ###



    // ### Public methods

    public void initCommands()
    {
        // ## First create all the command objects and put them in the list
        List<SClientCommand> clientCommands = new ArrayList<>();
        clientCommands.add(new BarrelSSCommand());
        clientCommands.add(new GetWoolCommand());
        clientCommands.add(new UpdateRedstoneCommand());
        clientCommands.add(new QuickStackCommand());
        clientCommands.add(new ForceColorCodeCommand());
        clientCommands.add(new RedstoneGiveCommand());
        clientCommands.add(new CopyFlipPasteCommand());
        clientCommands.add(new RedstoneNetworkCommand());


        // ## Register each command
        clientCommands.forEach(command -> command.register(ClientCommandManager.DISPATCHER));
    }

    // ###
}
