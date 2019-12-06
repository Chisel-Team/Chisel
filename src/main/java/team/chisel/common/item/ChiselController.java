package team.chisel.common.item;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.PaintingType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;
import team.chisel.api.carving.IVariationRegistry;
import team.chisel.common.util.NBTUtil;
import team.chisel.common.util.SoundUtil;

@ParametersAreNonnullByDefault
public class ChiselController {
    
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        PlayerEntity player = event.getPlayer();
        ItemStack held = event.getItemStack();
        
        if (held.getItem() instanceof IChiselItem) {

            ItemStack target = NBTUtil.getChiselTarget(held);
            IChiselItem chisel = (IChiselItem) held.getItem();
            
            IVariationRegistry registry = CarvingUtils.getChiselRegistry();
            BlockState state = event.getWorld().getBlockState(event.getPos());
            
            if (!chisel.canChiselBlock(event.getWorld(), player, event.getHand(), event.getPos(), state)) {
                return;
            }
            
            ICarvingGroup blockGroup = registry.getGroup(state.getBlock()).orElse(null);
            if (blockGroup == null) {
                return;
            }
            
            IChiselMode mode = NBTUtil.getChiselMode(held);
            Iterable<? extends BlockPos> candidates = mode.getCandidates(player, event.getPos(), event.getFace());
            
            if (!target.isEmpty()) {
                ICarvingGroup sourceGroup = registry.getGroup(target.getItem()).orElse(null);

                if (blockGroup == sourceGroup) {
                    ICarvingVariation variation = registry.getVariation(target.getItem()).orElse(null);
                    if (variation != null) {
                        if (variation.getBlock() != null) {
                            setAll(candidates, player, state, variation);
                        }
                    } else {
                        Chisel.logger.warn("Found itemstack {} in group {}, but it has no variation!", target, sourceGroup.getId());
                    }
                }
            } else {
                List<Block> variations = new ArrayList<>(blockGroup.getBlocks());
                
                variations = variations.stream().filter(v -> v.getBlock() != null).collect(Collectors.toList());
                        
                int index = variations.indexOf(state.getBlock());
                index = player.isSneaking() ? index - 1 : index + 1;
                index = (index + variations.size()) % variations.size();
                
                ICarvingVariation next = registry.getVariation(variations.get(index)).orElse(null);
                setAll(candidates, player, state, next);
            }
        }
    }

    private static void setAll(Iterable<? extends BlockPos> candidates, PlayerEntity player, BlockState origState, ICarvingVariation v) {
        for (BlockPos pos : candidates) {
            setVariation(player, pos, origState, v);
        }
    }
    /**
     * Assumes that the player is holding a chisel
     */
    private static void setVariation(PlayerEntity player, BlockPos pos, BlockState origState, ICarvingVariation v) {
        BlockState targetState = v.getBlock().getDefaultState();
        Preconditions.checkNotNull(targetState, "Variation state cannot be null!");
        
        World world = player.world;
        BlockState curState = world.getBlockState(pos);
        ItemStack held = player.getHeldItemMainhand();
        if (curState.getBlock() == v.getBlock()) {
            return; // don't chisel to the same thing
        }
        if (origState != curState) {
            return; // don't chisel if this doesn't match the target block (for the AOE modes)
        }

        if (held.getItem() instanceof IChiselItem) {
//            player.addStat(Statistics.blocksChiseled, 1); // TODO statistics
            IChiselItem chisel = ((IChiselItem)held.getItem());
            ItemStack current = CarvingUtils.getChiselRegistry().getVariation(curState.getBlock()).map(ICarvingVariation::getItem).map(ItemStack::new).orElse(null);
            current.setCount(1);
            ItemStack target = new ItemStack(v.getItem());
            target.setCount(1);
            chisel.craftItem(held, current, target, player, $ -> {}); // TODO 1.14
            chisel.onChisel(player.world, player, held, v);
            if (held.getCount() <= 0) {
                ItemStack targetStack = NBTUtil.getChiselTarget(held);
                player.inventory.mainInventory.set(player.inventory.currentItem, targetStack);
            }
            if (world.isRemote) {
                SoundUtil.playSound(player, held, targetState);
//                ClientUtil.addDestroyEffects(world, pos, curState); TODO 1.14
            }
            world.setBlockState(pos, targetState);
        }
    }
    /*
    @SideOnly(Side.CLIENT)
    private static ChiselModeGeometryCache cache;
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onBlockHighlight(DrawBlockHighlightEvent event) {
        ItemStack held = event.getPlayer().getHeldItemMainhand();
        if (held.getItem() instanceof IChiselItem && event.getTarget().typeOfHit == Type.BLOCK) {
            PlayerEntity player = event.getPlayer();
            BlockState state = player.world.getBlockState(event.getTarget().getBlockPos());
            if (state.getBlock() == Blocks.AIR) {
                return;
            }
            
            IChiselMode mode = NBTUtil.getChiselMode(held);
            
            if (cache == null) {
                cache = new ChiselModeGeometryCache(mode, event.getTarget().getBlockPos(), event.getTarget().sideHit);
                Minecraft.getInstance().world.addEventListener(cache);
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
    */
