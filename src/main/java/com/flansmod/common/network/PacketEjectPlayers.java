package com.flansmod.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.UUID;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.DriveableData;
import com.flansmod.common.driveables.DriveableType;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EntityVehicle;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketEjectPlayers extends PacketBase
{	
	
	int entityID;
	
	public PacketEjectPlayers() {}
	
	public PacketEjectPlayers(int id) {
		this.entityID = id;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
	{
		data.writeInt(this.entityID);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
	{
		
		this.entityID = data.readInt();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		System.out.println("received vehicle eject packet");
		EntityPlayerMP rider = (EntityPlayerMP) MinecraftServer.getServer().getEntityWorld().getEntityByID(this.entityID);
		if(rider.isRiding() && rider.ridingEntity instanceof EntitySeat)
		{
			EntitySeat seat = (EntitySeat) rider.ridingEntity;
			seat.riddenByEntity.mountEntity(null);
		}	
	}

	
	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
	}
}
