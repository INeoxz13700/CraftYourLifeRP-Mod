package com.flansmod.common.network;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.EntityVehicle;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.RadioGui;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class PacketRadio extends PacketBase {
	
	public int vehicleId;

	public List<Integer> entitiesId = new ArrayList();
	
	public int radioIndex;
	
	/*
	 *  0 = open gui
	 *  1 = change radio  
	 */
	public byte action;
	
	
	public PacketRadio()
	{
		
	}
	
	
	public PacketRadio(byte action)
	{
		this.action = action;
	}
	
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(action);

		if(action == 0) data.writeInt(vehicleId);
			
		data.writeInt(entitiesId.size());
		for(Integer id : entitiesId)
		{
			data.writeInt(id);
		}
		
		data.writeInt(radioIndex);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		action = data.readByte();


		if(action == 0) vehicleId = data.readInt();
			
		int size = data.readInt();
		for(int i = 0; i < size; i++)
		{
			entitiesId.add(data.readInt());
		}
		
		radioIndex = data.readInt();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		if(action == 1)
		{
			for(Integer id : entitiesId)
			{
				PacketRadio packet = new PacketRadio((byte)1);
				packet.radioIndex = radioIndex;
				FlansMod.packetHandler.sendTo(packet, (EntityPlayerMP) playerEntity.worldObj.getEntityByID(id));
			}
		}
		else
		{
			EntityVehicle vehicle = (EntityVehicle)playerEntity.worldObj.getEntityByID(vehicleId);
			
			PacketRadio packet = new PacketRadio((byte)0);
			packet.vehicleId = vehicleId;
			FlansMod.getPacketHandler().sendTo(packet, playerEntity);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		if(action == 1)
		{
			if(radioIndex == -1)
			{
				CraftYourLifeRPMod.radioHandler.stop();
				return;
			}
			CraftYourLifeRPMod.radioHandler.playRadio(radioIndex);
		}
		else
		{
			EntityVehicle vehicle = (EntityVehicle) clientPlayer.worldObj.getEntityByID(vehicleId);
			Minecraft.getMinecraft().displayGuiScreen(new RadioGui(vehicle));
		}
	}

}
