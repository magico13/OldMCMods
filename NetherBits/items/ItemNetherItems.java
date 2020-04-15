package magico13.mods.NetherBits.items;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemNetherItems extends Item{
	public static final String[] ItemNames = new String[] {"Copper", "Tin", "NetherCoal", "LavaCrystal", "ReformedShard", "ReformedCrystal"};
	public ItemNetherItems(int itemID) 
	{
		super(itemID);
	        this.setHasSubtypes(true);
	        this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	 
	@SideOnly(Side.CLIENT)
	public String getTextureFile(){
		return "/magico13/mods/NetherBits/items.png";
	}

	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int meta)
	{
		//metadata=meta;
		return meta;
	}

	private int metadata;
	@Override
	public int getItemStackLimit()
	{
		//if (metadata==6)
		//	this.setMaxStackSize(1);
		//else
		//	this.setMaxStackSize(64);
		
		return this.maxStackSize;
	}
	public String getItemNameIS(ItemStack itemstack) 
	{
		int var2 = MathHelper.clamp_int(itemstack.getItemDamage(), 0, ItemNames.length-1);
        return super.getItemName() + "." + ItemNames[var2];
	}
	
	public int getMetadata(int par1)
    {
        return par1;
    }
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < ItemNames.length; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
    {
    	if (!world.isRemote)
    	{
    		if (itemStack.getItemDamage() == 3)
    		{
    			//System.out.println(side);
    			int x1=x, y1=y, z1=z;
    			switch (side)
    			{
    			case 0: y1 = y-1; break;
    			case 1: y1 = y+1; break;
    			case 2: z1 = z-1; break;
    			case 3: z1 = z+1; break;
    			case 4: x1 = x-1; break;
    			case 5: x1 = x+1; break;
    			}
    			
    			if (world.isAirBlock(x1, y1, z1))
    			{
    				world.setBlockWithNotify(x1, y1, z1, Block.lavaMoving.blockID);
    				--itemStack.stackSize;
    				return true;
    			}
    		}
    	}
        return false;
    }
}

