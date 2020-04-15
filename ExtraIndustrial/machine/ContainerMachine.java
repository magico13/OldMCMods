package mods.magico13.ExtraIndustrial.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.SlotElectric;
import mods.magico13.ExtraIndustrial.core.SlotOutput;
import mods.magico13.ExtraIndustrial.core.SlotUpgrade;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMachine extends Container {
	protected TileEntityMachine tileEntity;
	//public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
    private int lastEnergy = 0;
    private int lastUpdateTicks = 0;
	
	public ContainerMachine(InventoryPlayer inventory, World world, int x,
			int y, int z) {
		tileEntity = (TileEntityMachine) world.getBlockTileEntity(x, y, z);
        
		addSlotToContainer(new SlotOutput(inventory.player, tileEntity, 0, 116, 35));
        addSlotToContainer(new Slot(tileEntity, 1, 56, 17));
        addSlotToContainer(new SlotElectric(inventory.player, tileEntity, 2, 56, 53));
        
        addSlotToContainer(new SlotUpgrade(inventory.player, tileEntity, 3, 150, 7));
        addSlotToContainer(new SlotUpgrade(inventory.player, tileEntity, 4, 150, 25));
        addSlotToContainer(new SlotUpgrade(inventory.player, tileEntity, 5, 150, 44));
        addSlotToContainer(new SlotUpgrade(inventory.player, tileEntity, 6, 150, 61));
        
        bindPlayerInventory(inventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}
	
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
            ItemStack stack = null;
            Slot slotObject = (Slot) inventorySlots.get(slot);

            if (slotObject != null && slotObject.getHasStack()) {
                    ItemStack stackInSlot = slotObject.getStack();
                    stack = stackInSlot.copy();
                    if (slot < 7) {
                            if (!mergeItemStack(stackInSlot, 7,
                                            inventorySlots.size(), false)) {
                                    return null;
                            }
                    } else if (!mergeItemStack(stackInSlot, 1, 2, false)) {
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
    
	@Override
	public void addCraftingToCrafters(ICrafting par1ICrafting)
	{
		super.addCraftingToCrafters(par1ICrafting);
		par1ICrafting.sendProgressBarUpdate(this, 0, this.tileEntity.energy);
		par1ICrafting.sendProgressBarUpdate(this, 1, this.tileEntity.updateTicks);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int var1 = 0; var1 < this.crafters.size(); ++var1)
		{
			ICrafting var2 = (ICrafting)this.crafters.get(var1);

			if (this.lastEnergy != this.tileEntity.energy)
			{
				var2.sendProgressBarUpdate(this, 0, this.tileEntity.energy);
			}
			
			if (this.lastUpdateTicks != this.tileEntity.updateTicks)
			{
				var2.sendProgressBarUpdate(this, 1, this.tileEntity.updateTicks);
			}
		}

		this.lastEnergy = this.tileEntity.energy;
		this.lastUpdateTicks = this.tileEntity.updateTicks;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int par1, int par2)
	{
		if (par1 == 0)
		{
			this.tileEntity.energy = par2;
		}

		if (par1 == 1)
		{
			this.tileEntity.updateTicks = par2;	
		}
	}
}
