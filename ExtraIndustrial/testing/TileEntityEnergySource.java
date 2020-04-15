package mods.magico13.ExtraIndustrial.testing;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import ic2.api.Direction;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySource;

public class TileEntityEnergySource extends TileEntity implements IEnergySource{
	int energyOutput = 32;
	public boolean redstone=false;
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return true;
	}

	@Override
	public boolean isAddedToEnergyNet() {
		return true;
	}

	@Override
	public int getMaxEnergyOutput() {
		return this.energyOutput;
	}

	@Override
	public void updateEntity() {
		this.updateRedstone();
		if (!this.redstone)
			this.sendEnergy(this.energyOutput);
	}
	
    public void sendEnergy(int var1)
    {
    	EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this, var1);
    	MinecraftForge.EVENT_BUS.post(sourceEvent);
    	return;
    }
    
    public void updateRedstone()
	{
		boolean powered = this.worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord);
		if (powered != this.redstone){
			this.redstone = powered;	
		}
	}
}
