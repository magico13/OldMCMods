package mods.magico13.ExtraIndustrial.testing;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ic2.api.Direction;
import ic2.api.energy.tile.IEnergySink;

public class TileEntityEnergySink extends TileEntity implements IEnergySink{

	public int maxEnergy = 16384;
	public int energy = 0;
	public boolean redstone=false;
	private int ticksSinceUpdate=0;
	
	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, Direction direction)
	{
		return true;
	}

	@Override
	public boolean isAddedToEnergyNet() 
	{
		return true;
	}

	@Override
	public int demandsEnergy() 
	{
		if (energy < maxEnergy)
			return (maxEnergy-energy);
		else
			return 0;
	}

	@Override
	public int injectEnergy(Direction directionFrom, int amount) 
	{
		if (energy+amount<maxEnergy){
			energy=amount+energy;
			return 0;
		}
		else if (energy<maxEnergy){
			int transfer = (maxEnergy-energy);
			energy=energy+transfer;
			return amount-transfer;
		}
		else{
			return 0;
		}
	}

	@Override
	public int getMaxSafeInput() 
	{
		return 4096;
	}
	
	@Override
    public void updateEntity()
	{
		//emptyEnergy();
		updateRedstone();
		++this.ticksSinceUpdate;
		if (this.ticksSinceUpdate>20)
		{
			this.ticksSinceUpdate=0;
			if (this.redstone)
				emptyEnergy();
		}
	}
	
	public void updateRedstone()
	{
		boolean powered = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
		if (powered != this.redstone){
			this.redstone = powered;
			if (this.redstone)
				emptyEnergy();
		}
	}
	public void emptyEnergy()
	{
		this.energy = 0;
	}
}
