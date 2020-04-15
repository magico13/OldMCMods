package mods.magico13.ExtraIndustrial.machine;

import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.Items;
import ic2.api.tile.IWrenchable;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.generator.ItemBlockGenerator;
import mods.magico13.ExtraIndustrial.generator.TileEntityFiveSolar;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMachine extends BlockContainer{

	private Icon[] iconBuffer = new Icon[16];
	public BlockMachine(int blockID)
	{
		super(blockID, Material.iron);
		//this.setRequiresSelfNotify();
		this.setHardness(3.0f);
		this.setResistance(5.0f);
		this.setStepSound(soundStoneFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		//this.setUnlocalizedName("MachineBlock");
	}

	
	@Override
	public Icon getBlockTexture(IBlockAccess block, int x, int y, int z, int side)
	{
		IWrenchable tE = (IWrenchable) block.getBlockTileEntity(x, y, z);
		short face = tE.getFacing();
		int meta = block.getBlockMetadata(x, y, z);
		switch (meta) {
		case 0:
			if (side < 2) 
				return iconBuffer[side];
			else if (side == face) 
				return iconBuffer[2];
			else
				return iconBuffer[1];
		case 1:
			switch (side) {
			case 0: return iconBuffer[0];
			case 1: return iconBuffer[3];
			default: return iconBuffer[1];
			}
		case 2:
			if (side == face) return iconBuffer[5];
			else return iconBuffer[4];
		}
		return iconBuffer[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		iconBuffer[0] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":machine0");
		iconBuffer[1] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":machine1");
		iconBuffer[2] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":IngotCompactor");
		iconBuffer[3] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":IndustrialCrafter");
		iconBuffer[4] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":CarbonBlock");
		iconBuffer[5] = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":BlueBlock");
	}
	
	@Override
	public Icon getIcon(int side, int metadata) 
	{
		switch (metadata){
		case 0:
			if (side < 2) return iconBuffer[side]; 
			else if (side == 3) return iconBuffer[2];
			else return iconBuffer[1];
		case 1:
			if (side < 1) return iconBuffer[0]; 
			else if (side == 1) return iconBuffer[3];
			else return iconBuffer[1];
		case 2:
			if (side == 3) return iconBuffer[5];
			else return iconBuffer[4];
		}
		return iconBuffer[0];
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (player.getCurrentEquippedItem() != null && (player.getCurrentEquippedItem().getItem() == Items.getItem("wrench").getItem()
				|| player.getCurrentEquippedItem().getItem() == Items.getItem("electricWrench").getItem()))
			return false;
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		int meta = world.getBlockMetadata(x, y, z);
		switch (meta)
		{
		case 0:
			player.openGui(ExtraIndustrialMod.instance, ExtraIndustrialReference.ingotCompactorGUI, world, x, y, z);
			return true;
		case 1:
			player.openGui(ExtraIndustrialMod.instance, ExtraIndustrialReference.industrialCrafterGUI, world, x, y, z);
			return true;
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
			return new TileEntityIngotCompactor();
		case 1:
			return new TileEntityIndustrialCrafter();
		case 2:
			return new TileEntityEVTransformer();
		}
		return null;

	}

	@Override
	public int idDropped(int metadata, Random par2Random, int par3)
	{
		return Items.getItem("machine").itemID;
	}

	@Override
	public int damageDropped(int metadata)
	{
		return 0;
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
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player, ItemStack itemstack)
	{
		
		if (!(world.getBlockTileEntity(x, y, z) instanceof IWrenchable))
		{
			return;
		}
		IWrenchable tE = (IWrenchable)world.getBlockTileEntity(x, y, z);

		if (player == null)
		{
			tE.setFacing((short)2);
		}
		else
		{
			int direction = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			int vertDir = Math.round(player.rotationPitch);
			
			if (world.getBlockMetadata(x, y, z) == 2)
			{
				if (vertDir >= 65)
				{
					tE.setFacing((short)1);
					return;
				}
				else if (vertDir <= -65)
				{
					tE.setFacing((short)0);
					return;
				}
			}
			switch (direction)
			{
			case 0:
				tE.setFacing((short)2);
				break;
			case 1:
				tE.setFacing((short)5);
				break;
			case 2:
				tE.setFacing((short)3);
				break;
			case 3:
				tE.setFacing((short)4);
				break;
			}
		}
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		if (world.getBlockTileEntity(x, y, z) instanceof TileEntityMachine)
		{
			TileEntityMachine t = (TileEntityMachine) world.getBlockTileEntity(x, y, z);
			t.dropItems(world, x, y, z);
		}
		if (world.getBlockTileEntity(x, y, z) instanceof IEnergyTile)
		{
			ExtraIndustrialFunctions.removeElectricBlockTileEntity(world, x, y, z);
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

}
