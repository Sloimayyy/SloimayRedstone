package me.sloimay.sredstone.keybindings;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

/**
 * Sloimay KeyBinding, which is a wrapper for a {@code KeyBinding} object with a few extra features.
 */
public abstract class SKeyBinding extends KeyBinding
{
    // ### INIT
    public SKeyBinding(String translationKey, InputUtil.Type type, int code, String category)
    {
        super(translationKey, type, code, category);
    }
    // ###

    // ### Public abstract methods
    /**
     * Executes the code associated to this keybinding.
     *
     * @param client
     */
    public abstract void execute(MinecraftClient client);
    // ###
}
