package team.chisel.common;

/**
 * Reference class for different mod information
 */
@SuppressWarnings({"UnnecessaryInterfaceModifier", "unused"})
public interface Reference {
    public static final String MOD_ID = "chisel";
    public static final String PROTOCOL_VERSION = "1";

    public static final String MOD_NAME = "Chisel";
    public static final String VERSION = "@VERSION@";

    public static final String CLIENT_PROXY = "team.chisel.client.ClientProxy";
    public static final String COMMON_PROXY = "team.chisel.common.CommonProxy";

    public static final String DEPENDENCIES = "after:appliedenergistics2;after:bloodmagic;after:thaumcraft";
}
