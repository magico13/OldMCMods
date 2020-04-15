package mods.magico13.ExtraIndustrial.generator;

import java.util.List;
import java.util.Random;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.testing.TileEntityEnergySink;
import mods.magico13.ExtraIndustrial.testing.TileEntityEnergySource;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGenerator extends BlockContainer{

	private Icon[] iconBuffer = new Icon[16];

	public BlockGenerator(int blockID)
	{
		super(blockID, Material.iron);
		this.setHardness(3.0f);
		this.setResistance(5.0f);
		this.setStepSound(soundStoneFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		//this.setUnlocalizedName("GeneratorBlock");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		iconBuffer[0] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FiveSolar0");
		iconBuffer[1] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FiveSolar1");
		iconBuffer[2] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FiveSolar2");
		iconBuffer[3] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":PeltierGenerator");
		iconBuffer[4] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FusionGeneratorCore");
		iconBuffer[5] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FusionGenerator0");
		iconBuffer[6] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FusionGenerator1");
		iconBuffer[7] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FusionGenerator2");
		iconBuffer[8] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FusionGeneratorOut1");
		iconBuffer[9] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":FusionGeneratorOut2");

	}


	@Override
	public Icon getBlockTexture(IBlockAccess block, int x, int y, int z, int side)
	{
		if (block.getBlockMetadata(x, y, z) != 3)
			return super.getBlockTexture(block, x, y, z, side);
		else
		{
			TileEntity tE = block.getBlockTileEntity(x, y, z);
			if (tE instanceof TileEntityFusionReactorWall)
			{
				int icon = 5;
				TileEntityFusionReactorWall wall = (TileEntityFusionReactorWall)tE;
				if (wall.core != null && wall.core.isFullyFormed())
				{
					++icon;
					if (wall.core.isGenerating())
						++icon;
				}
				if (wall.isConfiguredForOutput())
					icon = icon+2;
				return iconBuffer[icon];

			}
			return super.getBlockTexture(block, x, y, z, side);
		}
	}

	@Override
	public Icon getIcon(int side, int metadata)
	{
		switch (metadata){
		case 0:
			if (side == 0) return iconBuffer[2]; else if (side == 1) return iconBuffer[0]; else return iconBuffer[1];
		case 1:
			return iconBuffer[3];
		case 2:
			return iconBuffer[4];
		case 3:
			return iconBuffer[5];
		}
		return iconBuffer[0];
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		int meta = world.getBlockMetadata(x, y, z);
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}

		switch (meta)
		{
		case 3:
			if (world.getBlockTileEntity(x, y, z) instanceof TileEntityFusionReactorWall)
			{
				TileEntityFusionReactorCore core = ((TileEntityFusionReactorWall)world.getBlockTileEntity(x, y, z)).core;
				if (core != null && core.isFullyFormed())
				{
					player.openGui(ExtraIndustrialMod.instance, ExtraIndustrialReference.fusionReactorCoreGUI, world, core.xCoord, core.yCoord, core.zCoord);
					return true;
				}
			}
			return false;
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
			return new TileEntityFiveSolar();
		case 1:
			return new TileEntityPeltierGenerator();
		case 2:
			return new TileEntityFusionReactorCore();
		case 3:	
			return new TileEntityFusionReactorWall();
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
		if (metadata != 4)
			return metadata;
		else
			return 3;
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

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < ItemBlockGenerator.BlockNames.length; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) 
	{
		int meta = world.getBlockMetadata(x, y, z);
		if (meta == 3)
		{
			TileEntityFusionReactorWall tE = ((TileEntityFusionReactorWall)world.getBlockTileEntity(x, y, z));
			tE.initialize(true);
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) 
	{
		int meta = world.getBlockMetadata(x, y, z);
		if (meta == 3)
		{
			if (((TileEntityFusionReactorWall)world.getBlockTileEntity(x, y, z)).core != null)
				((TileEntityFusionReactorWall)world.getBlockTileEntity(x, y, z)).core.flagFormCheck();
		}
		else if (meta == 2)
		{
			((TileEntityFusionReactorCore)world.getBlockTileEntity(x, y, z)).coreDestruction();
		}

		ExtraIndustrialFunctions.removeElectricBlockTileEntity(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}

	private boolean blockIsCoreAndFormed(World world, int x, int y, int z)
	{
		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityFusionReactorCore)
		{
			return ((TileEntityFusionReactorCore)world.getBlockTileEntity(x, y, z)).isFullyFormed();
		}
		return false;
	}
}