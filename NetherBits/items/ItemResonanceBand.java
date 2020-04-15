package magico13.mods.NetherBits.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemResonanceBand extends Item{

	public ItemResonanceBand(int itemID) {
		super(itemID);
		this.setCreativeTab(CreativeTabs.tabTools);
		this.iconIndex=6;
		this.setItemName("ResonanceBand");
		this.setMaxStackSize(1);
		this.setMaxDamage(18000);
	}
	
	@SideOnly(Side.CLIENT)
	public String getTextureFile(){
		return "/magico13/mods/NetherBits/items.png";
	}

}
