package mods.magico13.ExtraIndustrial.generator;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemBlockGenerator extends ItemBlock
{
	public static final String[] BlockNames = new String[] {"FiveSolar", "PeltierGenerator", "FusionReactorCore", "FusionReactorWall"};
	public ItemBlockGenerator(int par1, Block block) 
	{
		super(par1);
		setHasSubtypes(true);
	}

	@Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
     return this.getUnlocalizedName() + BlockNames[itemStack.getItemDamage()];
	}
	
	@Override
	public int getMetadata(int metadata)
	{
		return metadata;
	}
}
