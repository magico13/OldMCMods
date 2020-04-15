package magico13.mods.NetherBits.machine;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMachine extends BlockContainer{

	public BlockMachine(int blockID)
	{
		super(blockID, Material.rock);
		this.setRequiresSelfNotify();
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setStepSound(soundStoneFootstep);
	}

	@Override
	public int getBlockTexture(IBlockAccess block, int x, int y, int z, int side)
	{
		int meta = block.getBlockMetadata(x, y, z);
		if (meta == 0)
			return super.getBlockTexture(block, x, y, z, side);
		if (meta == 1)
		{
			int startIndex = 16;
			int endIndex = 29;
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


			float fractionDone = tE.getFractionDone();
			int offset = (int) (13 * fractionDone);
			targIndex = startIndex + offset + 1;

			if (side == face)
				return targIndex;
			else if (side == 0 || side == 1)
				return startIndex;
			else
				return startIndex;
		}


		return super.getBlockTexture(block, x, y, z, side);
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) 
	{
		switch (metadata)
		{
		case 0:
			return side+10;
		case 1:
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
		return 0;
	}

	@Override
	public String getTextureFile()
	{
		return "/magico13/mods/NetherBits/terrain.png";
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < ItemBlockMachine.BlockNames.length; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		switch (metadata)
		{
		case 0:
			return new TileEntityCobbleGen();
		case 1:
			return new TileEntityLavaGen();
		}
		return null;

	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		int meta = world.getBlockMetadata(x, y, z);
		switch (meta)
		{
		case 0:
			player.openGui(NetherBitsMod.instance, NetherBitsMod.cobbleGenGUI, world, x, y, z);
			return true;
		case 1:
			// When right clicked, do nothing unless debugMode is active, then tell debug info
			if(world.isRemote)
			{
				if (NetherBitsMod.debugMode)
				{
					TileEntityLavaGen t = (TileEntityLavaGen) world.getBlockTileEntity(x, y, z);
					t.debug(player);
					return true;
				}
			}
			break;
		}

		return false;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) 
	{
		// When added to world, try to find viable pairs
		if (!world.isRemote)
		{
			TileEntity tE =  world.getBlockTileEntity(x, y, z);
			if (tE instanceof TileEntityLavaGen)
				((TileEntityLavaGen) tE).tryToSetPairs();
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntity tE = world.getBlockTileEntity(x,y,z);
		if (tE instanceof TileEntityCobbleGen)
		{
			TileEntityCobbleGen t = (TileEntityCobbleGen)tE;
			t.dropItems(world, x, y, z);
		}
		else if (tE instanceof TileEntityLavaGen)
		{
			TileEntityLavaGen t = (TileEntityLavaGen)tE;
			int[] pair = t.getPair();
			if (pair[1] != 0) {
				TileEntityLavaGen t2 = (TileEntityLavaGen) world.getBlockTileEntity(pair[0], pair[1], pair[2]);
				t2.setPairs(0, 0, 0, false, false);
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}
}
