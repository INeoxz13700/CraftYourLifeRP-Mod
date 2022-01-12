package com.flansmod.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketCYLRPSound extends PacketBase {

	private String soundName;
	
	public PacketCYLRPSound()
	{
		
	}
	
	public PacketCYLRPSound(String soundName)
	{
		this.soundName = soundName;
	}
	
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		ByteBufUtils.writeUTF8String(data, soundName);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		soundName = ByteBufUtils.readUTF8String(data);
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		playerEntity.worldObj.playSoundAtEntity(playerEntity, soundName, 1.0F, 1.0F);
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		// TODO Auto-generated method stub
		
	}

	
}
