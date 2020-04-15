package mods.magico13.ExtraIndustrial.storage;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemBlockStorageBlock extends ItemBlock
{
	public static final String[] BlockNames = new String[] {"CoalStorage", "NikoliteStorage"};
	public ItemBlockStorageBlock(int par1, Block block) 
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