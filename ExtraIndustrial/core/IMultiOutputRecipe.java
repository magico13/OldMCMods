package mods.magico13.ExtraIndustrial.core;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public interface IMultiOutputRecipe {
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    boolean matches(IInventory inventory);

    /**
     * Returns an Item that is the result of this recipe
     */
    ItemStack[] getCraftingResult();

    ItemStack[] getRecipeOutput();
}