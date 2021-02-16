package team.chisel.api.carving;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TranslationTextComponent;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICarvingGroup {
    
    ResourceLocation getId();
    
    String getTranslationKey();
    
    default TranslationTextComponent getDisplayName() {
        return new TranslationTextComponent(getTranslationKey());
    }
    
    default Tag<Item> getItemTag() {
        Tag<Item> ret = ItemTags.getCollection().get(getId());
        if (ret == null) {
            throw new IllegalStateException("Group " + getId() + " does not have an associated item tag");
        }
        return ret;
    }
    
    default Optional<Tag<Block>> getBlockTag() {
        return Optional.ofNullable(BlockTags.getCollection().get(getId()));
    }
	
	SoundEvent getSound();
}
