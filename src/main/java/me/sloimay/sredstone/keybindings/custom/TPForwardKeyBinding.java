package me.sloimay.sredstone.keybindings.custom;

import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.keybindings.SKeyBinding;
import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.SpectatorTeleportC2SPacket;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/**
 * Moves forward the WE selection.
 */
public class TPForwardKeyBinding extends SKeyBinding
{

    // ### Fields

    // ###



    // ### Init
    public TPForwardKeyBinding()
    {
        super(
                "key.sredstone.cool_tp",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_6,
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


        // ## Get the place the player is looking at
        HitResult hitResult = client.player.raycast(ClientDB.coolTPMaxDist, 1.0f, false);

        // ## Tp the player where they're looking at
        if (hitResult.getPos() == null) return;

        // # Processing the pos
        Vec3d tpPos = hitResult.getPos();
        // Some math so that the position doesn't end up in the block the ray touched, but right before
        tpPos = tpPos.add(client.player.getRotationVecClient().multiply(-0.00001));
        // Don't tp the player below -5 y under the lowest Y, so that they don't die in the void
        if (tpPos.y < client.player.getEntityWorld().getBottomY() - 5)
            tpPos = new Vec3d(tpPos.x, client.player.getEntityWorld().getBottomY() - 5, tpPos.z);
        // We want the TP Pos to be in the middle of the aimed position
        tpPos = SFabricLib.VectorUtils.floorVector(tpPos).add(0.5, 0.0, 0.5);

        // If the block above the TP spot is not air then
        // y -= -1 because otherwise, the head of player will be in a block.
        // We keep the same y otherwise, so that if both blocks aren't air, we automatically crawl
        BlockState blockUnderTPPos = client.player.getEntityWorld().getBlockState(
                SFabricLib.VectorUtils.vec3dToBlockPos(
                        new Vec3d(tpPos.x, tpPos.y - 1, tpPos.z)
                )
        );
        BlockState blockOverTPPos = client.player.getEntityWorld().getBlockState(
                SFabricLib.VectorUtils.vec3dToBlockPos(
                        new Vec3d(tpPos.x, tpPos.y + 1, tpPos.z)
                )
        );
        if (!blockOverTPPos.isAir() && blockUnderTPPos.isAir())
        {
            tpPos = new Vec3d(tpPos.x, tpPos.y - 1, tpPos.z);
        }


        // ## TP, handled differently depending if in singleplayer or multiplayer
        // ## Singleplayer : Access the world and tp
        // ## Multiplayer  : Send a /tp command
        if (client.isInSingleplayer())
        {
            SFabricLib.PlayerUtils.tpPlayerTo(client, tpPos);
        }
        else
        {
            // # On a server
            // Gen the tp command
            String tpCommand = "/tp " + client.player.getEntityName() + " " + SFabricLib.VectorUtils.vec3dToMinecraftVectorString(tpPos);
            // Dispatch the command
            client.player.sendChatMessage(tpCommand);
        }


        // ## Some effects
        // Whoosh! thingy in the actionbar
        client.inGameHud.setOverlayMessage(new LiteralText("Whoosh!"), false);
        // Teleport sound effect
        client.player.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, Integer.MAX_VALUE, 1);
    }
    // ###
}
