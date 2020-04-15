package mods.magico13.ExtraIndustrial.generator;


import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.PacketDispatcher;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GuiFusionReactorCore extends GuiContainer {

	private TileEntityFusionReactorCore teInv;
	private boolean lastState = false;
	
	public GuiFusionReactorCore (InventoryPlayer inventoryPlayer,
            World world, int x, int y, int z) {
		 super(new ContainerFusionReactorCore(inventoryPlayer, world, x, y, z));
		    this.teInv = (TileEntityFusionReactorCore) world.getBlockTileEntity(x, y, z);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
	    //draw text and stuff here
	    //the parameters for drawString are: string, x, y, color
	    fontRenderer.drawString("Fusion Reactor", 8, 6, 4210752);
	    fontRenderer.drawString(teInv.getChargeScaled(100)+" %", 100, 21, 0);
	    //fontRenderer.drawString(teInv.getCurrentEnergy()+" EU", 80, 21, 0);
	    //fontRenderer.drawString("H2: "+teInv.getCurrentHydrogen(), 8, 25, 4210752);
	    //draws "Inventory" or your regional equivalent
	    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,
	            int par3) {
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    this.mc.renderEngine.bindTexture(ExtraIndustrialReference.fusionReactorCoreGUIPNG);
	    int x = (width - xSize) / 2;
	    int y = (height - ySize) / 2;
	    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	    
	    int var7;
	    
	    var7 = this.teInv.getHydrogenScaled(50);
	    this.drawTexturedModalRect(x+36, y+65-var7+1, 177, 50-var7+1, 14, var7);
	 
	    var7 = this.teInv.getChargeScaled(100);
	    this.drawTexturedModalRect(x+64, y+18, 1, 167, var7, 14);

	}
	
	@Override
	public void initGui() {
		super.initGui();
		lastState = teInv.isGenerating();
		if (teInv.isGenerating())
			buttonList.add(new GuiButton(0, this.guiLeft+70, this.guiTop+50, 80, 16, "Stop Reactor"));
		else
			buttonList.add(new GuiButton(0, this.guiLeft+70, this.guiTop+50, 80, 16, "Start Reactor"));
	}

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		int valueToSend = 0;
		//Button 0 starts/stops the reactor
		if (guibutton.id == 0)
		{
			if (teInv.isGenerating())
				valueToSend = 0;
			else
				valueToSend = 1;
			teInv.generateFlag = true;
		}
		Packet packetToSend = PacketUtils.blockCommandPacket(teInv.xCoord, teInv.yCoord, teInv.zCoord, guibutton.id, valueToSend);
		PacketDispatcher.sendPacketToServer(packetToSend);
	}
	
	@Override
	 public void updateScreen()
    {
		super.updateScreen();
		boolean generating = teInv.isGenerating();
		if (generating != lastState)
		{
			lastState = generating;
			buttonList.clear();
			if (generating)
				buttonList.add(new GuiButton(0, this.guiLeft+70, this.guiTop+50, 80, 16, "Stop Reactor"));
			else
				buttonList.add(new GuiButton(0, this.guiLeft+70, this.guiTop+50, 80, 16, "Start Reactor"));
		}
    }
	
}
