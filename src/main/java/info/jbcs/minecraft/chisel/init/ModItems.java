package info.jbcs.minecraft.chisel.init;

import info.jbcs.minecraft.chisel.Chisel;
import info.jbcs.minecraft.chisel.Configurations;
import info.jbcs.minecraft.chisel.entity.EntityBallOMoss;
import info.jbcs.minecraft.chisel.entity.EntityCloudInABottle;
import info.jbcs.minecraft.chisel.entity.EntitySmashingRock;
import info.jbcs.minecraft.chisel.item.ItemBallOMoss;
import info.jbcs.minecraft.chisel.item.ItemChisel;
import info.jbcs.minecraft.chisel.item.ItemCloudInABottle;
import info.jbcs.minecraft.chisel.item.ItemSmashingRock;
import info.jbcs.minecraft.chisel.item.ItemUpgrade;
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
