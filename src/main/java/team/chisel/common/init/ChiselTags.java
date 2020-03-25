package team.chisel.common.init;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import team.chisel.Chisel;

public class ChiselTags {
    
    public static class Blocks {

        public static final Tag<Block> STONE_BASALT = compatTag("stone/basalt");
        public static final Tag<Block> STONE_LIMESTONE = compatTag("stone/limestone");
        public static final Tag<Block> STONE_MARBLE = compatTag("stone/marble");

        public static final Tag<Block> STORAGE_BLOCKS_ALUMINUM = compatTag("storage_blocks/aluminum");
        public static final Tag<Block> STORAGE_BLOCKS_BRONZE = compatTag("storage_blocks/bronze");
        public static final Tag<Block> STORAGE_BLOCKS_CHARCOAL = compatTag("storage_blocks/charcoal");
        public static final Tag<Block> STORAGE_BLOCKS_COBALT = compatTag("storage_blocks/cobalt");
        public static final Tag<Block> STORAGE_BLOCKS_COPPER = compatTag("storage_blocks/copper");
        public static final Tag<Block> STORAGE_BLOCKS_ELECTRUM = compatTag("storage_blocks/electrum");
        public static final Tag<Block> STORAGE_BLOCKS_INVAR = compatTag("storage_blocks/invar");
        public static final Tag<Block> STORAGE_BLOCKS_LEAD = compatTag("storage_blocks/lead");
        public static final Tag<Block> STORAGE_BLOCKS_NICKEL = compatTag("storage_blocks/nickel");
        public static final Tag<Block> STORAGE_BLOCKS_PLATINUM = compatTag("storage_blocks/platinum");
        public static final Tag<Block> STORAGE_BLOCKS_SILVER = compatTag("storage_blocks/silver");
        public static final Tag<Block> STORAGE_BLOCKS_STEEL = compatTag("storage_blocks/steel");
        public static final Tag<Block> STORAGE_BLOCKS_TIN = compatTag("storage_blocks/tin");
        public static final Tag<Block> STORAGE_BLOCKS_URANIUM = compatTag("storage_blocks/uranium");

        static Tag<Block> tag(String modid, String name) {
            return ChiselTags.tag(BlockTags.Wrapper::new, modid, name);
        }

        static Tag<Block> modTag(String name) {
            return tag(Chisel.MOD_ID, name);
        }

        static Tag<Block> compatTag(String name) {
            return tag("forge", name);
        }
    }

    static <T extends Tag<?>> T tag(Function<ResourceLocation, T> creator, String modid, String name) {
        return creator.apply(new ResourceLocation(modid, name));
    }

    static <T extends Tag<?>> T modTag(Function<ResourceLocation, T> creator, String name) {
        return tag(creator, Chisel.MOD_ID, name);
    }

    static <T extends Tag<?>> T compatTag(Function<ResourceLocation, T> creator, String name) {
        return tag(creator, "forge", name);
    }
}
