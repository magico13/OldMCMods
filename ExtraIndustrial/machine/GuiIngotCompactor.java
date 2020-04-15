package mods.magico13.ExtraIndustrial.machine;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiIngotCompactor extends GuiContainer {

	private TileEntityIngotCompactor compactorInv;
	
    public GuiIngotCompactor (InventoryPlayer inventoryPlayer,
            World world, int x, int y, int z) {
    //the container is instanciated and passed to the superclass for handling
    super(new ContainerMachine(inventoryPlayer, world, x, y, z));
    this.compactorInv = (TileEntityIngotCompactor) world.getBlockTileEntity(x, y, z);
}

@Override
protected void drawGuiContainerForegroundLayer(int par1, int par2) {
    //draw text and stuff here
    //the parameters for drawString are: string, x, y, color
    fontRenderer.drawString("Ingot Compactor", 8, 6, 4210752);
    //draws "Inventory" or your regional equivalent
    fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
}

@Override
protected void drawGuiContainerBackgroundLayer(float par1, int par2,
            int par3) {
    //draw your Gui here, only thing you need to change is the path
   // int texture = mc.renderEngine.getTexture(ExtraIndustrialReference.ingotCompactorGUIPNG);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(ExtraIndustrialReference.ingotCompactorGUIPNG);
    int x = (width - xSize) / 2;
    int y = (height - ySize) / 2;
    this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    int var7;
    
    if (this.compactorInv.getActive())
    {
    	//System.out.println(this.compactorInv.getActive());
        var7 = this.compactorInv.getProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, var7 + 1, 16);
    }
    var7 = this.compactorInv.getChargeScaled(14);
    this.drawTexturedModalRect(x + 56, y + 36 + 14 - var7, 176, 14 - var7, 14, var7 + 2);

}

}
