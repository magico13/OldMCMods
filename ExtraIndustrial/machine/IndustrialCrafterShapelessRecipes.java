package mods.magico13.ExtraIndustrial.machine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mods.magico13.ExtraIndustrial.core.IMultiOutputRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class IndustrialCrafterShapelessRecipes implements IMultiOutputRecipe {
	private final ItemStack[] recipeOutput;

	public final List recipeItems;

	public IndustrialCrafterShapelessRecipes(ItemStack[] output, List input)
	{
		this.recipeOutput = output;
		this.recipeItems = input;
	}

	public ItemStack[] getRecipeOutput()
	{
		return this.recipeOutput;
	}

	public boolean matches(IInventory inventory)
	{
		ArrayList inputItems = new ArrayList(this.recipeItems);

		for (int i = 14; i < 23; ++i)
		{

			ItemStack stack = inventory.getStackInSlot(i);

			if (stack != null)
			{
				boolean properItem = false;
				Iterator var8 = inputItems.iterator();

				while (var8.hasNext())
				{
					ItemStack inputItem = (ItemStack)var8.next();

					if (stack.itemID == inputItem.itemID && (inputItem.getItemDamage() == -1 || stack.getItemDamage() == inputItem.getItemDamage()))
					{
						properItem = true;
						inputItems.remove(inputItem);
						break;
					}
				}

				if (!properItem)
				{
					return false;
				}
			}
		}
		return inputItems.isEmpty();
	}


	public ItemStack[] getCraftingResult()
	{
		ItemStack[] output = new ItemStack[this.getRecipeOutput().length];
		for (int i=0; i<this.getRecipeOutput().length; i++)
		{
			output[i] = this.getRecipeOutput()[i].copy();
		}
		return output;
	}
}
