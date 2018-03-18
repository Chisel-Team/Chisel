package team.chisel.common.item;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Preconditions;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingRegistry;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;
import team.chisel.client.util.ChiselModeGeometryCache;
import team.chisel.client.util.ClientUtil;
import team.chisel.common.util.NBTUtil;
import team.chisel.common.util.SoundUtil;

@ParametersAreNonnullByDefault
public class ChiselController {
    
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        EntityPlayer player = event.getEntityPlayer();
        ItemStack held = event.getItemStack();
        
        if (held.getItem() instanceof IChiselItem) {

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
            
            IChiselMode mode = NBTUtil.getChiselMode(held);
            Iterable<? extends BlockPos> candidates = mode.getCandidates(player, event.getPos(), event.getFace());
            
            if (!target.isEmpty()) {
                ICarvingGroup sourceGroup = registry.getGroup(target);

                if (blockGroup == sourceGroup) {
                    ICarvingVariation variation = registry.getVariation(target);
                    if (variation != null) {
                        if (variation.getBlockState() != null) {
                            setAll(candidates, player, state, variation);
                        }
                    } else {
                        Chisel.logger.warn("Found itemstack {} in group {}, but it has no variation!", target, sourceGroup.getName());
                    }
                }
            } else {
                ICarvingVariation current = registry.getVariation(state);
                List<ICarvingVariation> variations = blockGroup.getVariations();
                
                variations = variations.stream().filter(v -> v.getBlockState() != null).collect(Collectors.toList());
                        
                int index = variations.indexOf(current);
                index = player.isSneaking() ? index - 1 : index + 1;
                index = (index + variations.size()) % variations.size();
                
                ICarvingVariation next = variations.get(index);
                setAll(candidates, player, state, next);
            }
        }
    }

    private static void setAll(Iterable<? extends BlockPos> candidates, EntityPlayer player, IBlockState origState, ICarvingVariation v) {
        for (BlockPos pos : candidates) {
            setVariation(player, pos, origState, v);
        }
    }
    /**
     * Assumes that the player is holding a chisel
     */
    private static void setVariation(EntityPlayer player, BlockPos pos, IBlockState origState, ICarvingVariation v) {
        IBlockState targetState = v.getBlockState();
        Preconditions.checkNotNull(targetState, "Variation state cannot be null!");
        
        World world = player.world;
        IBlockState curState = world.getBlockState(pos);
        ItemStack held = player.getHeldItemMainhand();
        if (curState == v.getBlockState()) {
            return; // don't chisel to the same thing
        }
        if (origState != curState) {
            return; // don't chisel if this doesn't match the target block (for the AOE modes)
        }

        if (held.getItem() instanceof IChiselItem) {
            world.setBlockState(pos, targetState);
//            player.addStat(Statistics.blocksChiseled, 1); // TODO statistics
            boolean breakChisel = false;
            IChiselItem chisel = ((IChiselItem)held.getItem());
            ItemStack current = CarvingUtils.getChiselRegistry().getVariation(curState).getStack();
            current.setCount(1);
            ItemStack target = v.getStack();
            target.setCount(1);
            chisel.craftItem(held, current, target, player);
            chisel.onChisel(player.world, player, held, v);
            if (held.getCount() <= 0) {
                ItemStack targetStack = NBTUtil.getChiselTarget(held);
                player.inventory.mainInventory.set(player.inventory.currentItem, targetStack);
                breakChisel = true;
            }
            if (world.isRemote) {
                SoundUtil.playSound(player, held, targetState);
                ClientUtil.addDestroyEffects(world, pos, curState);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private static ChiselModeGeometryCache cache;
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onBlockHighlight(DrawBlockHighlightEvent event) {
        ItemStack held = event.getPlayer().getHeldItemMainhand();
        if (held.getItem() instanceof IChiselItem && event.getTarget().typeOfHit == Type.BLOCK) {
            EntityPlayer player = event.getPlayer();
            IBlockState state = player.world.getBlockState(event.getTarget().getBlockPos());
            if (state.getBlock() == Blocks.AIR) {
                return;
            }
            
            IChiselMode mode = NBTUtil.getChiselMode(held);
            
            if (cache == null) {
                cache = new ChiselModeGeometryCache(mode, event.getTarget().getBlockPos(), event.getTarget().sideHit);
                Minecraft.getMinecraft().world.addEventListener(cache);
            } else {
                cache.setMode(mode);
                cache.setOrigin(event.getTarget().getBlockPos());
                cache.setSide(event.getTarget().sideHit);
            }
            
            // Don't bother rendering fancy for a single block
            if (cache.size() <= 1) {
                return;
            }
            
            double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.getPartialTicks();
            double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.getPartialTicks();
            double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

            GlStateManager.enableBlend();
            GlStateManager.enableCull();
            GlStateManager.disableTexture2D();

            GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(GL11.GL_GREATER, 0);

            Tessellator.getInstance().getBuffer().setTranslation(-px, -py, -pz);

            GlStateManager.doPolygonOffset(-4, -4);
            GlStateManager.enablePolygonOffset();
            GlStateManager.disableBlend();
            GlStateManager.colorMask(false, false, false, false);
            cache.draw();
            GlStateManager.enableBlend();
            GlStateManager.colorMask(true, true, true, true);
            cache.draw();
            GlStateManager.doPolygonOffset(0, 0);
            GlStateManager.disablePolygonOffset();
            
            Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);

            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            event.setCanceled(true);
        }
    }
    
//    private static final ITextureType CTM_TYPE = TextureTypeRegistry.getType("CTM");

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getWorld().isRemote) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof IChiselItem) {
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
            if (mainhandStack.getItem() instanceof IChiselItem) {
                event.setCanceled(true);
            }
        }
    }
    
    @SubscribeEvent
    public static void onLeftClickEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPainting) {
            ItemStack held = event.getEntityPlayer().getHeldItemMainhand();
            if (held.getItem() instanceof IChiselItem) {
                EntityPainting painting = (EntityPainting) event.getTarget();
                EnumArt[] values = EnumArt.values();
                do {
                    painting.art = values[((painting.art.ordinal() + (event.getEntityPlayer().isSneaking() ? -1 : 1)) + values.length) % values.length];
                    painting.updateFacingWithBoundingBox(painting.facingDirection);
                } while (!painting.onValidSurface());
                damageItem(held, event.getEntityPlayer());
                event.getEntityPlayer().world.playSound(event.getEntityPlayer(), event.getTarget().getPosition(), SoundEvents.ENTITY_PAINTING_PLACE, SoundCategory.NEUTRAL, 1, 1);
                event.setCanceled(true);
            }
        }
    }

    private static void damageItem(ItemStack stack, EntityPlayer player) {
        stack.damageItem(1, player);
        if (stack.getCount() <= 0) {
            player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
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
        if (event.getPlayer().capabilities.isCreativeMode && !stack.isEmpty() && stack.getItem() instanceof IChiselItem) {
            event.setCanceled(true);
        }
    }
}
