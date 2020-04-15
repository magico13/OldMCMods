package magico13.mods.NetherBits.machine;

import java.util.Date;
import java.util.Random;

import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cpw.mods.fml.client.FMLClientHandler;

public class BlockLavaGen extends BlockContainer {

	public BlockLavaGen(int blockID) {
		super(blockID, Material.rock);
		//this.setCreativeTab(CreativeTabs.tabBlock);
		this.setRequiresSelfNotify();
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setBlockName("LavaGen");
		this.setStepSound(soundStoneFootstep);
	}

	@Override
	public int getBlockTexture(IBlockAccess block, int x, int y, int z, int side)
	{
		int startIndex = 16;
		int targIndex = 20;

		TileEntityLavaGen tE = (TileEntityLavaGen)block.getBlockTileEntity(x, y, z);
		int face;
		if (tE != null)
			face = tE.getFacing();
		else
			face = 0;

		if (face == 0){
			if (side == 0 || side == 1)
				return startIndex;
			else
				return startIndex+1;
		}


		float perDone = tE.getFractionDone();
		if (perDone>=0.75F)
			targIndex = 29;
		else if (perDone>=0.50F)
			targIndex = 23;
		else if (perDone>=0.25F)
			targIndex = 20;
		else
			targIndex = startIndex+1;

		if (side == face)
			return targIndex;
		else if (side == 0 || side == 1)
			return startIndex;
		else
			return startIndex;
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) 
	{
		switch (side) 
		{
		case 0:return 16; //bottom -y
		case 1:return 16; //top +y
		case 2:return 29; //north -z
		case 3:return 29; //south +z
		case 4:return 29; //west -x
		case 5:return 29; //east +x
		default:return 16;
		}
	}

	@Override
	public String getTextureFile()
	{
		return "/magico13/mods/NetherBits/terrain.png";
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
    {
        return NetherBitsMod.MachineID;
    }
	
	@Override
	public int damageDropped(int metadata)
	{
		return 1;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) 
	{
		// When added to world, try to find viable pairs
		//if (!world.isRemote)
			((TileEntityLavaGen) world.getBlockTileEntity(x, y, z)).tryToSetPairs();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) 
	{
		// When right clicked, do nothing unless debugMode is active, then tell where the pair is
		if(world.isRemote)
		{
			if (NetherBitsMod.debugMode)
			{
				TileEntityLavaGen t = (TileEntityLavaGen) world.getBlockTileEntity(x, y, z);
				t.debug(player);
				return true;
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
	{
		TileEntityLavaGen t = (TileEntityLavaGen) par1World.getBlockTileEntity(par2, par3, par4);
		int[] pair = t.getPair();
		if (pair[1] != 0) {
			TileEntityLavaGen t2 = (TileEntityLavaGen) par1World.getBlockTileEntity(pair[0], pair[1], pair[2]);
			t2.setPairs(0, 0, 0, false, false);
		}
		super.breakBlock(par1World, par2, par3, par4, par5, par6);

	}


	// Specifies that the block has a tile entity.
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World par1World)
	{
		try
		{
			return new TileEntityLavaGen();
		}
		catch (Exception var3)
		{
			throw new RuntimeException(var3);
		}
	} 
}