//    private static final ITextureType CTM_TYPE = TextureTypeRegistry.getType("CTM");

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getWorld().isRemote) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof IChiselItem) {
                IChiselItem chisel = (IChiselItem) stack.getItem();
                if (chisel.canOpenGui(event.getWorld(), event.getPlayer(), event.getHand())) {
//                    event.getPlayer().openGui(Chisel.instance, 0, event.getWorld(), event.getHand().ordinal(), 0, 0); TODO 1.14
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == Hand.OFF_HAND) {
            ItemStack mainhandStack = event.getPlayer().getHeldItemMainhand();
            if (mainhandStack.getItem() instanceof IChiselItem) {
                event.setCanceled(true);
            }
        }
    }
    
    private static final MethodHandle _updateFacingWithBoundingBox; static {
        try {
            _updateFacingWithBoundingBox = MethodHandles.lookup().unreflect(ObfuscationReflectionHelper.findMethod(HangingEntity.class, "func_174859_a", Direction.class));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @SubscribeEvent
    public static void onLeftClickEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof PaintingEntity) {
            ItemStack held = event.getPlayer().getHeldItemMainhand();
            if (held.getItem() instanceof IChiselItem) {
                PaintingEntity painting = (PaintingEntity) event.getTarget();
                List<PaintingType> values = new ArrayList<>(ForgeRegistries.PAINTING_TYPES.getValues());
                do {
                    painting.art = values.get(((values.indexOf(painting.art) + (event.getPlayer().isSneaking() ? -1 : 1)) + values.size()) % values.size());
                    try {
                        _updateFacingWithBoundingBox.invokeExact((HangingEntity) painting, painting.getHorizontalFacing());
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                } while (!painting.onValidSurface());
                damageItem(held, event.getPlayer());
                event.getPlayer().world.playSound(event.getPlayer(), event.getTarget().getPosition(), SoundEvents.ENTITY_PAINTING_PLACE, SoundCategory.NEUTRAL, 1, 1);
                event.setCanceled(true);
            }
        }
    }

    private static void damageItem(ItemStack stack, PlayerEntity player) {
        stack.damageItem(1, player, $ -> {}); // TODO 1.14
        if (stack.getCount() <= 0) {
            player.setHeldItem(Hand.MAIN_HAND, ItemStack.EMPTY);
            ForgeEventFactory.onPlayerDestroyItem(player, stack, Hand.MAIN_HAND);
        }
    }
    
    private static void updateState(World world, BlockPos pos, ItemStack stack, PlayerEntity player, BlockState next) {
        BlockState current = world.getBlockState(pos);
        if (current != next) {
            world.setBlockState(pos, next);
            SoundUtil.playSound(player, stack, next);
            if (world.isRemote) {
//                ClientUtil.addDestroyEffects(world, pos, next); TODO 1.14
            }
        }
    }
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ItemStack stack = event.getPlayer().getHeldItemMainhand();
        if (event.getPlayer().abilities.isCreativeMode && !stack.isEmpty() && stack.getItem() instanceof IChiselItem) {
            event.setCanceled(true);
        }
    }
}
