package mods.magico13.ExtraIndustrial.tools;

import java.util.List;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class ItemPortableCrafting extends Item implements IElectricItem, IInventory{

	private ItemStack[] inv;
	private static int maxCharge, transferLimit, tier, useCost;
	public ItemPortableCrafting(int itemID) {
		super(itemID);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setUnlocalizedName("PortableCrafting");
		this.setMaxStackSize(1);
		this.setMaxDamage(27);
		this.canRepair = false;
		this.maxCharge = 10000;
		this.transferLimit = 100;
		this.tier = 1;
		this.useCost = 39;
		this.inv = new ItemStack[10];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":PocketCrafter");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		player.openGui(ExtraIndustrialMod.instance, ExtraIndustrialReference.craftingGUI, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
		return itemStack;
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
			if (!ExtraIndustrialMod.NEIinstalled)
				par3List.add((ElectricItem.discharge(par1ItemStack, this.maxCharge, 3, true, true))+"/"+this.maxCharge+" EU");
	}
	
	@Override
	public boolean hasContainerItem() {
		return true;
	}
	
	@Override
	public boolean canProvideEnergy(ItemStack itemstack) {
		return false;
	}

	@Override
	public int getChargedItemId(ItemStack itemstack) {
		return this.itemID;
	}

	@Override
	public int getEmptyItemId(ItemStack itemstack) {
		return this.itemID;
	}

	@Override
	public int getMaxCharge(ItemStack itemstack) {
		return this.maxCharge;
	}

	@Override
	public int getTier(ItemStack itemstack) {
		return this.tier;
	}

	@Override
	public int getTransferLimit(ItemStack itemstack) {
		return this.transferLimit;
	}

	@Override
	public int getSizeInventory() {
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		return stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public String getInvName() {
		return "crafterInv";
	}

	@Override
	public void onInventoryChanged() {}
	
	public int getEnergyUse()
	{
		return useCost;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		return true;
	}
}

