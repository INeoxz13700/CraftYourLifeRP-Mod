package fr.craftyourliferp.network;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketProning extends PacketBase {
	
	/*
	 * 0: change state of player to server
	 * 1: sync player state to players
	 */
	private byte action;
	
	private int entityId;
	private boolean isProning;
	
	
	public PacketProning()
	{
		
	}
	
	public static PacketProning syncPlayerState(EntityPlayer player, boolean state)
	{
		PacketProning packetProning = new PacketProning();
		packetProning.action = 1;
		packetProning.entityId = player.getEntityId();
		packetProning.isProning = state;
		return packetProning;
	}
	
	
	public static PacketProning setProning()
	{
		PacketProning packetProning = new PacketProning();
		packetProning.action = 0;
		return packetProning;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(action);
		if(action == 1)
		{
			data.writeInt(entityId);
			data.writeBoolean(isProning);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		action = data.readByte();
		if(action == 1)
		{
			entityId = data.readInt();
			isProning = data.readBoolean();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity)
	{
		//set proning
		/*PlayerCachedData cachedData = PlayerCachedData.getData(playerEntity);
		cachedData.setProning(!cachedData.isProning());*/
		
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
		extendedPlayer.setProning(!extendedPlayer.isProning());
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		Entity entity = clientPlayer.worldObj.getEntityByID(entityId);
		if(entity instanceof EntityPlayer)
		{
			/*PlayerCachedData cachedData = PlayerCachedData.getData((EntityPlayer)entity);
			if(cachedData != null) cachedData.setProning(isProning);*/
			
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get((EntityPlayer)entity);
			if(extendedPlayer != null) extendedPlayer.setProning(isProning);
		}
	}

}
