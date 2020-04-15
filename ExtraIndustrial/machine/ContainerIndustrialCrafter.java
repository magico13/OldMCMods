package mods.magico13.ExtraIndustrial.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.SlotIndicator;
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
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerIndustrialCrafter extends Container {
	protected TileEntityIndustrialCrafter tileEntity;
	//public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	//public IInventory craftResult = new InventoryCraftResult();
	private int lastEnergy = 0;
	private int lastUpdateTicks = 0;


	public ContainerIndustrialCrafter(InventoryPlayer inventory, World world, int x,
			int y, int z) {
		tileEntity = (TileEntityIndustrialCrafter) world.getBlockTileEntity(x, y, z);
		
		// Upgrade Slots
		for (int i=0; i<4; i++)
		{
			addSlotToContainer(new SlotUpgrade(inventory.player, tileEntity, i, 155, 5+i*18));
		}
		// Crafting Indicator Slot
		addSlotToContainer(new SlotIndicator(inventory.player, tileEntity, 4, 82, 21));
		
		// Output Slots
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                this.addSlotToContainer(new SlotOutput(inventory.player, tileEntity, 5 + j + i * 3, 100 + j * 18, 16 + i * 18));
            }
        }
        
        // Recipe Slots
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                this.addSlotToContainer(new Slot(tileEntity, 14 + j + i * 3, 28 + j * 18, 16 + i * 18));
            }
        }
        
        // Buffer Inventory Slots
        for (int i=0; i<9; i++)
        {
        	this.addSlotToContainer(new Slot(tileEntity, 23 + i, 8 + 18 * i, 84));
        }
        
        // Player Inventory Slots
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
                    if (slot < 32) {
                            if (!mergeItemStack(stackInSlot, 32,
                                            inventorySlots.size(), false)) {
                                    return null;
                            }
                    } else if (!mergeItemStack(stackInSlot, 23, 32, false)) {
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
    public ItemStack slotClick(int slotNum, int mouseClick, int par3, EntityPlayer player)
	{
    	if (slotNum >= 14 && slotNum <= 22)
		{
			Slot clickedSlot = (Slot)this.inventorySlots.get(slotNum);
			ItemStack heldStack = player.inventory.getItemStack();
			ItemStack cpStack = null;
			if (heldStack != null)
			{
				cpStack = heldStack.copy();
				cpStack.stackSize = 1;
			}
			clickedSlot.putStack(cpStack);

			return null;
		}
    	else if (slotNum == 4)
    	{
    		tileEntity.requestCrafting();
    	}
		return super.slotClick(slotNum, mouseClick, par3, player);
	}
    
    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 9; j++) {
    			addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
    					8 + j * 18, 109 + i * 18));
    		}
    	}

    	for (int i = 0; i < 9; i++) {
    		addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 167));
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