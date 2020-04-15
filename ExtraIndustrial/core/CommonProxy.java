package mods.magico13.ExtraIndustrial.core;

import mods.magico13.ExtraIndustrial.generator.*;
import mods.magico13.ExtraIndustrial.machine.*;
import mods.magico13.ExtraIndustrial.tools.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{

	/**
	 * Client side only register stuff...
	 */
	public void registerRenderInformation()
	{
		//unused server side. -- see ClientProxy for implementation
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		if (ID == ExtraIndustrialReference.craftingGUI) {
			return new ContainerPortableCrafting(player.inventory, world, x, y, z);
		}
		else if (ID == ExtraIndustrialReference.ingotCompactorGUI) {
			return new ContainerMachine(player.inventory, world, x, y, z);
		}
		else if (ID == ExtraIndustrialReference.industrialCrafterGUI) {
			return new ContainerIndustrialCrafter(player.inventory, world, x, y, z);
		}
		else if (ID == ExtraIndustrialReference.fusionReactorCoreGUI) {
			return new ContainerFusionReactorCore(player.inventory, world, x, y, z);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		if (ID == ExtraIndustrialReference.craftingGUI) {
			return new GuiPortableCrafting(player, world, x, y, z);
		}
		else if (ID == ExtraIndustrialReference.ingotCompactorGUI) {
			return new GuiIngotCompactor(player.inventory, world, x, y, z);
		}
		else if (ID == ExtraIndustrialReference.industrialCrafterGUI) {
			return new GuiIndustrialCrafter(player.inventory, world, x, y, z);
		}
		else if (ID == ExtraIndustrialReference.fusionReactorCoreGUI) {
			return new GuiFusionReactorCore(player.inventory, world, x, y, z);
		}
		return null;
	}

	public World getWorld() {
		return DimensionManager.getWorld(0);
	}

}