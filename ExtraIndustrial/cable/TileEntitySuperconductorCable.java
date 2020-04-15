package mods.magico13.ExtraIndustrial.cable;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.tile.IEnergyConductor;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;

public class TileEntitySuperconductorCable extends TileEntity implements IEnergyConductor, ic2.api.tile.IWrenchable{

	private boolean addedToENet;
	
	@Override
	public void updateEntity()
	{
		if (!addedToENet && this.worldObj != null)
		{
			EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
			MinecraftForge.EVENT_BUS.post(loadEvent);
			addedToENet = true;
		}
	}
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction) {
		return true;
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return addedToENet;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return true;
	}

	@Override
	public double getConductionLoss() {
		return 0.0D;
	}

	@Override
	public int getInsulationEnergyAbsorption() {
		return 9001;
	}

	@Override
	public int getInsulationBreakdownEnergy() {
		return 9001;
	}

	@Override
	public int getConductorBreakdownEnergy() {
		return 8193;
	}

	@Override
	public void removeInsulation() {	
	}

	@Override
	public void removeConductor() {
		this.worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		this.worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return false;
	}

	@Override
	public short getFacing() {
		return 0;
	}

	@Override
	public void setFacing(short facing) {}

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

}
