package mods.magico13.ExtraIndustrial.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;


import mods.magico13.ExtraIndustrial.machine.*;
import mods.magico13.ExtraIndustrial.generator.*;
import mods.magico13.ExtraIndustrial.tools.ContainerPortableCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class PacketUtils {

	public static Packet packetFromTileEntity(TileEntity te) {
		NBTTagCompound tag = new NBTTagCompound();
		te.writeToNBT(tag);
		return packetFromNBT(0, tag);
	}

	public static Packet packetFromNBT(int type, NBTTagCompound tag) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(bytes);
		try {
			stream.write(type);
			NBTBase.writeNamedTag(tag, stream);
		}
		catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
		byte[] data = bytes.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "EIUpdate";
		packet.length = data.length;
		packet.data = data;
		return packet;
	}

	public static NBTTagCompound nbtFromPacket(Packet250CustomPayload pkt) {
		DataInput stream = new DataInputStream(new ByteArrayInputStream(pkt.data, 1, pkt.length - 1));
		try {
			NBTBase tag = NBTBase.readNamedTag(stream);
			if (tag instanceof NBTTagEnd)
				return new NBTTagCompound();
			return (NBTTagCompound)tag;
		}
		catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
	}


	public static Packet guiUpdatePacket(int id, int value)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(bytes);
		try {
			stream.write(4);
			stream.writeInt(id);
			stream.writeInt(value);
		}
		catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
		byte[] data = bytes.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "EIUpdate";
		packet.length = data.length;
		packet.data = data;
		return packet;
	}
	
	public static Packet blockCommandPacket(int x, int y, int z, int id, int value)
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream stream = new DataOutputStream(bytes);
		try {
			stream.write(5);
			stream.writeInt(x);
			stream.writeInt(y);
			stream.writeInt(z);
			stream.writeInt(id);
			stream.writeInt(value);
		}
		catch (IOException e) {
			throw new RuntimeException(e.toString());
		}
		byte[] data = bytes.toByteArray();
		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "EIUpdate";
		packet.length = data.length;
		packet.data = data;
		return packet;
	}

	/* 
	 * Packet handling functions
	 */

	public static void handleTileEntityPacket(Packet250CustomPayload pkt, Player networkPlayer) {
		NBTTagCompound tag = PacketUtils.nbtFromPacket(pkt);
		EntityPlayer player = null;
		if (networkPlayer instanceof EntityPlayer)
			player = (EntityPlayer) networkPlayer;
		int x = tag.getInteger("x");
		int y = tag.getInteger("y");
		int z = tag.getInteger("z");

		World world = ExtraIndustrialMod.proxy.getWorld();
		if (world == null)
		{
			world = player.worldObj;
			if (world == null)
				return;
		}

		if (world.blockExists(x, y, z)) {
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if (te != null) {
				te.readFromNBT(tag);
			world.markBlockForRenderUpdate(x, y, z);
			}
		}
	}



	public static void handleToolPacket(Packet250CustomPayload pkt, Player networkPlayer)
	{
		NBTTagCompound tag = PacketUtils.nbtFromPacket(pkt);
		EntityPlayer player = (EntityPlayer) networkPlayer;

		player.inventory.getCurrentItem().setTagCompound(tag);

	}

	public static void handleCrafterPacket(Packet250CustomPayload pkt, Player networkPlayer) {
		NBTTagCompound tagCompound = PacketUtils.nbtFromPacket(pkt);
		EntityPlayer player = (EntityPlayer) networkPlayer;

		if (player.openContainer instanceof ContainerPortableCrafting)
		{
			player.inventory.getCurrentItem().setTagCompound(tagCompound);
			ContainerPortableCrafting craftingContainer = (ContainerPortableCrafting) player.openContainer;

			for (int i=0; i<9; i++)
			{
				craftingContainer.craftMatrix.setInventorySlotContents(i, null);
			}

			NBTTagList tagList = tagCompound.getTagList("Inventory");
			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
				byte slot = tag.getByte("Slot");
				if (slot >= 0 && slot < craftingContainer.craftMatrix.getSizeInventory()) {
					craftingContainer.craftMatrix.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(tag));
				}
			}
		}

	}
	public static void handleIndustrialCrafterPacket(Packet250CustomPayload pkt, Player networkPlayer) 
	{
		NBTTagCompound tag = PacketUtils.nbtFromPacket(pkt);
		EntityPlayer player = (EntityPlayer) networkPlayer;

		int x = tag.getInteger("x");
		int y = tag.getInteger("y");
		int z = tag.getInteger("z");
		World world = player.worldObj;
		TileEntityIndustrialCrafter tE = (TileEntityIndustrialCrafter)world.getBlockTileEntity(x, y, z);

		if (player.openContainer instanceof ContainerIndustrialCrafter)
		{
			ContainerIndustrialCrafter craftingContainer = (ContainerIndustrialCrafter)player.openContainer;
			for (int i=14; i<23; i++)
			{
				craftingContainer.putStackInSlot(i, null);
			}
			//craftingContainer.putStackInSlot(4, null);
			NBTTagList tagList = tag.getTagList("Inventory");
			for (int i = 0; i < tagList.tagCount(); i++) {
				NBTTagCompound tag1 = (NBTTagCompound) tagList.tagAt(i);
				byte slot = tag1.getByte("Slot");
				if (slot >= 14 && slot < 23) {
					craftingContainer.putStackInSlot(slot, ItemStack.loadItemStackFromNBT(tag1));
				}
			}
			tE.saveRecipe("recipe1", tag.getTagList("recipe1"));
			tE.saveRecipe("recipe2", tag.getTagList("recipe2"));
			tE.saveRecipe("recipe3", tag.getTagList("recipe3"));
			tE.modeAuto = tag.getBoolean("modeAuto");
		}
	}

	public static void handleGUIUpdatePacket(Packet250CustomPayload pkt, Player networkPlayer)
	{
		EntityPlayer player = (EntityPlayer) networkPlayer;
		Container updateContainer = player.openContainer;
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(pkt.data));
		int id;
		int value;

		try {
			inputStream.readByte();
			id = inputStream.readInt();
			value = inputStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		updateContainer.updateProgressBar(id, value);

	}
	
	public static void handleBlockCommandPacket(Packet250CustomPayload pkt, Player networkPlayer)
	{
		World world = ((EntityPlayer)networkPlayer).worldObj;
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(pkt.data));
		int x, y, z, id, value;

		try {
			inputStream.readByte();
			x = inputStream.readInt();
			y = inputStream.readInt();
			z = inputStream.readInt();
			id = inputStream.readInt();
			value = inputStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		TileEntity tE = world.getBlockTileEntity(x, y, z);
		if (tE instanceof INetworkCommandable)
		{
			((INetworkCommandable)tE).executeNetworkCommand(id, value);
		}
	}
}
