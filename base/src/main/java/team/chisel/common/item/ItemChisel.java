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
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
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
    public boolean canBeDepleted() {
        return Configurations.allowChiselDamage;
    }

    @Override
    public boolean isValidRepairItem(ItemStack damagedItem, ItemStack repairMaterial) {
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
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> list, TooltipFlag flag) {
        list.add(TT_CHISEL_GUI.format(ChatFormatting.AQUA, ChatFormatting.GRAY));
        if (type != ChiselType.IRON || Configurations.ironChiselCanLeftClick) {
            list.add(TT_CHISEL_LC1.format(ChatFormatting.AQUA, ChatFormatting.GRAY));
            list.add(TT_CHISEL_LC2.format(ChatFormatting.AQUA, ChatFormatting.GRAY));
        }
        if (type != ChiselType.IRON || Configurations.ironChiselHasModes) {
            list.add(new TextComponent(""));
            list.add(TT_CHISEL_MODES.getComponent());
            list.add(TT_CHISEL_SELECTED_MODE.format(ChatFormatting.GREEN, new TranslatableComponent(NBTUtil.getChiselMode(stack).getUnlocName() + ".name")));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();
        if (slot == EquipmentSlot.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Chisel Damage", type.attackDamage, Operation.ADDITION));
        }
        return multimap;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        return super.hurtEnemy(stack, attacker, target);
    }
    
    @Override
    public boolean canOpenGui(Level world, Player player, InteractionHand hand) {
        return true;
    }
    
    @Override
    public IChiselGuiType getGuiType(Level world, Player player, InteractionHand hand) {
        return type == ChiselType.HITECH ? ChiselGuiType.HITECH : ChiselGuiType.NORMAL;
    }

    @Override
    public boolean canChisel(Level world, Player player, ItemStack chisel, ICarvingVariation target) {
        return !chisel.isEmpty();
    }

    @Override
    public boolean onChisel(Level world, Player player, ItemStack chisel, ICarvingVariation target) {
        return Configurations.allowChiselDamage;
    }

    @Override
    public boolean canChiselBlock(Level world, Player player, InteractionHand hand, BlockPos pos, BlockState state) {
        return type == ChiselType.HITECH || type == ChiselType.DIAMOND || Configurations.ironChiselCanLeftClick;
    }

    @Override
    public boolean supportsMode(Player player, ItemStack chisel, IChiselMode mode) {
        return type == ChiselType.HITECH || ((type == ChiselType.DIAMOND || Configurations.ironChiselHasModes) && mode != ChiselMode.CONTIGUOUS && mode != ChiselMode.CONTIGUOUS_2D);
    }

    // TODO implement ChiselController
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, Level worldIn, Player playerIn, InteractionHand hand) {
//        if (!worldIn.isRemote) {
//            playerIn.openGui(Chisel.instance, 0, worldIn, hand.ordinal(), 0, 0);
//            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//        }
//        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//    }
    
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || !ItemStack.isSame(oldStack, newStack);
    }
}
