package magico13.mods.NetherBits.machine;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;

import magico13.mods.NetherBits.NetherBitsMod;
import magico13.mods.NetherBits.NetherBitsModFunctions;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet130UpdateSign;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityLavaGen extends TileEntity {

	// The pair coordinates, pairing status, generator status, and number of ticks since last update
	private int pairX=0;
	private int pairY=0;
	private int pairZ=0;
	private boolean paired=false;
	public boolean isGenerator;
	private int updateTicks=0;

	@Override
	public void updateEntity()
	{
		++updateTicks;
		
		if (this.isPaired()) {
			if ((updateTicks) % (20*NetherBitsMod.resonatorFrequency/13) == 0)
				this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}

		if (updateTicks>=(20*NetherBitsMod.resonatorFrequency))
		{
			updateTicks=0;
			if (!this.worldObj.isRemote)
			{
				if (!this.isPaired())
				{
					this.tryToSetPairs();
				}
				else
				{
					if (this.verifyPairing())
					{
						paired = true;
						if (this.isGenerator)
						{
							this.createLavaSource();
						}
					}
					else
					{
						paired = false;
						this.tryToSetPairs();
					}
				}
			}			
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return NetherBitsModFunctions.packetFromTileEntity(this);
	}
	
	/** Called when right clicked with debug mode active*/
	public void debug(EntityPlayer player) 
	{
		player.addChatMessage("Position: " + xCoord + " " + yCoord + " " + zCoord);

		int timeSince = (updateTicks/20);
		int timeTill = (NetherBitsMod.resonatorFrequency) - (updateTicks/20);

		player.addChatMessage("Time since update: " + timeSince + " seconds");
		player.addChatMessage("Time until update: " + timeTill + " seconds");
		player.addChatMessage("Paired? " + this.isPaired());
		if (this.isPaired())
		{
			player.addChatMessage("Pair: "+pairX+" "+pairY+" "+pairZ);
			player.addChatMessage("Generator? " + this.isGenerator);
		}
	}
	/**
	 * Set the resonator's pair coordinates to the ones passed
	 * @param x
	 * @param y
	 * @param z
	 * @param pairing
	 * @param generator
	 */
	public void setPairs(int x, int y, int z, boolean pairing, boolean generator)
	{
		this.updateTicks = 0;
		this.pairX = x;
		this.pairY = y;
		this.pairZ = z;
		this.paired = pairing;
		this.isGenerator = generator;
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		// Not sure why this isn't re-rendering the block correctly, had to do it in the
		// ClientProxy as well.
	}

	/**
	 * Checks to see if this resonator's pair has this block as it's pair.
	 * @return success
	 */
	public boolean verifyPairing()
	{
		if  (worldObj.getBlockTileEntity(pairX, pairY, pairZ) instanceof TileEntityLavaGen)
		{
			TileEntityLavaGen t = (TileEntityLavaGen) worldObj.getBlockTileEntity(pairX, pairY, pairZ);
			int[] pair = t.getPair();
			if (pair[0] == xCoord && pair[1] == yCoord && pair[2] == zCoord) 
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Attempts to find an unpaired resonator and set pairing.
	 * @return success
	 */
	public boolean tryToSetPairs()
	{
		int[] ResonatorPos = checkForResonators();
		if (ResonatorPos[1] != 0)
		{
			int pX=ResonatorPos[0];
			int pY=ResonatorPos[1];
			int pZ=ResonatorPos[2];
			this.setPairs(pX, pY, pZ, true, true);
			TileEntityLavaGen t2 = (TileEntityLavaGen) this.worldObj.getBlockTileEntity(pX, pY, pZ);
			t2.setPairs(xCoord, yCoord, zCoord, true, false);
			return true;
		}
		return false;
			
	}
	
	/** 
	 * Checks to see if the target block is viable for pairing
	 * @param targX
	 * @param targY
	 * @param targZ
	 * @return if the block can be paired with
	 */
	private boolean targetPairable(int targX, int targY, int targZ)
	{
		//if (this.worldObj.getBlockId(targX, targY, targZ) == NetherBitsMod.MachineID)
		{
			if (this.worldObj.getBlockTileEntity(targX, targY, targZ) instanceof TileEntityLavaGen)
			{
				TileEntityLavaGen tE = (TileEntityLavaGen)this.worldObj.getBlockTileEntity(targX, targY, targZ);
				if (!tE.isPaired())
					return true;
			}
		}
		return false;
	}
	/**
	 * Checks surrounding blocks for an open resonator
	 * @return coordinates of first open resonator, or 0,0,0 if none found
	 */
	private int[] checkForResonators()
	{
		int[] resonatorPos;
		// Check two blocks away, returning first found resonator's position.
		if (targetPairable(xCoord-2, yCoord, zCoord))
		{
			resonatorPos = new int[] {xCoord-2,yCoord,zCoord};
		}
		else if (targetPairable(xCoord+2, yCoord, zCoord))
		{
			resonatorPos = new int[] {xCoord+2,yCoord,zCoord};
		}
		else if (targetPairable(xCoord, yCoord, zCoord-2))
		{
			resonatorPos = new int[] {xCoord,yCoord,zCoord-2};
		}
		else if (targetPairable(xCoord, yCoord, zCoord+2))
		{
			resonatorPos = new int[] {xCoord,yCoord,zCoord+2};
		}
		else
		{// If no resonators found, return (0,0,0)
			resonatorPos = new int[] {0, 0, 0};
		}
		return resonatorPos;
	}
	
	/**
	 * Creates a lava source block between the two paired resonators 
	 * @return success
	 */
	 
	public boolean createLavaSource()
	{
		// Set the position of the source to between the two resonators
		int srcX = (xCoord + pairX) /2;
		int srcZ = (zCoord + pairZ) /2;
		int srcY = yCoord;
		// If the block is empty, or either lava block, set it to a lavaMoving block
		if (this.worldObj.isAirBlock(srcX, srcY, srcZ) || this.worldObj.getBlockId(srcX, srcY, srcZ) == Block.lavaMoving.blockID || this.worldObj.getBlockId(srcX, srcY, srcZ) == Block.lavaStill.blockID)
		{
			this.worldObj.setBlockWithNotify(srcX, srcY, srcZ, Block.lavaMoving.blockID);
			return true;
		}

		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.pairX = nbt.getInteger("pairX");
		this.pairY = nbt.getInteger("pairY");
		this.pairZ = nbt.getInteger("pairZ");
		this.paired = nbt.getBoolean("paired");
		this.isGenerator = nbt.getBoolean("isGenerator");
		this.updateTicks = nbt.getInteger("updateTicks");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("pairX", this.pairX);
		nbt.setInteger("pairY", this.pairY);
		nbt.setInteger("pairZ", this.pairZ);
		nbt.setBoolean("paired", this.paired);
		nbt.setBoolean("isGenerator", this.isGenerator);
		nbt.setInteger("updateTicks", this.updateTicks);
	}

	/**
	 * Gets which face the lava source block will be placed on
	 * @return face
	 */
	public int getFacing() {
		if (this.isPaired())
		{
			if (pairX > xCoord)
				return 5;
			else if (pairX < xCoord)
				return 4;
			else if (pairZ > zCoord)
				return 3;
			else if (pairZ < zCoord)
				return 2;
		}
		return 0;
	}

	/**
	 * Gets the resonator's pair coordinates
	 * @return pair coordinates, or 0,0,0 if unpaired
	 */
	public int[] getPair() {
		if (this.isPaired())
		{
			return new int [] {pairX, pairY, pairZ};
		}
		else
			return new int[] {0,0,0};
	}

	/**
	 * Gets whether the resonator is paired
	 * @return paired
	 */
	public boolean isPaired()
	{
		return this.paired;
	}
	
	/**
	 * Gets how far the resonator is in a cycle as a float from 0 to 1
	 * @return amount of cycle completed
	 */
	public float getFractionDone()
	{
		return (float)updateTicks/(20*NetherBitsMod.resonatorFrequency);
	}

}
