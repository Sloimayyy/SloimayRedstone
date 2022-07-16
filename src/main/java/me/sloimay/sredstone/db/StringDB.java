package me.sloimay.sredstone.db;

/**
 * Holds useful strings
 */
public class StringDB
{
    public static class Logging
    {
        public static final String prefix = "[S-Redstone] >> ";
    }

    public static class Commands
    {
        public static final String blockToBinPresetHelper = "A blocktobin preset is like built like this: " +
                "<blockgroup>_<bitCount>_<options concatenated>.\n" +
                "Blocks groups: [redstonelamps, wools], bit counts: [1 to 64], options: [2c, rev].\n" +
                "Example for a preset of a binary number represented with redstone lamps, 8 bit, 2s complement and " +
                "reversed: \"redstonelamps_8_2crev\"";
    }
}
