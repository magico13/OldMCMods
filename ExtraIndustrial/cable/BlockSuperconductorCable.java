package mods.magico13.ExtraIndustrial.cable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.machine.TileEntityIngotCompactor;
import mods.magico13.ExtraIndustrial.machine.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSuperconductorCable extends BlockContainer {

	public BlockSuperconductorCable(int blockID) {
		super(blockID, Material.iron);
		this.setHardness(0.2F);
		this.setStepSound(soundClothFootstep);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setUnlocalizedName("superconductorCable");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
        this.blockIcon = iconRegister.registerIcon(ExtraIndustrialReference.modTextures + ":superconductorCable" );
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
			return new TileEntitySuperconductorCable();
		}
		return null;

	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return null;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		ExtraIndustrialFunctions.removeElectricBlockTileEntity(world, x, y, z);
		super.breakBlock(world, x, y, z, par5, par6);
	}
}
