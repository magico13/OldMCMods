package magico13.mods.NetherBits.worldGen;

import java.util.Random;

import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DungeonHooks;

public class WorldGenNetherDungeon1 extends WorldGenerator
{
	public boolean generate(World world, Random random, int x, int y, int z)
	{
	/*	while (world.isAirBlock(x, y, z) && y > 2)
		{
			--y;
		}*/
		int nid = Block.netherBrick.blockID;
		if (world.getBlockId(x, y, z)!=Block.netherrack.blockID)
			return false;
		int height=8;
		int width=11;
		int length=11;
		int hWidth= width/2;
		int hLength = length/2;
		
		//Set the inside area to all air prior to further generation
		for (int k=y+2;k<=y+height-1;k++)
		{
			for (int i=x-hWidth+1;i<=x+hWidth-1;i++)
			{
				for (int j=z-hLength+1;j<=z+hLength-1;j++)
				{
					world.setBlock(i, k, j, 0);
				}
			}
		}

		//Generate two layers of floor & the roof
		for (int i=x-hWidth;i<=x+hWidth;i++)
		{
			for (int j=z-hLength;j<=z+hLength;j++)
			{
				world.setBlock(i, y, j, nid);
				world.setBlock(i, y+1, j, nid);
				//Generate roof
				world.setBlock(i, y+height, j, nid);
			}
		}

		//Generate walls
		for (int k=y;k<=y+height;k++)
		{
			for (int i=x-hWidth;i<=x+hWidth;i++)
			{
				world.setBlock(i, k, z-hLength, nid);
				world.setBlock(i, k, z+hLength, nid);
			}
			for (int j=z-(hLength-1);j<=z+(hLength-1);j++)
			{
				world.setBlock(x-hWidth, k, j, nid);
				world.setBlock(x+hWidth, k, j, nid);
			}
		}
		//Generate lava pool
		for (int i=x-2;i<=x+2;i++)
		{
			for (int j=z-2;j<=z+2;j++)
			{
				world.setBlock(i, y+1, j, Block.lavaMoving.blockID);
			}
		}
		//Generate lava falls	
		world.setBlock(x+1, y+height-1, z+1, nid);
		world.setBlock(x+1, y+height-1, z-1, nid);
		world.setBlock(x-1, y+height-1, z+1, nid);
		world.setBlock(x-1, y+height-1, z-1, nid);
		world.setBlock(x-2, y+height-1, z, nid);
		world.setBlock(x+2, y+height-1, z, nid);
		world.setBlock(x, y+height-1, z+2, nid);
		world.setBlock(x, y+height-1, z-2, nid);
		world.setBlock(x+1, y+height-1, z, Block.lavaMoving.blockID);
		world.setBlock(x-1, y+height-1, z, Block.lavaMoving.blockID);
		world.setBlock(x, y+height-1, z+1, Block.lavaMoving.blockID);
		world.setBlock(x, y+height-1, z-1, Block.lavaMoving.blockID);

		//Generate center block and chest
		world.setBlock(x, y+1, z, nid);
		world.setBlock(x, y+2, z, nid);
		world.setBlock(x, y+3, z, Block.chest.blockID);
		world.setBlockTileEntity(x, y+3, z, new TileEntityChest());
		TileEntityChest chest = (TileEntityChest)world.getBlockTileEntity(x, y+3, z);
		//Generate chest loot
		ChestGenHooks info = ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST);
        WeightedRandomChestContent.generateChestContents(random, info.getItems(random), chest, info.getCount(random));
        //20% chance of spawning a resonance band
        if (random.nextInt(5)==0)
        {
        	chest.setInventorySlotContents(random.nextInt(26), new ItemStack(NetherBitsMod.itemResonanceBand, 1));
        }
        //If no band spawned, 75% chance of spawning a resonator
        else if (random.nextInt(4)<3)
        {
        	chest.setInventorySlotContents(random.nextInt(26), new ItemStack(NetherBitsMod.blockLavaGen, 1));
        }
        //20% chance of spawning 1-2 resonators
        if (random.nextInt(5)==0)
        {
        	chest.setInventorySlotContents(random.nextInt(26), new ItemStack(NetherBitsMod.blockLavaGen, random.nextInt(2)+1));
        }
        //25% chance of spawning 1-3 reformed crystals
        if (random.nextInt(4)==0)
        {
        	chest.setInventorySlotContents(random.nextInt(26), new ItemStack(NetherBitsMod.itemNetherItems, random.nextInt(3)+1, 5));
        }
        //33% chance of spawning 3-7 reformed crystal shards
        if (random.nextInt(3)==0)
        {
        	chest.setInventorySlotContents(random.nextInt(26), new ItemStack(NetherBitsMod.itemNetherItems, random.nextInt(5)+3, 4));
        }
        //50% chance of spawning 10-25 crystal shards
        if (random.nextInt(3)==0)
        {
        	chest.setInventorySlotContents(random.nextInt(26), new ItemStack(NetherBitsMod.itemNetherItems, random.nextInt(16)+10, 3));
        }
    
		
		//Create blaze spawner
		TileEntityMobSpawner spawner = new TileEntityMobSpawner();
		spawner.setMobID("Blaze");
		world.setBlock(x, y+height-2, z, Block.mobSpawner.blockID);
		world.setBlockTileEntity(x, y+height-2, z, spawner);
		
		//Create half-step border
		for (int i=x-2;i<=x+2;i++)
		{
			world.setBlockAndMetadata(i, y+2, z-3, Block.stoneSingleSlab.blockID, 6);
			world.setBlockAndMetadata(i, y+2, z+3, Block.stoneSingleSlab.blockID, 6);
		}
		for (int j=z-2;j<=z+2;j++)
		{
			world.setBlockAndMetadata(x-3, y+2, j, Block.stoneSingleSlab.blockID, 6);
			world.setBlockAndMetadata(x+3, y+2, j, Block.stoneSingleSlab.blockID, 6);
		}
		
		
		return true;
	}
}