package team.chisel.common.item;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;
import team.chisel.Chisel;
import team.chisel.api.IChiselItem;
import team.chisel.api.block.ICarvable;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingGroup;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.api.carving.IChiselMode;
import team.chisel.api.carving.IVariationRegistry;
import team.chisel.client.ClientProxy;
import team.chisel.common.util.NBTUtil;
import team.chisel.common.util.SoundUtil;

public class ChiselController {
    
    @SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {

        Player player = event.getPlayer();
        ItemStack held = event.getItemStack();
        
        if (held.getItem() instanceof IChiselItem) {

            ItemStack target = NBTUtil.getChiselTarget(held);
            IChiselItem chisel = (IChiselItem) held.getItem();
            
            IVariationRegistry registry = CarvingUtils.getChiselRegistry();
            BlockState state = event.getWorld().getBlockState(event.getPos());
            
            if (!chisel.canChiselBlock(event.getWorld(), player, event.getHand(), event.getPos(), state)) {
                return;
            }
            
            ICarvingGroup blockGroup = state.getBlock() instanceof ICarvable ? ((ICarvable)state.getBlock()).getVariation().getGroup() : registry.getGroup(state.getBlock()).orElse(null);
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
                        setAll(candidates, player, state, variation);
                        event.setCanceled(true);
                        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientProxy::resetWaitTimer);
                    } else {
                        Chisel.logger.warn("Found itemstack {} in group {}, but it has no variation!", target, sourceGroup.getId());
                    }
                }
            } else {
                List<Block> variations = blockGroup.getBlockTag().stream().toList();
                
                variations = variations.stream().filter(v -> v != null).collect(Collectors.toList());

                int index = variations.indexOf(state.getBlock());
                index = player.isShiftKeyDown() ? index - 1 : index + 1;
                index = (index + variations.size()) % variations.size();
                
                ICarvingVariation next = registry.getVariation(variations.get(index)).orElse(null);
                setAll(candidates, player, state, next);
                event.setCanceled(true);
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientProxy::resetWaitTimer);
            }
        }
    }

    private static void setAll(Iterable<? extends BlockPos> candidates, Player player, BlockState origState, ICarvingVariation v) {
        if (!checkHackyCache(player)) return;
        for (BlockPos pos : candidates) {
            setVariation(player, pos, origState, v);
        }
    }

    private static final LoadingCache<Player, Long> HACKY_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .weakKeys()
            .build(new CacheLoader<Player, Long>() {
                
                public Long load(Player key) throws Exception {
                    return 0L;
                }
            });
    
    private static boolean checkHackyCache(Player player) {
        long time = player.getCommandSenderWorld().getGameTime();
        // TODO this is a hack (duh) and it prevents rapid clicking, but it fixes the block changing twice for every click
        // Until the left click event is improved in forge, not much else we can do
        if (HACKY_CACHE.getUnchecked(player) > time - 2) {
            return false; // Avoid double actions
        }
        HACKY_CACHE.put(player, time);
        return true;
    }
    
    /**
     * Assumes that the player is holding a chisel
     */
    private static void setVariation(Player player, BlockPos pos, BlockState origState, ICarvingVariation v) {
        Block targetBlock = v.getBlock();
        Preconditions.checkNotNull(targetBlock, "Variation state cannot be null!");
        
        Level world = player.level;
        
        BlockState curState = world.getBlockState(pos);
        ItemStack held = player.getMainHandItem();
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
            chisel.craftItem(held, current, target, player, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            chisel.onChisel(player.level, player, held, v);
            if (held.getCount() <= 0) {
                ItemStack targetStack = NBTUtil.getChiselTarget(held);
                player.getInventory().items.set(player.getInventory().selected, targetStack);
            }
            if (world.isClientSide) {
                SoundUtil.playSound(player, held, targetBlock);
                world.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(origState));
            }
            world.setBlockAndUpdate(pos, targetBlock.defaultBlockState());
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
            Player player = event.getPlayer();
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

            RenderSystem.enableBlend();
            RenderSystem.enableCull();
            RenderSystem.disableTexture2D();

            RenderSystem.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            RenderSystem.enableAlpha();
            RenderSystem.alphaFunc(GL11.GL_GREATER, 0);

            Tessellator.getInstance().getBuffer().setTranslation(-px, -py, -pz);

            RenderSystem.doPolygonOffset(-4, -4);
            RenderSystem.enablePolygonOffset();
            RenderSystem.disableBlend();
            RenderSystem.colorMask(false, false, false, false);
            cache.draw();
            RenderSystem.enableBlend();
            RenderSystem.colorMask(true, true, true, true);
            cache.draw();
            RenderSystem.doPolygonOffset(0, 0);
            RenderSystem.disablePolygonOffset();
            
            Tessellator.getInstance().getBuffer().setTranslation(0, 0, 0);

            RenderSystem.enableTexture2D();
            RenderSystem.enableDepth();
            event.setCanceled(true);
        }
    }
    */
//    private static final ITextureType CTM_TYPE = TextureTypeRegistry.getType("CTM");

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!event.getWorld().isClientSide) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() instanceof IChiselItem) {
                IChiselItem chisel = (IChiselItem) stack.getItem();
                if (chisel.canOpenGui(event.getWorld(), event.getPlayer(), event.getHand())) {
                    event.getPlayer().openMenu(chisel.getGuiType(event.getWorld(), event.getPlayer(), event.getHand()).provide(stack, event.getHand()));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() == InteractionHand.OFF_HAND) {
            ItemStack mainhandStack = event.getPlayer().getMainHandItem();
            if (mainhandStack.getItem() instanceof IChiselItem) {
                event.setCanceled(true);
            }
        }
    }
    
    private static final MethodHandle _updateFacingWithBoundingBox; static {
        try {
            _updateFacingWithBoundingBox = MethodHandles.lookup().unreflect(ObfuscationReflectionHelper.findMethod(HangingEntity.class, "setDirection", Direction.class));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    @SubscribeEvent
    public static void onLeftClickEntity(AttackEntityEvent event) {
        if (event.getTarget() instanceof Painting) {
            ItemStack held = event.getPlayer().getMainHandItem();
            if (held.getItem() instanceof IChiselItem) {
                Painting painting = (Painting) event.getTarget();
                List<Motive> values = new ArrayList<>(ForgeRegistries.PAINTING_TYPES.getValues());
                do {
                    painting.motive = values.get(((values.indexOf(painting.motive) + (event.getPlayer().isShiftKeyDown() ? -1 : 1)) + values.size()) % values.size());
                    try {
                        _updateFacingWithBoundingBox.invokeExact((HangingEntity) painting, painting.getDirection());
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                } while (!painting.survives());
                damageItem(held, event.getPlayer());
                event.getPlayer().level.playSound(event.getPlayer(), event.getTarget().blockPosition(), SoundEvents.PAINTING_PLACE, SoundSource.NEUTRAL, 1, 1);
                event.setCanceled(true);
            }
        }
    }

    private static void damageItem(ItemStack stack, Player player) {
        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        if (stack.getCount() <= 0) {
            player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            ForgeEventFactory.onPlayerDestroyItem(player, stack, InteractionHand.MAIN_HAND);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        ItemStack stack = event.getPlayer().getMainHandItem();
        if (event.getPlayer().getAbilities().instabuild && !stack.isEmpty() && stack.getItem() instanceof IChiselItem) {
            event.setCanceled(true);
        }
    }
}
