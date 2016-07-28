package team.chisel.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import team.chisel.client.ClientUtil;

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
        } else {
            putStack(itemstack.copy());

            player.inventory.setItemStack(null);

            if (crafted != null) {
                int damageLeft = chisel.getMaxDamage() - chisel.getItemDamage() + 1;
                int toCraft = Math.min(crafted.stackSize, damageLeft);
                chisel.damageItem(toCraft, player);
                if (chisel.stackSize <= 0) {
                    container.getInventoryPlayer().setInventorySlotContents(container.getChiselSlot(), null);
                    container.onChiselBroken();
                }
                player.inventory.setItemStack(new ItemStack(itemstack.getItem(), toCraft, itemstack.getItemDamage()));
                crafted.stackSize -= toCraft;
                container.getInventoryChisel().setInventorySlotContents(container.getInventoryChisel().size, crafted.stackSize <= 0 ? null : crafted);
            }
        }

        container.getInventoryChisel().updateItems();
        container.detectAndSendChanges();
        
        if (player.worldObj.isRemote) {
            String sound = container.getCarving().getVariationSound(crafted);
            ClientUtil.playSound(player.worldObj, MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ), sound, SoundCategory.BLOCKS);
        } else {
            //container.getInventoryPlayer().player.addStat(Statistics.blocksChiseled, crafted.stackSize);
        }
    }
}