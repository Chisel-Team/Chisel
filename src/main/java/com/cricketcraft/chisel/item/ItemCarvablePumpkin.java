package com.cricketcraft.chisel.item;

import com.cricketcraft.chisel.api.ICarvable;
import com.cricketcraft.chisel.carving.CarvableVariation;
import com.cricketcraft.chisel.config.Configurations;
import com.cricketcraft.chisel.utils.General;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemCarvablePumpkin extends ItemBlock {

    public ItemCarvablePumpkin(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public boolean isValidArmor(ItemStack itemStack, int armorType, Entity entity){
        return true;
    }

    @Override
    public int getMetadata(int i) {
        return i;
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return Block.getBlockFromItem(this).getIcon(2, damage);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips) {
        if (!Configurations.blockDescriptions)
            return;

        Item item = General.getItem(stack);
        if (item == null)
            return;

        Block block = Block.getBlockFromItem(this);
        if (!(block instanceof ICarvable))
            return;

        ICarvable carvable = (ICarvable) block;
        CarvableVariation var = carvable.getVariation(stack);
        if (var == null)
            return;

        lines.add(var.getDescription());
    }
}
