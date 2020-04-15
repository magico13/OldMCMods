package magico13.mods.NetherBits.machine;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCobbleGen extends Container{
	protected TileEntityCobbleGen tileEntity;
    
    public ContainerCobbleGen (InventoryPlayer inventoryPlayer, TileEntityCobbleGen te){
            tileEntity = te;

            //the Slot constructor takes the IInventory and the slot number in that it binds to
            //and the x-y coordinates it resides on-screen
            addSlotToContainer(new Slot(tileEntity, 0, 76, 37));

            //commonly used vanilla code that adds the player's inventory
            bindPlayerInventory(inventoryPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
            return tileEntity.isUseableByPlayer(player);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
            ItemStack stack = null;
            Slot slotObject = (Slot) inventorySlots.get(slot);

            //null checks and checks if the item can be stacked (maxStackSize > 1)
            if (slotObject != null && slotObject.getHasStack()) {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();

                    //merges the item into player inventory since its in the tileEntity
                    if (slot == 0) {
                            if (!mergeItemStack(stackInSlot, 1,
                                            inventorySlots.size(), true)) {
                                    return null;
                            }
                    //places it into the tileEntity is possible since its in the player inventory
                    } else if (!mergeItemStack(stackInSlot, 0, 1, false)) {
                            return null;
                    }

                    if (stackInSlot.stackSize == 0) {
                            slotObject.putStack(null);
                    } else {
                            slotObject.onSlotChanged();
                    }
            }

            return stack;
    }
    @Override
    public ItemStack slotClick(int slotNum, int par2, int par3, EntityPlayer player)
	{
    	if (slotNum == 0)
    	{
    		//Slot slot = (Slot)this.inventorySlots.get(slotNum);
    		ItemStack itemstack = player.inventory.getItemStack();
    		if (itemstack != null && itemstack.getItem() != new ItemStack(Block.cobblestone, 1).getItem())
    		{
    			return null;
    		}
    	}
    	return super.slotClick(slotNum, par2, par3, player);
	}

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 9; j++) {
    			addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
    					8 + j * 18, 84 + i * 18));
    		}
    	}

    	for (int i = 0; i < 9; i++) {
    		addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
    	}
    }
    
}
