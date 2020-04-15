package magico13.mods.NetherBits.handlers;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import magico13.mods.NetherBits.ClientProxy;
import magico13.mods.NetherBits.NetherBitsMod;
import magico13.mods.NetherBits.NetherBitsModFunctions;
import magico13.mods.NetherBits.machine.TileEntityLavaGen;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;


/*
 * MUCH thanks to gcewing on the forge forums whose code I took to get this to work.
 */
public class NetherBitsPacketHandler implements IPacketHandler{

	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {

		if (packet.channel.equals("NBUpdate")) {
			ClientProxy.receiveCustomPacket(packet);

		}

	}
}
