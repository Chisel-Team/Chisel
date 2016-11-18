package team.chisel.common.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
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
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.client.ClientUtil;
import team.chisel.common.util.NBTUtil;

public class ChiselController {
    
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        EntityPlayer player = event.getEntityPlayer();
        ItemStack held = event.getItemStack();
        
        if (held != null && held.getItem() instanceof ItemChisel) {

            ItemStack target = NBTUtil.getChiselTarget(held);
            
            ICarvingRegistry registry = CarvingUtils.getChiselRegistry();
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            
            ItemStack inWorldStack = state.getBlock().getPickBlock(state, new RayTraceResult(event.getHitVec(), event.getFace()), event.getWorld(), event.getPos(), player);
            ICarvingGroup blockGroup = registry.getGroup(inWorldStack);
            if (blockGroup == null) {
                return;
            }
            
            if (target != null) {
                ICarvingGroup sourceGroup = registry.getGroup(target);

                if (blockGroup == sourceGroup) {
                    ICarvingVariation variation = CarvingUtils.getChiselRegistry().getVariation(target);
                    updateState(event.getWorld(), event.getPos(), variation.getBlockState());
                    damageItem(held, player);
                }
            } else {
                ICarvingVariation current = registry.getVariation(inWorldStack);
                List<ICarvingVariation> variations = blockGroup.getVariations();
                int index = variations.indexOf(current);
                index = player.isSneaking() ? index - 1 : index + 1;
                index = (index + variations.size()) % variations.size();
                
                ICarvingVariation next = variations.get(index);
                updateState(event.getWorld(), event.getPos(), next.getBlockState());
                damageItem(held, player);
            }
        }
    }
    
    private static void damageItem(ItemStack stack, EntityPlayer player) {
        stack.damageItem(1, player);
        if (stack.getCount() <= 0) {
            player.setHeldItem(EnumHand.MAIN_HAND, (ItemStack)null);
            ForgeEventFactory.onPlayerDestroyItem(player, stack, EnumHand.MAIN_HAND);
        }
    }
    
    private static void updateState(World world, BlockPos pos, IBlockState state) {
        IBlockState current = world.getBlockState(pos);
        if (current != state) {
            world.setBlockState(pos, state);
            if (world.isRemote) {
                ClientUtil.playSound(world, pos, CarvingUtils.getChiselRegistry().getVariationSound(current), SoundCategory.BLOCKS);
                ClientUtil.addDestroyEffects(world, pos, state);
            }
        }
    }
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ItemStack stack = event.getPlayer().getHeldItemMainhand();
        if (event.getPlayer().capabilities.isCreativeMode && stack != null && stack.getItem() instanceof ItemChisel) {
            event.setCanceled(true);
        }
    }
}
