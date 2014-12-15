package com.cricketcraft.chisel.init;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.Configurations;
import com.cricketcraft.chisel.entity.EntityBallOMoss;
import com.cricketcraft.chisel.entity.EntityCloudInABottle;
import com.cricketcraft.chisel.entity.EntitySmashingRock;
import com.cricketcraft.chisel.item.ItemBallOMoss;
import com.cricketcraft.chisel.item.ItemChisel;
import com.cricketcraft.chisel.item.ItemCloudInABottle;
import com.cricketcraft.chisel.item.ItemSmashingRock;
import com.cricketcraft.chisel.item.ItemUpgrade;

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
