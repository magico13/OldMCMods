package mods.magico13.ExtraIndustrial.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.Direction;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

public class TileEntityIngotCompactor extends TileEntityMachine {
	public int opLength = 100;
	private int baseOpLength = 100;
	private boolean active = false;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (!this.worldObj.isRemote)
		{
			if (this.energy >= this.energyUse)
			{
				ItemStack stackIn0 = this.getStackInSlot(0);
				ItemStack stackIn1 = this.getStackInSlot(1);

				if (stackIn1 != null && stackIn1.stackSize>8)
				{				
					ItemStack result = craftResult();
					if (result != null && (stackIn0 == null || (stackIn0.stackSize+result.stackSize <= stackIn0.getMaxStackSize() && result.itemID == stackIn0.itemID && result.getItemDamage() == stackIn0.getItemDamage())))
					{
						// Modify the operation time by the number of overclockers
						opLength = (int) (Math.pow(0.7, numOverclockers) * baseOpLength); 
						if (opLength < 1)
							opLength = 1;
						this.active = true;
					}
					else
					{
						this.active = false;
					}
				}
				else
				{
					this.active = false;
				}

				if (active)
				{
					if (consumeEnergy(this.energyUse))
						++updateTicks;
					else
						return;

					if (updateTicks >= opLength)
					{
						updateTicks=0;
						ItemStack result = this.craftResult();
						if (result != null)
							craft(result);
					}
				}
				else
				{
					updateTicks = 0;
				}
			}
		}
		if (this.worldObj.isRemote)
		{
			opLength = (int) (Math.pow(0.7, numOverclockers) * baseOpLength); 
			if (opLength < 1)
				opLength = 1;
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketUtils.packetFromTileEntity(this);
	}

	public boolean getActive()
	{
		//return this.active;
		return this.updateTicks>0;
	}

	@SideOnly(Side.CLIENT)
	public int getChargeScaled(int par1)
	{
		return this.energy * par1 / this.maxEnergy;
	}

	@SideOnly(Side.CLIENT)
	public int getProgressScaled(int par1)
	{
		return this.updateTicks * par1 / this.opLength;
	}

	private ItemStack craftResult()
	{		
		ContainerFakeWorkbench bench = new ContainerFakeWorkbench();
		InventoryCrafting craftMatrixFake = new InventoryCrafting(bench, 3, 3);
		ItemStack slot1 = this.getStackInSlot(1);
		if (slot1 == null)
			return null;
		for (int i=0; i<9; i++)
		{
			craftMatrixFake.setInventorySlotContents(i, slot1) ;
		}
		ItemStack result = CraftingManager.getInstance().findMatchingRecipe(craftMatrixFake, this.worldObj);
		if (result == null) {
			return null;
		}
		else
			return result;
	}

	private void craft(ItemStack stack)
	{
		ItemStack slot0 = this.getStackInSlot(0);
		ItemStack slot1 = this.getStackInSlot(1);
		if (slot0 == null) {
			this.setInventorySlotContents(0, stack);
			this.decrStackSize(1, 9);
		}
		else if (slot0.getItem() == stack.getItem() && ((slot0.stackSize+stack.stackSize) < slot0.getMaxStackSize())) {
			this.decrStackSize(1, 9);
			slot0.stackSize = slot0.stackSize + 1;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.active = nbt.getBoolean("active");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("active", active);
	}

}
