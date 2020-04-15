package magico13.mods.NetherBits;

import magico13.mods.NetherBits.handlers.NetherBitsEventHandler;
import magico13.mods.NetherBits.handlers.NetherBitsGuiHandler;
import magico13.mods.NetherBits.handlers.NetherBitsPacketHandler;
import magico13.mods.NetherBits.items.ItemNetherItems;
import magico13.mods.NetherBits.items.ItemResonanceBand;
import magico13.mods.NetherBits.machine.BlockCobbleGen;
import magico13.mods.NetherBits.machine.BlockLavaGen;
import magico13.mods.NetherBits.machine.BlockMachine;
import magico13.mods.NetherBits.machine.ItemBlockMachine;
import magico13.mods.NetherBits.machine.TileEntityCobbleGen;
import magico13.mods.NetherBits.machine.TileEntityLavaGen;
import magico13.mods.NetherBits.torch.BlockNetherTorch;
import magico13.mods.NetherBits.worldGen.BlockNetherOres;
import magico13.mods.NetherBits.worldGen.ItemBlockNetherOres;
import magico13.mods.NetherBits.worldGen.WorldGeneratorNetherBits;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.src.ModLoader;
import java.io.File;


@Mod( modid = "NetherBits", name="Nether Bits", version="0.3.3")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
		channels=("NBUpdate"), packetHandler = NetherBitsPacketHandler.class)

public class NetherBitsMod {
	@SidedProxy(clientSide = "magico13.mods.NetherBits.ClientProxy", serverSide = "magico13.mods.NetherBits.CommonProxy")
	public static CommonProxy proxy; //This object will be populated with the class that you choose for the environment
	@Instance
	public static NetherBitsMod instance; //The instance of the mod that will be defined, populated, and callable
	
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		// Create Config File
		Configuration configuration = new Configuration(event.getSuggestedConfigurationFile());
		// loading the configuration from its file
		configuration.load();

