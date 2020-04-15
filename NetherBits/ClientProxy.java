package magico13.mods.NetherBits;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod.Instance;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderInformation()
	{  
		MinecraftForgeClient.preloadTexture("/magico13/mods/NetherBits/gui/items.png");
		MinecraftForgeClient.preloadTexture("/magico13/mods/NetherBits/terrain.png");
	}

	public static void receiveCustomPacket(Packet250CustomPayload pkt) {
		int type = pkt.data[0];
		switch (type) {
		case 0:
			handleTileEntityPacket(pkt);
			break;
		}
	}

	static void handleTileEntityPacket(Packet250CustomPayload pkt) {
		NBTTagCompound tag = NetherBitsModFunctions.nbtFromPacket(pkt);
		int x = tag.getInteger("x");
		int y = tag.getInteger("y");
		int z = tag.getInteger("z");
		//System.out.println(x+" "+y+" "+z);
		World world = Minecraft.getMinecraft().theWorld;

		if (world.blockExists(x, y, z)) {
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null) {
				te.readFromNBT(tag);
				world.markBlockForUpdate(x, y, z);
			}
		}
	}
}// End class ClientProxy