package com.cricketcraft.minecraft.chisel.init;

import com.cricketcraft.minecraft.chisel.Chisel;
import com.cricketcraft.minecraft.chisel.Configurations;
import com.cricketcraft.minecraft.chisel.entity.EntityBallOMoss;
import com.cricketcraft.minecraft.chisel.entity.EntityCloudInABottle;
import com.cricketcraft.minecraft.chisel.entity.EntitySmashingRock;
import com.cricketcraft.minecraft.chisel.item.ItemBallOMoss;
import com.cricketcraft.minecraft.chisel.item.ItemChisel;
import com.cricketcraft.minecraft.chisel.item.ItemCloudInABottle;
import com.cricketcraft.minecraft.chisel.item.ItemSmashingRock;
import com.cricketcraft.minecraft.chisel.item.ItemUpgrade;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {
    public static ItemChisel chisel;
    public static ItemCloudInABottle itemCloudInABottle;
    public static ItemBallOMoss itemBallOMoss;
    public static ItemSmashingRock smashingRock;
    public static ItemUpgrade upgrade;

    public static void load() {
        chisel = (ItemChisel) new ItemChisel().setTextureName("chisel:chisel").setCreativeTab(ModTabs.tabChisel);
        GameRegistry.registerItem(chisel, "chisel");

        if (Configurations.featureEnabled("cloud")) {
            itemCloudInABottle = (ItemCloudInABottle) new ItemCloudInABottle().setTextureName("Chisel:cloudinabottle-x").setCreativeTab(ModTabs.tabChisel);
            EntityRegistry.registerModEntity(EntityCloudInABottle.class, "CloudInABottle", 1, Chisel.instance, 40, 1, true);
            GameRegistry.registerItem(itemCloudInABottle, "cloudinabottle");
        }

        if (Configurations.featureEnabled("ballOfMoss")) {
            itemBallOMoss = (ItemBallOMoss) new ItemBallOMoss().setTextureName("Chisel:ballomoss").setCreativeTab(ModTabs.tabChisel);
            EntityRegistry.registerModEntity(EntityBallOMoss.class, "BallOMoss", 2, Chisel.instance, 40, 1, true);
            GameRegistry.registerItem(itemBallOMoss, "ballomoss");
        }

        if (Configurations.featureEnabled("smashingRock")) {
            smashingRock = (ItemSmashingRock) new ItemSmashingRock().setTextureName("Chisel:smashingrock").setCreativeTab(ModTabs.tabChisel);
            EntityRegistry.registerModEntity(EntitySmashingRock.class, "SmashingRock", 2, Chisel.instance, 40, 1, true);
            GameRegistry.registerItem(smashingRock, "smashingrock");
        }

        if(Configurations.featureEnabled("autoChiselUpgrades")){
            upgrade = (ItemUpgrade) new ItemUpgrade("upgrade").setCreativeTab(ModTabs.tabChisel);
            GameRegistry.registerItem(upgrade, upgrade.getUnlocalizedName());
        }
    }
}