		// Get or create IDs
		// Blocks
		NetherOreID = configuration.get(configuration.CATEGORY_BLOCK, "netherOreID", 3025).getInt();
		NetherTorchID = configuration.get(configuration.CATEGORY_BLOCK, "netherTorchID", 3026).getInt();
		LavaGenID = configuration.get(configuration.CATEGORY_BLOCK, "lavaGenID", 3027).getInt();
		CobbleGenID = configuration.get(configuration.CATEGORY_BLOCK, "cobbleGenID", 3028).getInt();
		MachineID = configuration.get(configuration.CATEGORY_BLOCK, "machineID", 3029).getInt();
		// Items
		NetherItemID = configuration.get(configuration.CATEGORY_ITEM, "netherIngotID", 6781).getInt();
		ResonanceBandID = configuration.get(configuration.CATEGORY_ITEM, "resonanceBandID", 6782).getInt();
		// Get or create general settings
		debugMode = configuration.get(configuration.CATEGORY_GENERAL, "debugMode", false).getBoolean(false);
		generateCopperTin = configuration.get(configuration.CATEGORY_GENERAL, "copperAndTinGeneration", false).getBoolean(false);
		modLanguage = configuration.get(configuration.CATEGORY_GENERAL, "language", 0).getInt();
		resonatorFrequency = configuration.get(configuration.CATEGORY_GENERAL, "resonatorFrequency", 90).getInt();
		dungeon1Frequency = configuration.get(configuration.CATEGORY_GENERAL, "dungeon1Frequency", 20).getInt();
		// Ore Generation Rates
		ironFrequency = configuration.get("Ore Generation Rates", "frequencyIron", 10).getInt();
		enderFrequency = configuration.get("Ore Generation Rates", "frequencyEnderOre", 10).getInt();
		diamondFrequency = configuration.get("Ore Generation Rates", "frequencyDiamond", 1).getInt();
		goldFrequency = configuration.get("Ore Generation Rates", "frequencyGold", 5).getInt();
		redstoneFrequency = configuration.get("Ore Generation Rates", "frequencyRedstone", 5).getInt();
		copperFrequency = configuration.get("Ore Generation Rates", "frequencyCopper", 12).getInt();
		tinFrequency = configuration.get("Ore Generation Rates", "frequencyTin", 10).getInt();
		coalFrequency = configuration.get("Ore Generation Rates", "frequencyCoal", 6).getInt();
		crystalFrequency = configuration.get("Ore Generation Rates", "frequencyCrystal", 2).getInt();
		// saving the configuration to its file
		configuration.save();
	}
    @Init
    public void load(FMLInitializationEvent event)
    {
    	// Register Render Information
    	proxy.registerRenderInformation();
    	
    	// Register Event Handler
    	MinecraftForge.EVENT_BUS.register(new NetherBitsEventHandler()); 
    	
    	// Register Ticker
    	//TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
    	// Register GUI Handler
        NetworkRegistry.instance().registerGuiHandler(this, new NetherBitsGuiHandler());
    	
        // Create new blocks and items
        blockNetherOres = new BlockNetherOres(NetherOreID);
        blockNetherTorch = new BlockNetherTorch(NetherTorchID);
        blockLavaGen = new BlockLavaGen(LavaGenID);
        blockCobbleGen = new BlockCobbleGen(CobbleGenID);
        blockMachine = new BlockMachine(MachineID);
        itemNetherItems = new ItemNetherItems(NetherItemID);
        itemResonanceBand = new ItemResonanceBand(ResonanceBandID);
        
        // Register Tile Entities
        GameRegistry.registerTileEntity(TileEntityLavaGen.class, "TileEntityLavaGen");
        GameRegistry.registerTileEntity(TileEntityCobbleGen.class, "TileEntityCobbleGen");

        // Register Mobs
        //EntityRegistry.registerModEntity(EntityLavaSquid.class, "LavaSquid", 1, this, 80, 3, true);
        //EntityRegistry.addSpawn(EntityLavaSquid.class, 10, 2, 4, EnumCreatureType.monster, BiomeGenBase.hell);
        
    	// Register Blocks
        Item.itemsList[NetherOreID] = new ItemBlockNetherOres(NetherOreID-256, blockNetherOres).setItemName("NetherOre");
		Item.itemsList[MachineID] = new ItemBlockMachine(MachineID-256, blockMachine).setItemName("MachineBlock");
        GameRegistry.registerBlock(blockNetherTorch, "blockNetherTorch");
        GameRegistry.registerBlock(blockLavaGen, "blockLavaGen");
        GameRegistry.registerBlock(blockCobbleGen, "blockCobbleGen");
  	
        // Register with Ore Dictionary
        if ( NetherBitsModFunctions.registerOreDictionary() != 1)
        	System.out.println("An error occured while adding Nether Ore Mod ores/ingots to Forge Ore Dictionary!");
      
    	// Add Names
        if (NetherBitsModFunctions.addNames(modLanguage) != 1)
        	 System.out.println("An error occured while adding Nether Ore Mod names!");
     
        // Generate Ores
        GameRegistry.registerWorldGenerator(new WorldGeneratorNetherBits());
        
        // Set Harvest Levels
        if (NetherBitsModFunctions.setHarvestLevels() != 1)
        	System.out.println("An error occured while adding Nether Ore Mod harvest levels!");
        
        if (NetherBitsModFunctions.createRecipes() != 1)
        	System.out.println("An error occured while adding Nether Ore Mod recipes!");
    }
	
    
    // Config Properties setup
	// Blocks
	public static int NetherOreID;
	public static int NetherTorchID;
	public static int LavaGenID;
	public static int CobbleGenID;
	public static int MachineID;
	// Items
	public static int NetherItemID;
	public static int ResonanceBandID;
	// General
	public static boolean generateCopperTin;
	public static int modLanguage;
	public static int resonatorFrequency;
	public static int ironFrequency, enderFrequency, diamondFrequency, goldFrequency,
		redstoneFrequency, copperFrequency, tinFrequency, coalFrequency, crystalFrequency;
	public static int dungeon1Frequency;
	public static boolean debugMode;
	// End Config Properties Setup
	
	// Block Definitions
	public static Block blockNetherOres;
	public static Block blockNetherTorch;
	public static Block blockLavaGen;
	public static Block blockCobbleGen;
	public static Block blockMachine;

	// Item Definitions
	public static Item itemNetherItems;
	public static Item itemResonanceBand;
	
	
	// GUIs
	public static int cobbleGenGUI = 0;
	
	// TODO: Use relative coordinates for lava gen?
	// TODO: Make crystals expand?
	// TODO: LiquidAPI Water for cobble gen recipe
	// TODO: Make Lava Squid
	
	/* Changelog:
	 * 0.3.3
	 * Machines use a single block ID now. YOU MUST BREAK AND REPLACE THEM, OR YOU WILL LOSE THEM IN THE FUTURE!
	 * Lava crystal resonators now have a smoother "filling" animation (13 total states)
	 * Dungeon spawn frequency is configurable. Default is 2% chance (lowered from 5%)
	 * Lava crystal resonator code cleaned significantly.
	 * Cobble gen bug fix, it would always keep 1 cobble in it even if a redstone signal was applied
	 * Resonance Band lasts half as long (I believe it's 5 minutes in lava/ 7.5 in fire)
	 * 
	 * 0.3.2
	 * Update to MC 1.4.7
	 * Resonator texture is now dependent on pairing. Old style for unpaired. When paired, the lava texture faces the paired block.
	 * Resonator texture is now animated. Has 4 stages, empty, low, mid, and full.
	 * Internal file structure changes. Cleans up the structure a lot.
	 * Cobble gen can be disabled with redstone
	 * Cobble Gen no longer lets you put anything except cobblestone into the slot (before would throw it out)
	 * Shards are now worth the same as lava buckets in a furnace. Reformed shards are worth 4x.
	 * 
	 * 0.3.1
	 * MC 1.4.6
	 * Change name to Nether Bits
	 * Changed Lava Gen and Cobble Gen to use updateEntity instead of block updates
	 * Added debug option
	 * 
	 * 0.3.0
	 * MC 1.4.5
	 * Lava crystals spawn taller, easier to get
	 * Added resonance band -- takes damage as used. must be in hotbar.
	 * Fixed pick block with ores
	 * Slight change to cobble gen texture
	 */
	
}
