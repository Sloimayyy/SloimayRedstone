package me.sloimay.sredstone.mixin;

import me.sloimay.sredstone.SRedstone;
import me.sloimay.sredstone.db.ClientDB;
import me.sloimay.sredstone.db.Db;
import me.sloimay.sredstone.features.autodustplacing.AutomaticRedstoneDustPlacing;
import me.sloimay.sredstone.scheduler.STask;
import me.sloimay.sredstone.utils.SFabricLib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//import net.minecraft.network.packet.s2c.play.ContainerSlotUpdateS2CPacket;
//import net.minecraft.network.packet.s2c.play.HeldItemChangeS2CPacket;
//import net.minecraft.network.packet.s2c.play.It

/**
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>
 */
@Mixin (BlockItem.class)
public class PlayerPlaceBlock {
	@Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
	private void restrict(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
		/*if(this.restrict(context, state)) {
			PlayerEntity entity = context.getPlayer();
			if(!(entity instanceof ServerPlayerEntity)) return;
			cir.setReturnValue(false);
		}*/

		PlayerEntity entity = context.getPlayer();
		if(!(entity instanceof ServerPlayerEntity)) return;
		ServerPlayerEntity p = (ServerPlayerEntity) entity;

		// ## Auto redstone placing stuff
		/*if (ClientDB.isRedstoneDustAutoPlacingActivated && p.getAbilities().creativeMode)
		{
			// Get where to put the redstone dust
			BlockPos redstoneDustPos = context.getBlockPos().up();

			// If the block where the redstone dust should be placed is air, then we can continue
			if (!p.world.getBlockState(redstoneDustPos).isAir()) return;

			// Place the block that we're about the place under the redstone
			p.world.setBlockState(redstoneDustPos.down(), Block.getBlockFromItem(p.getInventory().getMainHandStack().getItem()).getDefaultState());
			// Place the actual redstone
			p.world.setBlockState(redstoneDustPos, Blocks.REDSTONE_WIRE.getDefaultState());
		}*/

		// Will surely not work on multiplayer as it seems to be server-side
		/*System.out.println(context.canPlace());
		if (p.getAbilities().creativeMode && !state.getBlock().asItem().getTranslationKey().equals(Items.REDSTONE.getTranslationKey()))
		{
			System.out.println("block placed in creative");
			ItemStack mainhandItem = entity.getInventory().getMainHandStack();
			SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, new ItemStack(Items.REDSTONE, 1));
			BlockPos redstoneDustRelativePos = context.getBlockPos().up().subtract(entity.getBlockPos());



			SFabricLib.PlayerUtils.placeBlockInHandRelative(
					ClientDB.mcClient,
					SFabricLib.VectorUtils.blockPosToVec3d(redstoneDustRelativePos),
					true,
					Direction.UP
			);
			SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, mainhandItem);

			System.out.println(SFabricLib.VectorUtils.blockPosToVec3d(redstoneDustRelativePos).toString());

		}*/

		/*
		if (p.getAbilities().creativeMode && !state.getBlock().asItem().getTranslationKey().equals(Items.REDSTONE.getTranslationKey()))
		{
			BlockPos redstoneDustRelativePos = context.getBlockPos().up().subtract(entity.getBlockPos());
			BlockPos redstoneDustPos = context.getBlockPos().up();

			ItemStack mainhandItem = entity.getInventory().getMainHandStack();

			SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, new ItemStack(Items.REDSTONE, 1));
			SFabricLib.PlayerUtils.placeBlockInHandAbsolute(
					ClientDB.mcClient,
					SFabricLib.VectorUtils.blockPosToVec3d(redstoneDustPos),
					true,
					Direction.UP
			);
			SFabricLib.PlayerUtils.doPickBlock(ClientDB.mcClient, mainhandItem);
		}*/

		// ## Auto dust placing feature
		AutomaticRedstoneDustPlacing.handleFeature(state, context.getBlockPos());
	}

	/**
	 * @param context the context of the placement
	 * @param state the state to place
	 * @return return true if the player cannot place a block there
	 */
	@Unique
	public boolean restrict(ItemPlacementContext context, BlockState state) { return false; }
}