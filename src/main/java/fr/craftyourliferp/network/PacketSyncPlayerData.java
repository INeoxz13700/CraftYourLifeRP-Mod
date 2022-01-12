package fr.craftyourliferp.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.PlayerData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketSyncPlayerData extends PacketBase {

	/*
	 *  0: ask server data
	 *  1: send data to client
	 */
	private byte action;

	private boolean outservice;
	private double money;
	private String job;
		
		
	public PacketSyncPlayerData() 
	{
			
	}
	
	public static PacketSyncPlayerData askDataToServer()
	{
		PacketSyncPlayerData packet = new PacketSyncPlayerData();
		packet.action = 0;
		return packet;
	}
	
	
	/*public static PacketSyncPlayerData sendDataToClient(PlayerCachedData data)
	{
		PacketSyncPlayerData packet = new PacketSyncPlayerData();
		packet.action = 1;
		packet.outservice = data.serverData.outservice;
		packet.money = data.serverData.money;
		packet.job = data.serverData.job;
		return packet;
	}*/
	
	public static PacketSyncPlayerData sendDataToClient(ExtendedPlayer data)
	{
		PacketSyncPlayerData packet = new PacketSyncPlayerData();
		packet.action = 1;
		packet.outservice = data.serverData.outservice;
		packet.money = data.serverData.money;
		packet.job = data.serverData.job;
		return packet;
	}



	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) 
	{
		data.writeByte(action);
		if(action == 1)
		{
			ByteBufUtils.writeUTF8String(data, job);
			data.writeDouble(money);
			data.writeBoolean(outservice);
		}
	}
			


	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {	
		action = data.readByte();
		if(action == 1)
		{
			job = ByteBufUtils.readUTF8String(data);
			money = data.readDouble();
			outservice = data.readBoolean();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		if(action == 0)
		{
			/*PlayerCachedData data = PlayerCachedData.getData(playerEntity);
			data.syncData();*/
			
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
			extendedPlayer.syncData();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer)
	{
		if(action == 1)
		{
			CraftYourLifeRPMod.getClientData().cachedData.serverData.job = job;
			CraftYourLifeRPMod.getClientData().cachedData.serverData.outservice = outservice;
			CraftYourLifeRPMod.getClientData().cachedData.serverData.money = money;
		}
	}


}
