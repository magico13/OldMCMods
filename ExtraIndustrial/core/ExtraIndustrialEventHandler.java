package mods.magico13.ExtraIndustrial.core;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.api.recipe.Recipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;

public class ExtraIndustrialEventHandler {
	@ForgeSubscribe
    public void registerOre(OreRegisterEvent var1)
	{	
        String var2 = var1.Name;
        ItemStack var3 = var1.Ore;
        // Adds Nikolite storage if nikolite is found and storage is enabled
        if (var2.equals("dustNikolite") && ExtraIndustrialMod.storageID != 0)
        {
        	System.out.println("[EI] Nikolite found, adding compaction recipes.");
        	ItemStack nikolite = var3.copy();
        	nikolite.stackSize = 8;
        	if (ExtraIndustrialMod.requireCompressor)
        		Recipes.compressor.addRecipe(nikolite, new ItemStack(ExtraIndustrialMod.blockStorageBlock, 1, 2));
        	else
        		GameRegistry.addShapelessRecipe(new ItemStack(ExtraIndustrialMod.blockStorageBlock, 1, 2), nikolite);
        	
        	if (ExtraIndustrialMod.requireMacerator)
        		Recipes.macerator.addRecipe(new ItemStack(ExtraIndustrialMod.blockStorageBlock, 1, 2), nikolite);
        	else
        		GameRegistry.addShapelessRecipe(nikolite, new ItemStack(ExtraIndustrialMod.blockStorageBlock, 1, 2));
        }
        
    }
}
