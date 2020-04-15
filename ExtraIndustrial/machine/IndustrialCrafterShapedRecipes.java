package mods.magico13.ExtraIndustrial.machine;

import mods.magico13.ExtraIndustrial.core.IMultiOutputRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class IndustrialCrafterShapedRecipes implements IMultiOutputRecipe {

	public final ItemStack[] recipeItems;

	private ItemStack[] recipeOutput;


	public IndustrialCrafterShapedRecipes(ItemStack[] recipeInputItems, ItemStack[] recipeOutputItems)
	{
		this.recipeItems = recipeInputItems;
		this.recipeOutput = recipeOutputItems;
	}

	@Override
	public ItemStack[] getRecipeOutput()
	{
		return this.recipeOutput;
	}
	
	@Override
	public ItemStack[] getCraftingResult()
    {
        ItemStack[] output = new ItemStack[this.getRecipeOutput().length];
        for (int i=0; i<this.getRecipeOutput().length; i++)
        {
        	output[i] = this.getRecipeOutput()[i].copy();
        }
        return output;
    }

	@Override
	public boolean matches(IInventory inventory)
	{
		for (int i=14; i<23; i++)
		{
			ItemStack recipeStack = this.recipeItems[i-14];
			ItemStack testingStack = inventory.getStackInSlot(i);
			if (recipeStack != null || testingStack != null)
			{
				if (testingStack == null && recipeStack != null || testingStack != null && recipeStack == null)
				{
					return false;
				}

				if (recipeStack.itemID != testingStack.itemID)
				{
					return false;
				}

				if (recipeStack.getItemDamage() != -1 && recipeStack.getItemDamage() != testingStack.getItemDamage())
				{
					return false;
				}
			}
		}
		return true;
	}
}