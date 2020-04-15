package magico13.mods.NetherBits.worldGen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import magico13.mods.NetherBits.NetherBitsMod;

// Adapted from Glowstone Generation
public class WorldGenLavaCrystal extends WorldGenerator
{
    public boolean generate(World world, Random random, int x, int y, int z)
    {
        if (world.getBlockId(x, y, z) != Block.lavaStill.blockID)
        {
            return false;
        }
        else if (world.getBlockId(x, y - 1, z) != Block.netherrack.blockID)
        {
            return false;
        }
        else
        {
        	world.setBlockAndMetadata(x, y, z, NetherBitsMod.blockNetherOres.blockID, 8);
           // world.setBlockWithNotify(x, y, z, Block.glowStone.blockID);

            for (int i = 0; i < 500; ++i) //1500
            {
                int xToCheck = x + random.nextInt(3) - random.nextInt(3); //8
                int yToCheck = y + random.nextInt(12); //12
                int zToCheck = z + random.nextInt(3) - random.nextInt(3); //8

                if (world.getBlockId(xToCheck, yToCheck, zToCheck) == 0 || world.getBlockId(xToCheck, yToCheck, zToCheck) == Block.lavaStill.blockID)
                {
                    int connectCheck = 0;

                    for (int side = 0; side < 6; ++side)
                    {
                        int bID = 0;
                        int meta = 0;

                        if (side == 0)
                        {
                            bID = world.getBlockId(xToCheck - 1, yToCheck, zToCheck);
                            meta = world.getBlockMetadata(xToCheck - 1, yToCheck, zToCheck);
                        }

                        if (side == 1)
                        {
                            bID = world.getBlockId(xToCheck + 1, yToCheck, zToCheck);
                            meta = world.getBlockMetadata(xToCheck + 1, yToCheck, zToCheck);
                        }

                        if (side == 2)
                        {
                            bID = world.getBlockId(xToCheck, yToCheck - 1, zToCheck);
                            meta = world.getBlockMetadata(xToCheck, yToCheck - 1 , zToCheck);
                        }

                        if (side == 3)
                        {
                            bID = world.getBlockId(xToCheck, yToCheck + 1, zToCheck);
                            meta = world.getBlockMetadata(xToCheck, yToCheck + 1, zToCheck);
                        }

                        if (side == 4)
                        {
                            bID = world.getBlockId(xToCheck, yToCheck, zToCheck - 1);
                            meta = world.getBlockMetadata(xToCheck, yToCheck, zToCheck - 1);
                        }

                        if (side == 5)
                        {
                            bID = world.getBlockId(xToCheck, yToCheck, zToCheck + 1);
                            meta = world.getBlockMetadata(xToCheck, yToCheck, zToCheck + 1);
                        }

                        if (bID == NetherBitsMod.blockNetherOres.blockID && meta == 8)
                        {
                            ++connectCheck;
                        }
                    }

                    if (connectCheck == 1)
                    {
                    	world.setBlockAndMetadata(xToCheck, yToCheck, zToCheck, NetherBitsMod.blockNetherOres.blockID, 8);
                        //world.setBlockWithNotify(var7, var8, var9, Block.glowStone.blockID);
                    }
                }
            }

            return true;
        }
    }
}
