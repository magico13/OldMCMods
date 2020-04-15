package mods.magico13.ExtraIndustrial.core;

import ic2.api.item.IElectricItem;
import ic2.api.item.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotElectric extends Slot
{
    private EntityPlayer player;
    private IInventory inv;

    public SlotElectric(EntityPlayer player, IInventory inventory, int var3, int var4, int var5)
    {
        super(inventory, var3, var4, var5);
        this.player = player;
        this.inv = inventory;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
    	Item item = stack.getItem();
    	if (item instanceof IElectricItem)
    		return true;
    	else
    		return false;
    }
    
    @Override
    public boolean canTakeStack(EntityPlayer player)
    {
        return true;
    }
    
    @Override
    public void onSlotChanged()
    {
    	super.onSlotChanged();
    }
}
