package mods.magico13.ExtraIndustrial.testing;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockTestingBlock extends BlockContainer{

	private Icon[] iconBuffer = new Icon[2]; 
	public BlockTestingBlock(int blockID)
	{
		super(blockID, Material.iron);
		this.setHardness(3.0f);
		this.setResistance(5.0f);
		this.setStepSound(soundStoneFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		//this.setUnlocalizedName("TestingBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		iconBuffer[0] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":CarbonBlock");
		iconBuffer[1] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":BlueBlock");
	}

	@Override
	public Icon getIcon(int side, int metadata) 
	{
		return iconBuffer[metadata];
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}

		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityEnergySink)
		{
			if (!world.isRemote){
				TileEntityEnergySink t = (TileEntityEnergySink)world.getBlockTileEntity(x, y, z);
				player.addChatMessage("Stored Energy: "+t.energy);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch (metadata)
		{
		case 0:
			return new TileEntityEnergySource();
		case 1:
			return new TileEntityEnergySink();
		}
		return null;

	}

	@Override
	public int idDropped(int metadata, Random par2Random, int par3)
	{
		return this.blockID;
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata;
	}

	@Override
	public int quantityDropped(int metadata, int fortune, Random random)
	{
		return 1;
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z)
	{
		return world.getBlockMetadata(x, y, z);
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		ExtraIndustrialFunctions.removeElectricBlockTileEntity(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < ItemBlockTestingBlock.BlockNames.length; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}
}