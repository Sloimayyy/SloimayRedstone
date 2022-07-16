package me.sloimay.sredstone.utils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import me.sloimay.sredstone.db.ClientDB;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;

import java.util.HashMap;

public class SFabricLib
{
    private SFabricLib() {}


    public static class VectorUtils
    {
        /**
         * Converts a BlockPos object into a Vec3d object.
         *
         * @param blockPos
         * @return
         */
        public static Vec3d blockPosToVec3d(BlockPos blockPos)
        {
            return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        }

        /**
         * Converts a Vec3d object into a BlockPos object.
         *
         * @param vec3d
         * @return
         */
        public static BlockPos vec3dToBlockPos(Vec3d vec3d)
        {
            return new BlockPos(
                    MathHelper.floor(vec3d.x),
                    MathHelper.floor(vec3d.y),
                    MathHelper.floor(vec3d.z)
            );
        }

        /**
         * Converts a Vec3i object into a Vec3d object.
         *
         * @param vec3i
         * @return
         */
        public static Vec3d vec3iToVec3d(Vec3i vec3i)
        {
            return new Vec3d(
                    vec3i.getX(),
                    vec3i.getY(),
                    vec3i.getZ()
            );
        }

        /**
         * Converts a vec3d to a vec3i
         * @param vec3d
         * @return
         */
        public static Vec3i vec3dToVec3i(Vec3d vec3d)
        {
            return new Vec3i(vec3d.x, vec3d.y, vec3d.z);
        }

        /**
         * Returns a copy of the inputed Vec3i
         */
        public static Vec3i copyVec3i(Vec3i vec3i)
        {
            return new Vec3i(vec3i.getX(), vec3i.getY(), vec3i.getZ());
        }

        /**
         * Multiplies each component of the inputed Vec3i with the inputed scalar.
         * @param vec3i
         * @param scalar
         */
        public static Vec3i mult(Vec3i vec3i, int scalar)
        {
            return new Vec3i(vec3i.getX() * scalar, vec3i.getY() * scalar, vec3i.getZ() * scalar);
        }

        /**
         * Returns the inputted vector with its 3 components floored.
         *
         * @param vec
         * @return
         */
        public static Vec3d floorVector(Vec3d vec)
        {
            return new Vec3d(MathHelper.floor(vec.x), MathHelper.floor(vec.y), MathHelper.floor(vec.z));
        }

        /**
         * Returns the Minecraft Vector version used in commands from a Vec3d.
         * Example: new Vec3d(0.5, 0.45, 0.4545) -> "0.5 0.45 0.4545"
         *
         * @param vec
         * @return
         */
        public static String vec3dToMinecraftVectorString(Vec3d vec)
        {
            return vec.x + " " + vec.y + " " + vec.z;
        }

        /**
         * Returns true if both inputted block positions have the same coordinates.
         *
         * @param blockPos1
         * @param blockPos2
         * @return
         */
        public static boolean blockPosEquals(BlockPos blockPos1, BlockPos blockPos2)
        {
            return  blockPos1.getX() == blockPos2.getX() &&
                    blockPos1.getY() == blockPos2.getY() &&
                    blockPos1.getZ() == blockPos2.getZ();
        }
    }

    public static class BlockUtils
    {
        public static class BlockStateUtils
        {

            /**
             * Returns true if the 2 inputed block states are the same.
             * (Have the same toString() representation.)
             *
             * @param b1
             * @param b2
             * @return
             */
            public static boolean isSameBlockState(BlockState b1, BlockState b2)
            {
                return b1.toString().equals(b2.toString());
            }
        }
    }


    public static class PlayerUtils
    {
        /**
         *
         * @param player
         * @param maxDistance
         * @param tickDelta : Between 0 and 1, 1 meaning the different player values (pos, rotation etc) at this tick,
         *                    0.5 meaning the different player values exactly between this tick and the previous tick
         *                    and 0 being the different player values at the previous tick
         * @param includeFluids
         * @return
         */
        public static PositionedBlock getBlockLookingAtClient(ClientPlayerEntity player, double maxDistance, float tickDelta, boolean includeFluids)
        {
            // Send a ray from the player's head, and see which block they're facing
            HitResult hitResult = player.raycast(maxDistance, tickDelta, includeFluids);
            if (hitResult.getType() != HitResult.Type.BLOCK) return null;

            // Get the block hit, modify the hit result pos a bit so that the ray enters the block
            // itself
            Vec3d blockPos = hitResult.getPos().add(player.getRotationVecClient().multiply(0.00001));
            Block hitBlock = player.world.getBlockState(new BlockPos(blockPos)).getBlock();

            return new PositionedBlock(hitBlock, VectorUtils.vec3dToBlockPos(blockPos));
        }

