package mods.magico13.ExtraIndustrial.client;

import java.io.IOException;

import mods.magico13.ExtraIndustrial.core.CommonProxy;
import mods.magico13.ExtraIndustrial.core.ExtraIndustrialReference;
import mods.magico13.ExtraIndustrial.core.PacketUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.client.TextureFXManager;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderInformation()
	{  
		/*MinecraftForgeClient.preloadTexture(ExtraIndustrialReference.tool0PNG);
		MinecraftForgeClient.preloadTexture(ExtraIndustrialReference.generatorPNG);
		MinecraftForgeClient.preloadTexture(ExtraIndustrialReference.machinePNG);
		MinecraftForgeClient.preloadTexture(ExtraIndustrialReference.storagePNG);
		MinecraftForgeClient.preloadTexture(ExtraIndustrialReference.superconductorPNG);*/
	}
	
	@Override
	public World getWorld()
	{
		//return DimensionManager.getWorld(0);
		return Minecraft.getMinecraft().theWorld;
	}
}
