package mods.magico13.ExtraIndustrial.machine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ContainerFakeWorkbench extends Container {
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    
	public ContainerFakeWorkbench() {
		int var6;
        int var7;

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 3; ++var7)
            {
                this.addSlotToContainer(new Slot(this.craftMatrix, var7 + var6 * 3, 30 + var7 * 18, 17 + var6 * 18));
            }
        }
	}
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return false;
	}

}
