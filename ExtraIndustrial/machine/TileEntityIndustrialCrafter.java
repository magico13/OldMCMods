package mods.magico13.ExtraIndustrial.machine;

import java.util.Random;

import ic2.api.item.Items;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialCraftingManager;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TileEntityIndustrialCrafter extends TileEntityMachine {
	private boolean active;
	private int baseNormalOpLength = 40;
	private int baseSpecialOpLength = 200;
	private int opLength = 40;
	private boolean isSpecialOp;
	public boolean modeAuto;
	private boolean craftRequest;

	private NBTTagList savedRecipe1;
	private NBTTagList savedRecipe2;
	private NBTTagList savedRecipe3;

	public TileEntityIndustrialCrafter(){
		inv = new ItemStack[32];
		upgradeSlotStart = 0;
		energyUse = 1;
		chargeSlot=-1;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		ItemStack[] craftResult = this.craftResult();
		if (craftResult == null)
		{
			this.setInventorySlotContents(4, null);
		}
		else if (craftResult.length == 1)
		{
			if (this.getStackInSlot(4) != craftResult[0])
				this.setInventorySlotContents(4, craftResult[0]);
		}
		else
		{
			if (this.getStackInSlot(4) != Items.getItem("scrap"))
				this.setInventorySlotContents(4, Items.getItem("scrap"));
		}

		if (!this.worldObj.isRemote)
		{
		if (this.energy>=this.energyUse)
		{
			active = ((this.craftRequest || this.modeAuto) && this.getStackInSlot(4)!=null 
					&& this.hasNeededItems() && canMergeResult(craftResult));		

			if (this.isSpecialOp)
				opLength = (int) (Math.pow(0.7, numOverclockers) * baseSpecialOpLength);
			else
				opLength = (int) (Math.pow(0.7, numOverclockers) * baseNormalOpLength);
			if (opLength < 1)
				opLength = 1;

			if (active)
			{
				//System.out.println("Running special operation? "+this.isSpecialOp);
				if (consumeEnergy(this.energyUse))
					++updateTicks;
				else
					return;

				if (updateTicks >= opLength)
				{
					updateTicks=0;
					craft(craftResult);
					this.craftRequest = false;
				}
			}
			else
			{
				updateTicks = 0;
				this.craftRequest = false;
			}
		}
		}
		
		if (this.worldObj.isRemote)
		{		
			if (this.isSpecialOp)
				opLength = (int) (Math.pow(0.7, numOverclockers) * baseSpecialOpLength);
			else
				opLength = (int) (Math.pow(0.7, numOverclockers) * baseNormalOpLength);
			if (opLength < 1)
				opLength = 1;
		}

	}

	private boolean canMergeResult(ItemStack[] stack)
	{
		if (stack == null)
			return false;
		
		int[] slotsClaimed = new int[9];
		for (int j=0; j<stack.length; j++)
		{
			for (int i=5; i<14; i++)
			{
				ItemStack slotStack = this.getStackInSlot(i);
				if (slotStack != null)
				{
					if ((slotStack.getItem() == stack[j].getItem()) 
							&& (slotStack.getItemDamage() == stack[j].getItemDamage())
							&& (slotStack.stackSize + stack[j].stackSize < slotStack.getMaxStackSize()))
					{
						break;
					}
					if (i == 13)
					{
						return false;
					}
				}
				else
				{
					boolean claimed = false;
					for (int k=0; k<9; k++)
					{
						if (slotsClaimed[k] == i)
						{
							claimed = true;
						}
					}
					if (!claimed)
					{
						slotsClaimed[j] = i;
						break;
					}
				}
			}
		}
		return true;
	}

	private boolean hasNeededItems()
	{
		ItemStack[] tempCopy = new ItemStack[9];
		for (int i=23; i<32; i++)
		{
			tempCopy[i-23] = ItemStack.copyItemStack(inv[i]);
		}
		for (int i=14; i<23; i++)
		{
			ItemStack slotStack = this.getStackInSlot(i);
			if (slotStack != null)
			{
				if (!ExtraIndustrialFunctions.removeItemFromInventory(tempCopy, slotStack, 1))
					return false;
			}
		}
		return true;
	}

	private ItemStack[] craftResult()
	{		
		ContainerFakeWorkbench bench = new ContainerFakeWorkbench();
		InventoryCrafting craftMatrixFake = new InventoryCrafting(bench, 3, 3);
		for (int i=0; i<9; i++)
		{
			craftMatrixFake.setInventorySlotContents(i, this.getStackInSlot(i+14)) ;
		}

		ItemStack result =  CraftingManager.getInstance().findMatchingRecipe(craftMatrixFake, this.worldObj);
		if (result != null) {
			this.isSpecialOp = false;
			return new ItemStack[] {result};
		}
		else
		{
			ItemStack[] resultArray = ExtraIndustrialCraftingManager.getInstance().findMatchingIndustrialCrafterRecipe(this);
			if (resultArray == null)
				return null;
			this.isSpecialOp = true;
			return resultArray;
		}

	}

	private void mergeIntoOutput(ItemStack craftStack)
	{
		for (int i=5; i<14; i++)
		{
			ItemStack slotStack = this.getStackInSlot(i);
			if (slotStack == null)
			{
				this.setInventorySlotContents(i, craftStack);
				break;
			}
			else if (slotStack.getItem() == craftStack.getItem() 
					&& slotStack.getItemDamage() == craftStack.getItemDamage()
					&& slotStack.stackSize < slotStack.getMaxStackSize())
			{
				if (slotStack.stackSize + craftStack.stackSize <= slotStack.getMaxStackSize())
					slotStack.stackSize += craftStack.stackSize;
				else
				{
					craftStack.stackSize -= slotStack.getMaxStackSize() - slotStack.stackSize;
					slotStack.stackSize = slotStack.getMaxStackSize();
					mergeIntoOutput(craftStack);
				}
				break;
			}
		}
	}
	private void craft(ItemStack[] craftStack)
	{
		for (int i=14; i<23; i++)
		{
			ItemStack slotStack = this.getStackInSlot(i);
			ExtraIndustrialFunctions.removeItemFromInventory(inv, slotStack, 23, 9, 1);
		}
		for (int j=0; j<craftStack.length; j++)
		{
			mergeIntoOutput(craftStack[j]);
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketUtils.packetFromTileEntity(this);
	}

	public boolean isActive()
	{
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

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.active = nbt.getBoolean("active");
		this.savedRecipe1 = nbt.getTagList("recipe1");
		this.savedRecipe2 = nbt.getTagList("recipe2");
		this.savedRecipe3 = nbt.getTagList("recipe3");
		this.modeAuto = nbt.getBoolean("modeAuto");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("active", this.active);
		nbt.setBoolean("modeAuto", this.modeAuto);
		if (this.savedRecipe1 != null)
			nbt.setTag("recipe1", this.savedRecipe1);
		if (this.savedRecipe2 != null)
			nbt.setTag("recipe2", this.savedRecipe2);
		if (this.savedRecipe3 != null)
			nbt.setTag("recipe3", this.savedRecipe3);
	}

	public void saveRecipe(String recipeName, NBTTagList recipe)
	{
		if (recipeName.equals("recipe1"))
			this.savedRecipe1 = recipe; 
		else if (recipeName.equals("recipe2"))
			this.savedRecipe2 = recipe; 
		else if (recipeName.equals("recipe3"))
			this.savedRecipe3 = recipe; 
	}

	public NBTTagList loadRecipe(String recipeName)
	{
		if (recipeName.equals("recipe1"))
		{
			return this.savedRecipe1;
		}
		else if (recipeName.equals("recipe2"))
		{
			return this.savedRecipe2;
		}
		else if (recipeName.equals("recipe3"))
		{
			return this.savedRecipe3;
		}
		else
			return null;
	}

	public void requestCrafting()
	{
		this.craftRequest = true;
	}
	
	/*
	@Override
	public int getStartInventorySide(ForgeDirection side) {
		if (side == ForgeDirection.DOWN || side == ForgeDirection.DOWN) return 14;
		return 5;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 9;
	}
*/
	@Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        switch (side){
        case 1: return new int[] {23, 9};
        default: return new int[] {5, 9};
        }
    }
	
	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack)
	{
		if (i >=5 && i<14)
			return false;
		return super.isStackValidForSlot(i, itemstack);
	}
	
	@Override
	public void dropItems(World world, int x, int y, int z) {
		Random rand = new Random();

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (i<4 || (i<14 && i>4) || i>22)
			{
				ItemStack item = inventory.getStackInSlot(i);

				if (item != null && item.stackSize > 0) {
					float rx = rand.nextFloat() * 0.8F + 0.1F;
					float ry = rand.nextFloat() * 0.8F + 0.1F;
					float rz = rand.nextFloat() * 0.8F + 0.1F;

					EntityItem entityItem = new EntityItem(world,
							x + rx, y + ry, z + rz,
							new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

					if (item.hasTagCompound()) {
						entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
					}

					float factor = 0.05F;
					entityItem.motionX = rand.nextGaussian() * factor;
					entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
					entityItem.motionZ = rand.nextGaussian() * factor;
					world.spawnEntityInWorld(entityItem);
					item.stackSize = 0;
				}
			}
		}

	}
}
