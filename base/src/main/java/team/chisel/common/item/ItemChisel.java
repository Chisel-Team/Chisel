package team.chisel.common.item;

import static team.chisel.client.util.ChiselLangKeys.TT_CHISEL_GUI;
import static team.chisel.client.util.ChiselLangKeys.TT_CHISEL_LC1;
import static team.chisel.client.util.ChiselLangKeys.TT_CHISEL_LC2;
import static team.chisel.client.util.ChiselLangKeys.TT_CHISEL_MODES;
import static team.chisel.client.util.ChiselLangKeys.TT_CHISEL_SELECTED_MODE;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import team.chisel.api.IChiselGuiType;
import team.chisel.api.IChiselGuiType.ChiselGuiType;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;
import team.chisel.common.config.Configurations;
import team.chisel.common.util.NBTUtil;

@ParametersAreNonnullByDefault
public class ItemChisel extends Item implements IChiselItem {
    
    public enum ChiselType {
        IRON(Configurations.ironChiselMaxDamage, Configurations.ironChiselAttackDamage),
        DIAMOND(Configurations.diamondChiselMaxDamage, Configurations.diamondChiselAttackDamage),
        HITECH(Configurations.hitechChiselMaxDamage, Configurations.hitechChiselAttackDamage)
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
    
    public ItemChisel(ChiselType type, Item.Properties properties) {
        super(properties);
        this.type = type;
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

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        list.add(TT_CHISEL_GUI.format(TextFormatting.AQUA, TextFormatting.GRAY));
        if (type != ChiselType.IRON || Configurations.ironChiselCanLeftClick) {
            list.add(TT_CHISEL_LC1.format(TextFormatting.AQUA, TextFormatting.GRAY));
            list.add(TT_CHISEL_LC2.format(TextFormatting.AQUA, TextFormatting.GRAY));
        }
        if (type != ChiselType.IRON || Configurations.ironChiselHasModes) {
            list.add(new StringTextComponent(""));
            list.add(TT_CHISEL_MODES.getComponent());
            list.add(TT_CHISEL_SELECTED_MODE.format(TextFormatting.GREEN, new TranslationTextComponent(NBTUtil.getChiselMode(stack).getUnlocName() + ".name")));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EquipmentSlotType.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Chisel Damage", type.attackDamage, Operation.ADDITION));
        }
        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, p -> p.sendBreakAnimation(Hand.MAIN_HAND));
        return super.hitEntity(stack, attacker, target);
    }
    
    @Override
    public boolean canOpenGui(World world, PlayerEntity player, Hand hand) {
        return true;
    }
    
    @Override
    public IChiselGuiType getGuiType(World world, PlayerEntity player, Hand hand) {
        return type == ChiselType.HITECH ? ChiselGuiType.HITECH : ChiselGuiType.NORMAL;
    }

    @Override
    public boolean canChisel(World world, PlayerEntity player, ItemStack chisel, ICarvingVariation target) {
        return !chisel.isEmpty();
    }

    @Override
    public boolean onChisel(World world, PlayerEntity player, ItemStack chisel, ICarvingVariation target) {
        return Configurations.allowChiselDamage;
    }

    @Override
    public boolean canChiselBlock(World world, PlayerEntity player, Hand hand, BlockPos pos, BlockState state) {
        return type == ChiselType.HITECH || type == ChiselType.DIAMOND || Configurations.ironChiselCanLeftClick;
    }

    @Override
    public boolean supportsMode(PlayerEntity player, ItemStack chisel, IChiselMode mode) {
        return type == ChiselType.HITECH || ((type == ChiselType.DIAMOND || Configurations.ironChiselHasModes) && mode != ChiselMode.CONTIGUOUS && mode != ChiselMode.CONTIGUOUS_2D);
    }

    // TODO implement ChiselController
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, PlayerEntity playerIn, Hand hand) {
//        if (!worldIn.isRemote) {
//            playerIn.openGui(Chisel.instance, 0, worldIn, hand.ordinal(), 0, 0);
//            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//        }
//        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.areItemsEqual(oldStack, newStack);
    }
}
