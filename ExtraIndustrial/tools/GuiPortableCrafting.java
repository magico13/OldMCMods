package mods.magico13.ExtraIndustrial.tools;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;


/**
 * Many thanks to Pahimar and EE3 for the base of the portable crafting code.
 * https://github.com/pahimar/Equivalent-Exchange-3
 */

public class GuiPortableCrafting extends GuiContainer {

	private EntityPlayer guiPlayer;
	public GuiPortableCrafting(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerPortableCrafting(player.inventory, world, x, y, z));
		guiPlayer = player;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {

		this.fontRenderer.drawString("Digital Pocket Crafter", 28, 6, 4210752);
		//this.fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {

		//int texture = mc.renderEngine.getTexture(ExtraIndustrialReference.portableCrafterGUIPNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(ExtraIndustrialReference.portableCrafterGUIPNG);
		int var5 = (this.width - this.xSize) / 2;
		int var6 = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(var5, var6, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void initGui() {
		super.initGui();
		//make buttons
		//id, x, y, width, height, text
		//Clear crafting grid button
		buttonList.add(new GuiButton(1, this.guiLeft+9, this.guiTop+59, 19, 10, "Clr"));
		

		// Save/Load recipes. Recipe ID is i. 
		for (int i=1; i<4; i++)
		{
			buttonList.add(new GuiButton(2*i, this.guiLeft+9, this.guiTop+17+((i-1)*13), 10, 10, "S"));
			buttonList.add(new GuiButton(2*i+1, this.guiLeft+17, this.guiTop+17+((i-1)*13), 10, 10, "L"));
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		ContainerPortableCrafting crafterContainer = (ContainerPortableCrafting) this.inventorySlots;
		ItemStack crafterStack = guiPlayer.inventory.getCurrentItem();
		ItemPortableCrafting crafterItem = (ItemPortableCrafting) crafterStack.getItem();
		//Button 1 just clears the crafter
		if (guibutton.id == 1)
		{
			for (int i=0; i<10; i++)
			{
				crafterContainer.getSlot(i).putStack(null);
			}
		}
		//Other buttons save/load recipes
		else
		{
			int buttonType = guibutton.id % 2;
			int saveID = guibutton.id / 2;
			if (buttonType == 0)
			{
				this.saveRecipe("Saved"+saveID);
			}
			else if (buttonType == 1)
			{
				this.loadRecipe("Saved"+saveID);
			}
			else
			{
				return;
			}
		}
		crafterContainer.writeNBT(guiPlayer);
		PacketDispatcher.sendPacketToServer(PacketUtils.packetFromNBT(2, crafterStack.getTagCompound())); //send packet
	}

	private void saveRecipe(String saveName)
	{
		ContainerPortableCrafting crafterContainer = (ContainerPortableCrafting) this.inventorySlots;
		ItemStack crafterStack = guiPlayer.inventory.getCurrentItem();
		NBTTagCompound tagCompound = crafterStack.getTagCompound();
		if (tagCompound == null)
		{	
			tagCompound = new NBTTagCompound();
			crafterStack.setTagCompound(tagCompound);
		}
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < 10; i++) {
			ItemStack stack = crafterContainer.getSlot(i).getStack();
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag(saveName, itemList);
		guiPlayer.addChatMessage("[EI] Recipe saved!");
	}

	private void loadRecipe(String saveName)
	{
		ContainerPortableCrafting crafterContainer = (ContainerPortableCrafting) this.inventorySlots;
		ItemStack crafterStack = guiPlayer.inventory.getCurrentItem();
		for (int i=0; i<9; i++)
			crafterContainer.craftMatrix.setInventorySlotContents(i, null);
		NBTTagCompound tagCmp = crafterStack.getTagCompound();
		if (tagCmp == null)
			return;
		NBTTagList tagList = tagCmp.getTagList(saveName);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < 10) {
				crafterContainer.getSlot(slot).putStack(ItemStack.loadItemStackFromNBT(tag));
			}
		}
	}
}