package team.chisel.common.util;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.common.item.ItemChisel;

import java.util.List;

/**
 * Just some little things for chiseling entities
 */
public enum EntityChiseling {

    INSTANCE;


    @SubscribeEvent
    public void onPlayerHitEntity(AttackEntityEvent event){
        if (event.getEntityPlayer().getEntityWorld().isRemote){
            return;
        }
        if (isChisel(event.getEntityPlayer().getHeldItemMainhand()) || isChisel(event.getEntityPlayer().getHeldItemOffhand())){
            if (event.getTarget() instanceof EntitySheep){
                EntitySheep sheep = (EntitySheep) event.getTarget();
                sheep.setFleeceColor(cycleColor(sheep.getFleeceColor()));
                event.setCanceled(true);

            }
            else if (event.getTarget() instanceof EntitySkeleton) {
                EntitySkeleton spooky = (EntitySkeleton) event.getTarget();
                if (spooky.func_189771_df() == SkeletonType.WITHER){
                    List<EntitySkeleton> scary = spooky.getEntityWorld().getEntitiesWithinAABB(EntitySkeleton.class, spooky.getEntityBoundingBox().expandXyz(3));
                    int count = 0;
                    for (EntitySkeleton skeleton : scary){
                        if (skeleton.func_189771_df() == SkeletonType.WITHER){
                            count++;
                        }
                    }
                    if (count >= 3){
                        BlockPos pos = spooky.getPosition();
                        for (EntitySkeleton skeleton : scary){
                            if (skeleton.func_189771_df() == SkeletonType.WITHER){
                                skeleton.setHealth(0);
                            }
                        }
                        spooky.getEntityWorld().spawnEntityInWorld(new EntityLightningBolt(spooky.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), true));
                        EntityWither wither = new EntityWither(event.getEntityPlayer().getEntityWorld());
                        wither.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 0.55D, pos.getZ() + 0.5D, 0.0f, 0.0f);
                        wither.renderYawOffset = 0.0f;
                        wither.ignite();
                        event.getEntityPlayer().getEntityWorld().spawnEntityInWorld(new EntityWither(spooky.getEntityWorld()));
                        event.getEntityPlayer().addChatComponentMessage(new TextComponentTranslation("chisel.spawnwither").setStyle(new Style().setColor(TextFormatting.RED)));
                    }
                }
            }
        }
    }

    private static boolean isChisel(ItemStack stack){
        return (stack != null && stack.getItem() instanceof ItemChisel);
    }

    private static EnumDyeColor cycleColor(EnumDyeColor color){
        return EnumDyeColor.byMetadata( (color.getMetadata() + 1 ) % 16 );
    }
}
