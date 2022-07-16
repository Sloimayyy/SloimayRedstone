package me.sloimay.sredstone.keybindings;

import com.mojang.blaze3d.systems.RenderSystem;
import me.sloimay.sredstone.keybindings.custom.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

public class KeyBindingsRegister implements ClientModInitializer {

	// ### Fields

	// ###



	// ### Public methods

	@Override
	public void onInitializeClient()
	{
		// ## The keybindings in this mod
		List<SKeyBinding> keyBindings = new ArrayList<SKeyBinding>();
		keyBindings.add(new RQOLKeyBinding());
		keyBindings.add(new PlaceBlockInTheAirKeyBinding());
		keyBindings.add(new WEMoveSelectionForwardKeyBinding());
		keyBindings.add(new WEMoveSelectionBackwardKeyBinding());
		keyBindings.add(new TPForwardKeyBinding());

		// ## Register all of the keybindings
		keyBindings.forEach(keyBinding -> KeyBindingHelper.registerKeyBinding(keyBinding));

		// ## Register the keybinds execution methods
		ClientTickEvents.END_CLIENT_TICK.register(
				client -> keyBindings.forEach(
						keyBinding -> {
							while (keyBinding.wasPressed()) { keyBinding.execute(client); }
						}
				)
		);
	}

	// ###
}