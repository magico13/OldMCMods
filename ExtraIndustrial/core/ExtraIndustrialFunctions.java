package mods.magico13.ExtraIndustrial.core;

import mods.magico13.ExtraIndustrial.cable.*;
import mods.magico13.ExtraIndustrial.generator.*;
import mods.magico13.ExtraIndustrial.machine.*;
import mods.magico13.ExtraIndustrial.storage.*;
import mods.magico13.ExtraIndustrial.testing.*;
import mods.magico13.ExtraIndustrial.tools.*;
import mods.magico13.ExtraIndustrial.cable.BlockSuperconductorCable;
import mods.magico13.ExtraIndustrial.cable.TileEntitySuperconductorCable;
import mods.magico13.ExtraIndustrial.generator.BlockGenerator;
import mods.magico13.ExtraIndustrial.generator.ItemBlockGenerator;
import mods.magico13.ExtraIndustrial.generator.TileEntityFiveSolar;
import mods.magico13.ExtraIndustrial.generator.TileEntityFusionReactorCore;
import mods.magico13.ExtraIndustrial.generator.TileEntityFusionReactorWall;
import mods.magico13.ExtraIndustrial.generator.TileEntityPeltierGenerator;
import mods.magico13.ExtraIndustrial.machine.BlockMachine;
import mods.magico13.ExtraIndustrial.machine.IndustrialCrafterRecyclingRecipes;
import mods.magico13.ExtraIndustrial.machine.ItemBlockMachine;
import mods.magico13.ExtraIndustrial.machine.TileEntityIndustrialCrafter;
import mods.magico13.ExtraIndustrial.machine.TileEntityIngotCompactor;
import mods.magico13.ExtraIndustrial.machine.TileEntityMachine;
import mods.magico13.ExtraIndustrial.storage.BlockStorageBlock;
import mods.magico13.ExtraIndustrial.storage.ItemBlockStorageBlock;
import mods.magico13.ExtraIndustrial.testing.BlockTestingBlock;
import mods.magico13.ExtraIndustrial.testing.ItemBlockTestingBlock;
import mods.magico13.ExtraIndustrial.testing.TileEntityEnergySink;
import mods.magico13.ExtraIndustrial.testing.TileEntityEnergySource;
import mods.magico13.ExtraIndustrial.tools.ItemElectricToolDynamicDrill;
import mods.magico13.ExtraIndustrial.tools.ItemMeasuringStick;
import mods.magico13.ExtraIndustrial.tools.ItemPortableCrafting;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.oredict.OreDictionary.OreRegisterEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import ic2.api.recipe.Recipes;
import ic2.api.item.Items;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyTile;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ExtraIndustrialFunctions extends ExtraIndustrialMod{
	public static int addGenerators()
	{
		blockGenerator = new BlockGenerator(generatorID).setUnlocalizedName("GeneratorBlock");

		//Item.itemsList[generatorID] = new ItemBlockGenerator(generatorID-256, blockGenerator).setUnlocalizedName("GeneratorBlock");
		GameRegistry.registerBlock(blockGenerator, ItemBlockGenerator.class, "GeneratorBlock");

		// Five Solar
		{
			//	GameRegistry.addRecipe(new ItemStack(blockGenerator, 1, 0), new Object[]
			//			{"SSS", "SBS", "C C", 'S', Items.getItem("solarPanel"), 'B', Items.getItem("machine"), 'C', Items.getItem("electronicCircuit")});
			ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack(blockGenerator, 1, 0), 
					new Object[] {"SSS", "SBS", "C C", 'S', Items.getItem("solarPanel"), 'B', Items.getItem("machine"), 'C', Items.getItem("electronicCircuit")});

			ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapelessRecipe(new ItemStack[] {copyItemStackWithSize(Items.getItem("solarPanel"), 5),
					Items.getItem("machine"), Items.getItem("electronicCircuit")}, new Object[]
							{new ItemStack(blockGenerator, 1, 0)});

			GameRegistry.registerTileEntity(TileEntityFiveSolar.class, "fiveSolar");

			LanguageRegistry.addName(new ItemStack(blockGenerator, 1, 0), "Five Solar");
		}


		// Peltier Generator
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(blockGenerator, 2, 1), true, new Object[]
					{"T C", "TMC", "T C", 'T', "ingotTin", 'C', "ingotCopper", 'M', Items.getItem("machine")}));

			//ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack[] {new ItemStack(blockGenerator, 2, 1)}, 
			//		new Object[] {"T C", "TMC", "T C", 'T', "ingotTin", 'C', "ingotCopper", 'M', Items.getItem("machine")});

			GameRegistry.registerTileEntity(TileEntityPeltierGenerator.class, "peltierGenerator");

			LanguageRegistry.addName(new ItemStack(blockGenerator, 1, 1), "Peltier Generator");
		}

		//Fusion Reactor
		{
			ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack(blockGenerator, 1, 2),
					new Object[] {" W ", "WCW", " W ", 'W', new ItemStack(blockGenerator, 1, 3), 'C', Items.getItem("nuclearReactor")});
			
			if (ExtraIndustrialMod.cableID > 0)
				ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack(blockGenerator, 1, 3),
					new Object[] {"BBB", "CCC", "BBB", 'C', ExtraIndustrialMod.blockCable, 'B', Items.getItem("reinforcedStone")});
			else
				ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack(blockGenerator, 1, 3),
						new Object[] {"BBB", "PPP", "BBB", 'P', Items.getItem("iridiumPlate"), 'B', Items.getItem("reinforcedStone")});
				
			
			GameRegistry.registerTileEntity(TileEntityFusionReactorCore.class, "fusionGenerator");
			GameRegistry.registerTileEntity(TileEntityFusionReactorWall.class, "fusionGeneratorWall");

			LanguageRegistry.addName(new ItemStack(blockGenerator, 1, 2), "Fusion Core");
			LanguageRegistry.addName(new ItemStack(blockGenerator, 1, 3), "Containment Chamber Walling");
		}


		return 1;
	}

	public static int addMachines()
	{
		blockMachine = new BlockMachine(machineID).setUnlocalizedName("MachineBlock");

		//Item.itemsList[machineID] = new ItemBlockMachine(machineID-256, blockMachine).setUnlocalizedName("MachineBlock");
		GameRegistry.registerBlock(blockMachine, ItemBlockMachine.class, "MachineBlock");
		GameRegistry.registerTileEntity(TileEntityMachine.class, "machineTileEntity");

		// Ingot Compactor
		{
			//	GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 0), new Object[]
			//			{"   ", "PMP", " C ", 'P', Block.pistonBase, 'M', Items.getItem("machine"), 'C', Items.getItem("electronicCircuit")});

			ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack(blockMachine, 1, 0), 
					new Object[] {"   ", "PMP", " C ", 'P', Block.pistonBase, 'M', Items.getItem("machine"), 'C', Items.getItem("electronicCircuit")});

			ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapelessRecipe(new ItemStack[] {new ItemStack(Block.pistonBase, 1),
					Items.getItem("machine"), Items.getItem("electronicCircuit")}, new Object[]
							{new ItemStack(blockMachine, 1, 0)});

			GameRegistry.registerTileEntity(TileEntityIngotCompactor.class, "ingotCompactor");

			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 0), "Ingot Compactor");
		}

		// Industrial Crafter
		{
			new IndustrialCrafterRecyclingRecipes();

			GameRegistry.addRecipe(new ItemStack(blockMachine, 1, 1), new Object[]
					{" B ", "CMC", " S ", 'B', Block.workbench, 'M', Items.getItem("machine"), 'C', Items.getItem("electronicCircuit"), 'S', Block.chest});

			GameRegistry.registerTileEntity(TileEntityIndustrialCrafter.class, "industrialCrafter");

			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 1), "Industrial Crafting Table");
		}
		
		// EV Transformer
		{
			/*ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack(blockMachine, 1, 2), 
					new Object[] {" H ", "CTL", " H ", 'H', Items.getItem("trippleInsulatedIronCableItem"), 'T', Items.getItem("hvTransformer"), 
				'C', Items.getItem("advancedCircuit"), 'L', Items.getItem("lapotronCrystal")});
			
			ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapelessRecipe(new ItemStack[] {Items.getItem("lapotronCrystal"),
					Items.getItem("hvTransformer"), Items.getItem("advancedCircuit")}, new Object[]
							{new ItemStack(blockMachine, 1, 2)});*/

	
			GameRegistry.registerTileEntity(TileEntityEVTransformer.class, "EVTransformer");

			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 2), "EV Transformer");
		}

		return 1;
	}

	public static int addStorage()
	{
		blockStorageBlock = new BlockStorageBlock(storageID).setUnlocalizedName("StorageBlock");

		//Item.itemsList[storageID] = new ItemBlockStorageBlock(storageID-256, blockStorageBlock).setUnlocalizedName("StorageBlock");
		GameRegistry.registerBlock(blockStorageBlock, ItemBlockStorageBlock.class, "StorageBlock");
		
		ExtraIndustrialFunctions.createCompactionRecipes(requireCompressor, requireMacerator);

		//LanguageRegistry.addName(new ItemStack(blockStorageBlock, 1, 0), "Redstone Storage Block");
		LanguageRegistry.addName(new ItemStack(blockStorageBlock, 1, 0), "Carbon Block");
		LanguageRegistry.addName(new ItemStack(blockStorageBlock, 1, 1), "Blue Block");

		return 1;
	}

	public static int addCables()
	{
		blockCable = new BlockSuperconductorCable(cableID);
		GameRegistry.registerBlock(blockCable, "superconductorCableBlock");

		//	GameRegistry.addRecipe(new ItemStack(blockCable, 32), new Object[]
		//			{"PPP", "UUU", "PPP", 'P', Items.getItem("iridiumPlate"), 'U', Items.getItem("matter")});

		ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack(blockCable, 32),
				new Object[] {"PPP", "UUU", "PPP", 'P', Items.getItem("iridiumPlate"), 'U', Items.getItem("matter")});

		GameRegistry.registerTileEntity(TileEntitySuperconductorCable.class, "superconductorCable");

		LanguageRegistry.addName(blockCable, "Superconductor Cable");

		return 1;
	}

	public static int addItems()
	{
		// Dynamic Drill
		if (dynamicDrillID > 0)
		{
			itemDynamicDrill = new ItemElectricToolDynamicDrill(dynamicDrillID, 0);

			Recipes.advRecipes.addRecipe(new ItemStack(itemDynamicDrill, 1), new Object[]
					{" I ", "IDI", "CEC", 'I', Items.getItem("iridiumPlate"), 'D', Items.getItem("diamondDrill"), 'C', Items.getItem("advancedCircuit"), 'E', Items.getItem("energyCrystal")});
			//	ExtraIndustrialCraftingManager.getInstance().addIndustrialCrafterShapedRecipe(new ItemStack[] {new ItemStack(blockCable, 32)},
			//			new Object[] {" I ", "IDI", "CEC", 'I', Items.getItem("iridiumPlate"), 'D', Items.getItem("diamondDrill"), 'C', Items.getItem("advancedCircuit"), 'E', Items.getItem("energyCrystal")});

			LanguageRegistry.addName(itemDynamicDrill, "Dynamic Drill");
		}
		// Digital Pocket Crafter
		if (portableCraftingID > 0){
			itemPortableCrafting = new ItemPortableCrafting(portableCraftingID);

			Recipes.advRecipes.addRecipe(new ItemStack(itemPortableCrafting, 1), new Object[]
					{"  W", " C ", "B  ", 'W', new ItemStack(Block.workbench, 1), 'C', Items.getItem("electronicCircuit"), 'B', Items.getItem("reBattery")});

			Recipes.advRecipes.addRecipe(new ItemStack(itemPortableCrafting, 1), new Object[]
					{"  W", " C ", "B  ", 'W', new ItemStack(Block.workbench, 1), 'C', Items.getItem("electronicCircuit"), 'B', Items.getItem("chargedReBattery")});

			LanguageRegistry.addName(itemPortableCrafting, "Digital Pocket Crafter");
		}
		// Mesuring Stick
		if (measuringStickID > 0)
		{
			itemMeasuringStick = new ItemMeasuringStick(measuringStickID);

			GameRegistry.addShapelessRecipe(new ItemStack(itemMeasuringStick, 1), new Object[]
					{Item.stick, Item.dyePowder});

			LanguageRegistry.addName(itemMeasuringStick, "Measuring Stick");
		}


		return 1;
	}

	public static int addTesting()
	{
		blockTesting = new BlockTestingBlock(testingID).setUnlocalizedName("TestingBlock");

		//Item.itemsList[testingID] = new ItemBlockTestingBlock(testingID-256, blockTesting).setUnlocalizedName("TestingBlock");
		GameRegistry.registerBlock(blockTesting, ItemBlockTestingBlock.class, "TestingBlock");

		GameRegistry.registerTileEntity(TileEntityEnergySource.class, "energySource");
		GameRegistry.registerTileEntity(TileEntityEnergySink.class, "energySink");

		LanguageRegistry.addName(new ItemStack(blockTesting, 1, 0), "Energy Source");
		LanguageRegistry.addName(new ItemStack(blockTesting, 1, 1), "Energy Sink");

		return 1;
	}


	// If enableCompaction is true then these recipes will be added
	// They allow for compact storage of redstone, coal
	// The recipes change depending on whether compressors and macerators are to be used.
	public static int createCompactionRecipes(boolean c, boolean m) {

		// If compressors are to be used, then have the 8 dust be compressed into one storage block		
		if (c == true){
			// 8 Coal makes a Carbon block
			Recipes.compressor.addRecipe(new ItemStack(Item.coal, 8), new ItemStack(blockStorageBlock, 1, 1));

		} else {    // If compressors aren't used then the 8 dust makes the final block directly
			// 8 Coal makes a Carbon block
			GameRegistry.addShapelessRecipe(new ItemStack(blockStorageBlock, 1, 1), new ItemStack(Item.coal, 8));
		}

		// If macerators are used then the storage block must be macerated into 8 dust
		if (m == true){
			// A Carbon block macerates to 8 coal dust
			Recipes.macerator.addRecipe(new ItemStack(blockStorageBlock, 1, 1), new ItemStack(Items.getItem("coalDust").getItem(), 8));

		} else {}
		// Things that happen as long as compaction is enabled:
		// A carbon block makes 8 coal in a crafting table
		GameRegistry.addShapelessRecipe(new ItemStack(Item.coal, 8),
				new ItemStack(blockStorageBlock, 1, 1));

		// 1 Coal Dust makes 1 Coal in a compressor
		Recipes.compressor.addRecipe(Items.getItem("coalDust"), new ItemStack(Item.coal, 1));
		return 1;
	}

	/**
	 * Detects the presence of NEI. If found, alerts that the energy tooltips on items will be disabled.
	 * @return
	 */
	public static boolean detectNEI() {
		try {
			Class.forName("codechicken.nei.NEIController");
			System.out.println("[EI] NEI found, disabling energy tooltips.");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * Returns the Block ID of the block touching the specified face of the passed block
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param face
	 * @return
	 */
	public static int getBlockonFace(World world, int x, int y, int z, int face)
	{
		switch (face){
		case 0: return world.getBlockId(x, y-1, z);
		case 1: return world.getBlockId(x, y+1, z);
		case 2: return world.getBlockId(x, y, z-1);
		case 3: return world.getBlockId(x, y, z+1);
		case 4: return world.getBlockId(x-1, y, z);
		case 5: return world.getBlockId(x+1, y, z);
		}
		return 0;
	}
	
	/**
	 * Returns the Tile Entity of the block touching the specified face of the passed block
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param face
	 * @return
	 */
	public static TileEntity getTileEntityonFace(World world, int x, int y, int z, int face)
	{
		switch (face){
		case 0: return world.getBlockTileEntity(x, y-1, z);
		case 1: return world.getBlockTileEntity(x, y+1, z);
		case 2: return world.getBlockTileEntity(x, y, z-1);
		case 3: return world.getBlockTileEntity(x, y, z+1);
		case 4: return world.getBlockTileEntity(x-1, y, z);
		case 5: return world.getBlockTileEntity(x+1, y, z);
		}
		return null;
	}

	/**
	 * Removes the specified amount of the specified item from the passed inventory.
	 * Similar to consumeInventoryItem() but checks for equal metadata and works on any ItemStack[]
	 * @param inv
	 * @param stackToRemove
	 * @param amt
	 * @return
	 */
	public static boolean removeItemFromInventory(ItemStack[] inv, ItemStack stackToRemove, int amt)
	{
		return removeItemFromInventory(inv, stackToRemove, 0, inv.length, amt);
	}
	/**
	 * Removes the specified amount of the specified item from the passed inventory, within the inventory slots provided.
	 * Similar to consumeInventoryItem() but checks for equal metadata and works on any ItemStack[]
	 * Returns success.
	 * @param inv
	 * @param stackToRemove
	 * @param startIndex
	 * @param length
	 * @param amt
	 * @return
	 */
	public static boolean removeItemFromInventory(ItemStack[] inv, ItemStack stackToRemove, int startIndex, int length, int amt)
	{
		if (stackToRemove == null)
		{
			return true;
		}

		int amtFound = 0;

		// Ensure there are enough of the specified item in the inventory
		for (int i=startIndex; i<startIndex+length; i++)
		{
			ItemStack stack = inv[i];
			if (stack != null)
			{

				if (stack.getItem() == stackToRemove.getItem() && stack.getItemDamage() == stackToRemove.getItemDamage())
				{
					amtFound += stack.stackSize;
				}

			}
			// If/when enough are found, stop searching
			if (amtFound >= amt)
				break;
		}
		// If enough weren't found, return false
		if (amtFound < amt)
			return false;

		int amtRemoved = 0;
		// If enough were found, remove them
		for (int i=startIndex; i<startIndex+length; i++)
		{
			int amtRequired = amt - amtRemoved;
			ItemStack stack = inv[i];
			if (stack != null)
			{
				if (stack.getItem() == stackToRemove.getItem() && stack.getItemDamage() == stackToRemove.getItemDamage())
				{
					if (stack.stackSize < amtRequired)
					{
						amtRemoved += stack.stackSize;
						stack.stackSize = 0;
						inv[i] = null;
					}
					else if (stack.stackSize >= amtRequired)
					{
						amtRemoved += amtRequired;
						inv[i].stackSize -= amtRequired;
						//System.out.println("Stacksize: "+inv[i].stackSize);
						if (inv[i].stackSize < 1)
						{
							//	System.out.println("nullifying...");
							inv[i] = null;
						}
					}
				}
				if (amtRemoved == amt)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Causes the machine at the specified location to explode
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void explodeMachine(World world, int x, int y, int z) 
	{
		//TODO: This method is currently critically bugged
		//Or not...
		removeElectricBlockTileEntity(world, x, y, z);
		world.createExplosion((Entity)null, x, y, z, 0F, false);
		world.setBlockToAir(x, y, z);

		//System.out.println("[EI] Machine exploded at: "+x+","+y+","+z);

	}

	/**
	 * Properly removes the electric block tile entity from the world
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void removeElectricBlockTileEntity(World world, int x, int y, int z) 
	{
		if (world.getBlockTileEntity(x, y, z) instanceof IEnergyTile)
		{
			IEnergyTile EnergyTile = (IEnergyTile)world.getBlockTileEntity(x, y, z);
			if (EnergyTile.isAddedToEnergyNet())
			{
				EnergyTileUnloadEvent unloadEvent = new EnergyTileUnloadEvent((IEnergyTile) world.getBlockTileEntity(x, y, z));
				MinecraftForge.EVENT_BUS.post(unloadEvent);
			}
			world.removeBlockTileEntity(x, y, z);
		}
	}

	public static ItemStack copyItemStackWithSize(ItemStack stack, int size)
	{
		ItemStack returnStack = stack.copy();
		returnStack.stackSize = size;
		return returnStack;
	}
	
	public static boolean identicalItems(ItemStack itemstack1, ItemStack itemstack2)
	{
		if (itemstack1.getItem() == itemstack2.getItem())
			if (itemstack1.getItemDamage() == itemstack2.getItemDamage())
				return true;
		return false;
	}
}
