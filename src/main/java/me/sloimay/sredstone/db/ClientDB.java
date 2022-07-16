package me.sloimay.sredstone.db;

import me.sloimay.sredstone.features.redstonenetwork.RedstoneNetwork;
import net.minecraft.client.MinecraftClient;

public class ClientDB
{
    // ### Options
    public static boolean isRedstoneDustAutoPlacingActivated = false;
    public static MinecraftClient mcClient = MinecraftClient.getInstance();
    public static int coolTPMaxDist = 64;

    public static RedstoneNetwork redstoneNetwork = null;

    // ## Redstone network settings
    public static boolean constantlyPoolRedstoneNetwork = false;
    public static boolean showTimingsAsRedstoneTicks = false;
}
