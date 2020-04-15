package mods.magico13.ExtraIndustrial.core;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotIndicator extends Slot
{

    public SlotIndicator(EntityPlayer player, IInventory var2, int var3, int var4, int var5)
    {
        super(var2, var3, var4, var5);
    }

    @Override
    public boolean isItemValid(ItemStack var1)
    {
        return false;
    }
    
    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer)
    {
        return false;
    }
    
    @Override
    public void onSlotChanged()
    {
    	super.onSlotChanged();
    }
}
