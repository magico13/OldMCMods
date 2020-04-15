package magico13.mods.NetherBits.handlers;

import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class NetherBitsEventHandler {
	@ForgeSubscribe
	public void livingAttack(LivingAttackEvent event)
	{
		if (!(event.entityLiving instanceof EntityPlayer)) return;
		EntityPlayer eventPlayer = (EntityPlayer)event.entityLiving;
		//Check if the player has a resonance band
		for (int i=0; i<9; ++i)
		{
			//eventPlayer.inventory.hasItemStack(new ItemStack(NetherOreMod.itemNetherItems, 1, 6))
			ItemStack inventory = eventPlayer.inventory.mainInventory[i];
			if (inventory != null)
			{
				if (inventory.itemID == NetherBitsMod.itemResonanceBand.itemID)
				{
					if (event.source == DamageSource.lava || event.source == DamageSource.onFire || event.source == DamageSource.inFire)
					{
						eventPlayer.extinguish();
						event.setCanceled(true);
						inventory.damageItem(1, eventPlayer);
						//inventory.
						if (inventory.getItemDamage() >= inventory.getMaxDamage())
							eventPlayer.inventory.setInventorySlotContents(i, null);
						
						
					}
					break;
				}
			}
		}
	}
}
