package mods.magico13.ExtraIndustrial.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import ic2.api.item.IElectricItem;
import ic2.api.item.ElectricItem;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialMod;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class ItemElectricToolDynamicDrill extends ItemTool implements IElectricItem{
	public static Set blocksEffectiveAgainst = new HashSet();
	private int maxCharge, transferLimit, tier, useMode;
	private int operationEnergyCost[] = {80, 200, 150, 400, 50};
	private float efficiency[] = {16.0F, 12.0F, 12.0F, 75.0F, 8.0F};
	// MODES: balanced, fortune, silktouch, speed, lowpower

	public ItemElectricToolDynamicDrill(int ItemID, int IconIndex)
	{
		super(ItemID, IconIndex, EnumToolMaterial.EMERALD, new Block[0]);
		this.setUnlocalizedName("dynamicDrill");
		this.setCreativeTab(CreativeTabs.tabTools);
		this.setMinableBlocks();
		this.setMaxStackSize(1);
		this.setMaxDamage(27);
	//	this.setIconIndex(IconIndex);
		this.maxCharge = 100000;
		this.transferLimit = 1000;
		this.tier = 2;
		this.useMode = 0;
		this.damageVsEntity = 1;
		this.canRepair = false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister)
	{
		this.itemIcon = iconRegister.registerIcon(ExtraIndustrialReference.modTextures+":DynamicDrill");
	}
	
	// List of all blocks that the drill is effective against
	public void setMinableBlocks() {
		this.blocksEffectiveAgainst.add(Block.cobblestone);
		this.blocksEffectiveAgainst.add(Block.stoneSingleSlab);
		this.blocksEffectiveAgainst.add(Block.stoneDoubleSlab);
		this.blocksEffectiveAgainst.add(Block.stairsCobblestone);
		this.blocksEffectiveAgainst.add(Block.stone);
		this.blocksEffectiveAgainst.add(Block.sandStone);
		this.blocksEffectiveAgainst.add(Block.stairsSandStone);
		this.blocksEffectiveAgainst.add(Block.cobblestoneMossy);
		this.blocksEffectiveAgainst.add(Block.oreIron);
		this.blocksEffectiveAgainst.add(Block.blockIron);
		this.blocksEffectiveAgainst.add(Block.oreCoal);
		this.blocksEffectiveAgainst.add(Block.blockGold);
		this.blocksEffectiveAgainst.add(Block.oreGold);
		this.blocksEffectiveAgainst.add(Block.oreDiamond);
		this.blocksEffectiveAgainst.add(Block.blockDiamond);
		this.blocksEffectiveAgainst.add(Block.ice);
		this.blocksEffectiveAgainst.add(Block.netherrack);
		this.blocksEffectiveAgainst.add(Block.oreLapis);
		this.blocksEffectiveAgainst.add(Block.blockLapis);
		this.blocksEffectiveAgainst.add(Block.oreRedstone);
		this.blocksEffectiveAgainst.add(Block.oreRedstoneGlowing);
		this.blocksEffectiveAgainst.add(Block.brick);
		this.blocksEffectiveAgainst.add(Block.stairsBrick);
		this.blocksEffectiveAgainst.add(Block.glowStone);
		this.blocksEffectiveAgainst.add(Block.grass);
		this.blocksEffectiveAgainst.add(Block.dirt);
		this.blocksEffectiveAgainst.add(Block.mycelium);
		this.blocksEffectiveAgainst.add(Block.sand);
		this.blocksEffectiveAgainst.add(Block.gravel);
		this.blocksEffectiveAgainst.add(Block.snow);
		this.blocksEffectiveAgainst.add(Block.blockSnow);
		this.blocksEffectiveAgainst.add(Block.blockClay);
		this.blocksEffectiveAgainst.add(Block.tilledField);
		this.blocksEffectiveAgainst.add(Block.stoneBrick);
		this.blocksEffectiveAgainst.add(Block.stairsStoneBrick);
		this.blocksEffectiveAgainst.add(Block.netherBrick);
		this.blocksEffectiveAgainst.add(Block.stairsNetherBrick);
		this.blocksEffectiveAgainst.add(Block.slowSand);
		this.blocksEffectiveAgainst.add(Block.anvil);
		this.blocksEffectiveAgainst.add(Block.obsidian);
	}
	// When a block is destroyed, use charge from the drill
	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving)
	{

		if ((double)Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0D)
		{
			ElectricItem.use(par1ItemStack, par1ItemStack.stackTagCompound.getInteger("chargeUse"), (EntityPlayer)par7EntityLiving);
		}
		return true;
	}
	// When the drill is created, create the NBT tag and set the energy cost and efficiency parameters
	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par1ItemStack.setTagCompound(new NBTTagCompound());
		par1ItemStack.stackTagCompound.setInteger("chargeUse", operationEnergyCost[0]);
		par1ItemStack.stackTagCompound.setFloat("efficiencyAgainstProperMaterial", efficiency[0]);
	}
	// Set the enchantability to 0 so the player can't enchant the drill (they would be lost anyway)
	@Override
	public int getItemEnchantability()
	{
		return 0;
	}
	// Causes the item to glow
	@Override
	public boolean hasEffect(ItemStack itemstack)
	{
		return true;
	}
	// When attacking an entity, do nothing special, but don't damage the item
	@Override
	public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3)
	{
		return true;
	}
	// Add the EU level and mode to the item when hovering over it
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{

		if (!ExtraIndustrialMod.NEIinstalled)
			par3List.add((ElectricItem.discharge(par1ItemStack, this.maxCharge, 3, true, true))+"/"+this.maxCharge+" EU");
		if (par1ItemStack.stackTagCompound != null)
		{
			par3List.add("Mode: ");
			if (par1ItemStack.stackTagCompound.getInteger("useMode") == 0)
				par3List.add("Balanced");
			if (par1ItemStack.stackTagCompound.getInteger("useMode") == 3)
				par3List.add("Speed");
			if (par1ItemStack.stackTagCompound.getInteger("useMode") == 4)
				par3List.add("Low Power");
		}
	}
	// On right click change the mode
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		/*
    	if (par2World.isRemote)
    	{

    	Method ret, met;
    	Object pressed;
    	Object tK = null;

    		try {
    			Class keyboard = Class.forName("ic2.common.KeyboardClient");
    			//Class keyboardClient = Class.forName("ic2.common.KeyboardClient");
    			tK = keyboard.newInstance();
    			ret = keyboard.getMethod("isModeSwitchKeyDown", EntityPlayer.class);
    			met = keyboard.getDeclaredMethod("sendKeyUpdate");
    			ret.setAccessible(true);
    			met.setAccessible(true);
    			//ret.
    			met.invoke(tK);
    			pressed = ret.invoke(tK, par3EntityPlayer);
    			System.out.println(pressed);
    		} catch (ClassNotFoundException e) {
    			System.out.print("ExtraIndustrial could not locate the IC2 Keyboard Class!");
    		} catch (IllegalArgumentException e) {
	    		System.out.println("Illegal argument while invoking");
	    		e.printStackTrace();
	    	} catch (IllegalAccessException e) {
	    		System.out.println("Illegal access exception");
	    		e.printStackTrace();
	    	} catch (InvocationTargetException e) {
	    		System.out.println("Invocation target issue");
	    		e.printStackTrace();
	    	} catch (SecurityException e) {
	    		System.out.println("Security Exception");
	    		e.printStackTrace();
	    	} catch (NoSuchMethodException e) {
	    		System.out.println("Method not found");
	    		e.printStackTrace();
	    	} catch (InstantiationException e) {
	    		System.out.println("Instantiation issue");
	    	}
    	}

	 */
		/*if (!(pressed)){
			System.out.print("Not Pressed!");
			System.out.print(pressed);
    		return par1ItemStack;
		}
		System.out.print(pressed);*/
		//ret.(par3EntityPlayer);
		
		
		//if (!par2World.isRemote)
		if (player.isSneaking())
		{
			if (itemStack.stackTagCompound == null)
				itemStack.setTagCompound(new NBTTagCompound());
			// Remove all enchantments from the drill
			//if (!par2World.isRemote)
			{
				NBTTagList enchTag = (NBTTagList)itemStack.stackTagCompound.getTag("ench");
				if (enchTag != null){
					if (enchTag.tagCount() != 0){
						itemStack.stackTagCompound.removeTag("ench");
					}
				}
			}
			// Get the current mode, increment it by one, do modulo based on energy cost array length
			this.useMode = itemStack.stackTagCompound.getInteger("useMode");
			this.useMode =  (this.useMode + 1) % operationEnergyCost.length;
			itemStack.stackTagCompound.setInteger("useMode", this.useMode);
			// Balanced Mode: Same as diamond drill, add chat message indicating mode switch
			if (this.useMode == 0){
				if(!world.isRemote)
					player.addChatMessage("Mode: Balanced");
			}
			// Fortune Mode: fortune enchant lvl 4, add chat message indicating mode switch
			else if (this.useMode == 1){
				itemStack.addEnchantment(Enchantment.fortune, 2);
				if(!world.isRemote)
					player.addChatMessage("Mode: Fortune");
			}
			// Silktouch Mode: fortune enchant lvl 3, add chat message indicating mode switch
			else if (this.useMode == 2){
				itemStack.addEnchantment(Enchantment.silkTouch, 1);
				if(!world.isRemote)
					player.addChatMessage("Mode: Silktouch");
			}
			//Speed Mode: set efficiency to 24.0F, add chat message indicating mode switch
			else if (this.useMode == 3){
				if(!world.isRemote)
					player.addChatMessage("Mode: Speed");
			}
			//Low Power Mode: same as iron drill, add chat message indicating mode switch
			else if (this.useMode == 4){
				if(!world.isRemote)
					player.addChatMessage("Mode: Low Power");
			}
			// Change the power requirements in EU based on mode. Array is located at top.
			itemStack.stackTagCompound.setInteger("chargeUse", operationEnergyCost[this.useMode]);
			// Change the efficiency against blocks based on mode. Array is located at top.
			itemStack.stackTagCompound.setFloat("efficiencyAgainstProperMaterial", efficiency[this.useMode]);
		}
		if (!world.isRemote)
		{
			PacketDispatcher.sendPacketToPlayer(PacketUtils.packetFromNBT(1, itemStack.getTagCompound()), (Player) player);
		}
		return itemStack;
	}

	// If the block is in the "blocksEffectiveAgainst" list or the block is material iron or stone, then harvest it
	@Override
	public boolean canHarvestBlock(Block block)
	{
		return block.blockMaterial != Material.rock && block.blockMaterial != Material.iron ? this.blocksEffectiveAgainst.contains(block) : true;
	}
	// If there is sufficient charge and the block can be harvested, then work at full speed, otherwise work at 0.5F if there isn't enough charge
	@Override
	public float getStrVsBlock(ItemStack var1, Block var2)
	{
		return !ElectricItem.canUse(var1, var1.stackTagCompound.getInteger("chargeUse")) ? 1.5F : (ForgeHooks.isToolEffective(var1, var2, 0) ? var1.stackTagCompound.getFloat("efficiencyAgainstProperMaterial") : (this.canHarvestBlock(var2) ? var1.stackTagCompound.getFloat("efficiencyAgainstProperMaterial") : 1.5F));
	}
	// If there is sufficient charge and the block can be harvested, then work at full speed, otherwise work at 0.5F if there isn't enough charge
	@Override
	public float getStrVsBlock(ItemStack var1, Block var2, int var3)
	{
		return !ElectricItem.canUse(var1, var1.stackTagCompound.getInteger("chargeUse")) ? 1.5F : (ForgeHooks.isToolEffective(var1, var2, var3) ? var1.stackTagCompound.getFloat("efficiencyAgainstProperMaterial") : (this.canHarvestBlock(var2) ? var1.stackTagCompound.getFloat("efficiencyAgainstProperMaterial") : 1.5F));
	}
	// The drill cannot provide energy like a battery
	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return false;
	}
	// The charged/discharged item id is the same
	@Override
	public int getChargedItemId(ItemStack itemStack) {
		return this.itemID;
	}
	// The charged/discharged item id is the same
	@Override
	public int getEmptyItemId(ItemStack itemStack) {
		return this.itemID;
	}
	// The max charge is listed at the top
	@Override
	public int getMaxCharge(ItemStack itemStack) {
		return this.maxCharge;
	}
	// The tier (batbox, mfe, mfsu required) is listed at the top
	@Override
	public int getTier(ItemStack itemStack) {
		return this.tier;
	}
	// The transfer limit is listed at the top
	@Override
	public int getTransferLimit(ItemStack itemStack) {
		return this.transferLimit;
	}
}
