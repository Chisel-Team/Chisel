package team.chisel.common.util;

import java.util.List;

import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import team.chisel.common.item.ItemChisel;

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
            else if (event.getTarget() instanceof EntityWitherSkeleton) {
                EntityWitherSkeleton spooky = (EntityWitherSkeleton) event.getTarget();
                List<EntityWitherSkeleton> scary = spooky.getEntityWorld().getEntitiesWithinAABB(EntityWitherSkeleton.class, spooky.getEntityBoundingBox().expandXyz(3));
                if (scary.size() >= 3) {
                    BlockPos pos = spooky.getPosition();
                    for (EntityWitherSkeleton skeleton : scary) {
                        skeleton.setHealth(0);
                    }
                    spooky.getEntityWorld().spawnEntity(new EntityLightningBolt(spooky.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), true));
                    EntityWither wither = new EntityWither(event.getEntityPlayer().getEntityWorld());
                    wither.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 0.55D, pos.getZ() + 0.5D, 0.0f, 0.0f);
                    wither.renderYawOffset = 0.0f;
                    wither.ignite();
                    event.getEntityPlayer().getEntityWorld().spawnEntity(new EntityWither(spooky.getEntityWorld()));
                    event.getEntityPlayer().sendMessage(new TextComponentTranslation("chisel.spawnwither").setStyle(new Style().setColor(TextFormatting.RED)));
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
