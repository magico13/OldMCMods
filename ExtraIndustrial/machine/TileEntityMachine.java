package mods.magico13.ExtraIndustrial.machine;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.Items;
import ic2.api.tile.IWrenchable;

public class TileEntityMachine extends TileEntity implements IEnergySink, IWrenchable, IInventory, ISidedInventory {

	protected ItemStack[] inv;
	protected int energy = 0;
	protected int updateTicks=0;
	protected short facing;
	protected int upgradeSlotStart = 3;
	protected int chargeSlot=2;

	public boolean invChanged;

	protected int baseEnergyUse = 2;
	protected int energyUse = 2;

	protected int numOverclockers=0;
	protected int numEnergyUpgrade=0;
	protected int numTransformerUpgrade=0;
	

	protected boolean addedToENet;

	protected int maxEnergy = 256;
	protected int maxInput = 32;

	public TileEntityMachine(){
		inv = new ItemStack[7];
	}

	@Override
	public void updateEntity()
	{
		if (!addedToENet && this.worldObj != null)
		{
			EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
			MinecraftForge.EVENT_BUS.post(loadEvent);
			addedToENet = true;
		}


		// Check for Upgrade modules
		if (invChanged)
		{
			//System.out.println("Checking for Upgrades!");
			numOverclockers=0;
			numEnergyUpgrade=0;
			numTransformerUpgrade=0;
			for (int i=upgradeSlotStart;i<upgradeSlotStart+4;i++)
			{
				if (this.getStackInSlot(i) != null)
				{
					int item = this.getStackInSlot(i).itemID;
					if (item == Items.getItem("overclockerUpgrade").itemID)
					{
						int meta = this.getStackInSlot(i).getItemDamage();
						if (meta == 0)
						{
							numOverclockers = numOverclockers + this.getStackInSlot(i).stackSize;
						}
						else if (meta == 1)
						{
							numTransformerUpgrade = numTransformerUpgrade + this.getStackInSlot(i).stackSize;
						}
						else if (meta == 2)
						{
							numEnergyUpgrade = numEnergyUpgrade + this.getStackInSlot(i).stackSize;
						}
					}
				}
			}
			// Modify the EU cost by the number of overclockers
			energyUse = (int) (Math.pow(1.6, numOverclockers)*baseEnergyUse);

			// Modify the max energy storage by the number of energy storage upgrades
			maxEnergy = (10000 * numEnergyUpgrade) + 256;

			// Modify the Max Input amount by the number of transformer upgrades
			switch (numTransformerUpgrade)
			{
			case 0: maxInput = 32; break;
			case 1: maxInput = 128; break;
			default: maxInput = 512; break;
			}
			// Reset invChanged back to false
			invChanged = false;
			//System.out.println("Overclockers: "+numOverclockers);
			//System.out.println("Transformers: "+numTransformerUpgrade);
			//System.out.println("Energy Storage: "+numEnergyUpgrade);
		}
		
		if (chargeSlot>-1 && this.getStackInSlot(chargeSlot) != null && this.energy < this.maxEnergy && this.getStackInSlot(chargeSlot).getItem() instanceof IElectricItem)
		{
			ItemStack stackInSlot = this.getStackInSlot(chargeSlot);
			IElectricItem item = (IElectricItem) stackInSlot.getItem();
			if (item.canProvideEnergy(stackInSlot))
			{
				int limit = item.getTransferLimit(stackInSlot);
				int itemEnergy = ElectricItem.discharge(stackInSlot, item.getMaxCharge(stackInSlot), 3, true, true);
				int energyNeed = maxEnergy-energy;
				if (limit<=energyNeed && limit<=itemEnergy)
				{
					this.addEnergy(ElectricItem.discharge(stackInSlot, limit, this.getTier(), false, false));
				}
				else if (itemEnergy<=energyNeed && itemEnergy<limit)
				{
					this.addEnergy(ElectricItem.discharge(stackInSlot, itemEnergy, this.getTier(), false, false));
				}
				else if (energyNeed<=limit)
				{
					this.addEnergy(ElectricItem.discharge(stackInSlot, energyNeed, this.getTier(), false, false));
				}
			}
		}
	}

	@Override
	public void onInventoryChanged()
	{
		this.invChanged = true;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketUtils.packetFromTileEntity(this);
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return addedToENet;
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return false;
	}

	@Override
	public short getFacing() {
		if (facing >= 0)
			return this.facing;
		else
			return 3;
	}

	@Override
	public void setFacing(short facing) {	
		this.facing = facing;
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0F;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		Block block = Block.blocksList[this.worldObj.getBlockId(xCoord, yCoord, zCoord)];
		int meta = this.worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
		return new ItemStack(block, 1, meta);
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
		return true;
	}

	@Override
	public int demandsEnergy() {
		if (energy < maxEnergy)
			return (maxEnergy-energy);
		else
			return 0;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		if (amount > this.getMaxSafeInput())
		{
			ExtraIndustrialFunctions.explodeMachine(this.worldObj, xCoord, yCoord, zCoord);
			return 0;
		}
		else if (energy+amount<maxEnergy){
			this.addEnergy(amount);
			return 0;
		}
		else if (energy<maxEnergy){
			int transfer = (maxEnergy-energy);
			this.addEnergy(transfer);
			return amount-transfer;
		}
		else{
			return amount;
		}
	}
	
	private void addEnergy(int amt)
	{
		energy = amt+energy;
	}
	
	public int getTier()
	{
		return Math.min(this.numTransformerUpgrade+1, 3);
	}

	@Override
	public int getMaxSafeInput() {
		return this.maxInput;
	}


	// Inventory related functions:
	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}              
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this &&
				player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public String getInvName() {
		return "machineInv";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.energy = nbt.getInteger("energy");
		this.updateTicks = nbt.getInteger("updateTicks");
		this.facing = nbt.getShort("facing");

		NBTTagList tagList = nbt.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("energy", energy);
		nbt.setInteger("updateTicks", updateTicks);
		nbt.setShort("facing", facing);

		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		nbt.setTag("Inventory", itemList);
	}

	/*
	@Override
	public int getStartInventorySide(ForgeDirection side) {
		if (side == ForgeDirection.DOWN) return 2;
		if (side == ForgeDirection.UP) return 1;
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side) {
		return 1;
	}
*/
	public boolean consumeEnergy(int amnt)
	{
		if (this.energy - amnt > 0)
		{
			this.energy = this.energy - amnt;
			return true;
		}
		else
			return false;
	}

	public void dropItems(World world, int x, int y, int z) {
		Random rand = new Random();

		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (!(tileEntity instanceof IInventory)) {
			return;
		}
		IInventory inventory = (IInventory) tileEntity;

		for (int i = 0; i < inventory.getSizeInventory(); i++) {
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
					//entityItem.func_92014_d().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
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

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		if (i == chargeSlot)
		{
			if (itemstack.getItem() instanceof IElectricItem)
				return true;
			else
				return false;
		}
		else if (i >= upgradeSlotStart && i<upgradeSlotStart+4)
		{
			if (itemstack.getItem().equals(Items.getItem("overclockerUpgrade").getItem()))
				return true;
			else
				return false;
		}
		return true;
	}

	@Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        switch (side){
        case 0: return new int[] {0};
        case 1: return new int[] {1};
        default: return new int[] {2, 1};
        }
    }

	@Override
    public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return this.isStackValidForSlot(par1, par2ItemStack);
    }

	@Override
    public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3)
    {
        return true;
    }

}
