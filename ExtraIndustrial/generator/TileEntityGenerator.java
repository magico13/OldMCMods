package mods.magico13.ExtraIndustrial.generator;

import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import ic2.api.Direction;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileSourceEvent;
import ic2.api.energy.tile.IEnergySource;

public class TileEntityGenerator extends TileEntity implements IEnergySource, ic2.api.tile.IWrenchable{

	protected int tickCount=0;
	protected boolean addedToENet;
	
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
	public boolean isAddedToEnergyNet() {
		return addedToENet;
	}

	@Override
	public boolean emitsEnergyTo(TileEntity receiver, Direction direction) {
		return false;
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
	public void setFacing(short facing) {	
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
	public int getMaxEnergyOutput() {
		return 0;
	}
	
    public void sendEnergy(int amount)
    {
    	EnergyTileSourceEvent sourceEvent = new EnergyTileSourceEvent(this, amount);
    	MinecraftForge.EVENT_BUS.post(sourceEvent);
    	return;
    }
    
	@Override
	public Packet getDescriptionPacket()
	{
		return PacketUtils.packetFromTileEntity(this);
	}
}
