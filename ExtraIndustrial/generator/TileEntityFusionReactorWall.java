package mods.magico13.ExtraIndustrial.generator;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.item.Items;
import ic2.api.tile.IWrenchable;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityFusionReactorWall extends TileEntity implements IInventory, IEnergySink, IEnergySource, IWrenchable {

	protected ItemStack[] inv;
	protected int[] coreLoc={-1,-1,-1};
	public boolean allowedToEmitEnergy = false;
	public TileEntityFusionReactorCore core;
	private boolean addedToENet, updated, outputer;

	public TileEntityFusionReactorWall() {

	}

	public void initialize(boolean alertCore)
	{		
		if (!this.worldObj.isRemote)
		{
			if (coreLoc[1] < 0)
				coreLoc = locateCore();
			if (coreLoc[1] > -1 && core == null)
			{
				core = (TileEntityFusionReactorCore) this.worldObj.getBlockTileEntity(coreLoc[0], coreLoc[1], coreLoc[2]);
				if (core != null)
					inv = core.inv;
			}
			if (alertCore)
				alertCoreToChange();
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (core == null && coreLoc[1] > -1)
		{
			//System.out.println("entering function");
			//initialize(true);
			core = (TileEntityFusionReactorCore) this.worldObj.getBlockTileEntity(coreLoc[0], coreLoc[1], coreLoc[2]);
			if (core != null)
			{
				inv = core.inv;
				if (this.isConfiguredForOutput())
					core.updateOutputters(new int[] {xCoord,yCoord,zCoord});
			}
			else
				coreLoc=new int[] {-1,-1,-1};
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		}
		else if (core != null && coreLoc[1] < 0)
		{
			core = null;
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if (!this.worldObj.isRemote)
		{
			if (!addedToENet && this.worldObj != null && core != null)
			{
				EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
				MinecraftForge.EVENT_BUS.post(loadEvent);
				addedToENet = true;
			}

			if (core != null)
			{
				if (core.isGenerating() && this.allowedToEmitEnergy)
					this.sendEnergy(8192);
			}
		}
	}

	public int[] locateCore()
	{
		for (int x=xCoord-2;x<=xCoord+2;x++)
			for (int y=yCoord-2;y<=yCoord+2;y++)
				for (int z=zCoord-2;z<=zCoord+2;z++)
					if (!((Math.abs(xCoord-x)==2&&Math.abs(yCoord-y)==2)||(Math.abs(xCoord-x)==2&&Math.abs(zCoord-z)==2)||(Math.abs(yCoord-y)==2&&Math.abs(zCoord-z)==2)))
					{
						if (this.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityFusionReactorCore)
							return new int[] {x,y,z};
					}
		return new int[] {-1,-1,-1};
	}

	public boolean alertCoreToChange()
	{
		if (core != null)
		{
			core.flagFormCheck();
			if (this.isConfiguredForOutput())
				core.updateOutputters(new int[] {xCoord,yCoord,zCoord});
			return true;
		}
		return false;
	}


	public void sendEnergy(int amount)
	{
		EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this, amount);
		MinecraftForge.EVENT_BUS.post(sourceEvent);
		return;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
		if (!isConfiguredForOutput() && core != null)
			return true;
		else
			return false;
	}

	@Override
	public int demandsEnergy() {
		if (core == null)
			return 0;

		if (core.isGenerating() || !core.isFullyFormed())
			return 0;
		else if (core.getCurrentEnergy() < core.maxEnergy)
			return core.maxEnergy - core.getCurrentEnergy();
		else
			return 0;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		if (core.isGenerating() || !core.isFullyFormed())
			return amount;
		if (core.getCurrentEnergy()+amount<core.maxEnergy) {
			core.addEnergy(amount);
			return 0;
		}
		else if (core.getCurrentEnergy()<core.maxEnergy) {
			int transfer = (core.maxEnergy-core.getCurrentEnergy());
			core.addEnergy(transfer);
			return amount-transfer;
		}
		else{
			return amount;
		}
	}

	@Override
	public int getMaxSafeInput() {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getSizeInventory() {
		if (inv != null)
			return inv.length;
		else
			return 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		if (inv != null)
			return inv[slot];
		else
			return null;
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
		return "fusionInv";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.coreLoc = nbt.getIntArray("coreLoc");
		this.outputer = nbt.getBoolean("isOutputter");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setIntArray("coreLoc", this.coreLoc);
		nbt.setBoolean("isOutputter", this.outputer);
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketUtils.packetFromTileEntity(this);
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return this.addedToENet;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return this.isConfiguredForOutput();
	}

	@Override
	public int getMaxEnergyOutput() {
		return 8192;
	}

	public boolean isConfiguredForOutput() {
		return this.outputer;
	}

	public void changeOutputterState(boolean state)
	{
		this.outputer = state;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		if (entityPlayer.isSneaking() && this.core != null)
			return true;
		else
			return false;
	}

	@Override
	public short getFacing() {
		return 0;
	}

	@Override
	public void setFacing(short facing) {
		if (this.core != null)
		{
			this.changeOutputterState(!this.isConfiguredForOutput());
			this.alertCoreToChange();
		}
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return false;
	}

	@Override
	public float getWrenchDropRate() {
		return 0;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return null;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		if (core != null && core.isFullyFormed() && itemstack.getItem() == Items.getItem("electrolyzedWaterCell").getItem())
			return true;
		return false;
	}

}
