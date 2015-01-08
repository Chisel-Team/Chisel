package com.cricketcraft.chisel.item;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

import org.apache.commons.lang3.ArrayUtils;

import com.cricketcraft.chisel.Chisel;
import com.cricketcraft.chisel.PacketHandler;
import com.cricketcraft.chisel.api.ChiselMode;
import com.cricketcraft.chisel.api.IChiselMode;
import com.cricketcraft.chisel.carving.Carving;
import com.cricketcraft.chisel.carving.CarvingVariation;
import com.cricketcraft.chisel.client.GeneralChiselClient;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemChisel extends Item implements IChiselMode {

	public static Carving carving = Carving.chisel;

	@SideOnly(Side.CLIENT)
	private IIcon ironIcon;
	@SideOnly(Side.CLIENT)
	private IIcon diamondIcon;

	public ItemChisel() {
		super();
		setMaxStackSize(1);
		setHasSubtypes(true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) {
		ironIcon = register.registerIcon(Chisel.MOD_ID + ":chisel");
		diamondIcon = register.registerIcon(Chisel.MOD_ID + ":diamondChisel");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		return getIconFromDamage(stack.getItemDamage());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage) {
		return damage == 0 ? ironIcon : diamondIcon;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		list.add(new ItemStack(item, 1, 0));
		list.add(new ItemStack(item, 1, 1));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String name = stack.getItemDamage() == 0 ? "chisel" : "diamondChisel";
		return "item." + name;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held) {
		list.add(StatCollector.translateToLocal(getUnlocalizedName(stack) + ".desc"));
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		entityplayer.openGui(Chisel.instance, 0, world, 0, 0, 0);

		return itemstack;
	}

	@SubscribeEvent
	public void onLeftClickBlock(PlayerInteractEvent event) {
		ItemStack held = event.entityPlayer.getCurrentEquippedItem();
		int slot = event.entityPlayer.inventory.currentItem;
		// TODO config for diamond only
		if (event.action == Action.LEFT_CLICK_BLOCK && held != null && held.getItem() == this) {
			int x = event.x, y = event.y, z = event.z;
			Block block = event.world.getBlock(x, y, z);
			int metadata = event.world.getBlockMetadata(x, y, z);
			CarvingVariation[] variations = carving.getVariations(block, metadata);

			if (variations != null) {
				ItemStack target = getTarget(held);

				if (target != null) {
					for (CarvingVariation v : variations) {
						if (v.block == Block.getBlockFromItem(target.getItem()) && v.meta == target.getItemDamage()) {
							setVariation(event.entityPlayer, event.world, x, y, z, v);
						}
					}
				} else {
					for (int i = 0; i < variations.length; i++) {
						CarvingVariation v = variations[i];
						if (v.block == block && v.meta == metadata) {
							variations = ArrayUtils.remove(variations, i--);
						}
					}

					int index = event.world.rand.nextInt(variations.length);
					CarvingVariation newVar = variations[index];
					setVariation(event.entityPlayer, event.world, x, y, z, newVar);
					event.entityPlayer.inventory.currentItem = slot;
				}
			}
		}
	}

	/**
	 * Assumes that the player is holding a chisel
	 */
	private void setVariation(EntityPlayer player, World world, int x, int y, int z, CarvingVariation v) {
		PacketHandler.INSTANCE.sendTo(new PacketPlaySound(x, y, z, v), (EntityPlayerMP) player);
		world.setBlock(x, y, z, v.block);
		world.setBlockMetadataWithNotify(x, y, z, v.meta, 3);
		player.getCurrentEquippedItem().damageItem(1, player);
		if (player.getCurrentEquippedItem().stackSize <= 0) {
			player.destroyCurrentEquippedItem();
		}
	}

	public ItemStack getTarget(ItemStack chisel) {
		return chisel.hasTagCompound() ? ItemStack.loadItemStackFromNBT(chisel.stackTagCompound.getCompoundTag("chiselTarget")) : null;
	}

	@Override
	public ChiselMode getChiselMode(ItemStack itemStack) {
		// TODO
		return ChiselMode.SINGLE;
	}

	public static class PacketPlaySound implements IMessage {

		public PacketPlaySound() {
		}

		private int x, y, z;
		private int block;
		private byte meta;

		public PacketPlaySound(int x, int y, int z, CarvingVariation v) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.block = Block.getIdFromBlock(v.block);
			this.meta = (byte) v.meta;
		}

		@Override
		public void toBytes(ByteBuf buf) {
			buf.writeInt(x);
			buf.writeInt(y);
			buf.writeInt(z);
			buf.writeInt(block);
			buf.writeByte(meta);
		}

		@Override
		public void fromBytes(ByteBuf buf) {
			x = buf.readInt();
			y = buf.readInt();
			z = buf.readInt();
			block = buf.readInt();
			meta = buf.readByte();
		}
	}

	public static class PlaySoundHandler implements IMessageHandler<PacketPlaySound, IMessage> {

		@Override
		public IMessage onMessage(PacketPlaySound message, MessageContext ctx) {
			String sound = ItemChisel.carving.getVariationSound(Block.getBlockById(message.block), message.meta);
			GeneralChiselClient.spawnChiselEffect(message.x, message.y, message.z, sound);
			return null;
		}
	}
}