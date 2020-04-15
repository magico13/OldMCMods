package mods.magico13.ExtraIndustrial.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mods.magico13.ExtraIndustrial.machine.IndustrialCrafterRecyclingRecipes;
import mods.magico13.ExtraIndustrial.machine.IndustrialCrafterShapedRecipes;
import mods.magico13.ExtraIndustrial.machine.IndustrialCrafterShapelessRecipes;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;


public class ExtraIndustrialCraftingManager {

	private static final ExtraIndustrialCraftingManager instance = new ExtraIndustrialCraftingManager();

	private List industrialCrafterRecipes = new ArrayList();

	public static final ExtraIndustrialCraftingManager getInstance()
	{
		return instance;
	}

	private ExtraIndustrialCraftingManager()
	{
		
	}

	public void addIndustrialCrafterShapelessRecipe(ItemStack outputStack, Object ... inputObject)
	{
		this.addIndustrialCrafterShapelessRecipe(new ItemStack[] {outputStack}, inputObject);
	}
	
	public void addIndustrialCrafterShapelessRecipe(ItemStack[] outputStacks, Object ... inputObject)
	{
		ArrayList recipeItems = new ArrayList();
		Object[] totalInput = inputObject;
		int inputAmt = inputObject.length;

		for (int i = 0; i < inputAmt; ++i)
		{
			Object inputStack = totalInput[i];

			if (inputStack instanceof ItemStack)
			{
				recipeItems.add(((ItemStack)inputStack).copy());
			}
			else if (inputStack instanceof Item)
			{
				recipeItems.add(new ItemStack((Item)inputStack));
			}
			else
			{
				if (!(inputStack instanceof Block))
				{
					throw new RuntimeException("Invalid shapeless recipe!");
				}

				recipeItems.add(new ItemStack((Block)inputStack));
			}
		}

		this.industrialCrafterRecipes.add(new IndustrialCrafterShapelessRecipes(outputStacks, recipeItems));
	}

	
	public IndustrialCrafterShapedRecipes addIndustrialCrafterShapedRecipe(ItemStack outputStack, Object ... inputObject)
	{
		return this.addIndustrialCrafterShapedRecipe(new ItemStack[] {outputStack}, inputObject);
	}
	
	
	public IndustrialCrafterShapedRecipes addIndustrialCrafterShapedRecipe(ItemStack[] outputStack, Object ... inputObject)
	{
		String var3 = "";
		int var4 = 0;
		int var5 = 0;
		int var6 = 0;

		if (inputObject[var4] instanceof String[])
		{
			String[] var7 = (String[])((String[])inputObject[var4++]);

			for (int var8 = 0; var8 < var7.length; ++var8)
			{
				String var9 = var7[var8];
				++var6;
				var5 = var9.length();
				var3 = var3 + var9;
			}
		}
		else
		{
			while (inputObject[var4] instanceof String)
			{
				String var11 = (String)inputObject[var4++];
				++var6;
				var5 = var11.length();
				var3 = var3 + var11;
			}
		}

		HashMap var12;

		for (var12 = new HashMap(); var4 < inputObject.length; var4 += 2)
		{
			Character var13 = (Character)inputObject[var4];
			ItemStack var14 = null;

			if (inputObject[var4 + 1] instanceof Item)
			{
				var14 = new ItemStack((Item)inputObject[var4 + 1]);
			}
			else if (inputObject[var4 + 1] instanceof Block)
			{
				var14 = new ItemStack((Block)inputObject[var4 + 1], 1, -1);
			}
			else if (inputObject[var4 + 1] instanceof ItemStack)
			{
				var14 = (ItemStack)inputObject[var4 + 1];
			}

			var12.put(var13, var14);
		}

		ItemStack[] var15 = new ItemStack[var5 * var6];

		for (int var16 = 0; var16 < var5 * var6; ++var16)
		{
			char var10 = var3.charAt(var16);

			if (var12.containsKey(Character.valueOf(var10)))
			{
				var15[var16] = ((ItemStack)var12.get(Character.valueOf(var10))).copy();
			}
			else
			{
				var15[var16] = null;
			}
		}

		IndustrialCrafterShapedRecipes var17 = new IndustrialCrafterShapedRecipes(var15, outputStack);
		this.industrialCrafterRecipes.add(var17);
		return var17;
	}

	public ItemStack[] findMatchingIndustrialCrafterRecipe(IInventory inventory)
	{
		ItemStack var4 = null;
		ItemStack var5 = null;
		int i;

		for (i = 14; i < 23; ++i)
		{
			ItemStack stackInSlot = inventory.getStackInSlot(i);

			if (stackInSlot != null)
			{
				var4 = stackInSlot;
			}
		}

		for (i = 0; i < this.industrialCrafterRecipes.size(); ++i)
		{
			IMultiOutputRecipe recipe = (IMultiOutputRecipe)this.industrialCrafterRecipes.get(i);

			if (recipe.matches(inventory))
			{
				return recipe.getCraftingResult();
			}
		}
		return null;
	}

	public List getIndustrialCrafterRecipeList()
	{
		return this.industrialCrafterRecipes;
	}
}
