package com.cricketcraft.chisel.carving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.cricketcraft.chisel.Chisel;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Carving
{
    HashMap<String, CarvingGroup> carvingGroupsByName = new HashMap<String, CarvingGroup>();
    HashMap<String, CarvingGroup> carvingGroupsByOre = new HashMap<String, CarvingGroup>();
    HashMap<String, CarvingGroup> carvingGroupsByVariation = new HashMap<String, CarvingGroup>();

    public static Carving chisel = new Carving();
    public static Carving needle = new Carving();

    String key(Item item, int metadata)
    {
        return Item.itemRegistry.getNameForObject(item) + "|" + metadata;
    }

    String key(Block block, int metadata)
    {
        return Block.blockRegistry.getNameForObject(block) + "|" + metadata;
    }

    public boolean isVariationOfSameClass(Block target, int metadata1, Block block2, int metadata2)
    {
        CarvingGroup group1 = carvingGroupsByVariation.get(key(target, metadata1));
        if(group1 == null) return false;

        CarvingGroup group2 = carvingGroupsByVariation.get(key(block2, metadata2));
        if(group2 == null) return false;

        return group1 == group2 || group1.className.equals(group2.className) && !group1.className.isEmpty();

    }

    public CarvingVariation[] getVariations(Block block, int metadata)
    {
        CarvingGroup group = getGroup(block, metadata);
        if(group == null) return null;

        CarvingVariation[] res = group.variations.toArray(new CarvingVariation[group.variations.size()]);
        return res;
    }

    public String getOre(Block block, int metadata)
    {
        CarvingGroup group = getGroup(block, metadata);
        if(group == null)
            return null;

        return group.oreName;
    }

    public ArrayList<ItemStack> getItems(ItemStack chiseledItem)
    {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();

        int damage = chiseledItem.getItemDamage();

        CarvingGroup group = getGroup(Block.getBlockFromItem(chiseledItem.getItem()), damage);
        if(group == null)
            return items;

        HashMap<String, Integer> mapping = new HashMap<String, Integer>();

        if(group.variations != null)
        {
            for(CarvingVariation v : group.variations)
            {

                String key = Block.getIdFromBlock(v.block) + "|" + v.damage;
                if(mapping.containsKey(key)) continue;
                mapping.put(key, 1);

                items.add(new ItemStack(v.block, 1, v.damage));
            }
        }

        ArrayList<ItemStack> ores;
        if(group.oreName != null && ((ores = OreDictionary.getOres(group.oreName)) != null))
        {
            for(ItemStack stack : ores)
            {

                String key = Item.getIdFromItem(stack.getItem()) + "|" + stack.getItemDamage();
                if(mapping.containsKey(key)) continue;
                mapping.put(key, 2);

                items.add(stack);
            }
        }

        return items;
    }

    public CarvingGroup getGroup(Block block, int metadata)
    {
        if(block.equals(Blocks.stone))
            block = Blocks.stonebrick;

        // Check name first
        CarvingGroup res;
        int i = OreDictionary.getOreID(block.getUnlocalizedName());
        if(i < 1)
            return null;

        //if((res = carvingGroupsByOre.get(OreDictionary.getOreIDs(new ItemStack(block, 1, metadata)))) != null)
        //    return res;

        if((res = carvingGroupsByVariation.get(key(block, metadata))) != null)
            return res;

        return null;
    }

    CarvingGroup getGroup(String name)
    {
        CarvingGroup group = carvingGroupsByName.get(name);
        if(group == null)
        {
            group = new CarvingGroup(name);
            group.className = name;
            carvingGroupsByName.put(name, group);
        }

        return group;
    }

    public CarvingVariation addVariation(String name, Block block, int metadata, int order)
    {
        CarvingGroup group = getGroup(name);

        CarvingGroup blockGroup = carvingGroupsByVariation.get(key(block, metadata));
        if(blockGroup != null || blockGroup == group)
            return null;

        CarvingVariation variation = new CarvingVariation(block, metadata, order);
        group.variations.add(variation);
        Collections.sort(group.variations);
        carvingGroupsByVariation.put(key(block, metadata), group);

        return variation;
    }


    public void registerOre(String name, String oreName)
    {
        CarvingGroup group = getGroup(name);

        carvingGroupsByOre.put(oreName, group);

        group.oreName = oreName;
    }

    public void setGroupClass(String name, String className)
    {
        CarvingGroup group = getGroup(name);

        group.className = className;
    }

    public void setVariationSound(String name, String sound)
    {
        CarvingGroup group = getGroup(name);
        group.sound = sound;
    }

    public String getVariationSound(Block block, int metadata)
    {
        return getVariationSound(Item.getItemFromBlock(block), metadata);
    }

    public String getVariationSound(Item item, int metadata)
    {
        CarvingGroup blockGroup = carvingGroupsByVariation.get(key(item, metadata));
        if(blockGroup == null || blockGroup.sound == null)
            return Chisel.MOD_ID+":chisel.fallback";

        return blockGroup.sound;
    }
}

