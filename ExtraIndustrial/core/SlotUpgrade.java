package mods.magico13.ExtraIndustrial.core;

import ic2.api.item.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotUpgrade extends Slot
{
    private EntityPlayer player;
    private IInventory inv;

    public SlotUpgrade(EntityPlayer player, IInventory inventory, int var3, int var4, int var5)
    {
        super(inventory, var3, var4, var5);
        this.player = player;
        this.inv = inventory;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
    	Item item = stack.getItem();
    	if (item.equals(Items.getItem("overclockerUpgrade").getItem()))
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
