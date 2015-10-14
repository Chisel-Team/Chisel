package team.chisel.client;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import team.chisel.block.BlockGrimstone;
import team.chisel.block.BlockHolystone;
import team.chisel.block.BlockLavastone;
import team.chisel.block.BlockSnakestoneObsidian;
import team.chisel.block.tileentity.TileEntityAutoChisel;
import team.chisel.carving.Carving;
import team.chisel.config.Configurations;
import team.chisel.entity.fx.EntityBallOMossFX;
import team.chisel.entity.fx.EntityGrimstoneFX;
import team.chisel.entity.fx.EntityHolystoneFX;
import team.chisel.entity.fx.EntitySnakestoneObsidianFX;
import team.chisel.utils.GeneralClient;

public class GeneralChiselClient {

	public static Random rand = new Random();
	public static int tick = 0;

	public static void spawnLavastoneFX(World world, BlockLavastone block, int x, int y, int z) {
		if (Configurations.particlesTickrate != 0 && tick++ % Configurations.particlesTickrate != 0 || Minecraft.getMinecraft().gameSettings.particleSetting != 0)
			return;

		float f = 0.15F;
		double x1 = x + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0F) + f + block.getBlockBoundsMinX();
		double y1 = y + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0F) + f + block.getBlockBoundsMinY();
		double z1 = z + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0F) + f + block.getBlockBoundsMinZ();

		switch (rand.nextInt(6)) {
		case 0:
			y1 = y + block.getBlockBoundsMinY() - f;
			y--;
			break;
		case 1:
			y1 = y + block.getBlockBoundsMaxY() + f;
			y++;
			break;
		case 2:
			z1 = z + block.getBlockBoundsMinZ() - f;
			z--;
			break;
		case 3:
			z1 = z + block.getBlockBoundsMaxZ() + f;
			z++;
			break;
		case 4:
			x1 = x + block.getBlockBoundsMinX() - f;
			x--;
			break;
		case 5:
			x1 = x + block.getBlockBoundsMaxX() + f;
			x++;
			break;
		}

		if (world.getBlock(x, y, z).isOpaqueCube())
			return;

		EntityLavaFX res = new EntityLavaFX(world, x1, y1, z1);
		double multiplier = 0.45;
		res.motionX = -multiplier * (x1 - x - 0.5);
		res.motionY = multiplier * (y1 - y - 0.5);
		res.motionZ = -multiplier * (z1 - z - 0.5);

		Minecraft.getMinecraft().effectRenderer.addEffect(res);
	}

	public static void spawnHolystoneFX(World world, BlockHolystone block, int x, int y, int z) {
		if (Configurations.particlesTickrate != 0 && tick++ % Configurations.particlesTickrate != 0 || Minecraft.getMinecraft().gameSettings.particleSetting != 0)
			return;

		float f = 0.15F;
		double x1 = x + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0F) + f + block.getBlockBoundsMinX();
		double y1 = y + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0F) + f + block.getBlockBoundsMinY();
		double z1 = z + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0F) + f + block.getBlockBoundsMinZ();

		switch (rand.nextInt(6)) {
		case 0:
			y1 = y + block.getBlockBoundsMinY() - f;
			y--;
			break;
		case 1:
			y1 = y + block.getBlockBoundsMaxY() + f;
			y++;
			break;
		case 2:
			z1 = z + block.getBlockBoundsMinZ() - f;
			z--;
			break;
		case 3:
			z1 = z + block.getBlockBoundsMaxZ() + f;
			z++;
			break;
		case 4:
			x1 = x + block.getBlockBoundsMinX() - f;
			x--;
			break;
		case 5:
			x1 = x + block.getBlockBoundsMaxX() + f;
			x++;
			break;
		}

		if (world.getBlock(x, y, z).isOpaqueCube())
			return;

		EntityHolystoneFX res = new EntityHolystoneFX(world, block, x1, y1, z1);
		Minecraft.getMinecraft().effectRenderer.addEffect(res);
	}

	public static void spawnGrimstoneFX(World world, BlockGrimstone block, int x, int y, int z) {
		if (Configurations.particlesTickrate != 0 && tick++ % Configurations.particlesTickrate != 0)
			return;

		float f = 0.15F;
		double x1 = x + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0F) + f + block.getBlockBoundsMinX();
		double y1 = y + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0F) + f + block.getBlockBoundsMinY();
		double z1 = z + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0F) + f + block.getBlockBoundsMinZ();

		switch (rand.nextInt(6)) {
		case 0:
			y1 = y + block.getBlockBoundsMinY() - f;
			y--;
			break;
		case 1:
			y1 = y + block.getBlockBoundsMaxY() + f;
			y++;
			break;
		case 2:
			z1 = z + block.getBlockBoundsMinZ() - f;
			z--;
			break;
		case 3:
			z1 = z + block.getBlockBoundsMaxZ() + f;
			z++;
			break;
		case 4:
			x1 = x + block.getBlockBoundsMinX() - f;
			x--;
			break;
		case 5:
			x1 = x + block.getBlockBoundsMaxX() + f;
			x++;
			break;
		}

		if (world.getBlock(x, y, z).isOpaqueCube())
			return;

		EntityGrimstoneFX res = new EntityGrimstoneFX(world, block, x1, y1, z1);
		Minecraft.getMinecraft().effectRenderer.addEffect(res);
	}

	public static void spawnSnakestoneObsidianFX(World world, BlockSnakestoneObsidian block, int x, int y, int z) {
		if (Configurations.particlesTickrate != 0 || tick++ % Configurations.particlesTickrate != 0 || Minecraft.getMinecraft().gameSettings.particleSetting != 0) {
			EntitySnakestoneObsidianFX res = new EntitySnakestoneObsidianFX(world, block, x, y, z);
			Minecraft.getMinecraft().effectRenderer.addEffect(res);
		}
	}

	public static void spawnBallOMossFX(World world, double x, double y, double z) {
		if (Configurations.particlesTickrate == 0 || tick++ % Configurations.particlesTickrate == 0 || Minecraft.getMinecraft().gameSettings.particleSetting != 0) {
			EntityBallOMossFX res = new EntityBallOMossFX(world, x, y, z);
			Minecraft.getMinecraft().effectRenderer.addEffect(res);
		}
	}

	public static EntityDiggingFX addBlockHitEffects(World world, int x, int y, int z, int side) {
		Block block = world.getBlock(x, y, z);
		if (block.isAir(world, x, y, z) || Minecraft.getMinecraft().gameSettings.particleSetting != 0)
			return null;

		EffectRenderer renderer = Minecraft.getMinecraft().effectRenderer;

		float f = 0.1F;
		double d0 = x + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0F) + f + block.getBlockBoundsMinX();
		double d1 = y + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0F) + f + block.getBlockBoundsMinY();
		double d2 = z + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0F) + f + block.getBlockBoundsMinZ();

		switch (side) {
		case 0:
			d1 = y + block.getBlockBoundsMinY() - f;
			break;
		case 1:
			d1 = y + block.getBlockBoundsMaxY() + f;
			break;
		case 2:
			d2 = z + block.getBlockBoundsMinZ() - f;
			break;
		case 3:
			d2 = z + block.getBlockBoundsMaxZ() + f;
			break;
		case 4:
			d0 = x + block.getBlockBoundsMinX() - f;
			break;
		case 5:
			d0 = x + block.getBlockBoundsMaxX() + f;
			break;
		}

		EntityDiggingFX res = new EntityDiggingFX(world, d0, d1, d2, 0.0D, 0.0D, 0.0D, block, world.getBlockMetadata(x, y, z), side);
		res.motionX = d0 - (x + 0.5);
		res.motionY = d1 - (y + 0.5);
		res.motionZ = d2 - (z + 0.5);

		renderer.addEffect(res);

		return res;
	}

	public static void spawnChiselEffect(int x, int y, int z, String sound) {
		World world = Minecraft.getMinecraft().theWorld;
		for (int side = 0; side < 6; side++) {
			for (int j = 0; j < 16; j++) {
				EntityDiggingFX fx = addBlockHitEffects(Minecraft.getMinecraft().theWorld, x, y, z, side);
				if (fx == null)
					return;

				fx.multipleParticleScaleBy(0.25f + 0.5f * rand.nextFloat());
				fx.multiplyVelocity(0.3f * rand.nextFloat());
			}
		}

		GeneralClient.playChiselSound(world, x, y, z, sound);
	}

	public static void spawnAutoChiselFX(TileEntityAutoChisel te, ItemStack base) {
		if (base != null && Minecraft.getMinecraft().gameSettings.particleSetting == 0) {
			for (int i = 0; i < 10; i++) {
				EntityDiggingFX particle = new EntityDiggingFX(te.getWorldObj(), te.xCoord + 0.5, te.yCoord + 0.95, te.zCoord + 0.5, 0, 0, 0, Block.getBlockFromItem(base.getItem()),
						base.getItemDamage());
				particle.setVelocity((te.getWorldObj().rand.nextDouble() / 4) - 0.125, te.getWorldObj().rand.nextDouble() / 8, (te.getWorldObj().rand.nextDouble() / 4) - 0.125);
				Minecraft.getMinecraft().effectRenderer.addEffect(particle);
			}
			String sound = Carving.chisel.getVariationSound(base);
			GeneralClient.playChiselSound(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord, sound);
		}
	}
}
