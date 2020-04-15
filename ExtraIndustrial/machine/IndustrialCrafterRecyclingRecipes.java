package mods.magico13.ExtraIndustrial.machine;

import ic2.api.item.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialCraftingManager;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialFunctions;

public class IndustrialCrafterRecyclingRecipes {

	public IndustrialCrafterRecyclingRecipes()
	{
		//	craftingManager.getInstance().addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("electronicCircuit"), Items.getItem("generator")}, 
		//			new Object[] {Items.getItem("solarPanel")});
		ExtraIndustrialCraftingManager craftingInstance = ExtraIndustrialCraftingManager.getInstance();

		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("electronicCircuit"), Items.getItem("ironFurnace")}, 
				new Object[] {Items.getItem("electroFurnace")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("generator")}, 
				new Object[] {Items.getItem("waterMill"), Items.getItem("waterMill")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("generator"), ExtraIndustrialFunctions.copyItemStackWithSize(Items.getItem("refinedIronIngot"), 2)}, 
				new Object[] {Items.getItem("windMill")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("generator"), ExtraIndustrialFunctions.copyItemStackWithSize(Items.getItem("reactorChamber"), 3)}, 
				new Object[] {Items.getItem("nuclearReactor")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("machine"),  ExtraIndustrialFunctions.copyItemStackWithSize(Items.getItem("denseCopperPlate"), 3)}, 
				new Object[] {Items.getItem("reactorChamber")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("machine"), Items.getItem("reBattery")}, 
				new Object[] {Items.getItem("generator")});

		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("electronicCircuit"), Items.getItem("machine")}, 
				new Object[] {Items.getItem("compressor")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("electronicCircuit"), Items.getItem("machine")}, 
				new Object[] {Items.getItem("extractor")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("advancedMachine"), Items.getItem("electroFurnace")}, 
				new Object[] {Items.getItem("inductionFurnace")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("electronicCircuit"), Items.getItem("machine")}, 
				new Object[] {Items.getItem("macerator")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {ExtraIndustrialFunctions.copyItemStackWithSize(Items.getItem("advancedCircuit"), 2), 
				ExtraIndustrialFunctions.copyItemStackWithSize(Items.getItem("advancedMachine"), 2), Items.getItem("lapotronCrystal")}, 
				new Object[] {Items.getItem("massFabricator")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("compressor"), Items.getItem("refinedIronIngot")}, 
				new Object[] {Items.getItem("recycler")});

		craftingInstance.addIndustrialCrafterShapelessRecipe(ExtraIndustrialFunctions.copyItemStackWithSize(Items.getItem("reBattery"), 3), 
				new Object[] {Items.getItem("batBox")}); 
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("energyCrystal"), Items.getItem("energyCrystal"), 
				Items.getItem("energyCrystal"), Items.getItem("energyCrystal"), Items.getItem("machine")}, 
				new Object[] {Items.getItem("mfeUnit")});
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("lapotronCrystal"), Items.getItem("lapotronCrystal"), 
				Items.getItem("lapotronCrystal"), Items.getItem("lapotronCrystal"), Items.getItem("lapotronCrystal"), Items.getItem("lapotronCrystal"),
				Items.getItem("advancedMachine"), Items.getItem("mfeUnit")}, 
				new Object[] {Items.getItem("mfsUnit")});
		
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack(Block.cobblestone, 8), 
				new Object[] {Block.furnaceIdle}); 
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack(Block.planks, 8), 
				new Object[] {Block.chest}); 
		craftingInstance.addIndustrialCrafterShapelessRecipe(new ItemStack[] {new ItemStack(Item.ingotIron, 5), new ItemStack(Block.chest, 1)}, 
				new Object[] {Block.dropper}); 
		
		

	}
}

/*
		craftingManager.getInstance().addIndustrialCrafterShapelessRecipe(new ItemStack[] {}, 
				new Object[] {Items.getItem("")});
 */

