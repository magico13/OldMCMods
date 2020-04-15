package magico13.mods.NetherBits.worldGen;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemBlockNetherOres extends ItemBlock
{
	public static final String[] BlockNames = new String[] {"NetherIronOre", "EnderOre", "NetherDiamond", 
		"NetherGold", "NetherRedstone", "NetherCopper", "NetherTin", "NetherCoal", "LavaCrystalOre"};
	public ItemBlockNetherOres(int par1, Block block) 
{
		super(par1);
		setHasSubtypes(true);
}

public String getItemNameIS(ItemStack itemstack) 
{
	int var2 = MathHelper.clamp_int(itemstack.getItemDamage(), 0, BlockNames.length-1);
    return super.getItemName() + "." + BlockNames[var2];
}

public int getMetadata(int par1)
    {
        return par1;
    }
}