package team.chisel.common.inventory;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.common.util.SoundUtil;

public class SlotChiselSelection extends Slot {

    private final ContainerChisel container;

    public SlotChiselSelection(ContainerChisel container, InventoryChiselSelection inv, IInventory iinventory, int i, int j, int k) {
        super(iinventory, i, j, k);
        this.container = container;
    }

    @Override
    public boolean isItemValid(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return par1EntityPlayer.inventory.getItemStack() == null;
    }
    
    public ItemStack craft(EntityPlayer player, ItemStack itemstack, boolean simulate) {
        ItemStack heldStack = player.inventory.getItemStack();
        ItemStack crafted = container.getInventoryChisel().inventory[container.getInventoryChisel().size];
        ItemStack chisel = container.getChisel();
        if (simulate) {
            itemstack = itemstack.copy();
            crafted = crafted.copy();
            chisel = chisel.copy();
        }
        ItemStack res = null;

//        if (heldStack == null) {
//            if (!simulate) {
//                container.getInventoryChisel().decrStackSize(container.getInventoryChisel().size, 1);
//                container.onChiselSlotChanged();
//            }
//        } else {
//            player.inventory.setItemStack(null);

            if (crafted != null) {
                IChiselItem item = (IChiselItem) container.getChisel().getItem();
                res = item.craftItem(chisel, crafted, itemstack, player);
                if (!simulate) {
                    if (chisel.stackSize == 0) {
                        container.getInventoryPlayer().setInventorySlotContents(container.getChiselSlot(), null);
                        container.onChiselBroken();
                    }
                    container.getInventoryChisel().setInventorySlotContents(container.getInventoryChisel().size, crafted.stackSize == 0 ? null : crafted);
                    container.onChiselSlotChanged();
                    item.onChisel(player.world, player, chisel, CarvingUtils.getChiselRegistry().getVariation(itemstack));
                }
            }
//        }

        if (!simulate) {
            container.getInventoryChisel().updateItems();
            container.detectAndSendChanges();

//            if (player.world.isRemote) {
                ICarvingVariation v = CarvingUtils.getChiselRegistry().getVariation(crafted);
                IBlockState state = v == null ? null : v.getBlockState();
                if (state == null) {
                    if (crafted.getItem() instanceof ItemBlock) {
                        state = ((ItemBlock) crafted.getItem()).getBlock().getStateFromMeta(crafted.getItem().getMetadata(crafted.getItemDamage()));
                    } else {
                        state = Blocks.STONE.getDefaultState(); // fallback
                    }
                }
                SoundUtil.playSound(player, chisel, state);
//            } else {
//                // container.getInventoryPlayer().player.addStat(Statistics.blocksChiseled, crafted.stackSize);
//            }
        }
        
        return res;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack) {
        player.inventory.setItemStack(craft(player, itemstack, false));
    }
}