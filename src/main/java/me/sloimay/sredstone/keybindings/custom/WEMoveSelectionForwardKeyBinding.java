package me.sloimay.sredstone.keybindings.custom;

//import com.sk89q.worldedit.WorldEdit;
import me.sloimay.sredstone.keybindings.SKeyBinding;
import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Moves forward the WE selection.
 */
public class WEMoveSelectionForwardKeyBinding extends SKeyBinding
{

    // ### Fields

    // ###



    // ### Init
    public WEMoveSelectionForwardKeyBinding()
    {
        super(
                "key.sredstone.move_forwards",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_3,
                "key.category.sredstone"
        );
    }
    // ###



    // ### Public methods
    @Override
    public void execute(MinecraftClient client)
    {
        // ## Setup
        // Keybind only works in creative
        if(!client.player.getAbilities().creativeMode) return;

        // Dispatch cmd
        char worldEditDirection = SFabricLib.PlayerUtils.getPlayerWEMainDirection(client.player, false);
        System.out.println(worldEditDirection);
        client.player.sendChatMessage("//move " + worldEditDirection + " -s");


        /* ========== OLD IMPLEMENTATION
        // ## Generate the commands
        // # Setup
        float playerYaw = SFabricLib.PlayerUtils.getClientPlayerYaw(client.player);
        float playerPitch = client.player.getPitch();

        String moveCmd = "//move ";
        String expandCmd = "//expand 1 ";
        String contractCmd = "//contract 1 ";

        // # Generating the dir character to add to the commands
        // # Either we move up or down, or horizontally, but U and D have priority.
        String dir = null;
        if (playerPitch < -69 || playerPitch > 69)
        {
            // We're in the range for pitch to take priority (looking up or down)
            dir = playerPitch < -69 ? "u" : "d";
        }
        else
        {
            // We're looking mostly horizontally, so we aren't looking up or down
            dir =   playerYaw > -135 && playerYaw <= -45 ? "e" :
                    playerYaw > -45  && playerYaw <=  45 ? "s" :
                    playerYaw > 45   && playerYaw <= 135 ? "w" :
                                                           "n";
        }
        // Adding the computed dir to the commands
        moveCmd += dir; expandCmd += dir; contractCmd += dir;


        // ## Command dispatching
        client.player.sendChatMessage(moveCmd);
        client.player.sendChatMessage(expandCmd);
        client.player.sendChatMessage(contractCmd);
         */
    }
    // ###
}
