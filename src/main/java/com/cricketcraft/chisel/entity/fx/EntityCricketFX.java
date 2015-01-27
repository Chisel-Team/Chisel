package com.cricketcraft.chisel.entity.fx;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.utils.General;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityCricketFX extends EntityFX {

	private double tx, ty, tz, speed;

	public EntityCricketFX(World world, double x, double y, double z) {
		super(world, x + 1, y + 1, z + 1);
		particleScale = 0.5F + 0.5F * General.rand.nextFloat();
		speed = 0.04 + 0.04 * General.rand.nextDouble();

		particleMaxAge = (int) (Math.random() * 10.0D) + 40;

		tx = x + General.rand.nextDouble();
		ty = y + General.rand.nextDouble();
		tz = z + General.rand.nextDouble();

		switch (General.rand.nextInt(6)) {
		case 0:
			tx = x;
			break;
		case 1:
			tx = x + 1;
			break;
		case 2:
			ty = y;
			break;
		case 3:
			ty = y + 1;
			break;
		case 4:
			tz = z;
			break;
		case 5:
			tz = z + 1;
			break;
		}

		double dx = (tx - posX) * 3;
		double dy = (ty - posY) * 3;
		double dz = (tz - posZ) * 3;

		setPosition(posX + dx, posY + dy + 8, posZ + dz);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		noClip = true;
	}

	@Override
	public int getFXLayer() {
		return 1;
	}

	@Override
	public void renderParticle(Tessellator tessellator, float partialTick, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
		super.renderParticle(tessellator, partialTick, rotX, rotXZ, rotZ, rotYZ, rotXY);
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Chisel.MOD_ID + ":/assets/chisel/blocks/snakestone/obsidian/particles/" + General.rand.nextInt(8) + ".png"));
		if (particleAge++ >= particleMaxAge) {
			setDead();
			return;
		}

		double dx = tx - posX;
		double dy = ty - posY;
		double dz = tz - posZ;

		double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

		if (distance == 0) {
			setDead();
			return;
		}

		if (distance < 0.4) {
			particleAlpha = (float) (distance / 0.4);
		} else if (particleAge < 20) {
			particleAlpha = 1.0F * particleAge / 20;
		} else {
			particleAlpha = 1.0F;
		}

		if (distance < speed) {
			speed = distance;
		}

		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		double px = posX + dx / distance * speed;
		double py = posY + dy / distance * speed;
		double pz = posZ + dz / distance * speed;

		setPosition(px, py, pz);
	}
}
