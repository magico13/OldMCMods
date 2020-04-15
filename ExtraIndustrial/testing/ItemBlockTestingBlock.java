package mods.magico13.ExtraIndustrial.testing;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemBlockTestingBlock extends ItemBlock
{
	public static final String[] BlockNames = new String[] {"EnergySource", "EnergySink"};
	public ItemBlockTestingBlock(int par1, Block block) 
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