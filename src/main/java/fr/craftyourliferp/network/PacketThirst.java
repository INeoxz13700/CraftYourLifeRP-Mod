package fr.craftyourliferp.network;

import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketThirst extends PacketBase {

	/*
	 * 0: sync thirst
	 * 1: drink water from natural place
	 */
	public byte type;
	
	public float thirst;
	
	public static PacketThirst syncThirst(float thirst)
	{
		PacketThirst packet = new PacketThirst();
		packet.type = 0;
		packet.thirst = thirst;
		return packet;
	}
	
	public static PacketThirst syncThirst()
	{
		PacketThirst packet = new PacketThirst();
		packet.type = 0;
		packet.thirst = 0;
		return packet;
	}
	
	public static PacketThirst drinkWater()
	{
		PacketThirst packet = new PacketThirst();
		packet.type = 1;
		return packet;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(type);
		if(type == 0)
		{
			data.writeFloat(thirst);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		type = data.readByte();
		if(type == 0)
		{
			thirst = data.readFloat();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		if(type == 0)
		{
			ExtendedPlayer.get(playerEntity).syncThirst();
		}
		else if(type == 1)
		{
			ExtendedPlayer ep = ExtendedPlayer.get(playerEntity);
			if(ep.thirst.playerLookWater(playerEntity.worldObj))
			{
				ep.thirst.setThirst(ep.thirst.maxThirst);
			}
		}
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		ExtendedPlayer ep = ExtendedPlayer.get(clientPlayer);
		
		ep.thirst.prevThirst = ep.thirst.getThirst();
		ep.thirst.setThirst(thirst);
		
	}

}
