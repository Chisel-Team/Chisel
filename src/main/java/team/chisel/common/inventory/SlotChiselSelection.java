package team.chisel.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.chisel.api.IChiselItem;
import team.chisel.api.carving.CarvingUtils;
import team.chisel.api.carving.ICarvingVariation;
import team.chisel.client.util.ClientUtil;

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

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack) {
        ItemStack heldStack = player.inventory.getItemStack();
        ItemStack crafted = container.getInventoryChisel().inventory[container.getInventoryChisel().size];
        ItemStack chisel = container.getChisel();

        if (heldStack == null) {
            container.getInventoryChisel().decrStackSize(container.getInventoryChisel().size, 1);
            container.onChiselSlotChanged();
        } else {
            putStack(itemstack.copy());

            player.inventory.setItemStack(null);

            if (crafted != null) {
                IChiselItem item = (IChiselItem) container.getChisel().getItem();
                ICarvingVariation var = container.carving.getVariation(itemstack);
                ItemStack res = item.craftItem(chisel, crafted, itemstack, player);
                if (chisel.stackSize == 0) {
                    container.getInventoryPlayer().setInventorySlotContents(container.getChiselSlot(), null);
                    container.onChiselBroken();
                }
                player.inventory.setItemStack(res);
                container.getInventoryChisel().setInventorySlotContents(container.getInventoryChisel().size, crafted.stackSize == 0 ? null : crafted);
                container.onChiselSlotChanged();
            }
        }

        container.getInventoryChisel().updateItems();
        container.detectAndSendChanges();
        
        if (player.world.isRemote) {
            String sound = container.getCarving().getVariationSound(crafted);
            ClientUtil.playSound(player.world, new BlockPos(player), sound);
        } else {
            //container.getInventoryPlayer().player.addStat(Statistics.blocksChiseled, crafted.stackSize);
        }
    }
}