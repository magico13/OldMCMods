package magico13.mods.NetherBits.machine;

import java.util.Random;

import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCobbleGen extends BlockContainer {

	public BlockCobbleGen(int blockID) {
		super(blockID, Material.rock);
		//this.setCreativeTab(CreativeTabs.tabBlock);
		this.setRequiresSelfNotify();
		this.setHardness(2.0f);
		this.setResistance(10.0f);
		this.setBlockName("CobbleGen");
		this.setStepSound(soundStoneFootstep);
	}
	@Override
	public int getBlockTextureFromSideAndMetadata(int side, int metadata) 
	{
		return side+10;
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
		return 0;
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
			return new TileEntityCobbleGen();
		}
		catch (Exception var3)
		{
			throw new RuntimeException(var3);
		}
	} 
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		//opens gui, to be implemented later
		player.openGui(NetherBitsMod.instance, 0, world, x, y, z);
		//player.openGui();
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntityCobbleGen t = (TileEntityCobbleGen) world.getBlockTileEntity(x, y, z);
		t.dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}


}