        /**
         * Does a pick block action on the player.
         *
         * @param client : The client of the player
         * @param item : The item to be used in the pick block action
         */
        public static void doPickBlock(MinecraftClient client, ItemStack item)
        {
            client.player.getInventory().addPickBlock(item);
            client.interactionManager.clickCreativeStack(
                    client.player.getStackInHand(Hand.MAIN_HAND), 36 + client.player.getInventory().selectedSlot
            );
        }

        /**
         * Places the block the client has in hand relative to its feet position.
         *
         * @param client
         * @param offset
         * @param swingHand
         */
        public static void placeBlockInHandRelative(MinecraftClient client, Vec3d offset, boolean swingHand, Direction dir)
        {
            Vec3d blockToPlacePos = client.player.getPos().add(offset);

            BlockHitResult riggedBlockHitResult =
                    new BlockHitResult(
                            blockToPlacePos,
                            dir,
                            new BlockPos(blockToPlacePos),
                            true
                    );

            // Place the block
            ActionResult blockPlaced =
                    client.interactionManager.interactBlock(
                            client.player,
                            client.world,
                            Hand.MAIN_HAND,
                            riggedBlockHitResult
                    );

            // Swing the client's hand because yes
            if (swingHand && blockPlaced.isAccepted())
            {
                client.player.swingHand(Hand.MAIN_HAND);
            }
        }



        /**
         * Places the block the client has in hand at the inputted coordinates.
         *
         * @param client
         * @param offset
         * @param swingHand
         */
        public static void placeBlockInHandAbsolute(MinecraftClient client, Vec3d placePos, boolean swingHand, Direction dir)
        {
            BlockHitResult riggedBlockHitResult =
                    new BlockHitResult(
                            placePos,
                            dir,
                            new BlockPos(placePos),
                            true
                    );

            // Place the block
            ActionResult blockPlaced =
                    client.interactionManager.interactBlock(
                            client.player,
                            client.world,
                            Hand.MAIN_HAND,
                            riggedBlockHitResult
                    );

            // Swing the client's hand because yes
            if (swingHand && blockPlaced.isAccepted())
            {
                client.player.swingHand(Hand.MAIN_HAND);
            }
        }



        /**
         * Places the block the client has in hand relative to its feet position.
         *
         * @param serverPlayerEntity
         * @param offset
         * @param swingHand
         */
        public static void placeBlockInHandRelative(ServerPlayerEntity serverPlayerEntity, Vec3d offset, boolean swingHand, ItemStack item)
        {
            Vec3d blockToPlacePos = serverPlayerEntity.getPos().add(offset);

            BlockHitResult riggedBlockHitResult =
                    new BlockHitResult(
                            blockToPlacePos,
                            Direction.DOWN,
                            new BlockPos(blockToPlacePos),
                            true
                    );

            // Place the block
            ActionResult blockPlaced =
                    serverPlayerEntity.interactionManager.interactBlock(
                            serverPlayerEntity,
                            serverPlayerEntity.world,
                            item,
                            Hand.MAIN_HAND,
                            riggedBlockHitResult
                    );

            // Swing the client's hand because yes
            if (swingHand && blockPlaced.isAccepted())
            {
                serverPlayerEntity.swingHand(Hand.MAIN_HAND);
            }
        }

        /**
         * Returns the client player's yaw (as shown in the F3 screen)
         *
         * @param player
         * @return
         */
        public static float getClientPlayerYaw(ClientPlayerEntity player)
        {
            float playerYaw = player.getYaw() + 180;

            if (playerYaw < 0)
            {
                // yaw is negative

                // Flip
                playerYaw = -playerYaw;
                // Modulo
                playerYaw %= 360;
                // Unflip
                playerYaw = 360 - playerYaw;
                // Offset
                playerYaw -= 180;
            }
            else
            {
                // yaw is positive

                // Modulo
                playerYaw %= 360;
                // Offset
                playerYaw -= 180;
            }

            return playerYaw;
        }

