package magico13.mods.NetherBits;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import magico13.mods.NetherBits.handlers.NetherFuel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class NetherBitsModFunctions extends NetherBitsMod {
	// Add Block/Item Names
	public static int addNames(int language)
	{	// Add names based on language in config. 0 is English, 1 is German, 2 is L33T
		switch (language)
		{
		case 1:
		{
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,0), "Nether Eisenerz");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,1), "Ender Erz");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,2), "Nether Diamant");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,3), "Nether Golderz");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,4), "Nether Rotstein");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,5), "Nether Kupfererz");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,6), "Nether Zinnerz");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,7), "Nether Kohlerz");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,8), "Lava Kristall");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,0), "Nether Kupferbarren");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,1), "Nether Zinnbarren");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,2), "Nether Kohle");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,3), "Lava Kristallsplitter");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,4), "Reformierten Kristallsplitter");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,5), "Reformierten Lava Kristall");
			LanguageRegistry.addName(new ItemStack(itemResonanceBand,1), "Resonanzband");
			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 1), "Lava Kristallresonator");
			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 0), "Kopfsteinpflaster-Generator");
			LanguageRegistry.addName(blockNetherTorch, "Nether Lampe");
			return 1;
		}
		case 2:
		{
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,0), "|-|311 1R0|\\|");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,1), "5|<Y 3Y3z");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,2), "|-|311 1[3");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,3), "|-|311 BL1|\\|G");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,4), "|-|311 R3D $|-|1T");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,5), "|-|311 [0PP3R");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,6), "|-|311 T1|\\|");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,7), "|-|311 [041");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,8), "l4V4 [RY5T41");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,0), "|-|311 [0PP3R B4R");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,1), "|-|311 T1|\\| B4R");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,2), "|-|311 [041");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,3), "l4V4 [RY5T41 5|-|4RD");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,4), "R3F0R|\\/|3D [RY5T41 5|-|4RD");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,5), "R3F0R|\\/|3D l4V4 [RY5T41");
			LanguageRegistry.addName(new ItemStack(itemResonanceBand,1), "R350|\\|4|\\|[3 B4|\\|D");
			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 1), "l4V4 [RY5T41 R350|\\|4T0R");
			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 0), "5[R4P |\\/|4|<3R");
			LanguageRegistry.addName(blockNetherTorch, "|-|311 T0R[|-|");
			return 1;
		}
		default:
		{
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,0), "Nether Iron");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,1), "Ender Ore");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,2), "Nether Diamond Ore");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,3), "Nether Gold Ore");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,4), "Nether Redstone Ore");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,5), "Nether Copper Ore");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,6), "Nether Tin Ore");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,7), "Nether Coal Ore");
			LanguageRegistry.addName(new ItemStack(blockNetherOres,1,8), "Lava Crystal");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,0), "Nether Copper Ingot");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,1), "Nether Tin Ingot");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,2), "Nether Coal");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,3), "Lava Crystal Shard");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,4), "Reformed Crystal Shard");
			LanguageRegistry.addName(new ItemStack(itemNetherItems,1,5), "Reformed Lava Crystal");
			LanguageRegistry.addName(new ItemStack(itemResonanceBand,1), "Resonance Band");
			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 0), "Cobblestone Generator");
			LanguageRegistry.addName(new ItemStack(blockMachine, 1, 1), "Lava Crystal Resonator");
			LanguageRegistry.addName(blockNetherTorch, "Nether Torch");
			return 1;
		}
		}
	}
	// Set the block harvest levels (Iron, Copper, and Tin use stone, Coal/Crystals use wood, the others Iron)
	public static int setHarvestLevels()
	{
		
		MinecraftForge.setBlockHarvestLevel(blockNetherOres, 0, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(blockNetherOres, 5, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(blockNetherOres, 6, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(blockNetherOres, 7, "pickaxe", 0);
        MinecraftForge.setBlockHarvestLevel(blockNetherOres, 8, "pickaxe", 0);
        for (int i=1; i<=4; i++)
        	MinecraftForge.setBlockHarvestLevel(blockNetherOres, i, "pickaxe", 2);
        return 1;
	}
	// Register the Copper and Tin stuff with the Ore Dictionary
	public static int registerOreDictionary()
	{
		// Register Copper
        OreDictionary.registerOre("oreCopper", new ItemStack(blockNetherOres, 1, 5));
        OreDictionary.registerOre("ingotCopper", new ItemStack(itemNetherItems, 1, 0));
        // Register Tin
        OreDictionary.registerOre("oreTin", new ItemStack(blockNetherOres, 1, 6));
        OreDictionary.registerOre("ingotTin", new ItemStack(itemNetherItems, 1, 1));
		return 1;
	}
	// Add Crafting and Smelting recipes
	public static int createRecipes()
	{
	// Shaped Recipes
		// Crystal Resonator
		GameRegistry.addRecipe(new ItemStack(blockLavaGen, 1), new Object[]
				{"OOO", "OCO", "OOO", 'O', Block.obsidian, 'C', new ItemStack(itemNetherItems, 1, 5)});
		// Reformed Lava Crystal
		GameRegistry.addRecipe(new ItemStack(itemNetherItems, 1, 5), new Object[]
				{" C ", "CDC", " C ", 'D', Item.diamond, 'C', new ItemStack(itemNetherItems, 1, 4)});
		// Cobblestone Generator
		GameRegistry.addRecipe(new ItemStack(blockCobbleGen, 1), new Object[]
				{"BCL", "RAR", "CCC", 'C', Block.cobblestone, 'A', Item.pickaxeSteel, 'B', Item.bucketWater, 
					'L', new ItemStack(itemNetherItems, 1, 3), 'R', Item.redstone});
		GameRegistry.addRecipe(new ItemStack(blockCobbleGen, 1), new Object[]
				{"LCB", "RAR", "CCC", 'C', Block.cobblestone, 'A', Item.pickaxeSteel, 'B', Item.bucketWater, 
					'L', new ItemStack(itemNetherItems, 1, 3), 'R', Item.redstone});
		// Resonance Band
		GameRegistry.addRecipe(new ItemStack(itemResonanceBand, 1), 
				"CCC", "I I", " I ", 'C', new ItemStack(itemNetherItems, 1, 5), 'I', Item.ingotIron);
	// Shapeless Recipes
		//Compatibility Recipes
        GameRegistry.addShapelessRecipe(new ItemStack(Block.oreIron, 1),
				new ItemStack(blockNetherOres, 1 , 0));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.oreDiamond, 1),
				new ItemStack(blockNetherOres, 1 , 2));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.oreGold, 1),
				new ItemStack(blockNetherOres, 1 , 3));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.oreRedstone, 1),
				new ItemStack(blockNetherOres, 1 , 4));
        GameRegistry.addShapelessRecipe(new ItemStack(Block.oreCoal, 1),
				new ItemStack(blockNetherOres, 1 , 7));
        GameRegistry.addShapelessRecipe(new ItemStack(Item.coal, 1),
				new ItemStack(itemNetherItems, 1 , 2));
        
        // New Recipes
        // Nether Torch
        GameRegistry.addShapelessRecipe(new ItemStack(blockNetherTorch, 6),
				new ItemStack(itemNetherItems, 1 , 2), new ItemStack(Item.stick));
        // Reformed Shard
        GameRegistry.addShapelessRecipe(new ItemStack(itemNetherItems, 1, 4),
				new ItemStack(itemNetherItems, 1 , 3), new ItemStack(itemNetherItems, 1 , 3), 
				new ItemStack(itemNetherItems, 1 , 3), new ItemStack(itemNetherItems, 1 , 3));
        // Reformed shard to individual shards
        GameRegistry.addShapelessRecipe(new ItemStack(itemNetherItems, 4, 3),
				new ItemStack(itemNetherItems, 1 , 4));
        
    // Smelting Recipes
        FurnaceRecipes.smelting().addSmelting(blockNetherOres.blockID, 5, new ItemStack(itemNetherItems, 1, 0), 0);
        FurnaceRecipes.smelting().addSmelting(blockNetherOres.blockID, 6, new ItemStack(itemNetherItems, 1, 1), 0);
        FurnaceRecipes.smelting().addSmelting(blockNetherOres.blockID, 1, new ItemStack(Item.enderPearl, 1, 0), 0);
        
    // Add Fuel
        GameRegistry.registerFuelHandler(new NetherFuel());
        
        return 1;
	}
	
	
	// Packet Handling stuff
	// MUCH thanks to gcewing on the forge forums, whose code I shamefully took because I couldn't get my own to work
	public static Packet packetFromTileEntity(TileEntity te) {
	      NBTTagCompound tag = new NBTTagCompound();
	      te.writeToNBT(tag);
	      return packetFromNBT(0, tag);
	   }
	   
	   public static Packet packetFromNBT(int type, NBTTagCompound tag) {
	      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	      DataOutputStream stream = new DataOutputStream(bytes);
	      try {
	         stream.write(type);
	         NBTBase.writeNamedTag(tag, stream);
	      }
	      catch (IOException e) {
	         throw new RuntimeException(e.toString());
	      }
	      byte[] data = bytes.toByteArray();
	      Packet250CustomPayload packet = new Packet250CustomPayload();
	      packet.channel = "NBUpdate";
	      packet.length = data.length;
	      packet.data = data;
	      return packet;
	   }
	   
	   public static NBTTagCompound nbtFromPacket(Packet250CustomPayload pkt) {
	      DataInput stream = new DataInputStream(new ByteArrayInputStream(pkt.data, 1, pkt.length - 1));
	      try {
	         NBTBase tag = NBTBase.readNamedTag(stream);
	         if (tag instanceof NBTTagEnd)
	        	 return new NBTTagCompound();
	         return (NBTTagCompound)tag;
	      }
	      catch (IOException e) {
	         throw new RuntimeException(e.toString());
	      }
	   }
	
}
