package mods.magico13.ExtraIndustrial.machine;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemBlockMachine extends ItemBlock
{
	public static final String[] BlockNames = new String[] {"IngotCompactor", "IndustrialCrafter", "EVTransformer"};
	public ItemBlockMachine(int par1, Block block) 
	{
		super(par1);
		setHasSubtypes(true);
	}

	@Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
     return this.getUnlocalizedName() + BlockNames[itemStack.getItemDamage()];
	}

	public int getMetadata(int metadata)
	{
		return metadata;
	}
}
