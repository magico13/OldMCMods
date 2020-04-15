package mods.magico13.ExtraIndustrial.storage;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockStorageBlock extends Block{
	
	private Icon[] iconBuffer = new Icon[2]; 
    public BlockStorageBlock(int blockID)
    {
        super(blockID, Material.iron);
        this.setHardness(3.0f);
        this.setResistance(5.0f);
        this.setStepSound(soundStoneFootstep);
        this.setCreativeTab(CreativeTabs.tabBlock);
        //this.setUnlocalizedName("StorageBlock");
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
    public int idDropped(int metadata, Random par2Random, int par3)
    {
    	return this.blockID;
    }
    
	@Override
    public int damageDropped(int sexy)
    {
    	return sexy;
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
	@Override
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < ItemBlockStorageBlock.BlockNames.length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}