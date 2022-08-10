package team.chisel.api.carving;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

public interface ICarvingGroup {

    ResourceLocation getId();

    String getTranslationKey();

    default TranslatableComponent getDisplayName() {
        return new TranslatableComponent(getTranslationKey());
    }

    default ITag<Item> getItemTag() {
        return ForgeRegistries.ITEMS.tags().getTag(ForgeRegistries.ITEMS.tags().createTagKey(getId()));
    }

    default ITag<Block> getBlockTag() {
        return ForgeRegistries.BLOCKS.tags().getTag(ForgeRegistries.BLOCKS.tags().createTagKey(getId()));
    }

    SoundEvent getSound();
}
