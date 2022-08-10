package team.chisel.api.carving;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.Objects;

public interface ICarvingGroup {

    ResourceLocation getId();

    String getTranslationKey();

    @SuppressWarnings("unused")
    default TranslatableComponent getDisplayName() {
        return new TranslatableComponent(getTranslationKey());
    }

    default ITag<Item> getItemTag() {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).createTagKey(getId()));
    }

    default ITag<Block> getBlockTag() {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).createTagKey(getId()));
    }

    SoundEvent getSound();
}
