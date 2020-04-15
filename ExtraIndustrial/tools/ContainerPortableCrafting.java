package mods.magico13.ExtraIndustrial.tools;

import ic2.api.item.ElectricItem;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.SlotOutput;
import mods.magico13.ExtraIndustrial.machine.ContainerFakeWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;

/**
 * Many thanks to Pahimar and EE3 for the base of the portable crafting code.
 * https://github.com/pahimar/Equivalent-Exchange-3
 */

public class ContainerPortableCrafting extends Container {

	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	public IInventory craftResult = new InventoryCraftResult();
	public TileEntityChest inv = new TileEntityChest();
	private World worldObj;

	public ContainerPortableCrafting(InventoryPlayer inventoryPlayer, World world, int x, int y, int z) {
		this.worldObj = world;
		this.readNBT(inventoryPlayer);
		this.addSlotToContainer(new SlotOutput(inventoryPlayer.player, this.craftResult, 0, 124, 35));
		for (int xGui = 0; xGui < 3; ++xGui)
		{
			for (int yGui = 0; yGui < 3; ++yGui)
			{
				this.addSlotToContainer(new Slot(this.craftMatrix, yGui + xGui * 3, 30 + yGui * 18, 17 + xGui * 18));
			}
		}
		this.addSlotToContainer(new Slot(inv, 10, 94, 53));
		bindPlayerInventory(inventoryPlayer);
		this.onCraftMatrixChanged(this.craftMatrix);
	}

	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
	{
		this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {

		return true;
	}

	@Override
	public void onCraftGuiClosed(EntityPlayer player) {
		if (this.getSlot(10).getHasStack())
		{
			ItemStack crafterStack = player.inventory.getCurrentItem();
			if (ElectricItem.canUse(crafterStack, this.getSlot(10).getStack().stackSize*4))
			{
					ElectricItem.use(crafterStack, this.getSlot(10).getStack().stackSize*4, player);
					this.getSlot(10).putStack(null);
			}
			else
			{
				player.dropPlayerItem(this.getSlot(10).getStack());
				this.getSlot(10).putStack(null);
			}
		}
		super.onCraftGuiClosed(player);
		writeNBT(player);
	}

	@Override
	public ItemStack slotClick(int slotNum, int mouseClick, int par3, EntityPlayer player)
	{		
		int crafterSlot = 38+player.inventory.currentItem;
		ItemStack crafterStack = player.inventory.getCurrentItem();
		ItemPortableCrafting crafterItem = (ItemPortableCrafting) crafterStack.getItem();

		if (slotNum == 0)
		{
			ItemStack returnStack;
			Slot craftingSlot = (Slot)this.inventorySlots.get(slotNum);
			if (!ElectricItem.canUse(crafterStack, crafterItem.getEnergyUse())){
				return null;
			}

			if (craftingSlot.getStack() != null)
			{
				if (player.inventory.getItemStack()!= null && player.inventory.getItemStack().stackSize+craftingSlot.getStack().stackSize > craftingSlot.getStack().getMaxStackSize())
					return null;
				if (!simulateRemoval(player.inventory))
				{
					return null;
				}
				else
				{
					//System.out.println("simulation succeeded");
					{
						for (int j=0; j<9; j++)
						{
							if (this.craftMatrix.getStackInSlot(j) != null)
							{
								ExtraIndustrialFunctions.removeItemFromInventory(player.inventory.mainInventory, this.craftMatrix.getStackInSlot(j), 1);
								//player.inventory.consumeInventoryItem(this.craftMatrix.getStackInSlot(j).itemID);
							}
						}
					}
				}
				ElectricItem.use(crafterStack, crafterItem.getEnergyUse(), (EntityPlayer)player);
				returnStack = super.slotClick(slotNum, mouseClick, par3, player);
				this.onCraftMatrixChanged(this.craftMatrix);
				return returnStack;
			}
		}
		else if (slotNum == crafterSlot)
		{
			return null;
		}
		else if (slotNum == 10)
		{
			int deleteCost = 10;
			ItemStack stackToTrash = player.inventory.getItemStack();
			if (stackToTrash != null)
			{
				if (ElectricItem.canUse(crafterStack, stackToTrash.stackSize*4))
				{
					if (this.getSlot(slotNum).getHasStack())
					{
						this.getSlot(slotNum).putStack(null);
						ElectricItem.use(crafterStack, stackToTrash.stackSize*4, player);	
					}
					return super.slotClick(slotNum, mouseClick, par3, player);
				}
				else
				{
					return null;
				}
			}
		}
		else if (slotNum > 0 && slotNum < 10)
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
		return super.slotClick(slotNum, mouseClick, par3, player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotNum)
	{
		if (!player.worldObj.isRemote)
		{
			ItemStack stackInSlotCopy = null;
			Slot slot = (Slot)this.inventorySlots.get(slotNum);

			if (slot != null && slot.getHasStack())
			{
				ItemStack stackInSlot = slot.getStack();
				stackInSlotCopy = stackInSlot.copy();

				if (slotNum == 0)
				{
					if (!this.mergeItemStack(stackInSlotCopy, 10, 46, true))
					{
						return null;
					}

					slot.onSlotChange(stackInSlotCopy, stackInSlot);

				}
				else if (slotNum > 10 && slotNum < 38)
				{
					if (!this.mergeItemStack(stackInSlot, 38, 47, false))
					{
						return null;
					}
				}
				else if (slotNum >= 38 && slotNum < 47)
				{
					if (!this.mergeItemStack(stackInSlot, 11, 38, false))
					{
						return null;
					}
				}
				else if (!this.mergeItemStack(stackInSlot, 11, 47, false))
				{
					return null;
				}

				if (stackInSlot.stackSize == 0)
				{
					slot.putStack((ItemStack)null);
				}
				else
				{
					slot.onSlotChanged();
				}

				if (stackInSlot.stackSize == stackInSlotCopy.stackSize)
				{
					return null;
				}

				slot.onPickupFromSlot(player, stackInSlot);
			}
			return stackInSlotCopy;
		}
		return null;
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

	protected boolean simulateRemoval(InventoryPlayer invPlayer)
	{
		InventoryPlayer inventory = new InventoryPlayer(invPlayer.player);
		inventory.copyInventory(invPlayer);
		for (int i=0; i<9; i++)
		{
			ItemStack craftingSlot = this.craftMatrix.getStackInSlot(i);
			if (craftingSlot != null)
			{
				//System.out.println(this.craftMatrix.getStackInSlot(i).getDisplayName());
				if (inventory.hasItemStack(craftingSlot))
				{
					if (!ExtraIndustrialFunctions.removeItemFromInventory(inventory.mainInventory, craftingSlot, 1))
						return false;
					/*	
					 * Not working properly, only dependent on itemID
					if (!inventory.consumeInventoryItem(this.craftMatrix.getStackInSlot(i).itemID))
						return false;*/
				}
				else
					return false;
			}
		}
		//System.out.println("Inventory consumption succeeded");
		if (inventory.getFirstEmptyStack()>=0)
		{
			//System.out.println("Empty stack!");
			return true;
		}
		else
		{
			ItemStack result = this.craftResult.getStackInSlot(0);
			for (int j=0; j<inventory.mainInventory.length; j++)
			{
				ItemStack stack = inventory.mainInventory[j];
				//System.out.println(j+":"+stack.getDisplayName());
				if (stack != null)
				{
					if (stack.getItem() == result.getItem() && stack.getItemDamage() == result.getItemDamage() && ((stack.stackSize+result.stackSize)<stack.getMaxStackSize()))
					{
						return true;
					}
				}
			}
			return false;
		}
	}

	public void writeNBT(EntityPlayer player)
	{
		NBTTagCompound tagCompound = player.inventory.getCurrentItem().getTagCompound();
		if (tagCompound == null)
		{	
			tagCompound = new NBTTagCompound();
			player.inventory.getCurrentItem().setTagCompound(tagCompound);
		}
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < this.craftMatrix.getSizeInventory(); i++) {
			ItemStack stack = this.craftMatrix.getStackInSlot(i);
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
	}

	public void readNBT(InventoryPlayer player)
	{
		NBTTagCompound tagCompound = player.getCurrentItem().getTagCompound();
		if (tagCompound == null)
			return;
		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < this.craftMatrix.getSizeInventory()) {
				craftMatrix.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tag));
			}
		}
	}
}