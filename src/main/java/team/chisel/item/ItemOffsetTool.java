package team.chisel.item;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import team.chisel.api.ChiselTabs;
import team.chisel.api.ICarvable;
import team.chisel.api.rendering.TextureType;
import team.chisel.utils.NBTSaveable;
import team.chisel.utils.PerChunkData;
import team.chisel.utils.PerChunkData.ChunkDataBase;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOffsetTool extends Item {

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
			if (dir.offsetX != 0) {
				xOffset = updateValue(xOffset, dir.offsetX);
			} else if (dir.offsetY != 0) {
				yOffset = updateValue(yOffset, dir.offsetY);
			} else {
				zOffset = updateValue(zOffset, dir.offsetZ);
			}
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
			current += move;
			current %= 16;
			if (current < 0) {
				current += 16;
			}
			return current;
		}
	}

	public static final String DATA_KEY = "offsettool";
	private static final List<TextureType> validTypes = Lists.newArrayList(TextureType.V4, TextureType.V9 /* SOON, TextureType.V16 */);

	public ItemOffsetTool() {
		super();
		setCreativeTab(ChiselTabs.tabChisel);
		setUnlocalizedName("chisel.offsettool");
		setTextureName("offsettool");
		PerChunkData.INSTANCE.registerChunkData(DATA_KEY, new ChunkDataBase<OffsetData>(OffsetData.class, true));
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		if (block instanceof ICarvable) {
			if (world.isRemote) {
				ICarvable carvable = (ICarvable) block;
				if (validTypes.contains(carvable.getManager(world.getBlockMetadata(x, y, z)).getType())) {
					return true;
				}
				return false;
			} else {
				ChunkDataBase<OffsetData> cd = PerChunkData.INSTANCE.getData(DATA_KEY);
				OffsetData data = cd.getDataForChunk(world.getChunkFromBlockCoords(x, z));
				ForgeDirection face = ForgeDirection.getOrientation(side);
				data.move(player.isSneaking() ? face.getOpposite() : face);
				PerChunkData.INSTANCE.chunkModified(world.getChunkFromBlockCoords(x, z), DATA_KEY);
				System.out.println(data.xOffset + " " + data.yOffset + " " + data.zOffset);
			}
		}
		return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	// Currently serves no purpose but eventually will be how we draw the interaction method with this item
	// Draws an X on the face of the block atm
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onBlockHighlight(DrawBlockHighlightEvent event) {
		MovingObjectPosition mop = event.target;
		EntityPlayer player = event.player;

		Block block = player.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
		if (block instanceof ICarvable) {
			ICarvable carvable = (ICarvable) block;
			if (!validTypes.contains(carvable.getManager(player.worldObj.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ)).getType())) {
				return;
			}
		} else {
			return;
		}

		ForgeDirection face = ForgeDirection.getOrientation(mop.sideHit);
		Tessellator tess = Tessellator.instance;
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDepthMask(false);
		tess.startDrawing(GL11.GL_LINES);
		tess.setColorOpaque_I(0);
		double px = -(player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks);
		double py = -(player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks);
		double pz = -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks);
		GL11.glTranslated(px, py, pz);
		GL11.glTranslated(mop.blockX, mop.blockY, mop.blockZ);
		if (face.offsetX != 0) {
			double x = Math.max(0, face.offsetX) + (0.01 * face.offsetX);
			tess.addVertex(x, 0, 0);
			tess.addVertex(x, 1, 1);
			tess.addVertex(x, 1, 0);
			tess.addVertex(x, 0, 1);
		} else if (face.offsetY != 0) {
			double y = Math.max(0, face.offsetY) + (0.01 * face.offsetY);
			tess.addVertex(0, y, 0);
			tess.addVertex(1, y, 1);
			tess.addVertex(1, y, 0);
			tess.addVertex(0, y, 1);
		} else {
			double z = Math.max(0, face.offsetZ) + (0.01 * face.offsetZ);
			tess.addVertex(0, 0, z);
			tess.addVertex(1, 1, z);
			tess.addVertex(1, 0, z);
			tess.addVertex(0, 1, z);
		}
		tess.draw();
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}
}
