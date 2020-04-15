package magico13.mods.NetherBits.worldGen;

import java.util.Random;

import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import cpw.mods.fml.common.IWorldGenerator;

public class WorldGeneratorNetherBits implements IWorldGenerator 
{
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
	{
		switch (world.provider.dimensionId)
		{
		case 0: generateSurface(world, random, chunkX*16, chunkZ*16); break;
		case -1: generateNether(world, random, chunkX*16, chunkZ*16); break;
		}
	}



	private void generateSurface(World world, Random random, int blockX, int blockZ) 
	{

	}

	private void generateNether(World world, Random random, int blockX, int blockZ) 
	{
		// Generate Nether Dungeons between 1 and 110
		if (random.nextInt(1000)<NetherBitsMod.dungeon1Frequency)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = 1+random.nextInt(110);
			int Zcoord = blockZ + random.nextInt(16);
			(new WorldGenNetherDungeon1()).generate(world, random, Xcoord, Ycoord, Zcoord);
		}

		// Generate Nether Iron (between 0 and 125, in netherrack. 8 per vein, 10 attempts per chunk)
		for (int i=0; i < NetherBitsMod.ironFrequency; i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = random.nextInt(126);
			int Zcoord = blockZ + random.nextInt(16);
			//NetherOreMod.blockNetherOre.blockID, 0, 8 <-- 8 is the amount that spawn in a group, 0 is the metadata
			(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 0, 8)).generate(world, random, Xcoord, Ycoord, Zcoord);
		}

		// Generate Ender Ore (between 100 and 127, in netherrack, 4 per vein, 10 attempts per chunk)
		for (int i=0; i < NetherBitsMod.enderFrequency; i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = 100 + random.nextInt(28);
			int Zcoord = blockZ + random.nextInt(16);
			(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 1, 4)).generate(world, random, Xcoord, Ycoord, Zcoord);
		}

		// Generate Nether Diamond (between 23 and 30 (lava layer), in netherrack, 5 per vein, 1 attempt per chunk)
		for (int i=0; i < NetherBitsMod.diamondFrequency; i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = 23 + random.nextInt(8);
			int Zcoord = blockZ + random.nextInt(16);
			(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 2, 5)).generate(world, random, Xcoord, Ycoord, Zcoord);
		}

		// Generate Nether Gold (between 0 and 45, in netherrack, 5 per vein, 5 attempts per chunk)
		for (int i=0; i < NetherBitsMod.goldFrequency; i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = random.nextInt(46);
			int Zcoord = blockZ + random.nextInt(16);
			(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 3, 5)).generate(world, random, Xcoord, Ycoord, Zcoord);
		}

		// Generate Nether Redstone (between 0 and 30, in netherrack, 8 per vein, 5 attempts per chunk)
		for (int i=0; i < NetherBitsMod.redstoneFrequency; i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = random.nextInt(30);
			int Zcoord = blockZ + random.nextInt(16);
			(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 4, 8)).generate(world, random, Xcoord, Ycoord, Zcoord);
		}

		// Generate Nether Coal (between 0 and 125, in netherrack, 10 per vein, 6 attempts per chunk)
		for (int i=0; i < NetherBitsMod.coalFrequency; i++)
		{
			int Xcoord = blockX + random.nextInt(16);
			int Ycoord = random.nextInt(125);
			int Zcoord = blockZ + random.nextInt(16);
			(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 7, 10)).generate(world, random, Xcoord, Ycoord, Zcoord);
		}

		if (NetherBitsMod.generateCopperTin == true)
		{
			// Generate Nether Copper (between 0 and 127, in netherrack, 8 per vein, 12 attempts per chunk)
			for (int i=0; i < NetherBitsMod.copperFrequency; i++)
			{
				int Xcoord = blockX + random.nextInt(16);
				int Ycoord = random.nextInt(128);
				int Zcoord = blockZ + random.nextInt(16);
				(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 5, 8)).generate(world, random, Xcoord, Ycoord, Zcoord);
			}
			// Generate Nether Tin (between 0 and 127, in netherrack, 5 per vein, 6 attempts per chunk)
			for (int i=0; i < NetherBitsMod.tinFrequency; i++)
			{
				int Xcoord = blockX + random.nextInt(16);
				int Ycoord = random.nextInt(128);
				int Zcoord = blockZ + random.nextInt(16);
				(new WorldGenMinableNether(NetherBitsMod.blockNetherOres.blockID, 6, 5)).generate(world, random, Xcoord, Ycoord, Zcoord);
			}
		}
		// Generate Lava Crystals in Lava, between 21 and 30, twice per chunk
		for (int i=0; i < NetherBitsMod.crystalFrequency; i++)
		{
			int Xcoord1 = blockX + random.nextInt(16);
			int Ycoord1 = 20 + random.nextInt(10);
			int Zcoord1 = blockZ + random.nextInt(16);

			(new WorldGenLavaCrystal()).generate(world, random, Xcoord1, Ycoord1, Zcoord1);
		}
	}

}