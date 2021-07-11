package team.chisel.common.init;

import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;

public class ChiselCompatTags {

    public static final ResourceLocation STONE_DIABASE = compatTag("stone/diabase");
    public static final ResourceLocation STONE_LIMESTONE = compatTag("stone/limestone");
    public static final ResourceLocation STONE_MARBLE = compatTag("stone/marble");

    public static final ResourceLocation STORAGE_BLOCKS_ALUMINUM = compatTag("storage_blocks/aluminum");
    public static final ResourceLocation STORAGE_BLOCKS_BRONZE = compatTag("storage_blocks/bronze");
    public static final ResourceLocation STORAGE_BLOCKS_CHARCOAL = compatTag("storage_blocks/charcoal");
    public static final ResourceLocation STORAGE_BLOCKS_COBALT = compatTag("storage_blocks/cobalt");
    public static final ResourceLocation STORAGE_BLOCKS_COPPER = compatTag("storage_blocks/copper");
    public static final ResourceLocation STORAGE_BLOCKS_ELECTRUM = compatTag("storage_blocks/electrum");
    public static final ResourceLocation STORAGE_BLOCKS_INVAR = compatTag("storage_blocks/invar");
    public static final ResourceLocation STORAGE_BLOCKS_LEAD = compatTag("storage_blocks/lead");
    public static final ResourceLocation STORAGE_BLOCKS_NICKEL = compatTag("storage_blocks/nickel");
    public static final ResourceLocation STORAGE_BLOCKS_PLATINUM = compatTag("storage_blocks/platinum");
    public static final ResourceLocation STORAGE_BLOCKS_SILVER = compatTag("storage_blocks/silver");
    public static final ResourceLocation STORAGE_BLOCKS_STEEL = compatTag("storage_blocks/steel");
    public static final ResourceLocation STORAGE_BLOCKS_TIN = compatTag("storage_blocks/tin");
    public static final ResourceLocation STORAGE_BLOCKS_URANIUM = compatTag("storage_blocks/uranium");

    static ResourceLocation modTag(String name) {
        return new ResourceLocation(Chisel.MOD_ID, name);
    }

    static ResourceLocation compatTag(String name) {
        return new ResourceLocation("forge", name);
    }
}
