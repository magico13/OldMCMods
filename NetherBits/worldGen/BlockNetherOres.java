package magico13.mods.NetherBits.worldGen;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockNetherOres extends Block
{
    public BlockNetherOres(int blockID)
    {
        super(blockID, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setRequiresSelfNotify();
        this.setHardness(2.0f);
        this.setResistance(5.0f);
        //this.setLightValue(1F);
    }
    
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int metadata) 
    {
    	switch (metadata) 
    	{
    	case 0:		//Nether Iron
    		return 0;
    	case 1:		//Ender Ore
    		return 1;
    	case 2:		//Nether Diamond
    		return 2;
    	case 3:		//Nether Gold
    		return 3;
    	case 4:		//Nether Redstone
    	{
    		//this.setLightValue(0.625F);
    		return 4;
    	}
    	case 5:		//Nether Copper
    		return 5;
    	case 6:		//Nether Tin
    		return 6;
    	case 7:		//Nether Coal
    		return 7;
    	case 8:		//Lava Crystals
    		return 9;
    	default:	//Default
    		return 0;
    	}
    }
    
    @Override
    public String getTextureFile()
    {
            return "/magico13/mods/NetherBits/terrain.png";
    }
   
    @Override
    public int idDropped(int metadata, Random par2Random, int par3)
    {
    	switch(metadata)
    	{
    	case 0:
    		return Block.oreIron.blockID;
    	case 1:
    		return Item.enderPearl.itemID;
    	case 2:
    		return Item.diamond.itemID;
    	case 3:
    		return Block.oreGold.blockID;
    	case 4:
    		return Item.redstone.itemID;
    	case 5:
    		return this.blockID;
    	case 6:
    		return this.blockID;
    	case 7:
    		return NetherBitsMod.itemNetherItems.itemID;
    	case 8:
    		return NetherBitsMod.itemNetherItems.itemID;
    	default: 
    		return Block.netherrack.blockID;
    	}
    }
    
    @Override
    public int damageDropped(int metadata)
    {
    	switch(metadata)
    	{
    	case 5:
    		return 5;
    	case 6:
    		return 6;
    	case 7:
    		return 2;
    	case 8:
    		return 3;
    	default:
    		return 0;
    	}
    }
    
    @Override
    public int quantityDropped(int metadata, int fortune, Random random)
    {
    	// Ender Ore/Crystals drop 1 to 2 items, Redstone drops 4 to 5,otherwise 1, plus fortune
    	switch (metadata)
    	{
    	case 1:
    		//return 1 + random.nextInt(2);
    		return 1 + random.nextInt(2) + random.nextInt(fortune + 1);
    	case 2:
    		return 1 + random.nextInt(fortune+1);
    	case 4:
    		//return 4 + random.nextInt(2);
    		return 4 + random.nextInt(2) + random.nextInt(fortune + 1);
    	case 7:
    		return 1 + random.nextInt(fortune+1);
    	case 8:
    		return 1 + random.nextInt(fortune + 1);
        default:
        	return 1;
    	}
    }
    
    @Override
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        super.dropBlockAsItemWithChance(par1World, par2, par3, par4, par5, par6, par7);

        int dropID = this.idDropped(par5, par1World.rand, par7);
        if ((dropID == Item.enderPearl.itemID) || (dropID == Item.diamond.itemID) || (dropID == Item.redstone.itemID) || (dropID == NetherBitsMod.itemNetherItems.itemID))
        {
            int var8 = 1 + par1World.rand.nextInt(5);
            this.dropXpOnBlockBreak(par1World, par2, par3, par4, var8);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int idPicked(World world, int x, int y, int z)
    {
        return this.blockID;
    }

    @Override
    public int getDamageValue(World world, int x, int y, int z)
    {
        return world.getBlockMetadata(x, y, z);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 <= 8; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}
