package team.chisel.api.carving;

import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.network.chat.TranslatableComponent;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICarvingGroup {
    
    ResourceLocation getId();
    
    String getTranslationKey();
    
    default TranslatableComponent getDisplayName() {
        return new TranslatableComponent(getTranslationKey());
    }
    
    default Tag<Item> getItemTag() {
    	Tag<Item> ret = ItemTags.getAllTags().getTag(getId());
        if (ret == null) {
            throw new IllegalStateException("Group " + getId() + " does not have an associated item tag");
        }
        return ret;
    }
    
    default Optional<Tag<Block>> getBlockTag() {
        return Optional.ofNullable(BlockTags.getAllTags().getTag(getId()));
    }
	
	SoundEvent getSound();
}
