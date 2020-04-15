package mods.magico13.ExtraIndustrial.machine;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IWrenchable;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityEVTransformer extends TileEntity implements IEnergySource, IEnergySink, IWrenchable{

	private int highOutput = 8192;
	private int lowOutput = 2048;
	private short facing = 0;
	private boolean addedToENet = false;
	private int energy = 0;
	private int maxEnergy = 8192;
	private boolean redstone = false;

	@Override
	public void updateEntity()
	{
		if (!addedToENet && this.worldObj != null)
		{
			EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
			MinecraftForge.EVENT_BUS.post(loadEvent);
			addedToENet = true;
		}
		this.updateRedstone();
		
		if (this.redstone)
        {
            if (this.energy >= this.highOutput)
            {
                this.energy -= this.highOutput - this.sendEnergy(this.highOutput);
            }
        }
        else
        {
            for (int var1 = 0; var1 < 4 && this.energy >= this.lowOutput; ++var1)
            {
                this.energy -= this.lowOutput - this.sendEnergy(this.lowOutput);
            }
        }
	}
	
	private void updateRedstone()
	{
		if (this.worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) != this.redstone)
		{
			this.redstone = this.worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		}
	}
	
    private int sendEnergy(int amount)
    {
    	EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this, amount);
    	MinecraftForge.EVENT_BUS.post(sourceEvent);
    	return sourceEvent.amount;
    }
    
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		//return direction.toSideValue() != this.getFacing();
		return this.redstone ? this.facingMatchesDirection(direction) : !this.facingMatchesDirection(direction);
	}
	
	public boolean facingMatchesDirection(Direction direction)
	{
		return direction.toSideValue() == this.getFacing();
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return this.addedToENet;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
		return this.redstone ? !this.facingMatchesDirection(direction) : this.facingMatchesDirection(direction);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return this.getFacing() != side;
	}

	@Override
	public short getFacing() {
		return this.facing;
	}

	@Override
	public void setFacing(short facing) {	
		this.facing = facing;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
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
	public int demandsEnergy() {
		if (this.energy < this.maxEnergy && this.redstone)
			return this.lowOutput;
		else if (this.energy < this.maxEnergy && !this.redstone)
			return this.highOutput;
		else
			return 0;
	}
	@Override
	public int injectEnergy(Direction directionFrom, int amount) {
		if ((!this.redstone || amount <= this.lowOutput) && (this.redstone || amount <= this.highOutput || this.highOutput == 2048))
		{
			int transfer = amount;

			if (this.energy + amount >= this.maxEnergy + this.highOutput)
			{
				transfer = this.maxEnergy + this.highOutput - this.energy - 1;
			}

			this.energy += transfer;
			return amount - transfer;
		}
		return 0;
	}

	@Override
	public int getMaxSafeInput() {
		return this.highOutput;
	}

	@Override
	public int getMaxEnergyOutput() {
		return this.highOutput;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return PacketUtils.packetFromTileEntity(this);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound var1)
	{
		super.readFromNBT(var1);
		this.energy = var1.getInteger("energy");
		this.facing = var1.getShort("facing");

	}
	
	@Override
	public void writeToNBT(NBTTagCompound var1)
	{
		super.writeToNBT(var1);
		var1.setInteger("energy", this.energy);
		var1.setShort("facing", this.facing);
	}

}
