package mods.magico13.ExtraIndustrial.core;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;



public class ExtraIndustrialPacketHandler implements IPacketHandler {
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player) {

		if (packet.channel.equals("EIUpdate")) {
			//System.out.println("[EI] Packet Received!");
			receiveCustomPacket(packet, player);
		}
	}

	public static void receiveCustomPacket(Packet250CustomPayload pkt, Player player) {
		int type = pkt.data[0];
		switch (type) {
		case 0:
			PacketUtils.handleTileEntityPacket(pkt, player);
			break;
		case 1:
			PacketUtils.handleToolPacket(pkt, player);
			break;
		case 2:
			PacketUtils.handleCrafterPacket(pkt, player);
			break;
		case 3:
			PacketUtils.handleIndustrialCrafterPacket(pkt, player);
			break;
		case 4:
			PacketUtils.handleGUIUpdatePacket(pkt, player);
			break;
		case 5:
			PacketUtils.handleBlockCommandPacket(pkt, player);
			break;
		}
	}
}
