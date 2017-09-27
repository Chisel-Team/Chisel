package team.chisel.common.item;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.client.ClientUtil;
import team.chisel.common.util.NBTUtil;
import team.chisel.common.util.SoundUtil;

public class ChiselController {
    
    @SuppressWarnings({ "unused" })
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        EntityPlayer player = event.getEntityPlayer();
        ItemStack held = event.getItemStack();
        
        if (held != null && held.getItem() instanceof IChiselItem) {

            ItemStack target = NBTUtil.getChiselTarget(held);
            IChiselItem chisel = (IChiselItem) held.getItem();
            
            ICarvingRegistry registry = CarvingUtils.getChiselRegistry();
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            
            if (!chisel.canChiselBlock(event.getWorld(), player, event.getHand(), event.getPos(), state)) {
                return;
            }
            
            ICarvingGroup blockGroup = registry.getGroup(state);
            if (blockGroup == null) {
                return;
            }
            
            if (target != null) {
                ICarvingGroup sourceGroup = registry.getGroup(target);

                if (blockGroup == sourceGroup) {
                    ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(target);
                    if (variation != null) {
                        updateState(event.getWorld(), event.getPos(), held, event.getEntityPlayer(), variation.getBlockState());
                        damageItem(held, player);
                    } else {
                        Chisel.logger.warn("Found itemstack {} in group {}, but it has no variation!", target, sourceGroup.getName());
                    }
                }
            } else {
                ICarvingVariation current = registry.getVariation(state);
                List<IBlockState> variations = blockGroup.getVariations().stream().map(ICarvingVariation::getBlockState).collect(Collectors.toList());
                
                variations = variations.stream().distinct().collect(Collectors.toList());
                        
                int index = variations.indexOf(current.getBlockState());
                index = player.isSneaking() ? index - 1 : index + 1;
                index = (index + variations.size()) % variations.size();
                
                IBlockState next = variations.get(index);
                updateState(event.getWorld(), event.getPos(), held, event.getEntityPlayer(), next);
                damageItem(held, player);
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getWorld().isRemote) {
            ItemStack stack = event.getItemStack();
            if (stack != null && stack.getItem() instanceof IChiselItem) {
                IChiselItem chisel = (IChiselItem) stack.getItem();
                if (chisel.canOpenGui(event.getWorld(), event.getEntityPlayer(), event.getHand())) {
                    event.getEntityPlayer().openGui(Chisel.instance, 0, event.getWorld(), event.getHand().ordinal(), 0, 0);
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == EnumHand.OFF_HAND) {
            ItemStack mainhandStack = event.getEntityPlayer().getHeldItemMainhand();
            if (mainhandStack != null && mainhandStack.getItem() instanceof IChiselItem) {
                event.setCanceled(true);
            }
        }
    }

    private static void damageItem(ItemStack stack, EntityPlayer player) {
        stack.damageItem(1, player);
        if (stack.stackSize <= 0) {
            player.setHeldItem(EnumHand.MAIN_HAND, (ItemStack)null);
            ForgeEventFactory.onPlayerDestroyItem(player, stack, EnumHand.MAIN_HAND);
        }
    }
    
    private static void updateState(World world, BlockPos pos, ItemStack stack, EntityPlayer player, IBlockState next) {
        IBlockState current = world.getBlockState(pos);
        if (current != next) {
            world.setBlockState(pos, next);
            SoundUtil.playSound(player, stack, next);
            if (world.isRemote) {
                ClientUtil.addDestroyEffects(world, pos, next);
            }
        }
    }
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ItemStack stack = event.getPlayer().getHeldItemMainhand();
        if (event.getPlayer().capabilities.isCreativeMode && stack != null && stack.getItem() instanceof IChiselItem) {
            event.setCanceled(true);
        }
    }
}
