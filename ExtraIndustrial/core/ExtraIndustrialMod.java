package mods.magico13.ExtraIndustrial.core;

import mods.magico13.ExtraIndustrial.*;
import mods.magico13.ExtraIndustrial.client.*;
import mods.magico13.ExtraIndustrial.generator.*;
import mods.magico13.ExtraIndustrial.machine.*;
import mods.magico13.ExtraIndustrial.storage.*;
import mods.magico13.ExtraIndustrial.testing.*;
import mods.magico13.ExtraIndustrial.tools.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod( modid = ExtraIndustrialMod.modid, name="ExtraIndustrial", version="0.1.4")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
channels=("EIUpdate"), packetHandler = ExtraIndustrialPacketHandler.class)

public class ExtraIndustrialMod {
	@SidedProxy(clientSide = "mods.magico13.ExtraIndustrial.client.ClientProxy", serverSide = "mods.magico13.ExtraIndustrial.core.CommonProxy")
	public static CommonProxy proxy;
	@Instance
	public static ExtraIndustrialMod instance;

	public static final String modid = "ExtraIndustrialMod";
	
	// Configuration Variables
	public static int storageID;
	public static int machineID;
	public static int generatorID;
	public static int cableID;
	public static int testingID;
	public static int dynamicDrillID;
	public static int portableCraftingID;
	public static int measuringStickID;
	public static boolean requireCompressor, requireMacerator;

	// Item Definitions
	public static Item itemDynamicDrill;
	public static Item itemPortableCrafting;
	public static Item itemMeasuringStick;

	// Block Definitions
	public static Block blockGenerator;
	public static Block blockMachine;
	public static Block blockStorageBlock;
	public static Block blockCable;
	public static Block blockTesting;

	// Other Stuff
	public static boolean NEIinstalled;
	

	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		// Create Config File
		Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());
		// loading the configuration from its file
		configuration.load();

		// Get or create IDs
		// Blocks
		storageID = configuration.get(configuration.CATEGORY_BLOCK,"storageID", 1367).getInt();
		machineID = configuration.get(configuration.CATEGORY_BLOCK,"machineID", 1368).getInt();
		generatorID = configuration.get(configuration.CATEGORY_BLOCK,"generatorID", 1369).getInt();
		cableID = configuration.get(configuration.CATEGORY_BLOCK,"cableID", 1370).getInt();
		testingID = configuration.get(configuration.CATEGORY_BLOCK,"testingID", 0).getInt();

		// Items
		dynamicDrillID = configuration.get(configuration.CATEGORY_ITEM,"dynamicDrillID", 16259).getInt();
		portableCraftingID = configuration.get(configuration.CATEGORY_ITEM,"portableCraftingID", 16260).getInt();
		measuringStickID = configuration.get(configuration.CATEGORY_ITEM,"measuringStickID", 16261).getInt();

		// Compaction settings
		//enableCompaction = configuration.get("Compaction","enableCompaction",  true).getBoolean(true);
		requireCompressor = configuration.get("Compaction","requireCompressor", true).getBoolean(true);
		requireMacerator = configuration.get("Compaction","requireMacerator",  true).getBoolean(true);

		// saving the configuration to its file
		configuration.save();
	}

	@Init
	public void load(FMLInitializationEvent event)
	{
		// Register GUI handler
		NetworkRegistry.instance().registerGuiHandler(instance, proxy);

		// Register Event Handler
    	MinecraftForge.EVENT_BUS.register(new ExtraIndustrialEventHandler()); 
    	
		// Check if NEI is installed
		NEIinstalled = ExtraIndustrialFunctions.detectNEI();

		// Pre-render blocks
		proxy.registerRenderInformation();

		//Add the blocks and all associated information
		if (cableID > 0)
			ExtraIndustrialFunctions.addCables();
		if (generatorID > 0)
			ExtraIndustrialFunctions.addGenerators();
		if (machineID > 0)
			ExtraIndustrialFunctions.addMachines();
		if (storageID > 0)
			ExtraIndustrialFunctions.addStorage();
		if (testingID > 0)
			ExtraIndustrialFunctions.addTesting();
		
		//Add the items and all associated information
		ExtraIndustrialFunctions.addItems();
	}    	
}
/* TODO:
 * This version:
 * More recycling recipes
 * Inductive power transfer
 * Fix pocket crafter's stupid shift-click method
 * Method for disabling specific blocks, strings parsed char by char
 * Superconductor animation?
 * Fusion Reactor Hydrogen gas (liquid) support
 * 
 * Fusion Reactor (BAISCS DONE!)
 * EV transformer (DON'T NEED IT!)
 * Charge slot in machines (DONE!)
 * IndCrafter outputting into stacks better (DONE! Doesn't like the last stack)
 * IndCrafter Manual/Auto mode (STOP DESTROYING MY MACHINE BLOCKS YOU WHORE!) (DONE!)
 */

