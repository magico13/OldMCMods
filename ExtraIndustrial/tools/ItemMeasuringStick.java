package mods.magico13.ExtraIndustrial.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemMeasuringStick extends Item {

	public ItemMeasuringStick(int itemID) {
		super(itemID);
		this.setUnlocalizedName("itemMeasuringStick");
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setMaxStackSize(1);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":MeasuringStick");
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10)
	{
		if (!world.isRemote)
		{
			NBTTagCompound tagCompound = itemStack.getTagCompound();
			if (tagCompound == null)
				tagCompound = new NBTTagCompound();
			if (!tagCompound.getBoolean("alreadyClicked"))
			{
				int x1 = x;
				int y1 = y;
				int z1 = z;
				tagCompound.setInteger("x1", x1);
				tagCompound.setInteger("y1", y1);
				tagCompound.setInteger("z1", z1);
				tagCompound.setBoolean("alreadyClicked", true);
				itemStack.setTagCompound(tagCompound);
				return true;
			}
			else
			{
				outputDistance(itemStack, player, x, y, z);
				tagCompound.setBoolean("alreadyClicked", false);
				itemStack.setTagCompound(tagCompound);
				return true;
			}
		}
		return false;
	}

	private void outputDistance(ItemStack stack, EntityPlayer player, int x2, int y2, int z2)
	{
		NBTTagCompound tagCompound = stack.getTagCompound();
		int x1 = tagCompound.getInteger("x1");
		int y1 = tagCompound.getInteger("y1");
		int z1 = tagCompound.getInteger("z1");
		int delX = Math.abs(x1-x2);
		int delY = Math.abs(y1-y2);
		int delZ = Math.abs(z1-z2);
		if (delX != 0)
			delX = delX+1;
		if (delY != 0)
			delY = delY+1;
		if (delZ != 0)
			delZ = delZ+1;
		double dist = Math.sqrt(Math.pow(delX, 2)+Math.pow(delY, 2)+Math.pow(delZ, 2));
		dist = Math.floor(100*dist)/100;
		player.addChatMessage("Total Distance: " + dist);
		player.addChatMessage("X dist: " + delX + " Z dist: " + delZ + " Y dist: " + delY);
	}
}
