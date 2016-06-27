package team.chisel.common.item;

import java.util.List;
import java.util.Locale;

import lombok.Getter;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import team.chisel.Chisel;
import team.chisel.common.config.Configurations;
import team.chisel.common.init.ChiselTabs;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class ItemChisel extends Item {
    
    public enum ChiselType {
        IRON(Configurations.ironChiselMaxDamage, Configurations.ironChiselAttackDamage),
        DIAMOND(Configurations.diamondChiselMaxDamage, Configurations.diamondChiselAttackDamage),
        HITECH(Configurations.diamondChiselMaxDamage, Configurations.diamondChiselAttackDamage)
        ;

        final int maxDamage;
        final int attackDamage;

        private ChiselType(int maxDamage, int attackDamage) {
            this.maxDamage = maxDamage;
            this.attackDamage = attackDamage;
        }
    }
    
    @Getter
    private final ChiselType type;
    
    public ItemChisel(ChiselType type) {
        super();
        this.type = type;
        setMaxStackSize(1);
        String name = "chisel_" + type.name().toLowerCase(Locale.US);
        setRegistryName(name);
        setUnlocalizedName(Chisel.MOD_ID + "." + name);
        setCreativeTab(ChiselTabs.tab);
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        if (Configurations.allowChiselDamage)
            return type.maxDamage;
        return 0;
    }

    @Override
    public boolean isDamageable() {
        return Configurations.allowChiselDamage;
    }

    @Override
    public boolean getIsRepairable(ItemStack damagedItem, ItemStack repairMaterial) {
        switch (type) {
        case DIAMOND:
        case HITECH:
            return repairMaterial.getItem().equals(Items.DIAMOND);
        case IRON:
            return repairMaterial.getItem().equals(Items.IRON_INGOT);
        }

        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held) {
        String base = "item.chisel.chisel.desc.";
        String gui = I18n.format(base + "gui");
        String lc1 = I18n.format(base + "lc1");
        String lc2 = I18n.format(base + "lc2");
        String modes = I18n.format(base + "modes");
        list.add(gui);
        if (type == ChiselType.DIAMOND || Configurations.ironChiselCanLeftClick) {
            list.add(lc1);
            list.add(lc2);
        }
        if (type == ChiselType.DIAMOND || Configurations.ironChiselHasModes) {
            list.add("");
            list.add(modes);
        }
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = HashMultimap.create();
        if (slot.getSlotType() == Type.HAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Chisel Damage", type.attackDamage, 0));
        }
        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return super.hitEntity(stack, attacker, target);
    }
    
// TODO implement IChiselItem
//    @Override
//    public boolean canOpenGui(World world, EntityPlayer player, ItemStack chisel) {
//        return true;
//    }
//
//    @Override
//    public boolean canChisel(World world, ItemStack chisel, ICarvingVariation target) {
//        return true;
//    }
//
//    @Override
//    public boolean onChisel(World world, ItemStack chisel, ICarvingVariation target) {
//        return Configurations.allowChiselDamage;
//    }
//
//    @Override
//    public boolean canChiselBlock(World world, EntityPlayer player, int x, int y, int z, Block block, int metadata) {
//        return type == ChiselType.DIAMOND || type == ChiselType.OBSIDIAN || Configurations.ironChiselCanLeftClick;
//    }
//
//    @Override
//    public boolean hasModes(ItemStack chisel) {
//        return type == ChiselType.DIAMOND || type == ChiselType.OBSIDIAN || Configurations.ironChiselHasModes;
//    }

    // TODO implement ChiselController
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            playerIn.openGui(Chisel.instance, 0, worldIn, hand.ordinal(), 0, 0);
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
    }
}