/* Change Log:
 * v0.1.4
 * UPDATE: Updated to MC 1.5.2
 * 
 * ADDED: Manual and Automatic modes to Industrial Crafting Table. Click the output item to craft in manual mode. Similar to RailCraft rolling machine
 * ADDED: Fusion reactor. Produces large amount of power from Electrolyzed Water Cells. Multi-block structure. Buggy, but should work.
 * ADDED: Charge slot in Ingot Compactor works now
 * 
 * BUGFIX: Machine GUIs should update correctly
 * BUGFIX: Five solars are actually craftable now... (in Industrial Crafting table)
 * BUGFIX: Measuring stick works server-side instead of client-side. It was working properly before, then suddenly stopped.
 * BUGFIX: Industrial Crafting table output should fill stacks now (except apparently with the final stack)
 * 
 * CHANGED: Machines now use the vanilla sided-behavior for use with hoppers. The ingot compactor is just like a furnace, the industrial crafter inputs through the top and outputs everywhere else.
 * 
 * v0.1.3
 * ADDED: IC2 upgrades work in machines
 * ADDED: Industrial Crafting table. Automatic crafting, will be only way to craft new machines/items. Can also break down machines
 * ADDED: Measuring stick. It's old code, and doesn't always work how you want, but it gets the job done. Stick + ink sac
 * 
 * CHANGED: All TileEntityMachine blocks (including the Ingot Compactor) explode when given more than LV.
 * CHANGED: Superconductor recipe makes 32 cables
 * CHANGED: Superconductor works with 8192 EU/t instead of 4096.
 * CHANGED: Small texture changes to machines
 * 
 * BUGFIX: Fix for ingot compactor messing up with blocks that use the same ID

 * INTERNAL: removeItemFromInventory function works with any ItemStack[] and any amounts
 * 
 * v0.1.2
 * Attempt to fix issues with drill mode being out of sync on the client.
 * Portable crafter item voiding now requires 4 EU per individual item in a stack. Only uses EU when it deletes the item.
 * Added buttons to the crafter. "Clr" clears the crafting grid, "S" saves the current recipe, and "L" loads that recipe later.
 * Portable Crafter has 3 sets of saved recipes.
 * Internal structure change with creation of blocks and items.
 * Added superconductor cable. 4096 EU/t max, no EU loss.
 * Actually fixed the ingot compactor packet issues. It will retain it's facing now.
 * 
 */

/* Want to add: 
 * 
 * Eventually:
 * Better shift-click with upgrades
 * Upgrade for IndCrafter that makes it perfectly reverse recipes
 * Backup recipes for if the IndCrafter is disabled
 * More recycling recipes
 * Charge aware Industrial Crafting
 * Ore dictionary aware Industrial Crafting
 * Make packet handler less crappy
 * Make machines update way less frequently
 * Make testing blocks more useful
 * NEI integration
 * 
 * Electrifier block, makes metal blocks shocky
 * 
 * 
 * Energy Gates-EU powered teleport gates
 * Additional Storage-Nikolite! Forcicium? Vanilla items. UltraCompact storage of storage blocks?
 * InfiniChest-Infinite item storage at an increasing EU cost
 * New Machines?
 * Particle Accelerator- Create Matter/antimatter. Not sure of antimatter uses yet.
 * Variable Transformer - convert 1-4096 EU
 * Cloaking device. Invisibility enchantment with EU cost
 * 
 * DONE: Fusion Reactor-Large EU requirement to start, larger return. Needs fuel, "multiblock" structure
 * DONE: Precise Drill-silk touch/fortune/or fast "enchantments"  (need to add m-key functionality) Make buying enchants?
 * DONE: Portable Digital Crafting Table - portable crafting with EU cost (doesn't update right away, but still functions)
 * DONE: Five-solar - power of 5 solar panels in one block
 * DONE: Superconductor-UV (4096) EU/t, no loss
 */
