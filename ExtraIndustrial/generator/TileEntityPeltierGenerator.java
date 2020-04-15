package mods.magico13.ExtraIndustrial.generator;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileSourceEvent;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityPeltierGenerator extends TileEntityGenerator {
	private int output = 1;
	private boolean conditionsMet;
	boolean iceFound = false;
	boolean waterFound = false;
	
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return true;
	}
	
	@Override
	public int getMaxEnergyOutput() {
		return this.output;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		++tickCount;
		if (tickCount >= 8){
			tickCount = 0;
			conditionsMet = checkConditions();
		}
		
		if (conditionsMet)
		{
			if (iceFound)
				this.sendEnergy(2);
			else if (waterFound && tickCount%4 == 0)
				this.sendEnergy(1);		
		}
			
		
	}
	protected boolean checkConditions()
	{
		waterFound = false;
		iceFound = false;
		for (int i=0; i<6; i=i+2)
		{
			int block1 = ExtraIndustrialFunctions.getBlockonFace(worldObj, xCoord, yCoord, zCoord, i);
			int block2 = ExtraIndustrialFunctions.getBlockonFace(worldObj, xCoord, yCoord, zCoord, i+1);
			if ((block1 == Block.lavaStill.blockID || block2 == Block.lavaStill.blockID)
					&& (block1 == Block.waterStill.blockID || block2 == Block.waterStill.blockID)) {
				waterFound = true;
			}
				
			else if ((block1 == Block.lavaStill.blockID || block2 == Block.lavaStill.blockID)
					&& (block1 == Block.ice.blockID || block2 == Block.ice.blockID)) {
				iceFound = true;
			}
		}
		
		if (waterFound || iceFound)
			return true;
		else
			return false;
	}
	
	
    public void sendEnergy(int amount)
    {
    	EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this, amount);
    	MinecraftForge.EVENT_BUS.post(sourceEvent);
    	return;
    }
    
}
