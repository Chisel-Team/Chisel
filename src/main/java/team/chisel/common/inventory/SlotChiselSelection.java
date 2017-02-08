package team.chisel.common.inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.client.ClientUtil;

@ParametersAreNonnullByDefault
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
        return par1EntityPlayer.inventory.getItemStack().isEmpty();
    }

    @Override
    public ItemStack onTake(EntityPlayer player, ItemStack itemstack) {
        ItemStack heldStack = player.inventory.getItemStack();
        ItemStack crafted = container.getInventoryChisel().getStackInSpecialSlot();
        ItemStack chisel = container.getChisel();

        if (heldStack.isEmpty()) {
            container.getInventoryChisel().decrStackSize(container.getInventoryChisel().size, 1);
            container.onChiselSlotChanged();
        } else {
            putStack(itemstack.copy());

            player.inventory.setItemStack(ItemStack.EMPTY);

            if (!crafted.isEmpty()) {
                
                IChiselItem item = (IChiselItem) container.getChisel().getItem();
                ItemStack res = item.craftItem(chisel, crafted, itemstack, player);
                if (chisel.getCount() == 0) {
                    container.getInventoryPlayer().setInventorySlotContents(container.getChiselSlot(), ItemStack.EMPTY);
                    container.onChiselBroken();
                }
                player.inventory.setItemStack(res);
                container.getInventoryChisel().setInventorySlotContents(container.getInventoryChisel().size, crafted.getCount() == 0 ? ItemStack.EMPTY : crafted);
                container.onChiselSlotChanged();
            }
        }

        container.getInventoryChisel().updateItems();
        container.detectAndSendChanges();
        
        if (player.world.isRemote) {
            String sound = container.getCarving().getVariationSound(crafted);
            ClientUtil.playSound(player.world, new BlockPos(player), sound);
        } else {
            //container.getInventoryPlayer().player.addStat(Statistics.blocksChiseled, crafted.getCount());
        }
        
        return ItemStack.EMPTY; // TODO 1.11 ???
    }
}