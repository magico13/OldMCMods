package mods.magico13.ExtraIndustrial.generator;

import java.util.Vector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ic2.api.Direction;
import ic2.api.item.Items;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyTile;
import mods.magico13.ExtraIndustrial.cable.BlockSuperconductorCable;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import mods.magico13.ExtraIndustrial.core.INetworkCommandable;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import mods.magico13.ExtraIndustrial.generator.TileEntityFusionReactorWall;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityFusionReactorCore extends TileEntity implements IInventory, INetworkCommandable {

	/* TODO:
	 * Fix pipes/cables coming disconnected (Done? Need to double check pipes)
	 * Explode if broken while running (DONE! And it's awesome!)
	 * Figure out a better way of determining where to output power (Single output now, which is fine)
	 * Put lava inside when running (Done!)
	 * TEXTURES! (almost)
	 * GUI textures
	 */


	public int energy=0;
	public int maxEnergy = 50000000;
	public int hydrogenAmount=0;
	public ItemStack[] inv;
	protected boolean fullyFormed;
	protected int updateTicks=0;
	private boolean generating;
	private boolean setup;
	private int[] outCoord;
	private boolean checkFormNextTick = false;
	public boolean generateFlag = false;

	public TileEntityFusionReactorCore()
	{
		inv = new ItemStack[1];
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (this.checkFormNextTick)
		{
			this.checkForm();
			this.checkFormNextTick = false;
		}

		if (!this.worldObj.isRemote)
		{
			if (!this.isFullyFormed() && outCoord != null)
			{
				disableOutputter();
			}

			if (this.isFullyFormed())
			{
				if (!this.isGenerating() && generateFlag && this.getCurrentEnergy() >= this.maxEnergy && this.getCurrentHydrogen() > 1)
				{
					this.generateFlag = false;
					this.generating = true;
					this.energy = 0;
					this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					for (int x=xCoord-1; x<=xCoord+1; x++)
						for (int y=yCoord-1; y<=yCoord+1; y++)
							for (int z=zCoord-1; z<=zCoord+1; z++)
							{
								if (this.worldObj.getBlockId(x, y, z) == 0)
									this.worldObj.setBlock(x, y, z, Block.lavaMoving.blockID);
							}
				}
				
				ItemStack stack0 = this.getStackInSlot(0);
				if (stack0 != null)
				{
					if (ExtraIndustrialFunctions.identicalItems(stack0, Items.getItem("electrolyzedWaterCell")) && this.hydrogenAmount < 9001)
					{
						this.decrStackSize(0, 1);
						this.hydrogenAmount += 1000;
					}
					if (ExtraIndustrialFunctions.identicalItems(stack0, new ItemStack(ExtraIndustrialMod.blockTesting,1,0)))
					{
						if (this.hydrogenAmount != 10000)
							this.hydrogenAmount = 10000;
						if (this.energy != this.maxEnergy)
							this.energy = this.maxEnergy;
					}
					if (ExtraIndustrialFunctions.identicalItems(stack0, new ItemStack(ExtraIndustrialMod.blockTesting,1,1)))
					{
						if (this.hydrogenAmount != 0)
							this.hydrogenAmount = 0;
						if (this.energy != 0)
							this.energy = 0;
					}
				}
			}

			if (this.isFullyFormed() && this.isGenerating())
			{
				if (this.hydrogenAmount >= 20)
					this.hydrogenAmount -= 20;
				if (this.hydrogenAmount < 20)
					stopGenerating();
			}

			if (!this.isFullyFormed() && this.isGenerating())
			{
				this.generating = false;
				this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				this.worldObj.createExplosion(null, xCoord, yCoord, zCoord, 100.0F, true);
			}
			
			if(this.generateFlag)
				this.generateFlag = false;
		}
	}
	
	private void stopGenerating()
	{
		this.generating = false;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		for (int x=xCoord-1; x<=xCoord+1; x++)
			for (int y=yCoord+1; y>=yCoord-1; y--)
				for (int z=zCoord-1; z<=zCoord+1; z++)
					if (this.worldObj.getBlockId(x, y, z) == Block.lavaMoving.blockID
							|| this.worldObj.getBlockId(x, y, z) == Block.lavaStill.blockID)
						this.worldObj.setBlockToAir(x, y, z);
	}

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
		return "fusionInv";
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.energy = nbt.getInteger("energy");
		this.hydrogenAmount = nbt.getInteger("hydrogen");
		this.generating = nbt.getBoolean("generating");
		this.fullyFormed = nbt.getBoolean("formed");
		this.outCoord = nbt.getIntArray("outCoord");

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
		nbt.setInteger("energy", this.energy);
		nbt.setInteger("hydrogen", this.hydrogenAmount);
		nbt.setBoolean("generating", this.generating);
		nbt.setBoolean("formed", this.fullyFormed);
		if (outCoord != null)
			nbt.setIntArray("outCoord", this.outCoord);

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

	public boolean isFullyFormed()
	{
		return fullyFormed;
	}

	public void flagFormCheck()
	{
		this.checkFormNextTick = true;
	}

	public void checkForm()
	{
		boolean failed = false;
		// Check the outer blocks to see if they're correct
		for (int x=xCoord-2;x<=xCoord+2;x++)
			for (int y=yCoord-2;y<=yCoord+2;y++)
				for (int z=zCoord-2;z<=zCoord+2;z++)
				{
					if (!((Math.abs(xCoord-x)==2&&Math.abs(yCoord-y)==2)||(Math.abs(xCoord-x)==2&&Math.abs(zCoord-z)==2)||(Math.abs(yCoord-y)==2&&Math.abs(zCoord-z)==2)))
						if ( (Math.abs(xCoord-x)==2||Math.abs(yCoord-y)==2||Math.abs(zCoord-z)==2))
							//if(!((Math.abs(xCoord-x)==1&&Math.abs(yCoord-y)==1)||(Math.abs(xCoord-x)==1&&Math.abs(zCoord-z)==1)||(Math.abs(yCoord-y)==1&&Math.abs(zCoord-z)==1)))
							if (!checkBlock(x, y, z))
							{
								failed = true;
								break;
							}

				}
		if (failed)
		{
			fullyFormed = false;
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return;
		}
		// Check the inner connecting blocks to see if they're correct
		for (int i=0;i<6;i++)
		{
			if (!(ExtraIndustrialFunctions.getTileEntityonFace(worldObj, xCoord, yCoord, zCoord, i) instanceof TileEntityFusionReactorWall))
			{
				failed = true;
				break;
			}
		}

		if (failed)
		{
			fullyFormed = false;
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			return;
		}

		fullyFormed = true;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		// Alert all blocks of status, so they can find the core if they didn't already
		for (int x=xCoord-2;x<=xCoord+2;x++)
			for (int y=yCoord-2;y<=yCoord+2;y++)
				for (int z=zCoord-2;z<=zCoord+2;z++)
					if (!((Math.abs(xCoord-x)==2&&Math.abs(yCoord-y)==2)||(Math.abs(xCoord-x)==2&&Math.abs(zCoord-z)==2)||(Math.abs(yCoord-y)==2&&Math.abs(zCoord-z)==2)))
					{
						if (this.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityFusionReactorWall)
						{
							TileEntityFusionReactorWall wall = (TileEntityFusionReactorWall)this.worldObj.getBlockTileEntity(x, y, z);
							//if (wall.core == null)
							{
								wall.coreLoc = new int[] {xCoord, yCoord, zCoord};
								wall.core = this;
								this.worldObj.markBlockForUpdate(x, y, z);
							}
						}

					}
	}

	public void coreDestruction()
	{
		for (int x=xCoord-2;x<=xCoord+2;x++)
			for (int y=yCoord-2;y<=yCoord+2;y++)
				for (int z=zCoord-2;z<=zCoord+2;z++)
					//if (!((Math.abs(xCoord-x)==2&&Math.abs(yCoord-y)==2)||(Math.abs(xCoord-x)==2&&Math.abs(zCoord-z)==2)||(Math.abs(yCoord-y)==2&&Math.abs(zCoord-z)==2)))
					if (this.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityFusionReactorWall)
					{
						TileEntityFusionReactorWall wall = (TileEntityFusionReactorWall)this.worldObj.getBlockTileEntity(x, y, z);
						if (wall.core != null)
						{
							wall.coreLoc = new int[] {-1, -1, -1};
							wall.core = null;
							this.worldObj.markBlockForUpdate(x, y, z);
						}
					}
	}


	private boolean checkBlock(int x, int y, int z)
	{
		if (this.worldObj.getBlockTileEntity(x, y, z) instanceof TileEntityFusionReactorWall)
		{
			return true;
		}
		return false;
	}

	public void updateOutputters(int[] coords)
	{
		disableOutputter();
		outCoord = coords;
		TileEntityFusionReactorWall newOut = ((TileEntityFusionReactorWall)this.worldObj.getBlockTileEntity(outCoord[0], outCoord[1], outCoord[2]));
		newOut.changeOutputterState(true);
		newOut.allowedToEmitEnergy = true;

	}

	private void disableOutputter()
	{
		if (outCoord!=null && outCoord.length==3 && outCoord[1]>-1)
		{
			TileEntity tE = this.worldObj.getBlockTileEntity(outCoord[0], outCoord[1], outCoord[2]);
			if (tE instanceof TileEntityFusionReactorWall)
			{
				TileEntityFusionReactorWall wallTE = (TileEntityFusionReactorWall)tE;
				wallTE.changeOutputterState(false);
				wallTE.allowedToEmitEnergy = false;
				outCoord=null;
			}
		}
		outCoord=null;
	}

	public int getCurrentEnergy()
	{
		return this.energy;
	}

	public int getCurrentHydrogen()
	{
		return this.hydrogenAmount;
	}

	public void addEnergy(int amount)
	{
		this.energy = this.energy + amount;
	}

	public boolean isGenerating()
	{
		return this.generating;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		if (this.isFullyFormed() && ExtraIndustrialFunctions.identicalItems(itemstack, Items.getItem("electrolyzedWaterCell")))
			return true;
		return false;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketUtils.packetFromTileEntity(this);
	}

	@SideOnly(Side.CLIENT)
	public int getChargeScaled(int par1)
	{
		return (int)((float)this.energy * par1 / this.maxEnergy);
	}

	@SideOnly(Side.CLIENT)
	public int getHydrogenScaled(int par1)
	{
		return this.hydrogenAmount * par1 / 10000;
	}

	@Override
	public void executeNetworkCommand(int commandID, int value) 
	{
		switch(commandID) {
		case 0: 
			if (value == 1) this.generateFlag = true;
			else if (value == 0) this.stopGenerating();
			break;
		}	
	}


}
