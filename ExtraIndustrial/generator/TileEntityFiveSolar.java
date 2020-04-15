package mods.magico13.ExtraIndustrial.generator;

import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.tile.IEnergySource;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityFiveSolar extends TileEntityGenerator {

	private int sunOutput = 5;
	private boolean sun;
	private int light;
	private int darkOutput = 0;
	
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		if (direction.toForgeDirection().equals(ForgeDirection.UP))
			return false;
		else
			return true;
	}

	@Override
	public int getMaxEnergyOutput() {
		return this.sunOutput;
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		++tickCount;
		if (tickCount >= 10){
			sun = canSeeSun();
			tickCount = 0;
			if (!sun)
			{
				light = this.worldObj.getBlockLightValue(xCoord, yCoord+1, zCoord);
				if (light < 7)
					darkOutput = 0;
				else if (light >= 7 && light < 15)
					darkOutput = 1;
				else if (light >= 15)
					darkOutput = 2;
			}
		}
		if (this.sun)
			this.sendEnergy(this.sunOutput);
		else if (this.darkOutput != 0)
			this.sendEnergy(this.darkOutput);
	}
    
    public boolean canSeeSun()
    {
    	boolean hasNoSun=worldObj.provider.hasNoSky;
    	boolean canRain=worldObj.getWorldChunkManager().getBiomeGenAt(xCoord, zCoord).getIntRainfall()>0;
    	boolean rain = (this.worldObj.isRaining() || this.worldObj.isThundering());
    	
    	if (this.worldObj.canBlockSeeTheSky(this.xCoord, this.yCoord+1, this.zCoord) && 
    			this.worldObj.isDaytime() && (!rain || !canRain))
    		return true;
    	else
    		return false;
    }
}
