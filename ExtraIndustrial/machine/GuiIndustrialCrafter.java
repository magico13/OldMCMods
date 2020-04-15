package mods.magico13.ExtraIndustrial.machine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import mods.magico13.ExtraIndustrial.tools.ContainerPortableCrafting;
import mods.magico13.ExtraIndustrial.tools.ItemPortableCrafting;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiIndustrialCrafter extends GuiContainer {

	private TileEntityIndustrialCrafter crafterInv;
	private EntityPlayer guiPlayer;

	public GuiIndustrialCrafter (InventoryPlayer inventoryPlayer,
			World world, int x, int y, int z) {
		//the container is instanciated and passed to the superclass for handling
		super(new ContainerIndustrialCrafter(inventoryPlayer, world, x, y, z));
		this.crafterInv = (TileEntityIndustrialCrafter) world.getBlockTileEntity(x, y, z);
		this.guiPlayer = inventoryPlayer.player;

		ySize = 190;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRenderer.drawString("Industrial Crafting Table", 8, 6, 4210752);
		fontRenderer.drawString("Mode:", 6, 73, 4210752);
		if (crafterInv.modeAuto)
			fontRenderer.drawString("Auto", 33, 73, 4210752);
		else
			fontRenderer.drawString("Manual", 33, 73, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 89, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
			int par3) {
		//draw your Gui here, only thing you need to change is the path
		//int texture = mc.renderEngine.getTexture(ExtraIndustrialReference.industrialCrafterGUIPNG);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(ExtraIndustrialReference.industrialCrafterGUIPNG);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		int var7;

		//	if (this.crafterInv.isActive())
		{
			//System.out.println(this.compactorInv.getActive());
			var7 = this.crafterInv.getProgressScaled(28);
			this.drawTexturedModalRect(x + 87, y + 67 - var7, 176, 15 + 28 - var7, 7, var7 + 1);
		}
		var7 = this.crafterInv.getChargeScaled(14);
		this.drawTexturedModalRect(x + 83, y + 69 + 14 - var7, 176, 14 - var7, 14, var7 + 1);

	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(0, this.guiLeft+70, this.guiTop+72, 8, 8, "S"));

		//Clear crafting grid button
		buttonList.add(new GuiButton(1, this.guiLeft+5, this.guiTop+58, 19, 10, "Clr"));

		// Save/Load recipes. Recipe ID is i. 
		for (int i=1; i<4; i++)
		{
			buttonList.add(new GuiButton(2*i, this.guiLeft+5, this.guiTop+16+((i-1)*13), 10, 10, "S"));
			buttonList.add(new GuiButton(2*i+1, this.guiLeft+13, this.guiTop+16+((i-1)*13), 10, 10, "L"));
		}
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		//Button 0 changes the mode
		if (guibutton.id == 0)
		{
			crafterInv.modeAuto = !crafterInv.modeAuto;
		}
		//Button 1 just clears the crafting grid
		else if (guibutton.id == 1)
		{
			for (int i=14; i<23; i++)
			{
				crafterInv.setInventorySlotContents(i, null);
			}
		}
		//Other buttons save/load recipes
		else
		{
			int buttonType = guibutton.id % 2;
			int saveID = guibutton.id / 2;
			if (buttonType == 0)
			{
				this.saveRecipe("recipe"+saveID);
			}
			else if (buttonType == 1)
			{
				this.loadRecipe("recipe"+saveID);
			}
			else
			{
				return;
			}
		}
		NBTTagCompound tag = new NBTTagCompound(); 
		crafterInv.writeToNBT(tag);
		Packet packetToSend = PacketUtils.packetFromNBT(3, tag);
		PacketDispatcher.sendPacketToServer(packetToSend);
	}

	private void saveRecipe(String saveName)
	{
		NBTTagList itemList = new NBTTagList();
		for (int i = 14; i < 23; i++) {
			ItemStack stack = crafterInv.getStackInSlot(i);
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		crafterInv.saveRecipe(saveName, itemList);
		guiPlayer.addChatMessage("[EI] Recipe saved!");

	}

	private void loadRecipe(String saveName)
	{
		for (int i=14; i<23; i++)
			crafterInv.setInventorySlotContents(i, null);

		NBTTagList tagList = crafterInv.loadRecipe(saveName);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 14 && slot < 23) {
				crafterInv.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tag));
			}
		}
	}
}
