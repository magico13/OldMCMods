package magico13.mods.NetherBits.torch;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockTorch;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNetherTorch extends BlockTorch {
	public BlockNetherTorch(int blockID)
    {
        super(blockID, 0);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setLightValue(1F);
        this.setHardness(0.0F);
        this.setStepSound(soundWoodFootstep);
        this.setBlockName("NetherTorch");
        this.setRequiresSelfNotify();
        this.blockIndexInTexture = 8;
    }
	
	@Override
	public String getTextureFile()
	{
		return "/magico13/mods/NetherBits/terrain.png";
	}

	
	@SideOnly(Side.CLIENT)
	@Override
    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int var6 = par1World.getBlockMetadata(par2, par3, par4);
        double var7 = (double)((float)par2 + 0.5F);
        double var9 = (double)((float)par3 + 0.7F);
        double var11 = (double)((float)par4 + 0.5F);
        double var13 = 0.2199999988079071D;
        double var15 = 0.27000001072883606D;

        if (var6 == 1)
        {
        	FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityRedFlameFX(par1World, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D));
            par1World.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("dripLava", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
        }
        else if (var6 == 2)
        {
            par1World.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("dripLava", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityRedFlameFX(par1World, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D));
            
        }
        else if (var6 == 3)
        {
            par1World.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("dripLava", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityRedFlameFX(par1World, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D));
            
        }
        else if (var6 == 4)
        {
            par1World.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("dripLava", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityRedFlameFX(par1World, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D));
            
        }
        else
        {
            par1World.spawnParticle("smoke", var7, var9, var11, 0.0D, 0.0D, 0.0D);
           // par1World.spawnParticle("dripLava", var7, var9, var11, 0.0D, 0.0D, 0.0D); The Drip Lava looks like it's bleeding
           // par1World.spawnParticle("flame", var7, var9, var11, 0.0D, 0.0D, 0.0D);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityRedFlameFX(par1World,var7, var9, var11, 0.0D, 0.0D, 0.0D));
   
        }
    }
}
