package team.chisel.item;

import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import team.chisel.Chisel;
import team.chisel.api.ChiselTabs;
import team.chisel.api.ICarvable;
import team.chisel.api.carving.IVariationInfo;
import team.chisel.api.rendering.IOffsetRendered;
import team.chisel.api.rendering.IShaderRenderItem;
import team.chisel.api.rendering.TextureType;
import team.chisel.utils.NBTSaveable;
import team.chisel.utils.PerChunkData;
import team.chisel.utils.PerChunkData.ChunkDataBase;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class ItemOffsetTool extends Item implements IShaderRenderItem {

	public static class OffsetData implements NBTSaveable {

		private int xOffset, yOffset, zOffset;

		@Override
		public void write(NBTTagCompound tag) {
			tag.setShort("offset", (short) (xOffset << 8 | yOffset << 4 | zOffset));
		}

		@Override
		public void read(NBTTagCompound tag) {
			short offset = tag.getShort("offset");
			xOffset = (offset >> 8) & 0xF;
			yOffset = (offset >> 4) & 0xF;
			zOffset = offset & 0xF;
		}

		public void move(ForgeDirection dir) {
			xOffset = updateValue(xOffset, dir.offsetX);
			yOffset = updateValue(yOffset, dir.offsetY);
			zOffset = updateValue(zOffset, dir.offsetZ);
		}

		public int getOffsetX() {
			return xOffset;
		}

		public int getOffsetY() {
			return yOffset;
		}

		public int getOffsetZ() {
			return zOffset;
		}

		private int updateValue(int current, int move) {
			current -= move;
			current %= 16;
			if (current < 0) {
				current += 16;
			}
			return current;
		}
	}

	public static final String DATA_KEY = "offsettool";
	private static final List<TextureType> validTypes = Lists.newArrayList(TextureType.V4, TextureType.V9 /* SOON, TextureType.V16 */);
	
    private IIcon overlay;

	public ItemOffsetTool() {
		super();
		setCreativeTab(ChiselTabs.tabChisel);
		setUnlocalizedName("chisel.offsettool");
		setTextureName(Chisel.MOD_ID + ":tools/offsetTool_mask");
		setFull3D();
		PerChunkData.INSTANCE.registerChunkData(DATA_KEY, new ChunkDataBase<OffsetData>(OffsetData.class, true));
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		if (block instanceof ICarvable) {
			if (world.isRemote) {
				return canOffset(player, world, x, y, z, side);
			} else {
				ChunkDataBase<OffsetData> cd = PerChunkData.INSTANCE.getData(DATA_KEY);
				OffsetData data = cd.getDataForChunk(world.getChunkFromBlockCoords(x, z));
				ForgeDirection face = ForgeDirection.getOrientation(side);
				data.move(getMoveDir(face, hitX, hitY, hitZ));
				PerChunkData.INSTANCE.chunkModified(world.getChunkFromBlockCoords(x, z), DATA_KEY);
			}
		}
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	public ForgeDirection getMoveDir(ForgeDirection face, double xCoord, double yCoord, double zCoord) {
		Map<Double, ForgeDirection> map = Maps.newHashMap();
		if (face.offsetX != 0) {
			fillMap(map, zCoord, yCoord, DOWN, UP, NORTH, SOUTH);
		} else if (face.offsetY != 0) {
			fillMap(map, xCoord, zCoord, NORTH, SOUTH, WEST, EAST);
		} else if (face.offsetZ != 0) {
			fillMap(map, xCoord, yCoord, DOWN, UP, WEST, EAST);
		}
		List<Double> keys = Lists.newArrayList(map.keySet());
		Collections.sort(keys);
		return map.get(keys.get(0));
	}

	private void fillMap(Map<Double, ForgeDirection> map, double x, double y, ForgeDirection... dirs) {
		map.put(Line2D.ptLineDistSq(0, 0, 1, 0, x, y), dirs[0]);
		map.put(Line2D.ptLineDistSq(0, 1, 1, 1, x, y), dirs[1]);
		map.put(Line2D.ptLineDistSq(0, 0, 0, 1, x, y), dirs[2]);
		map.put(Line2D.ptLineDistSq(1, 0, 1, 1, x, y), dirs[3]);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onBlockHighlight(DrawBlockHighlightEvent event) {
		MovingObjectPosition mop = event.target;
		EntityPlayer player = event.player;

		if (canOffset(player, player.worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit)) {

			ForgeDirection face = ForgeDirection.getOrientation(mop.sideHit);
			Tessellator tess = Tessellator.instance;
			GL11.glPushMatrix();
			GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDepthMask(false);

			// Draw the X
			tess.startDrawing(GL11.GL_LINES);
			tess.setColorOpaque_I(0);

			double px = -(player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks);
			double py = -(player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks);
			double pz = -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks);
			double x = Math.max(0, face.offsetX) + (0.01 * face.offsetX);
			double y = Math.max(0, face.offsetY) + (0.01 * face.offsetY);
			double z = Math.max(0, face.offsetZ) + (0.01 * face.offsetZ);

			GL11.glTranslated(px, py, pz);
			GL11.glTranslated(mop.blockX, mop.blockY, mop.blockZ);
			if (face.offsetX != 0) {
				tess.addVertex(x, 0, 0);
				tess.addVertex(x, 1, 1);
				tess.addVertex(x, 1, 0);
				tess.addVertex(x, 0, 1);
			} else if (face.offsetY != 0) {
				tess.addVertex(0, y, 0);
				tess.addVertex(1, y, 1);
				tess.addVertex(1, y, 0);
				tess.addVertex(0, y, 1);
			} else {
				tess.addVertex(0, 0, z);
				tess.addVertex(1, 1, z);
				tess.addVertex(1, 0, z);
				tess.addVertex(0, 1, z);
			}
			tess.draw();

			Vec3 hit = mop.hitVec;

			// Draw the triangle highlight
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_CULL_FACE);
			tess.startDrawing(GL11.GL_TRIANGLES);
			tess.setColorRGBA_I(0xFFFFFF, 0x55);
			ForgeDirection moveDir = getMoveDir(face, hit.xCoord - mop.blockX, hit.yCoord - mop.blockY, hit.zCoord - mop.blockZ);
			int clampedX = Math.max(0, moveDir.offsetX);
			int clampedY = Math.max(0, moveDir.offsetY);
			int clampedZ = Math.max(0, moveDir.offsetZ);
			boolean isX = moveDir.offsetX != 0;
			boolean isY = moveDir.offsetY != 0;
			boolean isZ = moveDir.offsetZ != 0;

			// Always draw the center point first, then draw the next two points.
			// Use either the move dir offset, or 0/1 if the move dir is not offset in this direction
			if (face.offsetX != 0) {
				tess.addVertex(x, 0.5, 0.5);
				tess.addVertex(x, isY ? clampedY : 0, isZ ? clampedZ : 0);
				tess.addVertex(x, isY ? clampedY : 1, isZ ? clampedZ : 1);
			} else if (face.offsetY != 0) {
				tess.addVertex(0.5, y, 0.5);
				tess.addVertex(isX ? clampedX : 0, y, isZ ? clampedZ : 0);
				tess.addVertex(isX ? clampedX : 1, y, isZ ? clampedZ : 1);
			} else {
				tess.addVertex(0.5, 0.5, z);
				tess.addVertex(isX ? clampedX : 0, isY ? clampedY : 0, z);
				tess.addVertex(isX ? clampedX : 1, isY ? clampedY : 1, z);
			}
			tess.draw();
			GL11.glPopAttrib();
			GL11.glPopMatrix();
		}
	}

	private boolean canOffset(EntityPlayer player, World world, int x, int y, int z, int side) {
		Block block = world.getBlock(x, y, z);
		if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == this && block instanceof ICarvable) {
			ICarvable carvable = (ICarvable) block;
			IVariationInfo info = carvable.getManager(player.worldObj.getBlockMetadata(x, y, z));
			if (info.getManager() instanceof IOffsetRendered) {
				return ((IOffsetRendered) info.getManager()).canOffset(world, x, y, z, side);
			} else if (carvable instanceof IOffsetRendered) {
				return ((IOffsetRendered) carvable).canOffset(world, x, y, z, side);
			}
			return validTypes.contains(info.getType());
		}
		return false;
	}

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        this.overlay = iconRegister.registerIcon(Chisel.MOD_ID + ":tools/offsetTool_anim");
    }

	@Override
	public IIcon getOverlayTexture(ItemStack stack, EntityPlayer player) {
		return overlay;
	}

	@Override
	public float getMaskMultiplier(ItemStack stack, EntityPlayer player) {
		return 1.0f;
	}
}