        /**
         * Returns the pitch of the client player.
         *
         * @param player
         * @return
         */
        public static float getClientPlayerPitch(ClientPlayerEntity player)
        {
            float playerPitch = player.getPitch();

            return playerPitch;
        }

        /**
         * Returns the direction the player is looking at in terms of world edit directions,
         * but only the 6 main ones : u, d, e, s, w, n
         * In WE there are also "es" for east-south etc.. but we're not accounting for that in this method.
         *
         * @param player
         * @param reversed
         * @return
         */
        public static char getPlayerWEMainDirection(ClientPlayerEntity player, boolean reversed)
        {
            // ## Get yaw and pitch
            float playerYaw = SFabricLib.PlayerUtils.getClientPlayerYaw(player);

            // yaw€[-180; 180[
            // Normally to rotate it 180 degrees we would do:
            // yaw += 180 (map it to [0; 360[
            // yaw += 180 (rotate it)
            // yaw %= 360 (roll it back to a value between 0 and 360 if out of range)
            // yaw -= 180 (map it back to [-180; 180[
            // But this expression emulates that as well:
            playerYaw += reversed ? (playerYaw >= 0 ? -180 : 180) : 0;

            float playerPitch = player.getPitch() * (reversed ? -1 : 1);

            // ## Get dir
            char dir;

            dir =   playerPitch < -69 || playerPitch > 69 ?
                        playerPitch < -69 ? 'u' :
                                            'd'
                    :
                        playerYaw > -135 && playerYaw <= -45 ? 'e' :
                        playerYaw > -45  && playerYaw <=  45 ? 's' :
                        playerYaw > 45   && playerYaw <= 135 ? 'w' :
                                                               'n';

            // ## Retrun
            return dir;
        }

        /**
         * TP's the inputed client player entity to the inputed position in the
         * dimension they're currently in
         *
         * @param client
         * @param position
         */
        public static void tpPlayerTo(MinecraftClient client, Vec3d position)
        {
            // # Setup
            // only works if in singleplayer
            if (!client.isInSingleplayer()) return;

            // Get the server player entity, then use it to tp the player
            ServerPlayerEntity serverPlayer = client.getServer().getPlayerManager().getPlayer(client.player.getEntityName());
            serverPlayer.teleport(
                    serverPlayer.getServerWorld(),
                    position.x, position.y, position.z,
                    getClientPlayerYaw(client.player), getClientPlayerPitch(client.player)
            );
        }

        /**
         * Converts the inputted player into its {@code ServerPlayerEntity} equivalent.
         * May not work on servers. (To test)
         *
         * @param player
         * @return
         */
        public static ServerPlayerEntity clientPlayerToServerPlayerEntity(ClientPlayerEntity player)
        {
            return player.getServer().getPlayerManager().getPlayer(player.getEntityName());
        }


        /**
         * Stores a block and its block pos
         */
        public static class PositionedBlock
        {
            // ### Fields
            private final Block block;
            private final BlockPos blockPos;
            // ###
            // ### Init
            public PositionedBlock(Block block, BlockPos blockPos)
            {
                this.block = block;
                this.blockPos = blockPos;
            }
            // ###
            // ### Getters and setters
            public Block getBlock() {
                return block;
            }
            public BlockPos getBlockPos() {
                return blockPos;
            }
            // ###
        }

        /**
         * Stores a block state and its block pos
         */
        public static class PositionedBlockState
        {
            // ### Fields
            private final BlockState blockState;
            private final BlockPos blockPos;
            // ###
            // ### Init
            public PositionedBlockState(BlockState blockState, BlockPos blockPos)
            {
                this.blockState = blockState;
                this.blockPos = blockPos;
            }
            // ###
            // ### Getters and setters
            public BlockState getBlockState() {
                return blockState;
            }
            public BlockPos getBlockPos() {
                return blockPos;
            }
            // ###
        }
    }

    /**
     * Utils for commands
     */
    public static class CommandUtils
    {

    }
}
