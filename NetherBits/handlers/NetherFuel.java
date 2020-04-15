package magico13.mods.NetherBits.handlers;

import magico13.mods.NetherBits.NetherBitsMod;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.IFuelHandler;

public class NetherFuel implements IFuelHandler {

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (fuel.itemID == NetherBitsMod.itemNetherItems.itemID)
		{
			switch (fuel.getItemDamage())
			{
			case 2:
				return 2400;
			case 3:
				return 20000;
			case 4:
				return 80000;
			default:
				return 0;
			}
		}
		return 0;
	}

}
